package com.qcxk.controller.model.page;

import com.github.pagehelper.PageInfo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class Pagination implements Serializable {
    // 当前页
    private long page = 1;
    // 总页数
    private long totalPage;
    // 总记录数
    private long totalRecord;
    // 页大小
    private long pageSize = 10;
    // 起始记录条数
    private long start = -1;

    public Pagination() {
    }

    public Pagination(int page, int pageSize) {
        setPage(page);
        setPageSize(pageSize);
    }


    /**
     * 分页方法
     *
     * @param <T> 集合的泛型
     * @param <E> 必须继承分页的类
     */
    public static <T, E extends PageAndPageSize> Pagination buildPagination(List<T> list, E pageInfo) {
        PageInfo<T> info = new PageInfo<>(list);
        Pagination pagination = new Pagination();
        pagination.setPage(pageInfo.getPage());
        pagination.setTotalPage(pagination.getTotalPage(info.getTotal(), pageInfo.getPageSize()));
        pagination.setTotalRecord(info.getTotal(), pageInfo.getPageSize());
        pagination.setPageSize(pageInfo.getPageSize());
        return pagination;
    }

    public long getPage() {
        return this.page;
    }

    public void setPage(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("page must be greater than 1");
        }
        this.page = page;
        this.start = -1;
    }

    public long getTotalPage() {
        return this.totalPage;
    }

    public long getTotalPage(long totalRecord, long pageSize) {
        totalPage = totalRecord / pageSize;
        if (totalRecord % pageSize > 0) {
            totalPage += 1;
        }
        return this.totalPage;
    }

    public void setTotalPage(long totalPage) {
        if (totalPage < 0) {
            throw new IllegalArgumentException("totalPage must be greater than or equal 0");
        }
        this.totalPage = totalPage;
    }

    public long getTotalRecord() {
        return this.totalRecord;
    }

    public void setTotalRecord(long totalRecord, @NotNull(message = "每页大小不能为空") int pageSize) {
        if (totalRecord < 0) {
            throw new IllegalArgumentException("totalRecord must be greater than or equal 0");
        }
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        totalPage = totalRecord / this.pageSize;
        if (totalRecord % this.pageSize > 0) {
            totalPage += 1;
        }
        if (totalPage == 0) {
            page = 1;
            start = -1;
        } else if (page > totalPage) {
            page = totalPage;
            start = -1;
        }
    }

    public long getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(long pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be greater than  0");
        }
        this.pageSize = pageSize;
    }

    public long getStart() {
        if (start < 0) {
            start = (page - 1) * pageSize;
        }
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Pagination(long page, long totalPage, long totalRecord, long pageSize) {
        this.page = page;
        this.totalPage = totalPage;
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
    }
}
