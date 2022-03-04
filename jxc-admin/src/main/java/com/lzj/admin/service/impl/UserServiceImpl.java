package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.pojo.User;
import com.lzj.admin.mapper.UserMapper;
import com.lzj.admin.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.StringUtil;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 老李
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    /**
     * 用户登录方法
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User login(String userName, String password) {
        //两个参数必须合法
        AssertUtil.isTrue(StringUtil.isEmpty(userName),"用户名不能为空!");
        AssertUtil.isTrue(StringUtil.isEmpty(password),"密码不能为空!");
        //用户名和密码不为空，接下来判断用户是否存在
        User user = this.findUserByUserName(userName);
        AssertUtil.isTrue(null==user,"用户不存在或已注销!");
        /**
         * 后续会引入SpringSecurity使用框架处理密码
         */
        AssertUtil.isTrue(!(user.getPassword().equals(password)),"密码输入错误!");
        return user;
    }

    @Override
    public User findUserByUserName(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<User>().eq("is_del", 0).eq("user_name", userName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        //用户名非空且唯一
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUserName()),"用户名不能为空!");
        User temp = this.findUserByUserName(user.getUserName());
        //当temp不为空且(即数据库里已经存在当前用户名)当前参数中id与库里的id不同时,则不合法
        AssertUtil.isTrue(temp!=null && !(temp.getId().equals(user.getId())),"用户名已存在!");
        AssertUtil.isTrue(!(this.updateById(user)),"用户信息更新失败!");
    }

}
