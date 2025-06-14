package com.cgr.service.impl;

import com.cgr.constant.Role;
import com.cgr.entity.CPUser;
import com.cgr.entity.LoginUser;
import com.cgr.entity.Pay;
import com.cgr.exception.CustomException;
import com.cgr.mapper.PayMapper;
import com.cgr.mapper.UserMapper;
import com.cgr.service.PayService;
import com.cgr.utils.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayServiceImpl implements PayService {
    @Resource
    private PayMapper payMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 新增
     */
    public void add(Pay pay) {
        payMapper.insert(pay);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        payMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            payMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Pay pay) {
        // 查询余额
        CPUser user = userMapper.selectById(pay.getUserId());
        if (user.getAccount() < pay.getPrice()) {
            throw new CustomException(500, "您的余额不足，请到个人中心充值");
        }

        user.setAccount(user.getAccount() - pay.getPrice());
        userMapper.updateById(user);

        pay.setStatus("已缴费");
        payMapper.updateById(pay);
    }

    /**
     * 根据ID查询
     */
    public Pay selectById(Integer id) {
        return payMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Pay> selectAll(Pay pay) {
        return payMapper.selectAll(pay);
    }

    /**
     * 分页查询
     */
    public PageInfo<Pay> selectPage(Pay pay, Integer pageNum, Integer pageSize) {
        //如果不是管理员，设置id，查询当前用户相关信息
        LoginUser  currentUser = SecurityUtil.getLoginUser();
        List<String> roleList = currentUser.getRoleList();
        if (!roleList.contains(Role.ROLE_ADMIN)) {
            Long userId = currentUser.getUser().getId();
            if (userId != null && userId >= Integer.MIN_VALUE && userId <= Integer.MAX_VALUE) {
                pay.setId(userId.intValue());
            } else {
                throw new IllegalArgumentException("userId 超出 Integer 范围！");
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Pay> list = this.selectAll(pay);
        return PageInfo.of(list);
    }
}
