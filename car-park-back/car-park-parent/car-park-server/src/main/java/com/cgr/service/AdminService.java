package com.cgr.service;

import com.cgr.entity.CPUser;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {
    void add(CPUser user);

    void deleteById(Long id);

    void deleteBatch(List<Long> ids);

    CPUser selectById(Long id);

    void updateById(CPUser user);

    List<CPUser> selectAll(CPUser user);

    PageInfo<CPUser> selectPage(CPUser user, Integer pageNum, Integer pageSize);

    void liftToAdmin(List<Long> ids);

    void downToUser(List<Long> ids);
}
