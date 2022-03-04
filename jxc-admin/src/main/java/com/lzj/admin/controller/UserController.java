package com.lzj.admin.controller;


import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.User;
import com.lzj.admin.service.IUserService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 老李
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("login")
    //最终响应的是json
    @ResponseBody
    public RespBean login(String userName,String password,HttpSession session){
        try {
            User user = userService.login(userName, password);
            session.setAttribute("user",user);
            return RespBean.success("用户登录成功!");
        } catch (ParamsException e) {
            e.printStackTrace();
            return RespBean.error(e.getMsg());
        }catch (Exception e){
            return RespBean.error("用户登录失败!");
        }
    }

    /**
     * 用户信息设置页面
     * @return
     */
    @RequestMapping("setting")
    public String setting(){
        return "user/setting";
    }

    /**
     *
     * @param user 前端传来的User对象
     * @return
     */
    @RequestMapping("updateUserInfo")
    @ResponseBody
    public RespBean updateUserInfo(User user){
        try {
            userService.updateUserInfo(user);
            return RespBean.success("用户信息更新成功!");
        } catch (ParamsException e) {
            e.printStackTrace();
            return RespBean.error(e.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            return RespBean.error("用户信息更新失败!");
        }
    }
}
