package com.lzj.admin.interceptors;

import com.lzj.admin.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author sunyj
 * @Date 2022/3/7 20:13
 * @Version 1.0
 */
public class NoLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户是否登录
        User user = (User)request.getSession().getAttribute("user");
        if (null == user){
            //如果用户当前未登录，或者session过期
            response.sendRedirect("index");
            return false;
        }
        return true;
    }
}
