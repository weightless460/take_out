package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtPropertiesl;

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){

        log.info("微信用户登录：{}",userLoginDTO.getCode());
        //微信登录
        User user = userService.wxLogin(userLoginDTO);
        //生成令牌
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        String jwt = JwtUtil.createJWT(jwtPropertiesl.getUserSecretKey(), jwtPropertiesl.getUserTtl(), claims);
        UserLoginVO build = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(jwt)
                .build();

        return Result.success(build);

    }
}
