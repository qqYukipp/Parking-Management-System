package com.cgr.service;

import com.cgr.entity.Notice;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface NoticeService {

    void add(Notice notice);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    void updateById(Notice notice);

    Notice selectById(Integer id);

    List<Notice> selectAll(Notice notice);

    PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize);

}
