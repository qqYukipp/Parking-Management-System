package com.cgr.mapper;

import com.cgr.entity.Charge;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChargeMapper {

    Charge getInfo();

    void updateCharge(Charge charge);
}
