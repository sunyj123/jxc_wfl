package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Role;
import com.lzj.admin.mapper.RoleMapper;
import com.lzj.admin.pojo.User;
import com.lzj.admin.pojo.UserRole;
import com.lzj.admin.query.RoleQuery;
import com.lzj.admin.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.service.IUserRoleService;
import com.lzj.admin.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
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
 * 角色表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2022-03-11
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private IUserRoleService userRoleService;

    @Override
    public Role findRoleByRoleName(String roleName) {
        return this.baseMapper.selectOne(new QueryWrapper<Role>().eq("is_del", 0).eq("name", roleName));
    }

    @Override
    public Map<String, Object> roleList(RoleQuery query) {

        /**
         * mybatisPlus 提供了专门的分页方式
         */
        IPage<Role> page = new Page<>(query.getPage(),query.getLimit());
        QueryWrapper<Role>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del",0);
        if(StringUtils.isNotBlank(query.getRoleName())){
            queryWrapper.like("name",query.getRoleName());
        }
        page = this.baseMapper.selectPage(page,queryWrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("data",page.getRecords());
        map.put("count",page.getTotal());
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveRole(Role role) {
        //角色名不能为空且不可重
          //角色可用
        AssertUtil.isTrue(!(StringUtils.isNotBlank(role.getName())),"角色名不能为空!");
        AssertUtil.isTrue(null != findRoleByRoleName(role.getName()),"角色名不可重复！");
        role.setIsDel(0);
        AssertUtil.isTrue(!(this.save(role)),"角色添加失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateRole(Role role) {
        AssertUtil.isTrue(!(StringUtils.isNotBlank(role.getName())),"角色名不能为空!");
        Role temp = findRoleByRoleName(role.getName());
        AssertUtil.isTrue((temp != null && temp.getId().equals(role.getId())),"角色名已存在!");
        AssertUtil.isTrue(!(this.updateById(role)),"角色修改失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteRole(Integer id) {
       AssertUtil.isTrue(id ==null ,"请选择要删除的角色!" );
       Role role = this.getById(id);
       AssertUtil.isTrue(role== null,"删除记录不存在!");
       role.setIsDel(1);
       //如果角色被删除，那么user_role表里面对应的角色信息也应该被删除
       userRoleService.remove(new QueryWrapper<UserRole>().eq("role_id",id));

       AssertUtil.isTrue(!(this.updateById(role)),"角色删除失败!");
    }

    @Override
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return this.baseMapper.queryAllRoles(userId);
    }
}
