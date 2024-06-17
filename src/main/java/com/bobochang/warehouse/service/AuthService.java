package com.bobochang.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.dto.AssignAuthDto;
import com.bobochang.warehouse.entity.Auth;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【auth_info(权限表)】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface AuthService extends IService<Auth> {
    //根据用户id查询用户权限(菜单)树的业务方法
    public List<Auth> findAuthTree(Integer userId);

    //查询整个权限(菜单)树的业务方法
    public List<Auth> allAuthTree();

    //给角色分配权限(菜单)的业务方法
    public void assignAuth(AssignAuthDto assignAuthDto);
}
