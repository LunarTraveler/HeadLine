package com.xcu.service;

import com.xcu.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xcu.pojo.vo.PortalVo;
import com.xcu.utils.Result;

/**
* @author 刘涛
* @description 针对表【news_type】的数据库操作Service
* @createDate 2024-09-23 20:26:17
*/
public interface TypeService extends IService<Type> {

    Result findAllTypes();

}
