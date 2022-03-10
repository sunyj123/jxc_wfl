package com.lzj.admin.controller;


import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.User;
import com.lzj.admin.service.IUserService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.http.HttpRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.security.Principal;

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

    /**
     * 用户信息设置页面
     * @return
     */
    @RequestMapping("setting")
    public String setting(Principal principal, Model model){
        User user = userService.findUserByUserName(principal.getName());
        model.addAttribute(user);
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

            userService.updateUserInfo(user);
            return RespBean.success("用户信息更新成功!");
    }

    @RequestMapping("password")
    public String password(){
        return "user/password";
    }

    /**
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @param principal springSecurity自带的
     * @return
     */
    @RequestMapping("updateUserPassword")
    @ResponseBody
    public RespBean updatePassword(String oldPassword, String newPassword, String confirmPassword, Principal principal){
        User user =userService.findUserByUserName(principal.getName());
        userService.updatepassword(oldPassword,newPassword,confirmPassword,user);
        return RespBean.success("用户密码更新成功！");
    }
}
