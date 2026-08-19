# 微信小程序登录 — 对齐 smartorder 代码风格

## 目标
将 gongdi 后端的登录与整体工程代码风格对齐 `D:\develop\code\smartorder\smartorder-backend`（smartorder-common 的认证模块），同时保留微信小程序登录（`wx.login` code → openid）与账号密码登录的本质差异。用户已确认三个方向：**全量对齐整个工程**、**接入真实 jscode2session**、**拦截器+ThreadLocal 全量改造**。

## 关键风格差异（smartorder → gongdi 现状）
| 维度 | smartorder（目标） | gongdi（现状） |
|---|---|---|
| DTO/VO/Entity | Lombok `@Data` POJO | Java record |
| 包结构 | `model/{dto,entity,vo}` | `domain/{dto,entity,vo}` + 混合大小写子包 `controller/authController` |
| Service 接口 | `I*Service` 前缀 | 无前缀 `AuthService` |
| 依赖注入 | `@RequiredArgsConstructor` + `private final` | 手写构造器 |
| 统一响应 | `Result<T>`(code/msg/data) + `ResultCode` 枚举 | `ApiResponse<T>`(code/message/data) |
| 异常体系 | `BaseException`→`BusinessException`/`ValidationException`/`SystemException` + `GlobalExceptionHandler` 返回 `Result` | 单一 `BizException(code,msg)` + `GlobalExceptionHandler` 返回 `ResponseEntity` |
| Token | 静态 `JwtTokenUtils` 真实 JWT（userId+role） | `mock-token-{userId}` 字符串 |
| 登录态传递 | `LoginInterceptor`+`ThreadLocalUtils`，控制器从 ThreadLocal 取用户 | 每个控制器 `@RequestHeader`+`authService.currentUser()` |
| 注释 | 中文 Javadoc + `@author`/`@since` | 中文 Javadoc（无 author/since） |

## 现状缺陷（本次一并修复）
- `domain/entity/User.java` 是个残缺 stub（无 `id()`/构造器），整个工程**当前无法编译**。新设计用 `ThreadLocalUtils.getCurrentUserId()` 取代 `User` 上下文对象，删除该 stub。

## 行为变更（需前端配合，请在审批时确认）
1. **HTTP 状态码**：异常响应由 `ResponseEntity(4xx/5xx)` 改为 smartorder 风格的**直接返回 `Result`（HTTP 200）**，错误信息放在 body `code`/`msg`。这是小程序最佳实践（`wx.request` 的 success 总能拿到 body，按 `code==0` 判断成功）。前端若曾按 `res.statusCode` 判断错误，需改为按 `res.data.code` 判断。
2. **错误码**：业务错误 body `code=400`（与现状一致，不破坏前端按 400 分支的逻辑）；未登录 `code=401`；系统错误 `code=500`。
3. **登录态**：`Authorization: Bearer <JWT>`，token 为真实 JWT（非 `mock-token-1`）。
4. **新用户**：未知 openid 自动注册为「微信用户/员工」。
5. **本地开发**：`wechat.mock-enabled=true`（默认）时跳过真实微信调用，按 `code` 派生 openid，便于无 appid/secret 时本地/测试运行；生产置 `false` 并配置真实 appid/secret。

## 微信登录 vs 网页登录的差异处理
- 无密码 → 不引入 `BCryptPasswordEncoder`、不引入 `spring-boot-starter-security`（smartorder 引入它是因有密码 + `@PreAuthorize`）。鉴权完全由 `LoginInterceptor` 承担，与 smartorder 实际鉴权路径一致。
- 无 Cookie → 不做 HttpOnly refresh cookie；token 直接放 body（`LoginVO.accessToken`）。微信小程序 token 过期后重新 `wx.login` 是惯用做法，故暂不做 refresh token（与 smartorder 双 token 的差异点，已说明）。
- 真实 `jscode2session` → 新增 `IWechatService` + `WechatServiceImpl`（JDK `HttpClient` 调用微信接口，Jackson 解析），按 openid 查找/创建用户。

---

## 实施步骤

### 阶段 1：依赖与配置
- `pom.xml`：新增 Lombok（`provided`）；JJWT 0.12.3 三件套（`jjwt-api` compile、`jjwt-impl`/`jjwt-jackson` runtime，采用官方拆分坐标保证可解析）。保留现有 `webmvc`/`webmvc-test`。
- `application.properties`：新增 `wechat.appid=`、`wechat.secret=`、`wechat.mock-enabled=true`（占位 + 开发默认 mock）。

