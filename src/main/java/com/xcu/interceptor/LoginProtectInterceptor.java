package com.xcu.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcu.utils.JwtHelper;
import com.xcu.utils.Result;
import com.xcu.utils.ResultCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class LoginProtectInterceptor implements HandlerInterceptor {

    private final JwtHelper jwtHelper;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)){
            Result result = Result.build(null, ResultCodeEnum.NOT_LOGIN);
            // json把result对象转成json字符串，并返回到前端
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().print(json);
            // 拦截
            return false;
        }
        // 放行
        return true;
    }
}