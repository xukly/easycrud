package net.dunotech.venus.system.dto.common;

import net.dunotech.venus.system.utils.PageParam;

import java.util.List;

/**
 * 页面返回数据dto
 * @author dunoinfo
 * @since 2018/09/30
 */
public class TablePageData {

    private PageParam pageParam;

    private List<?> tableData;

    public PageParam getPageParam() {
        return pageParam;
    }

    public void setPageParam(PageParam pageParam) {
        this.pageParam = pageParam;
    }

    public List<?> getTableData() {
        return tableData;
    }

    public void setTableData(List<?> tableData) {
        this.tableData = tableData;
    }
}
