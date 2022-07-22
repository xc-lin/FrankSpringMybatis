package com.lxc.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */

public interface MemberMapper {
    @Select("SELECT 'user'")
    String getUserName();
}
