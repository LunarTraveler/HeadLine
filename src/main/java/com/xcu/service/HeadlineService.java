package com.xcu.service;

import com.xcu.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xcu.pojo.vo.PortalVo;
import com.xcu.utils.Result;

/**
* @author 刘涛
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-09-23 20:26:16
*/
public interface HeadlineService extends IService<Headline> {


    Result findNewsPage(PortalVo portalVo);

    Result publish(Headline headline);

    Result findNewPage(PortalVo portalVo);

    Result showHeadlineDetail(Integer hid);

    Result findHeadlineByHid(Integer hid);

    Result updateHeadline(Headline headline);

}
