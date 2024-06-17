package com.bobochang.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.entity.Auth;
import com.bobochang.warehouse.entity.BusLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface AuthMapper extends BaseMapper<Auth> {

	//根据用户id查询用户所有权限(菜单)的方法
	public List<Auth> findAllAuth(int userId);

	//查询所有状态正常的权限(菜单)的方法
	public List<Auth> getAllAuth();

	//根据角色id删除给角色已分配的所有权限(菜单)
	public int delAuthByRoleId(Integer roleId);

	//添加角色权限(菜单)关系的方法
	public void insertRoleAuth(Integer roleId, Integer authId);

}
