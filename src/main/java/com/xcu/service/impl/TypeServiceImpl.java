package com.xcu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xcu.pojo.Type;
import com.xcu.pojo.vo.PortalVo;
import com.xcu.service.TypeService;
import com.xcu.mapper.TypeMapper;
import com.xcu.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 刘涛
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2024-09-23 20:26:17
*/
@Service
@RequiredArgsConstructor
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

    private final TypeMapper typeMapper;


    @Override
    public Result findAllTypes() {

        List<Type> types = typeMapper.selectList(null);
        return Result.ok(types);
    }
}




