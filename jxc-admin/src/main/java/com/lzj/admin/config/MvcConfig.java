package com.lzj.admin.config;

import com.lzj.admin.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author sunyj
 * @Date 2022/3/7 20:22
 * @Version 1.0
 * @Desprection 生效拦截器
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor())
        .addPathPatterns("/**")//拦截所有资源
        .excludePathPatterns("/index","/user/login",
                "/css/**","/error/**","/images/**","/js/**","/lib/**");

    }
}
