package net.dunotech.venus.system.service.listener;

import net.dunotech.venus.system.service.event.CommonApplicationEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class CommonEventListener implements ApplicationListener<CommonApplicationEvent> {

    private static final Logger log = Logger.getLogger(CommonEventListener.class);

    private static final String LISTEN_SERVICE = "ListenService";

    @Autowired
    private WebApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(CommonApplicationEvent commonApplicationEvent) {
        String phase = commonApplicationEvent.getPhase();
        String action = commonApplicationEvent.getAction();
        String reportId = commonApplicationEvent.getReportId();
        Map<String,Object> paramMap = commonApplicationEvent.getParamMap();
        log.info("do something "+phase+" "+action+": "+reportId);
        String reportIdInClass = reportId.substring(0,1).toLowerCase()+reportId.substring(1);
        Object obj = applicationContext.getBean(reportIdInClass+LISTEN_SERVICE);
        try {
            Method method = obj.getClass().getDeclaredMethod(getMethodName(phase,action),Map.class);
            method.invoke(obj,paramMap);
        }catch (Exception e){
            e.printStackTrace();
            if(e instanceof InvocationTargetException){
                throw new RuntimeException((e.getCause().getMessage()));
            }

        }
    }

    private String getMethodName(String phase,String action){
        action = action.substring(0,1).toUpperCase()+action.substring(1);
        return phase+action;
    }
}
