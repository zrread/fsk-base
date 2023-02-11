package com.fsk.framework.bean.response;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

public class PageBody<T> implements Serializable {
    private int pageNum;
    private int pageSize;
    private Long totalCount;
    private int pageNext;
    private List<T> list;

    public PageBody() {
    }

    /**
     * 如果 查询和返回的list是同一个list，那就直接
     * Param queryList
     * Param returnList
     */
    public PageBody(List queryList, List<T> returnList) {
        PageInfo<Object> pageInfoList = new PageInfo<>(queryList);
        this.pageNum = pageInfoList.getPageNum();
        this.pageSize = pageInfoList.getPageSize();
        this.totalCount = pageInfoList.getTotal();
        this.pageNext = pageInfoList.getNextPage();
        this.list = returnList;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNext() {
        return pageNext;
    }

    public void setPageNext(int pageNext) {
        this.pageNext = pageNext;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
