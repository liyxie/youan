package com.bobochang.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.entity.User;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
 * 2023/7/12 - 10:02
 *
 * @author bobochang
 * @description
 */
public interface UserMapper extends BaseMapper<User> {

    // 根据账号查询用户信息
    public User findUserByCode(String userCode);

    public User findUserById(Integer userId);


    //查询用户总行数的方法
    public int selectUserCount(User user);

    //分页查询用户的方法
    public List<User> selectUserPage(Page page, User user);

    //添加用户的方法
    public int insertUser(User user);

    //根据用户id修改用户状态的方法
    public int updateUserState(User user);

    //根据用户id将用户状态修改为删除状态
    public int setUserDelete(Integer userId);

    //根据用户id修改用户昵称的方法
    public int updateNameById(User user);

    //根据用户id修改密码的方法
    public int updatePwdById(User user);

    public User searchById(int UserId);

    List<String> searchRoleCodeById(int userId);

    List<User> searchUserBySalary();

    List<User> selectAll();
}
