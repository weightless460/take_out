package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    public static  final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties properties;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Autowired
    private UserMapper mapper;
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //调用微信接口服务，获取当前微信用户的openID
        Map<String, String> map = new HashMap<>();
        map.put("appid",properties.getAppid());
        map.put("secret",properties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        String s = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject object = JSON.parseObject(s);
        String openid = object.getString("openid");

        //判断id是否为空，是否登录成功
        if (openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判读用户是否为新用户

        User user = mapper.getByOpenId(openid);
        //如果是新用户，自动完成注册
        if (user==null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            mapper.inset(user);
        }
        //返回用户对象
        return user;
    }
}
