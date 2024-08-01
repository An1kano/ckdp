package org.ckzs.ckdp.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ckzs.ckdp.pojo.Order;

@Mapper
public interface OrderMapper {
    void insert(Order order);
}
