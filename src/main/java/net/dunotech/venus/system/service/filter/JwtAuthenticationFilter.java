package net.dunotech.venus.system.service.filter;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.dunotech.venus.system.config.Constants;
import net.dunotech.venus.system.dto.sys.LoginUserDto;
import net.dunotech.venus.system.entity.common.JwtUser;
import net.dunotech.venus.system.entity.sys.SysLog;
import net.dunotech.venus.system.entity.sys.SysToken;
import net.dunotech.venus.system.entity.sys.SysUser;
import net.dunotech.venus.system.mapper.sys.SysLogMapper;
import net.dunotech.venus.system.mapper.sys.SysTokenMapper;
import net.dunotech.venus.system.mapper.sys.SysUserMapper;
import net.dunotech.venus.system.service.common.ValidateLoginService;
import net.dunotech.venus.system.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private JwtTokenUtils jwtTokenUtils = new JwtTokenUtils();

    private static final String SECRET = "jwtsecret";

    private static final String ACCOUNT = "account";

    private static final String PASSWORD = "password";

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
//        //验证验证码
//        String verify = request.getParameter("verify");
//        String uuid = request.getParameter("uuid");
//        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
//        RedisUtils redisUtils = (RedisUtils)applicationContext.getBean("restUtil");
//        String value = String.valueOf(redisUtils.get(uuid));
//        if(StringUtils.isEmpty(value) || !verify.equals(value)) {
//            return null;
//        }

        // 从输入流中获取到登录的信息
//        try {
//            LoginUserDto loginUserDto = new ObjectMapper().readValue(request.getInputStream(), LoginUserDto.class);
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginUserDto.getAccount(), loginUserDto.getPassword(), new ArrayList<>())
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }

        String param = RestUtil.getRestData(request,response);
        Map<String,String> realParam = (Map<String,String>) JacksonJsonUtil.jsonToBean(param,Map.class);
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setAccount(realParam.get(ACCOUNT));
        loginUserDto.setPassword(realParam.get(PASSWORD));
        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginUserDto.getAccount(), loginUserDto.getPassword(), new ArrayList<>())
        );
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        log.info("jwtUser:" + jwtUser.toString());
        String token = jwtTokenUtils.createToken(jwtUser.getUsername(), request.getParameter("isRememberMe")!=null && request.getParameter("isRememberMe").equals("true"));
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的格式应该是 `Venus+token`
        String realToken = JwtTokenUtils.TOKEN_PREFIX + token;
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("token",realToken);
        String returnMsg = JacksonJsonUtil.beanToJson(ResultMsg.getResultMsg("登录成功!",resultMap,200));
        OutputStream outputStream = response.getOutputStream();
        response.setHeader("content-type", "text/html;charset=UTF-8");
        byte[] dataByteArr = returnMsg.getBytes("UTF-8");
        outputStream.write(dataByteArr);

        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());

        //相应token存入数据库
        SysToken sysToken = new SysToken();
        Date expiration = getTokenBody(token).getExpiration();
        sysToken.setExpireTime(new Timestamp(expiration.getTime()));
        sysToken.setToken(JwtTokenUtils.TOKEN_PREFIX + token);
        SysTokenMapper sysTokenMapper = (SysTokenMapper) applicationContext.getBean("sysTokenMapper");
        sysTokenMapper.insert(sysToken);

        //登录信息存入数据库
        SysLogMapper sysLogMapper = (SysLogMapper) applicationContext.getBean("sysLogMapper");
        SysUserMapper sysUserMapper = (SysUserMapper) applicationContext.getBean("sysUserMapper");
        SysLog sysLog = new SysLog();
        sysLog.setResult("登录成功");
        sysLog.setUserAction(Constants.LOG_ACTION_LOGIN);
        sysLog.setDescription("用户"+jwtUser.getUsername()+"登录");
        sysLog.setUsername(jwtUser.getUsername());
        SysUser sysUser = sysUserMapper.selectSysUserByAccount(jwtUser.getUsername());
        sysLog.setUserId(sysUser.getId());
        sysLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sysLog.setUserIp(CommonUtils.getIpByRequest(request));
        sysLogMapper.insert(sysLog);
    }

    // 这是验证失败时候调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        OutputStream outputStream = response.getOutputStream();
        response.setHeader("content-type", "text/html;charset=UTF-8");
        String returnMsg = JacksonJsonUtil.beanToJson(ResultMsg.getResultMsg("登录失败：用户名或密码错误",failed.getMessage(),10000));
        byte[] dataByteArr = returnMsg.getBytes("UTF-8");
        outputStream.write(dataByteArr);
    }

    private static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
