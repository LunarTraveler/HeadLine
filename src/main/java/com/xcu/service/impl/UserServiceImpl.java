package com.xcu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.xcu.pojo.User;
import com.xcu.service.UserService;
import com.xcu.mapper.UserMapper;
import com.xcu.utils.JwtHelper;
import com.xcu.utils.MD5Util;
import com.xcu.utils.Result;
import com.xcu.utils.ResultCodeEnum;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 刘涛
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-09-23 20:26:17
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final UserMapper userMapper;

    private final JwtHelper jwtHelper;

    @Override
    public Result regist(User user) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, user.getUsername());
        Long count = userMapper.selectCount(queryWrapper);

        if (count == 0) {
            user.setUserPwd(MD5Util.encrypt(user.getUsername()));
            userMapper.insert(user);
            return Result.ok(null);
        }

        return Result.build(null, ResultCodeEnum.USERNAME_USED);
    }

    @Override
    public Result checkUserName(String username) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return Result.ok(null);
        }

        return Result.build(null, ResultCodeEnum.USERNAME_USED);

    }

    @Override
    public Result getUserInfo(String token) {

        // 判定是否在有效期内
        if (jwtHelper.isExpiration(token)) {
            return Result.build(null, ResultCodeEnum.NOT_LOGIN);
        }

        int userId = jwtHelper.getUserId(token).intValue();
        User user = userMapper.selectById(userId);

        if (user != null) {
            // 防止在回送的过程中，密码泄露
            user.setUserPwd(null);
            Map data = new HashMap<>();
            data.put("loginUser", user);
            return Result.ok(data);
        }

        return Result.build(null, ResultCodeEnum.NOT_LOGIN);

    }

    @Override
    public Result login(User user) {

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        // 从数据库中查找这个用户名的用户
        User loginUser = userMapper.selectOne(lambdaQueryWrapper);

        // 账号判断
        if (loginUser == null) {
            // 如果不存在，则返回501用户名不存在
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        // 密码判断
        if (!StringUtils.isNullOrEmpty(loginUser.getUserPwd()) &&
                // 从数据库中查到到密码（加密过后的） 与 前端来的密码进行比对
                loginUser.getUserPwd().equals(MD5Util.encrypt(user.getUserPwd()))) {

            // 为这个用户生成token
            String token = jwtHelper.createToken(loginUser.getUid().longValue());

            Map data = new HashMap();
            data.put("token", token);

            return Result.ok(data);
        }

        // 密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }
}




