package com.qcxk.controller.model.response;


import com.qcxk.controller.model.page.Pagination;

/**
 * Created by 安震 [a00377319] on 2016/9/7.
 */
public class PageResponse extends Response {

    //分页信息
    private Pagination pagination;

    public PageResponse() {
    }

    public static PageResponse build() {
        return new PageResponse();
    }

    @Override
    public PageResponse fail(String mesasge) {
        super.fail(mesasge);
        return this;
    }

    public PageResponse(Pagination pagination) {
        this.pagination = pagination;
    }

    public static PageResponse pageResponse(Pagination pagination,Object result) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPagination(pagination);
        pageResponse.setResult(result);
        return pageResponse;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
