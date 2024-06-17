package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.UserRole;
import com.bobochang.warehouse.mapper.UserRoleMapper;
import com.bobochang.warehouse.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
* @author HuihuaLi
* @description 针对表【user_role(用户角色表)】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

}




