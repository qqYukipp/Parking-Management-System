package com.cgr.service;

import com.cgr.entity.Pay;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PayService {

    void add(Pay pay);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    void updateById(Pay pay);

    Pay selectById(Integer id);

    List<Pay> selectAll(Pay pay);

    PageInfo<Pay> selectPage(Pay pay, Integer pageNum, Integer pageSize);
}
