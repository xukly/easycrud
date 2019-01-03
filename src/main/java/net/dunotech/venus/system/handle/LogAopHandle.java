package net.dunotech.venus.system.handle;

import net.dunotech.venus.system.entity.sys.SysLog;
import net.dunotech.venus.system.entity.sys.SysUser;
import net.dunotech.venus.system.mapper.sys.SysLogMapper;
import net.dunotech.venus.system.mapper.sys.SysUserMapper;
import net.dunotech.venus.system.service.annotation.EnableWriteLog;
import net.dunotech.venus.system.utils.CommonUtils;
import net.dunotech.venus.system.utils.JacksonJsonUtil;
import net.dunotech.venus.system.utils.JwtTokenUtils;
import net.dunotech.venus.system.utils.ResultMsg;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Aspect
@Component
public class LogAopHandle {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Pointcut("@annotation(net.dunotech.venus.system.service.annotation.EnableWriteLog)")
    private void logPointCutMethod(){}

    @AfterReturning(value = "@annotation(enableWriteLog)",argNames = "point,enableWriteLog,result",returning = "result")
    public void recordLog(JoinPoint point, EnableWriteLog enableWriteLog,Object result){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String tokenAll = request.getHeader("Authorization");
        String token = tokenAll.substring(JwtTokenUtils.TOKEN_PREFIX.length());
        String username = JwtTokenUtils.getUsername(token);
        SysUser sysUser = sysUserMapper.selectSysUserByAccount(username);
        SysLog sysLog = new SysLog();

        Object[] args = point.getArgs();
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        int index = ArrayUtils.indexOf(parameterNames, "reportId");
        if(index!=-1){
            String reportId = String.valueOf(args[index]);
            sysLog.setDescription(reportId+enableWriteLog.remark());
        }else{
            sysLog.setDescription(enableWriteLog.remark());
        }

        sysLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sysLog.setUserId(sysUser.getId());
        sysLog.setUsername(username);
        sysLog.setUserAction(enableWriteLog.operationType());
        if(result instanceof String){
            ResultMsg resultMsg = (ResultMsg)JacksonJsonUtil.jsonToBean(String.valueOf(result), ResultMsg.class);
            sysLog.setResult("code:"+resultMsg.getCode()+";msg:"+resultMsg.getMsg()+";data:"+JacksonJsonUtil.beanToJson(resultMsg.getData()));
        }
        sysLog.setUserIp(CommonUtils.getIpByRequest(request));
        sysLogMapper.insert(sysLog);
    }
}
