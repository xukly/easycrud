package net.dunotech.venus.system.dto.statistics;

import java.io.Serializable;
import java.util.Map;

public class PieDataDto implements Serializable {

    private String title;

    private String remark;

    private Map<String,Object> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
