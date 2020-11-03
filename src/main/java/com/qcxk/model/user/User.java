package com.qcxk.model.user;

import lombok.Data;

import java.util.Date;

import static com.qcxk.common.Constants.DISABLED;

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
    private Date delTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /*=======权限标识=======*/
    private Integer devicePermission = DISABLED;
    private Integer delDevicePermission = DISABLED;
    private Integer dataListPermission = DISABLED;
    private Integer alarmPermission = DISABLED;
    private Integer settingPermission = DISABLED;
    private Integer originalDataPermission = DISABLED;
    private Integer userPermission = DISABLED;
}
