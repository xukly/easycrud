package net.dunotech.venus.system.service.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class CommonApplicationEvent extends ApplicationEvent {

    /**
     * crudParam:元素参数;entity:实体类;result:处理结果;id:id
     */
    private Map<String,Object> paramMap;

    /**
     * 操作动作
     */
    private String action;

    /**
     * 业务模块标识
     */
    private String reportId;

    /**
     * 动作发生阶段
     */
    private String phase;

    public CommonApplicationEvent(Object source, Map<String, Object> paramMap, String action, String reportId, String phase) {
        super(source);
        this.paramMap = paramMap;
        this.action = action;
        this.reportId = reportId;
        this.phase = phase;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
}
