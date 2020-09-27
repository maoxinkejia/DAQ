package com.qcxk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    @TableId(type = IdType.AUTO)
    /*   */
    private Integer id;
    /* 消息内容  */
    private String con;
    /* 状态0-待发送1-已发送  */
    private String state;
    /* 生成时间  */
    private String inserttime;
    /* 发送时间  */
    private String sendtime;
    /* 保留字段1  */
    private String o1;
    /* 保留字段2  */
    private String o2;
    /* 保留字段3  */
    private String o3;
    /* 保留字段4  */
    private String o4;
}