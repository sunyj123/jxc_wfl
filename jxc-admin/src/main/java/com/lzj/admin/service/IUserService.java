package com.lzj.admin.service;

import com.lzj.admin.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.UserQuery;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 老李
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户名查询用户记录
     *
     * @param userName
     * @return
     */
    public User findUserByUserName(String userName);

    void updateUserInfo(User user);

    void updatepassword(String oldpassword,String newpassword,String confirpassword,User user);

    Map<String, Object> userList(UserQuery query);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(Integer[] ids);

}
