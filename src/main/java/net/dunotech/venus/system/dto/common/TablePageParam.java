package net.dunotech.venus.system.dto.common;

import net.dunotech.venus.system.utils.PageParam;

import java.util.Map;

/**
 * 页面参数dto
 * @author dunoinfo
 * @since 2018/09/30
 */
public class TablePageParam {

    private PageParam pageParam;

    private Map<String,Object> param;

    public PageParam getPageParam() {
        return pageParam;
    }

    public void setPageParam(PageParam pageParam) {
        this.pageParam = pageParam;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
