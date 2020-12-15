package com.qcxk.model.device;

import lombok.Data;

import java.util.Date;

@Data
public class TerminalPictures {
    private Long id;
    private String deviceNum;
    /**
     * 图片地址
     */
    private String picturePath;
    /**
     * 图片状态 0：未删除 1：已删除
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 删除时间
     */
    private Date delTime;
}
