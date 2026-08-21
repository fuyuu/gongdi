package com.gongdi.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongdi.config.SmsProperties;
import com.gongdi.domain.dto.WxSmsLoginDTO;
import com.gongdi.domain.entity.SysUser;
import com.gongdi.domain.entity.SysUserPhone;
import com.gongdi.domain.vo.LoginVO;
import com.gongdi.domain.vo.WxSessionVO;
import com.gongdi.exception.BusinessException;
import com.gongdi.mapper.SysUserPhoneMapper;
import com.gongdi.mapper.UserMapper;
import com.gongdi.service.IAuthService;
import com.gongdi.service.IWechatService;
import com.gongdi.util.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.yulichang.toolkit.StrUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类，负责小程序 logincode 登录、openid 自动注册与双 token 签发。
 *
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    /**
     * refreshToken 在 Redis 中的 key 前缀，值为 refreshToken，TTL 与 JwtTokenUtils.REFRESH_TOKEN_EXPIRE 一致
     */
    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
    /**
     * 微信 session_key 在 Redis 中的 key 前缀，用于后续解密手机号
     */
    private static final String WX_SESSION_PREFIX = "wx:session:";

    /**
     * 短信验证码在 Redis 中的 key 前缀，一个手机号对应一个 Hash，包含 code / createTime / lastSendTime
     */
    private static final String SMS_CODE_PREFIX = "sms:code:";
    /**
     * 新用户默认昵称，后续由前端引导补充真实姓名
     */
    private static final String DEFAULT_USER_NAME = "微信用户";

    private final UserMapper userMapper;
    private final SysUserPhoneMapper sysUserPhoneMapper;
    private final IWechatService wechatService;
    private final StringRedisTemplate stringRedisTemplate;
    private final SmsCodeSender smsCodeSender;
    private final SmsProperties smsProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO loginByCodeId(String loginCode) {
        // 1. 用 loginCode 向微信换取 openid / session_key / unionid
        WxSessionVO session = wechatService.code2session(loginCode);
        String openid = session.getOpenid();

        // 2. 按 openid 查询用户，未找到则自动注册新账号
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpenid, openid));
        boolean isNewUser = false;
        if (user == null) {
            user = registerByOpenid(session);
            isNewUser = true;
        }

        // 3. 缓存 session_key，用于后续 getPhoneNumber 解密（7 天过期）
        if (StrUtils.isNotBlank(session.getSessionKey())) {
            stringRedisTemplate.opsForValue().set(WX_SESSION_PREFIX + user.getId(), session.getSessionKey(), 7, TimeUnit.DAYS);
        }

        // 4. 签发双 token 返回
        return generateTokens(user, isNewUser);
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        if (StrUtils.isBlank(refreshToken)) {
            throw new BusinessException("刷新令牌不能为空");
        }

        Long userId;
        try {
            Claims claims = JwtTokenUtils.getClaimsFromToken(refreshToken, JwtTokenUtils.REFRESH_TOKEN_SECRET);
            userId = claims.get("userId", Long.class);
            // 校验 Redis 中保存的 refreshToken 是否一致，防止已登出的旧令牌被复用
            String savedRefreshToken = stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
            if (!refreshToken.equals(savedRefreshToken)) {
                log.warn("刷新令牌已失效或已登出, userId: {}", userId);
                throw new BusinessException("登录状态已失效，请重新登录");
            }

            SysUser user = userMapper.selectById(userId);
            if (user == null) {
                throw new BusinessException("用户不存在");
            }
            return generateTokens(user, false);

        } catch (Exception e) {
            log.warn("刷新令牌解析失败: {}", e.getMessage());
            throw new BusinessException("刷新令牌无效或已过期");
        }
    }

    @Override
    public void logout(String userId) {
        // 删除 Redis 中的刷新令牌，使旧 refreshToken 立即失效，无法再用于刷新
        stringRedisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
        log.info("用户{}已登出，刷新令牌已删除", userId);
    }

    /**
     * 按 openid 注册新用户，返回已插入的用户实体（id 已回填）。
     */
    private SysUser registerByOpenid(WxSessionVO session) {
        SysUser user = new SysUser();
        long snowflakeNextId = IdUtil.getSnowflakeNextId();
        user.setName(DEFAULT_USER_NAME + snowflakeNextId);

        user.setOpenid(session.getOpenid());
        user.setUnionid(session.getUnionid());
        user.setStatus(1);
        userMapper.insert(user);

        // 同步在 sys_user_phone 表创建一条空手机号记录，便于后续绑定手机号时按 user_id 更新
        SysUserPhone userPhone = new SysUserPhone();
        userPhone.setUserId(user.getId());
        userPhone.setStatus(1);
        sysUserPhoneMapper.insert(userPhone);

        log.info("新用户注册成功, openid: {}, userId: {}", session.getOpenid(), user.getId());
        return user;
    }

    /**
     * 生成双 token（accessToken + refreshToken），并将 refreshToken 写入 Redis。
     *
     * @param user      用户实体
     * @param isNewUser 是否新注册用户
     * @return 登录结果
     */
    private LoginVO generateTokens(SysUser user, boolean isNewUser) {
        Long userId = user.getId();

        // 移除旧的 refreshToken，保证同一时间仅一份有效刷新令牌
        stringRedisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("openid", user.getOpenid());
        String accessToken = JwtTokenUtils.generateAccessToken(claims);
        String refreshToken = JwtTokenUtils.generateRefreshToken(claims);

        stringRedisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + userId, refreshToken,
                JwtTokenUtils.REFRESH_TOKEN_EXPIRE, TimeUnit.MILLISECONDS);

        LoginVO vo = new LoginVO();
        vo.setAccessToken(accessToken);
        vo.setRefreshToken(refreshToken);
        vo.setIsNewUser(isNewUser);
        return vo;
    }

    @Override
    public LoginVO loginByPhone(String loginCode, String phone, String code) {
        // 手机号登录涉及 getPhoneNumber 与手机号绑定，尚未实现
        return smsLogin(new WxSmsLoginDTO(loginCode,phone,code));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO smsLogin(WxSmsLoginDTO dto) {
        String phone = dto.getPhone();
        String smsCode = dto.getSmsCode();

        // 1. 参数校验
        if (StrUtils.isBlank(phone)) {
            throw new BusinessException("手机号不能为空");
        }
        if (!phone.matches("^1\\d{10}$")) {
            throw new BusinessException("手机号格式不正确");
        }
        if (StrUtils.isBlank(smsCode)) {
            throw new BusinessException("验证码不能为空");
        }
        if (StrUtils.isBlank(dto.getLoginCode())) {
            throw new BusinessException("登录凭证不能为空");
        }

        // 2. 先校验短信验证码：与 Redis 中存储的不一致直接拒绝；
        //    校验成功后立即消费（删除），防止同一验证码被重复使用
        verifyPhoneCode(phone, smsCode);

        // 3. 验证码通过后再走微信登录，用 loginCode 换取 openid
        WxSessionVO session = wechatService.code2session(dto.getLoginCode());
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpenid, session.getOpenid()));
        boolean isNewUser = false;
        if (user == null) {
            user = registerByOpenid(session);
            isNewUser = true;
        }

        // 4. 绑定手机号：手机号已被其他微信账号绑定时拒绝登录
        bindPhone(user, phone);

        // 5. 缓存 session_key，用于后续解密手机号（7 天过期）
        if (StrUtils.isNotBlank(session.getSessionKey())) {
            stringRedisTemplate.opsForValue().set(WX_SESSION_PREFIX + user.getId(), session.getSessionKey(), 7, TimeUnit.DAYS);
        }

        // 6. 签发双 token 返回
        return generateTokens(user, isNewUser);
    }

    /**
     * 将手机号绑定到当前用户：已绑定其他用户则报错；本用户已绑定则幂等返回；
     * 注册时创建的空手机号记录优先复用更新，否则新增绑定记录。
     */
    private void bindPhone(SysUser user, String phone) {
        Long phoneLong = Long.parseLong(phone);

        SysUserPhone bound = sysUserPhoneMapper.selectOne(
                new LambdaQueryWrapper<SysUserPhone>().eq(SysUserPhone::getPhone, phoneLong));
        if (bound != null) {
            if (!bound.getUserId().equals(user.getId())) {
                log.warn("手机号绑定冲突, phone: {}, 已绑定 userId: {}, 当前 userId: {}", phone, bound.getUserId(), user.getId());
                throw new BusinessException("该手机号已绑定其他微信账号");
            }
            return;
        }

        SysUserPhone own = sysUserPhoneMapper.selectOne(
                new LambdaQueryWrapper<SysUserPhone>()
                        .eq(SysUserPhone::getUserId, user.getId())
                        .isNull(SysUserPhone::getPhone));
        if (own != null) {
            own.setPhone(phoneLong);
            sysUserPhoneMapper.updateById(own);
        } else {
            SysUserPhone record = new SysUserPhone();
            record.setUserId(user.getId());
            record.setPhone(phoneLong);
            record.setStatus(1);
            sysUserPhoneMapper.insert(record);
        }

        log.info("手机号绑定成功, userId: {}, phone: {}", user.getId(), phone);
    }

    // ==================== 短信验证码 ====================

    @Override
    public void sendPhoneCode(String phone) {
        // 1. 参数校验---这段代码检验的是 11位手机号。正则 ^1\\d{10}$ 的含义：
        //^1：必须以数字 1 开头；\\d{10}：紧接着是 10 个数字（共 11 位）
        if (StrUtils.isBlank(phone)) {
            throw new BusinessException("手机号不能为空");
        }
        if (!phone.matches("^1\\d{10}$")) {
            throw new BusinessException("手机号格式不正确");
        }

        String codeKey = SMS_CODE_PREFIX + phone;

        // 2. 发送频率校验：从 Hash 中读取 lastSendTime，同一手机号在 resendIntervalSeconds 秒内只能发送一次
        Object lastSendTimeObj = stringRedisTemplate.opsForHash().get(codeKey, "lastSendTime");
        if (lastSendTimeObj != null) {
            long lastSendTime = Long.parseLong((String) lastSendTimeObj);
            long elapsed = System.currentTimeMillis() - lastSendTime;
            long intervalMs = TimeUnit.SECONDS.toMillis(smsProperties.getResendIntervalSeconds());
            if (elapsed < intervalMs) {
                long remainSeconds = (intervalMs - elapsed) / 1000;
                throw new BusinessException("请不要频繁发送验证码，等待 " + remainSeconds + " 秒后重试");
            }
        }

        // 3. 生成验证码：优先使用固定码（测试用），否则随机生成 codeLength 位数字
        String code;
        if (StrUtils.isNotBlank(smsProperties.getFixedCode())) {
            code = smsProperties.getFixedCode();
        } else {
            code = RandomUtil.randomNumbers(smsProperties.getCodeLength());
        }

        long now = System.currentTimeMillis();

        // 4. 先存储验证码到 Redis Hash（设好 TTL），再调用短信网关发送
        //    先存后发，避免网关成功但 Redis 失败导致用户收不到
        stringRedisTemplate.opsForHash().put(codeKey, "code", code);
        stringRedisTemplate.opsForHash().put(codeKey, "createTime", String.valueOf(now));
        stringRedisTemplate.opsForHash().put(codeKey, "lastSendTime", String.valueOf(now));
//        stringRedisTemplate.expire(codeKey, smsProperties.getCodeTtlMinutes(), TimeUnit.MINUTES);
        stringRedisTemplate.expire(codeKey, smsProperties.getCodeTtlMinutes(), TimeUnit.MINUTES);

        // 5. 调用短信网关发送，失败时回滚已存储的验证码
        try {
            smsCodeSender.send(phone, code, smsProperties.getCodeTtlMinutes());
        } catch (Exception e) {
            stringRedisTemplate.delete(codeKey);
            throw e;
        }

        log.info("短信验证码已发送, phone: {}", phone);
    }

    @Override
    public void verifyPhoneCode(String phone, String code) {
        // 1. 参数校验
        if (StrUtils.isBlank(phone)) {
            throw new BusinessException("手机号不能为空");
        }
        if (StrUtils.isBlank(code)) {
            throw new BusinessException("验证码不能为空");
        }

        // 2. 从 Redis Hash 中获取存储的验证码
        String codeKey = SMS_CODE_PREFIX + phone;
        Object storedCodeObj = stringRedisTemplate.opsForHash().get(codeKey, "code");

        if (storedCodeObj == null) {
            throw new BusinessException("验证码已过期，请重新获取");
        }

        String storedCode = (String) storedCodeObj;
        if (!storedCode.equals(code)) {
            throw new BusinessException("验证码错误，请重新输入");
        }

        // 3. 验证成功后移除整个 Hash，防止重复使用
        stringRedisTemplate.delete(codeKey);

        log.info("短信验证码校验成功, phone: {}", phone);
    }
}