package com.cgr.entity;


import java.time.LocalDateTime;

/**
 * 车辆信息
*/
public class Vehicle {
    /** 主键ID */
    private Integer id;
    /** 车牌号 */
    private String name;
    /** 所属用户Id */
    private Long userId;
    private String userName;


    //新增字段
    /**
     * 1 内部车
     * 2 月租车
     * 3 临时车
     * 4 黑名单
     */
    private int type;
    //月卡时间
    LocalDateTime startTime;
    LocalDateTime endTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}