package com.xcu.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xcu.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xcu.pojo.vo.PortalVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author 刘涛
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-09-23 20:26:16
* @Entity com.xcu.pojo.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {

    // 自定义分页查询方法
    IPage<Map> selectPageMap(IPage<Headline> page, @Param("portalVo") PortalVo portalVo);

    // 分页查询头条详情
    Map selectDetailMap(Integer hid);

}




