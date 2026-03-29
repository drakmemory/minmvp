package com.minmvp.interceptor;

import com.alibaba.fastjson.JSON;
import com.minmvp.common.Constants;
import com.minmvp.common.Result;
import com.minmvp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(Constants.JWT_HEADER);
        if (authHeader != null && authHeader.startsWith(Constants.JWT_PREFIX)) {
            String token = authHeader.substring(Constants.JWT_PREFIX.length());
            if (jwtUtil.isTokenValid(token)) {
                request.setAttribute("userId", jwtUtil.getUserId(token));
                request.setAttribute("username", jwtUtil.getUsername(token));
                request.setAttribute("role", jwtUtil.getRole(token));
                return true;
            }
        }

        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.error(401, "未授权，请先登录")));
        return false;
    }
}
