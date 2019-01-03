package net.dunotech.venus.system.utils;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.HashMap;
import java.util.Map;

public class PageParam {
    private int pageIndex;// 第几页（当前第几页）
    private int pageRows;// 每页多少数据（每页的行数）
    private long startIndex;// 开始位置（数据库搜索条件起始位置）
    private long totalRows;// 总行数
    private long totalPages;// 总页数


    private Map<String, Object> param = new HashMap<String, Object>();//查询参数

    private PageParam(){};

    /**
     * @param pageIndex
     * @param pageRows
     * @return
     */
    public static PageParam getPage(int pageIndex, int pageRows) {
        PageParam pageParam = new PageParam();
        pageParam.setPageIndex(pageIndex);
        pageParam.setPageRows(pageRows);
        pageParam.setStartIndex(pageRows * (pageIndex - 1));
        return pageParam;
    }

    public static <T> Page<T> initPagination(int pageIndex, int pageRows){
        return new Page<T>(pageIndex, pageRows);
    }

    public void setTotalRows(long totalRows) {

        this.totalRows = totalRows;
        long left = totalRows % this.pageRows;
        long totalPages = totalRows / this.pageRows;
        if(left>0){
            totalPages++;
        }
        this.totalPages = totalPages;
    }

    public long getPageSize() {
        return pageRows;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageRows() {
        return pageRows;
    }

    public void setPageRows(int pageRows) {
        this.pageRows = pageRows;

    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }


}
