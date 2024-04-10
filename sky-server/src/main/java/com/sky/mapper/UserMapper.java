package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openId
     * @return
     */
    @Select("select * from user where openid=#{openId}")
     User getByOpenId(String openId);

    void inset(User user);
}
