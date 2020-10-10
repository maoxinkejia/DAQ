package com.qcxk.controller.model.page;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PageAndPageSize implements Serializable {
    /**
     * 当前页码
     */
    @NotNull(message = "当前页码不能为空")
    private Integer page = 0;

    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    private Integer pageSize = 10;
}
