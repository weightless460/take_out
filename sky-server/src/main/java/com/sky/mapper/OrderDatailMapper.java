package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDatailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);
}
