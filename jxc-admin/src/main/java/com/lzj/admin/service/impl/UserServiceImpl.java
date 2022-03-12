package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.pojo.User;
import com.lzj.admin.mapper.UserMapper;
import com.lzj.admin.pojo.UserRole;
import com.lzj.admin.query.UserQuery;
import com.lzj.admin.service.IUserRoleService;
import com.lzj.admin.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.StringUtil;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private IUserRoleService userRoleService;

    @Override
    public User findUserByUserName(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<User>().eq("is_del", 0).eq("user_name", userName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        //用户名非空且唯一
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUsername()), "用户名不能为空!");
        User temp = this.findUserByUserName(user.getUsername());
        //当temp不为空且(即数据库里已经存在当前用户名)当前参数中id与库里的id不同时,则不合法
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(user.getId())), "用户名已存在!");
        AssertUtil.isTrue(!(this.updateById(user)), "用户信息更新失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatepassword(String oldpassword, String newpassword, String confirpassword, User user) {
        //如果原密码输入错误，修改失败
        if (!(passwordEncoder.matches(oldpassword, user.getPassword()))) {
            throw new ParamsException("原密码输入错误，修改失败!");
        }
        //如果确认密码和新密码不匹配，修改失败
        if (!newpassword.equals(confirpassword)) {
            throw new ParamsException("确认密码与新密码不一致,请核对!");
        }
        //修改成功
        user.setPassword(passwordEncoder.encode(newpassword));
        this.saveOrUpdate(user);
    }

    @Override
    public Map<String, Object> userList(UserQuery query) {
        IPage<User> page = new Page<User>(query.getPage(), query.getLimit());
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        //列表中只展示isdel为0的数据
        queryWrapper.eq("is_del", 0);
        if (StringUtils.isNotBlank(query.getUserName())){
            queryWrapper.like("user_name",query.getUserName());
        }
        page = this.baseMapper.selectPage(page,queryWrapper);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("data",page.getRecords());
        map.put("count",page.getTotal());
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUser(User user) {
        /**
         * 用户名不能为空，且不可重复
         * 密码统一为123456
         * 添加的用户要可用
         */
        AssertUtil.isTrue(!StringUtils.isNotBlank(user.getUsername()),"用户名不能为空!");
        AssertUtil.isTrue(null != this.findUserByUserName(user.getUsername()),"用户名已存在!");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setIsDel(0);
        AssertUtil.isTrue(false ==this.save(user),"用户添加失败!");

        //此时已经添加成功，我们把添加成功的用户查出来
        User temp = this.findUserByUserName(user.getUsername());

        /**
         * 给用户分配角色
         */
        relationUserRole(temp.getId(),user.getRoleIds());



    }

    private void relationUserRole(Integer userId, String roleIds) {
        /**
         * 修改操作也会用到此方法，所以此处不能只考虑添加操作
         * 添加:将拿到的id和roleIds分割后的每一个存到user_role里
         * 修改:首先判断该用户原先是否存在已经分配的角色，如果没有分配，直接添加，
         * 如果分配了，此时需要比较新分配的角色与原角色是否存在冲突，处理起来相对麻烦，
         * 取最好的方法就是无论是在添加用户，还是修改用户的时候，涉及到角色有关的操作，都直接将原来的角色删除，重新添加
         * 删除：无论是删除角色，还是删除用户，都要对应的将user_role中的信息删除
         */

        //先去查询userrole表通过userid查询表里面有多少条数据
        int count = userRoleService.count(new QueryWrapper<UserRole>().eq("user_id",userId));
        //如果有数据就删除
        if (count>0) {
            userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
        }
        //没有数据就直接添加
        if (StringUtils.isNotBlank(roleIds)){
            String[] roleids = roleIds.split(",");
            for (String roleid : roleids) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleid));
                userRole.setUserId(userId);
                AssertUtil.isTrue(!(userRoleService.save(userRole)),"修改角色失败!");
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUser(User user) {

        /**
         * 用户名不能为空，且不可重复
         */
        AssertUtil.isTrue(!StringUtils.isNotBlank(user.getUsername()),"用户名不能为空!");
        User temp = this.findUserByUserName(user.getUsername());
        AssertUtil.isTrue((null !=temp && temp.getId() != user.getId()),"用户名已存在!");
        //修改角色
        relationUserRole(temp.getId(),user.getRoleIds());
        AssertUtil.isTrue(!(this.updateById(user)),"更新失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteUser(Integer[] ids) {
        //首先保证id有效并存在
        AssertUtil.isTrue((null == ids || ids.length == 0),"请选择待删除的记录id");
        List<User> users = new ArrayList<>();
        for (Integer id : ids) {
            User user = this.getById(id);

            //如果用户被删除，那么user_role中的数据也要被删除
            userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",id));

            user.setIsDel(1);
            users.add(user);
        }
        //updateBatchById实际只是进行了批量更新操作，并没有实际删除数据
        AssertUtil.isTrue(!(this.updateBatchById(users)),"用户删除失败!");



    }
}
