package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.pojo.Menu;
import com.lzj.admin.mapper.MenuMapper;
import com.lzj.admin.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.service.IRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2022-03-13
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private IRoleMenuService roleMenuService;
    @Override
    public List<TreeDto> queryAllMenus(Integer roleId) {
        //查询所有的菜单
        List<TreeDto> dtos = this.baseMapper.queryAllMenus();
        //根据角色绑定菜单去查，查询当前角色所有的菜单
        List<Integer> menu_ids = roleMenuService.queryRoleHasAllMenusByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(menu_ids)){
            dtos.forEach(dto->{
                if (menu_ids.contains(dto.getId())){
                    dto.setChecked(true);
                }
            });
        }
        return dtos;
    }
}
