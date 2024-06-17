package com.bobochang.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Auth;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【auth_info(权限表)】的数据库操作Service
* @createDate 2023-10-19 17:22:39
*/
public interface AuthInfoService extends IService<Auth> {
    List<Auth> findAuthTree(Integer userId);
}
