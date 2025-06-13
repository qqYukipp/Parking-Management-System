package com.cgr.service;

import com.cgr.entity.CPUser;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CommonUserService {
    void add(CPUser user);

    void deleteById(Long id);

    void deleteBatch(List<Long> ids);

    void updateById(CPUser user);

    CPUser selectById(Long id);

    List<CPUser> selectAll(CPUser user);

    PageInfo<CPUser> selectPage(CPUser user, Integer pageNum, Integer pageSize);
}
