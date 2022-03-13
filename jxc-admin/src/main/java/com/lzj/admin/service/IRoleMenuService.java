package com.lzj.admin.service;

import com.lzj.admin.pojo.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author 老李
 * @since 2022-03-13
 */
public interface IRoleMenuService extends IService<RoleMenu> {
    List<Integer> queryRoleHasAllMenusByRoleId(Integer roleId);


}
