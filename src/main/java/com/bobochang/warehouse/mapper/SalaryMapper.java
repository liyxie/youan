package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.Salary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.entity.User;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.page.PageCheckinDto;

import java.util.List;

/**
* @author magic'book
* @description 针对表【salary】的数据库操作Mapper
* @createDate 2023-12-11 15:06:13
* @Entity com.bobochang.warehouse.entity.Salary
*/
public interface SalaryMapper extends BaseMapper<Salary> {

    List<Salary> querySalaryPage(PageCheckinDto page, Salary salary);

    List<User> searchUserBySalary(PageCheckinDto page);

    void save(Salary salary);

    int updateSalaryById(Salary salary);

    int selectSalaryCount(PageCheckinDto page, Salary salary);
}




