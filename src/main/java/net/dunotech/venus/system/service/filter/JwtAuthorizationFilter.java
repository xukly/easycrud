package net.dunotech.venus.system.service.filter;

import net.dunotech.venus.system.utils.JwtTokenUtils;
import net.dunotech.venus.system.utils.RedisUtils;
import net.dunotech.venus.system.utils.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static RedisUtils redisUtils;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    // 这里从token中获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        String username = JwtTokenUtils.getUsername(token);
        String tokenRedis = String.valueOf(redisUtils.get(username));
        if (username != null && tokenRedis != null && !StringUtils.equals(tokenRedis,"null") && tokenRedis.equals(token)){
            return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        }
        return null;
    }

    public void setRedisUtils(RedisUtils redisUtils) {
        JwtAuthorizationFilter.redisUtils = redisUtils;
    }
}
