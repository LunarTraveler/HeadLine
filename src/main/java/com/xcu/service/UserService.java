package com.xcu.service;

import com.xcu.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xcu.utils.Result;

/**
* @author 刘涛
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-09-23 20:26:17
*/
public interface UserService extends IService<User> {

    Result login(User user);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(User user);
}
