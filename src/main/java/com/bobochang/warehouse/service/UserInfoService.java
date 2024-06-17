package com.bobochang.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.User;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author HuihuaLi
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface UserInfoService extends IService<User> {

    public User findUserByCode(String userCode);

    public Page queryUserPage(Page page, User user);

    //添加用户的业务方法
    public Result saveUser(User user);

    //修改用户状态的业务方法
    public Result updateUserState(User user);

    //根据用户id删除用户的业务方法
    public int deleteUserById(Integer userId);

    //修改用户昵称的业务方法
    public Result updateUserName(User user);

    //重置密码的业务方法
    public Result resetPwd(Integer userId);

    // 查询用户的创建日期
    String searchUserHiredate(int userId);

    Result searchById(int userId);

    List<String> searchRoleCodeById(int userId);

    List<User> searchUserBySalary();
}
