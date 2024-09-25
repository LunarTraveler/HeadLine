package com.xcu.controller;

import com.xcu.pojo.vo.PortalVo;
import com.xcu.service.HeadlineService;
import com.xcu.service.TypeService;
import com.xcu.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("portal")
@CrossOrigin
@RequiredArgsConstructor
public class PortalController {

    private final TypeService typeService;

    private final HeadlineService headlineService;

    @GetMapping("findAllTypes")
    public Result findAllTypes() {
        Result result = typeService.findAllTypes();
        return result;
    }

    // sql语句作用的
    @PostMapping("findNewsPage")
    public Result findNewsPage(@RequestBody PortalVo portalVo) {
        Result result = headlineService.findNewsPage(portalVo);
        return result;
    }

    // 手动映射作用的
    @PostMapping("findNewPage")
    public Result findNewPage(@RequestBody PortalVo portalVo) {
        Result result = headlineService.findNewPage(portalVo);
        return result;
    }

    @PostMapping("showHeadlineDetail")
    public Result showHeadlineDetail(Integer hid) {
        Result result = headlineService.showHeadlineDetail(hid);
        return result;
    }


}
