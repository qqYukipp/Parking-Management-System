package com.cgr.entity;


import java.util.List;

/**
 * 停车区域表
*/
public class Location {
    /** 主键ID */
    private Integer id;
    /** 区域名称 */
    private String name;
    /** 总车位数 */
    private Integer total;
    /** 空闲车位 */
    private Integer num;

    private List<ParkingLot> parkingLots;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public void setParkingLots(List<ParkingLot> parkingLots) {
        this.parkingLots = parkingLots;
    }
}