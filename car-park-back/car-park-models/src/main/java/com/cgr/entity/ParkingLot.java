package com.cgr.entity;


/**
 * 车位信息
*/
public class ParkingLot {
    /** 主键ID */
    private Integer id;
    /** 车位编号 */
    private String name;
    /** 区域Id */
    private Integer locationId;
    /** 车位状态 */
    private String status;
    private String locationName;

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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}