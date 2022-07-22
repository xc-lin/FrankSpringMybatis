package com.lxc.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author Frank_lin
 * @date 2022/7/22
 */

public interface OrderMapper {
    @Select("SELECT 'order'")
    String getOrderName();
}
