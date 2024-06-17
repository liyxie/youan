package com.bobochang.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.dto.AssignRoleDto;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Role;
import com.bobochang.warehouse.entity.RoleAuth;
import com.bobochang.warehouse.page.Page;

import java.util.List;

public interface RoleService extends IService<Role> {

    //查询所有角色的业务方法
    public List<Role> getAllRole();

    //查询用户已分配的角色的业务方法
    public List<Role> queryRolesByUserId(Integer userId);

    //给用户分配角色的业务方法
    public void assignRole(AssignRoleDto assignRoleDto);

    //分页查询角色的业务方法
    public Page queryRolePage(Page page, Role role);

    //添加角色的业务方法
    public Result saveRole(Role role);

    //修改角色状态的业务方法
    public Result updateRoleState(Role role);

    //查询角色已分配的权限(菜单)的业务方法
    public List<Integer> queryAuthIds(Integer roleId);

    //删除角色的业务方法
    public void deleteRole(Integer roleId);

    //修改角色描述的业务方法
    public Result updateRoleDesc(Role role);

    List<String> findAllUserIdByRoleId(Integer roleId);

    String findRolesByUserId(Integer userId);
}