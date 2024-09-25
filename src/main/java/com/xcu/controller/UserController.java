package com.xcu.controller;

import com.alibaba.druid.util.StringUtils;
import com.xcu.pojo.User;
import com.xcu.service.UserService;
import com.xcu.utils.JwtHelper;
import com.xcu.utils.Result;
import com.xcu.utils.ResultCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("user")
@CrossOrigin
@RequiredArgsConstructor // lombok简化构造器注入方法的代码
public class UserController {

    private final UserService userService;

    private final JwtHelper jwtHelper;


    @PostMapping("login")
    public Result login(@RequestBody User user) {
        Result result = userService.login(user);
        return result;
    }

    @GetMapping("getUserInfo")
    public Result getUserInfo(@RequestHeader String token) {
        Result result = userService.getUserInfo(token);
        return result;
    }

    @PostMapping("checkUserName")
    public Result checkUserName(String username) {
        Result result = userService.checkUserName(username);
        return result;
    }

    @PostMapping("regist")
    public Result regist(@RequestBody User user) {
        Result result = userService.regist(user);
        return result;
    }

    @RequestMapping("checkLogin")
    public Result checkLogin(@RequestHeader String token){

        if (StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)) {
            return Result.build(null, ResultCodeEnum.NOT_LOGIN);
        }

        return Result.ok(null);
    }

}
