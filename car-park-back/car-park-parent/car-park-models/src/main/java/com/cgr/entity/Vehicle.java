package com.cgr.entity;


/**
 * 车辆信息
*/
public class Vehicle {
    /** 主键ID */
    private Integer id;
    /** 车牌号 */
    private String name;
    /** 所属用户Id */
    private Integer userId;
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}