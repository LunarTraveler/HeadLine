package com.xcu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xcu.pojo.Headline;
import com.xcu.pojo.vo.HeadlineVo;
import com.xcu.pojo.vo.PortalVo;
import com.xcu.service.HeadlineService;
import com.xcu.mapper.HeadlineMapper;
import com.xcu.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author 刘涛
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-09-23 20:26:16
*/
@Service
@RequiredArgsConstructor
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    private final HeadlineMapper headlineMapper;

    @Override
    public Result updateHeadline(Headline headline) {

        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
        headline.setVersion(version);
        headline.setUpdateTime(new Date());
        headlineMapper.updateById(headline);
        return Result.ok(null);

    }

    @Override
    public Result findHeadlineByHid(Integer hid) {

        Headline headline = headlineMapper.selectById(hid);
        Map data = new HashMap();
        data.put("headline", headline);
        return Result.ok(data);

    }

    @Override
    public Result showHeadlineDetail(Integer hid) {

        Map headLineDetail = headlineMapper.selectDetailMap(hid);
        //拼接头条对象(阅读量和version)进行数据更新
        Headline headline = new Headline();
        headline.setHid(hid);
        headline.setPageViews((int)headLineDetail.get("pageViews") + 1);
        headline.setVersion((int)headLineDetail.get("version")); //乐观锁判断
        headlineMapper.updateById(headline);

        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("headline",headLineDetail);
        return Result.ok(pageInfoMap);

    }

    @Override
    public Result findNewPage(PortalVo portalVo) {
        // 原来是两种的方案，一种是自己手写sql语句，
        // 另一种是用queryWrapper作为条件的（这个在处理属性列的时候太过于麻烦要自己创建一个DTO来接受，不如sql语句）
        // 用mybatis-plus映射不如直接sql语句，如果sql语句不会的话，那么先查出来，在业务层进行转换映射
        // 先获取参数这样比较清晰一点
        String keyWords = portalVo.getKeyWords();
        int type = portalVo.getType();
        int currentPage = portalVo.getPageNum();
        int pageSize = portalVo.getPageSize();

        // 1.条件拼接，并且要非空判断
        LambdaQueryWrapper<Headline> queryWrapper = new LambdaQueryWrapper<>();
        // 这里有条件判断，最好分开写，或者加上if判断
        queryWrapper.like(!StringUtils.isEmpty(keyWords), Headline::getTitle, keyWords);
        queryWrapper.eq(type != 0, Headline::getType, type);

        // 2.设置分页参数
        Page<Headline> page = new Page<>(currentPage, pageSize);

        // 3.分页查询
        Page<Headline> resultPage = headlineMapper.selectPage(page, queryWrapper);

        // 4.结果封装
        List<HeadlineVo> pageData = resultPage.getRecords().stream().map(
          map -> {
              HeadlineVo vo = new HeadlineVo();
              vo.setHid(map.getHid());
              vo.setTitle(map.getTitle());
              vo.setType(map.getType());
              vo.setPageViews(map.getPageViews());
              vo.setPublisher(map.getPublisher());
              vo.setPastHours(calculatePastHours(map.getCreateTime()));
              return vo;
          }
        ).collect(Collectors.toList());

        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("pageData", pageData);
        pageInfo.put("pageNum", resultPage.getCurrent());
        pageInfo.put("pageSize", resultPage.getSize());
        pageInfo.put("totalPage", resultPage.getTotal());
        pageInfo.put("totalSize", resultPage.getSize());

        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("pageInfo",pageInfo);
        // 响应JSON
        return Result.ok(pageInfoMap);
    }

    private int calculatePastHours(Date creatTime) {
        // 计算时间差（小时）
        long diffInMillis = new Date().getTime() - creatTime.getTime();
        return (int) TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public Result publish(Headline headline) {

        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());
        headline.setPageViews(0);
        headlineMapper.insert(headline);
        return Result.ok(null);

    }

    @Override
    public Result findNewsPage(PortalVo portalVo) {

        //2.分页参数
        IPage<Headline> page = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());

        //3.分页查询(自己实现了sql语句)
        headlineMapper.selectPageMap(page, portalVo);

        //4.结果封装
        //分页数据封装
        Map<String,Object> pageInfo =new HashMap<>();
        pageInfo.put("pageData",page.getRecords());
        pageInfo.put("pageNum",page.getCurrent());
        pageInfo.put("pageSize",page.getSize());
        pageInfo.put("totalPage",page.getPages());
        pageInfo.put("totalSize",page.getTotal());

        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("pageInfo",pageInfo);
        // 响应JSON
        return Result.ok(pageInfoMap);

    }

}