### 阶段 2：公共基础设施（smartorder 风格）
新建包：`constant/`、`exception/`、`util/`、`interceptor/`、`model/{dto,entity,vo}`。
- `constant/ResultCode.java`：枚举（BUSINESS_ERROR 400、VALIDATION_ERROR 400、UNAUTHORIZED 401、FORBIDDEN 403、NOT_FOUND 404、SYSTEM_ERROR 500、WECHAT_LOGIN_ERROR 4001）。
- `model/vo/Result.java`：`@Data` 统一响应（code/msg/data，success=0），工厂方法 `success/success(data,msg)/fail(ResultCode)/fail(ResultCode,msg)`。
- `exception/`：`BaseException`、`BusinessException`（含 varargs）、`ValidationException`、`SystemException`（含 varargs）——照搬 smartorder。
- `exception/GlobalExceptionHandler.java`：`@Slf4j @RestControllerAdvice`，返回 `Result`（HTTP 200）；处理 `ValidationException`→VALIDATION_ERROR、`BusinessException`→BUSINESS_ERROR、`SystemException`→SYSTEM_ERROR、`NoResourceFoundException`→NOT_FOUND、`Exception`→SYSTEM_ERROR。
- 删除 `common/ApiResponse.java`、`common/BizException.java`、`common/GlobalExceptionHandler.java`（`common` 包清空删除）。

### 阶段 3：JWT 与登录态工具
- `util/JwtTokenUtils.java`：静态类，照搬 smartorder（密钥改 `gongdi-access-secret`/`gongdi-refresh-secret`，30min/7d），`generateAccessToken/RefreshToken(Map claims)`、`getClaimsFromToken`、`isTokenExpired`。claims 含 `userId`+`role`。
- `util/ThreadLocalUtils.java`：照搬 smartorder；`getCurrentUserId()` 改为抛 `BusinessException(UNAUTHORIZED)`（非受检），简化控制器。
- `interceptor/LoginInterceptor.java`：`@Slf4j @Component`，`preHandle` 解析 `Authorization: Bearer`（回退 `?token=`）→ `JwtTokenUtils.getClaimsFromToken` → 存 `ThreadLocalUtils`；失败时写 HTTP 200 + `Result.fail(UNAUTHORIZED)` JSON 体并返回 false。`afterCompletion` 清理 ThreadLocal。（不依赖 Spring Security。）
- `config/WebConfig.java`：`@Configuration @RequiredArgsConstructor`，注册 `LoginInterceptor`，`excludePathPatterns("/api/auth/wechat-login","/error")`。

### 阶段 4：真实微信登录
- `config/WechatProperties.java`：`@Component @ConfigurationProperties(prefix="wechat")`，字段 `appid`、`secret`、`mockEnabled`。
- `model/vo/WxSessionVO.java`：`@Data`，字段 `openid`、`sessionKey`（`@JsonProperty("session_key")`）、`unionid`、`errcode`、`errmsg`。
- `service/IWechatService.java` + `service/impl/WechatServiceImpl.java`：`WxSessionVO code2session(String code)`。`mockEnabled=true` 时返回 `openid-"openid-"+code`（不联网）；否则用 JDK `HttpClient` GET `https://api.weixin.qq.com/sns/jscode2session?appid=&secret=&js_code=&grant_type=authorization_code`，Jackson 解析，`errcode!=0` 抛 `BusinessException(WECHAT_LOGIN_ERROR, "微信登录失败:"+errmsg)`。
- `model/entity/UserEntity.java`：record→`@Data @NoArgsConstructor @AllArgsConstructor`，**新增 `openid` 字段**。
- `mapper/UserMapper.java`：新增 `findByOpenid(String)`、`insert(UserEntity)`（自增 id=max+1）；`findById` 保留；访问器改 getter。
- `mapper/MockDataStore.java`：张工种子增加 `openid="openid-zhanggong"`；`UserEntity` 构造补 openid；`newCheckOut` 等访问器改 getter；新增 `nextUserId()`。

### 阶段 5：认证服务与控制器
- `service/IAuthService.java`（原 `AuthService` 加 I 前缀）：`LoginVO loginByWechatCode(String code)`（返回 token+用户，控制器更薄）。**移除 `currentUser(authorization)`**。
- `service/impl/AuthServiceImpl.java`：`@Slf4j @Service @RequiredArgsConstructor`，依赖 `IWechatService`、`UserMapper`、`IUserService`。流程：校验 code 非空（`ValidationException`）→ `code2session` 得 openid → `findByOpenid`，无则自动注册（name「微信用户」、role「员工」、position「新员工」、`insert`）→ `JwtTokenUtils.generateAccessToken({userId,role})` → 返回 `new LoginVO(accessToken,"Bearer",userService.toUserVO(user))`。
- `controller/AuthController.java`（从 `controller/authController/` 上移）：`@Slf4j @RequiredArgsConstructor @RestController @RequestMapping("/api/auth")`，`@PostMapping("/wechat-login")` 返回 `Result<LoginVO>`。
- `model/dto/WechatLoginDTO.java`（原 `WechatLoginRequest` 改名+Lombok）：`@Data`，字段 `code`。

