package com.qcxk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class Message {
    public Message() {

    }
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
    
    public Integer getId() {
        return this.id;
    }
    public String getCon() {
        return this.con;
    }
    public String getState() {
        return this.state;
    }
    public String getInserttime() {
        return this.inserttime;
    }
    public String getSendtime() {
        return this.sendtime;
    }
    public String getO1() {
        return this.o1;
    }
    public String getO2() {
        return this.o2;
    }
    public String getO3() {
        return this.o3;
    }
    public String getO4() {
        return this.o4;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setCon(String con) {
        this.con = con;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }
    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }
    public void setO1(String o1) {
        this.o1 = o1;
    }
    public void setO2(String o2) {
        this.o2 = o2;
    }
    public void setO3(String o3) {
        this.o3 = o3;
    }
    public void setO4(String o4) {
        this.o4 = o4;
    }
}