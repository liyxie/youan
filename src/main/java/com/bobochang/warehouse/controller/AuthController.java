package com.bobochang.warehouse.controller;

import com.bobochang.warehouse.entity.Auth;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/auth")
@RestController
public class AuthController {

    //注入AuthService
    @Resource
    private AuthService authService;

    /**
     * 查询整个权限(菜单)树的url接口/auth/auth-tree
     */
    @GetMapping("/auth-tree")
    public Result allAuthTree() {
        //执行业务
        List<Auth> allAuthTree = authService.allAuthTree();
        //响应
        return Result.ok(allAuthTree);
    }

}