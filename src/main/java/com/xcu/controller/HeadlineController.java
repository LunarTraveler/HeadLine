package com.xcu.controller;

import com.alibaba.druid.util.StringUtils;
import com.xcu.pojo.Headline;
import com.xcu.pojo.vo.PortalVo;
import com.xcu.service.HeadlineService;
import com.xcu.utils.JwtHelper;
import com.xcu.utils.Result;
import com.xcu.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadlineController {

    // 最原始的构造器注入
    private final HeadlineService headlineService;

    private final JwtHelper jwtHelper;

    public HeadlineController(HeadlineService headlineService, JwtHelper jwtHelper) {
        this.headlineService = headlineService;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline, @RequestHeader String token){

        int userId = jwtHelper.getUserId(token).intValue();
        headline.setPublisher(userId);
        Result result = headlineService.publish(headline);
        return result;

    }

    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid){
        Result result = headlineService.findHeadlineByHid(hid);
        return result;
    }

    @PostMapping("update")
    public Result update(@RequestBody Headline headline){
        Result result = headlineService.updateHeadline(headline);
        return result;
    }

    @PostMapping("removeByHid")
    public Result removeByHid(Integer hid){
        headlineService.removeById(hid);
        return Result.ok(null);
    }


}
