package net.dunotech.venus.system.config.security;

import net.dunotech.venus.system.entity.common.JwtUser;
import net.dunotech.venus.system.service.filter.JwtAuthenticationFilter;
import net.dunotech.venus.system.service.filter.JwtAuthorizationFilter;
import net.dunotech.venus.system.service.sys.SysUserService;
import net.dunotech.venus.system.utils.JwtTokenUtils;
import net.dunotech.venus.system.utils.RedisUtils;
import net.dunotech.venus.system.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisUtils redisUtils;

    @Bean
    public Md5PasswordEncoder md5PasswordEncoder(){
        return new Md5PasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserService).passwordEncoder(md5PasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager());
        jwtAuthorizationFilter.setRedisUtils(redisUtils);
        http.cors().and().csrf().disable()
                .authorizeRequests()
                // 测试用资源，需要验证了的用户才能访问
                .antMatchers("/common/**").authenticated()
                .requestMatchers(CorsUtils::isCorsRequest).permitAll()
                // 其他都放行了
                .anyRequest().permitAll()
                .and().logout().permitAll().logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(jwtAuthorizationFilter)
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().accessDecisionManager(accessDecisionManager());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        RoleBasedVoter roleBasedVoter = new RoleBasedVoter();
        roleBasedVoter.setSysUserService(sysUserService);
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter(),
                roleBasedVoter,
                new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() { //登出处理
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                String token = httpServletRequest.getHeader(JwtTokenUtils.TOKEN_HEADER);
                String username = JwtTokenUtils.getUsername(token.replace(JwtTokenUtils.TOKEN_PREFIX, StringUtils.EMPTY));
                redisUtils.del(username);
            }
        };
    }
}