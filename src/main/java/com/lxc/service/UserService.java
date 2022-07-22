package com.lxc.service;

import com.lxc.mapper.MemberMapper;
import com.lxc.mapper.OrderMapper;
import com.lxc.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */
@Service
public class UserService {

    @Autowired
    // mybatis产生这个代理对象--->Bean
    private UserMapper userMapper;

    @Autowired
    // mybatis产生这个代理对象--->Bean
    private OrderMapper orderMapper;

    @Autowired
    // mybatis产生这个代理对象--->Bean
    private MemberMapper memberMapper;





    public void test(){
        System.out.println(userMapper.getUserName());
        System.out.println(orderMapper.getOrderName());
        System.out.println(memberMapper.getUserName());
    }
}
