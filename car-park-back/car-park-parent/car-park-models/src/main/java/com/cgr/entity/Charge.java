package com.cgr.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("charge")
@AllArgsConstructor
@NoArgsConstructor
public class Charge implements Serializable {

    private Long id;

    private Double firstHour;

    private Double perHour;

    private Double dailyCap;

    private Double monthlyFee;

    private Integer freeMinutes;
}
