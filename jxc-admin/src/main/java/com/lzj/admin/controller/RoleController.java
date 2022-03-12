package com.lzj.admin.controller;


import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Role;
import com.lzj.admin.query.RoleQuery;
import com.lzj.admin.service.IRoleService;
import com.lzj.admin.service.IUserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 老李
 * @since 2022-03-11
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;
    /**
     * 角色管理主页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /**
     * 角色列表展示
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> roleList(RoleQuery query){
        return roleService.roleList(query);
    }

    /**
     * 添加|修改角色页面展示
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateUserPage(Integer id, Model model){
        //如果id不为空，查询当前id对应的信息
        if (null != id){
            model.addAttribute("role",roleService.getById(id));
        }
        return "role/add_update";
    }

    /**
     * 添加用户接口
     * @param role
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveRole(Role role){
        roleService.saveRole(role);
        return RespBean.success("添加用户成功!");
    }

    /**
     * 修改用户接口
     * @param role
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateRole(Role role){
        roleService.updateRole(role);
        return RespBean.success("修改用户成功!");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteRole(Integer id){
        roleService.deleteRole(id);
        return RespBean.success("角色删除成功!");
    }
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }
}
