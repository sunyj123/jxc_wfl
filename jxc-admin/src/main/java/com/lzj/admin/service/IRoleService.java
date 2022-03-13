package com.lzj.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.User;
import com.lzj.admin.query.RoleQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 老李
 * @since 2022-03-11
 */
public interface IRoleService extends IService<Role> {

    Map<String, Object> roleList(RoleQuery query);

    void saveRole(Role role);

    void updateRole(Role role);

    Role findRoleByRoleName(String roleName);

    void deleteRole(Integer id);

    List<Map<String, Object>> queryAllRoles(Integer userId);

    void addGrant(Integer[] mids, Integer roleId);
}
