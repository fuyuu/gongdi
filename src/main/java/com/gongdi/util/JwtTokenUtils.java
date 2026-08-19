package com.gongdi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类，负责登录 token 的生成与解析。
 * @author Ma Qiang
 * @since 2026/8/13
 */
public class JwtTokenUtils {

    public static final String ACCESS_TOKEN_SECRET = "gongdi-access-secret";
    /**
     * Access Token 过期时间（30 分钟）
     */
    public static final long ACCESS_TOKEN_EXPIRE = 30 * 60 * 1000;

    public static final String REFRESH_TOKEN_SECRET = "gongdi-refresh-secret";
    /**
     * Refresh Token 过期时间（7 天）
     */
    public static final long REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000;

    /**
     * 生成 Access Token
     */
    public static String generateAccessToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
                .signWith(generateKey(ACCESS_TOKEN_SECRET))
                .compact();
    }

    /**
     * 生成 Refresh Token
     */
    public static String generateRefreshToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE))
                .signWith(generateKey(REFRESH_TOKEN_SECRET))
                .compact();
    }

    /**
     * 生成安全的 Key 对象（HS256 至少 32 字节，不足时补零）
     */
    public static SecretKey generateKey(String secret) {
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, keyBytes.length);
            keyBytes = padded;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 验证 Token 并获取 claims
     */
    public static Claims getClaimsFromToken(String token, String secret) {
        SecretKey key = generateKey(secret);
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 检查 Token 是否过期
     */
    public static boolean isTokenExpired(String token, String secret) {
        try {
            Claims claims = getClaimsFromToken(token, secret);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("token验证失败: " + e.getMessage());
        }
    }
}
