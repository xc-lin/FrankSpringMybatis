package com.lxc.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */

public interface UserMapper {
    @Select("SELECT 'user'")
    String getUserName();
}