### 阶段 6：全量 record→Lombok + 包重排
将 `domain/{dto,entity,vo}` 全部迁到 `model/{dto,entity,vo}` 并转 `@Data`（带 `@NoArgsConstructor @AllArgsConstructor` 以兼容现有 `new XxxVO(...)` 位置构造）。
- DTO：`WechatLoginDTO`、`AttendanceActionDTO`（原 `AttendanceActionRequest`）、`UpdateTaskStatusDTO`（原 `UpdateTaskStatusRequest`）。
- Entity：`UserEntity`、`ProjectEntity`、`TaskEntity`、`MessageEntity`、`AttendanceEntity`。
- VO：`Result`、`LoginVO`、`UserVO`、`ProjectVO`、`TaskVO`、`MessageVO`（`isUnread` 加 `@JsonProperty("isUnread")` 保持 JSON 不变）、`AttendanceVO`、`WxSessionVO`。
- 全工程 record 访问器 `.field()` → getter `.getField()` / `.isField()`；`new Xxx(...)` 构造顺序不变（仅 `UserEntity` 多 openid）。
- 删除 `domain/entity/User.java`（残缺 stub）。

### 阶段 7：全量控制器改造（拦截器+ThreadLocal）
6 个控制器（User/Home/Attendance/Task/Message/Project）从 `controller/xxxController/` 上移到 `controller/`：
- 移除 `@RequestHeader("Authorization")` 参数与 `AuthService` 依赖；改 `@RequiredArgsConstructor` + `private final`。
- 取用户：`Long userId = ThreadLocalUtils.getCurrentUserId();`
- 返回 `Result<...>`；服务方法改传 `Long userId`（不再传 `User`）。
- 补 `@Slf4j` 与 `@author`/`@since` 中文 Javadoc。

### 阶段 8：全量服务改造（User→Long userId）
- `IUserService`/impl：`me(Long userId)`、`toUserVO(UserEntity)`。
- `IHomeService`/impl：`home(Long userId,projectId)`，内部 `userMapper.findById(userId)`。
- `IAttendanceService`/impl：`checkIn/checkOut/today(Long userId,projectId)`。
- `ITaskService`/impl：`myTasks/updateStatus/taskSummary(Long userId,...)`。
- `IMessageService`/impl：`myMessages/unreadCount/markRead(Long userId,...)`。
- `IProjectService`/impl：`myProjects/requireProjectMember(Long userId,...)`。
- `BizException`→`BusinessException`（业务规则）/`ValidationException`（参数校验）。
- 访问器全改 getter。

### 阶段 9：Mapper 访问器改造
`ProjectMapper`/`TaskMapper`/`MessageMapper`/`AttendanceMapper`/`DashboardMapper`/`MockDataStore`：record 访问器→getter；`new XxxEntity(...)` 顺序不变。

### 阶段 10：测试更新
`GongdiApiIntegrationTest.java`：
- `@BeforeEach`：`mockDataStore.reset()` + 生成 `accessToken = JwtTokenUtils.generateAccessToken(Map.of("userId",1L,"role","管理员"))`，后续请求用 `Bearer <accessToken>`（替代硬编码 `mock-token-1`）。
- 登录用例：`POST /api/auth/wechat-login` body `{"code":"zhanggong"}`（mock 模式 openid=`openid-zhanggong`→张工），断言 `$.code==0`、`$.data.accessToken` 非空、`$.data.user.name=="张工"`。
- 新增用例：未知 openid 自动注册——`{"code":"newuser"}`→`$.data.user.name=="微信用户"`，再次同 code 登录返回同一用户（幂等）。
- 其余用例：`isBadRequest()`→`isOk()`，保留 `$.code==400` 断言；token 改用生成的 JWT。
- `wechat.mock-enabled=true` 由 `application.properties` 继承，测试不联网。

### 阶段 11：编译验证
`./mvnw -q clean compile`（必要时 `test-compile`）确保全量重构后可编译；修复遗漏的 import/访问器。

---

## 影响面
- 新增文件 ~17，删除文件 ~6，重写/迁移 ~30。
- 不引入 Spring Security / MyBatis / Redis / Knife4j（与 WeChat 场景与 Spring Boot 4.1.0 兼容性不符；smartorder 引入它们是因网页登录+其他业务需要）。
- 前端需配合：错误判断改为按 body `code`（HTTP 统一 200）；登录 token 改用后端返回的 JWT。
