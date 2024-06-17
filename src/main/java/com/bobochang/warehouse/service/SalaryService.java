package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.Salary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.page.PageCheckinDto;

import java.text.ParseException;

/**
* @author magic'book
* @description 针对表【salary】的数据库操作Service
* @createDate 2023-12-11 15:06:13
*/
public interface SalaryService extends IService<Salary> {

    PageCheckinDto querySalaryPage(PageCheckinDto page, Salary salary);

    void addSalaryOnMonth(PageCheckinDto page) throws ParseException;

    int updateSalaryById(Salary salary);
}
