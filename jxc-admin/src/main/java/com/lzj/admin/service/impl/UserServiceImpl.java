package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.pojo.User;
import com.lzj.admin.mapper.UserMapper;
import com.lzj.admin.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.StringUtil;
import net.bytebuddy.asm.Advice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 老李
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserByUserName(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<User>().eq("is_del", 0).eq("user_name", userName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        //用户名非空且唯一
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUsername()),"用户名不能为空!");
        User temp = this.findUserByUserName(user.getUsername());
        //当temp不为空且(即数据库里已经存在当前用户名)当前参数中id与库里的id不同时,则不合法
        AssertUtil.isTrue(temp!=null && !(temp.getId().equals(user.getId())),"用户名已存在!");
        AssertUtil.isTrue(!(this.updateById(user)),"用户信息更新失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updatepassword(String oldpassword, String newpassword, String confirpassword,User user) {
        //如果原密码输入错误，修改失败
        if (!(passwordEncoder.matches(oldpassword,user.getPassword()))){
            throw new ParamsException("原密码输入错误，修改失败!");
        }
        //如果确认密码和新密码不匹配，修改失败
        if (!newpassword.equals(confirpassword)){
            throw new ParamsException("确认密码与新密码不一致,请核对!");
        }
        //修改成功
        user.setPassword(passwordEncoder.encode(newpassword));
        this.saveOrUpdate(user);
    }
}
