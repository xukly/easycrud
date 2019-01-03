package net.dunotech.venus.system.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Venus";

    private static final String SECRET = "jwtsecret";
    private static final String ISS = "venus";

    private static Long expirationNotRemember;

    private static Long expirationRemember;

    private static RedisUtils redisUtils;

    // 创建token
    public String createToken(String username, boolean isRememberMe) {
        long expiration = isRememberMe ? expirationRemember:expirationNotRemember;
        Date date = new Date(System.currentTimeMillis() + expiration * 1000);
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .compact();
        redisUtils.set(username,token,expiration);
        return token;
    }

    // 从token中获取用户名
    public static String getUsername(String token){
        return getTokenBody(token).getSubject();
    }

    // 是否已过期
    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public void setExpirationNotRemember(Long expirationNotRemember) {
        this.expirationNotRemember = expirationNotRemember;
    }

    public void setExpirationRemember(Long expirationRemember) {
        this.expirationRemember = expirationRemember;
    }

    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }
}
