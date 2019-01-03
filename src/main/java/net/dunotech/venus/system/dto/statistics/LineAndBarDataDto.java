package net.dunotech.venus.system.dto.statistics;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LineAndBarDataDto implements Serializable {

    private String xAxis;

    private Map<String,List<Object>> series;

    private String title;

    private String remark;

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public Map<String, List<Object>> getSeries() {
        return series;
    }

    public void setSeries(Map<String, List<Object>> series) {
        this.series = series;
    }

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
}
