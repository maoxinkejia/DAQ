package com.qcxk.model.user;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    /**
     * 登陆名
     */
    private String username;
    /**
     * 登陆密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 删除标识
     */
    private Integer delStatus;
    /**
     * 删除时间
     */
    private Integer delTime;
    /**
     * 创建时间
     */
    private Date createTime;


    /*=======权限标识=======*/
    private Integer devicePermission;
    private Integer dataListPermission;
    private Integer alarmPermission;
    private Integer settingPermission;
    private Integer originalDataPermission;
    private Integer userPermission;
}
