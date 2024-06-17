package com.bobochang.warehouse.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.Salary;
import com.bobochang.warehouse.entity.User;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.page.PageCheckinDto;
import com.bobochang.warehouse.service.SalaryService;
import com.bobochang.warehouse.mapper.SalaryMapper;
import com.bobochang.warehouse.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
* @author magic'book
* @description 针对表【salary】的数据库操作Service实现
* @createDate 2023-12-11 15:06:13
*/
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary>
    implements SalaryService{
    
    @Autowired
    private SalaryMapper salaryMapper;
    
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public PageCheckinDto querySalaryPage(PageCheckinDto page, Salary salary) {
        int count = salaryMapper.selectSalaryCount(page,salary);
        List<Salary> salaryList = salaryMapper.querySalaryPage(page,salary);
        
        page.setResultList(salaryList);
        page.setTotalNum(count);
        return page;
    }

    @Override
    public void addSalaryOnMonth(PageCheckinDto page) throws ParseException {
        // 获取需要创建的工资单的月份的总天数
        // 创建Calendar对象并设置为提取的日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(page.getStartDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 获取该月份的最后一天的日期
        calendar.set(Calendar.DAY_OF_MONTH, daysInMonth); // 设置为月份的最后一天
        Date lastDayOfMonth = calendar.getTime();
        String lastDayString = sdf.format(lastDayOfMonth);
        page.setEndDate(lastDayString);
        
        // 查询所有有基本工资的用户
        List<User> users = salaryMapper.searchUserBySalary(page);

        for (User user : users){
            System.out.println(user);
            Salary salary = new Salary();
            salary.setUserId(user.getUserId());
            
            salary.setCreateTime(date);
            salary.setMonthDays(daysInMonth);
            salary.setCheckinDays(user.getCheckinSum());
            
            salary.setSalary(user.getSalary());
            
            // 如果实际出勤天数与计薪天数相差不超过四天，则不用扣钱
            // 超过就需要按基本工资除以计薪天数乘以相差天数，然后用基本工资减去
            BigDecimal payableSalary = BigDecimal.valueOf(0.00);
            if(salary.getMonthDays() - salary.getCheckinDays() > 4){
                BigDecimal days = new BigDecimal(salary.getMonthDays());
                // 算出每日工资
                BigDecimal daySalary = salary.getSalary().divide(days, 4, BigDecimal.ROUND_HALF_UP);
                System.out.println(daySalary);
                
                // 缺勤日期
                BigDecimal noCheckinDays = new BigDecimal(salary.getMonthDays() - salary.getCheckinDays());
                System.out.println(salary.getMonthDays() - salary.getCheckinDays());


                payableSalary = salary.getSalary().subtract(noCheckinDays.multiply(daySalary));
                System.out.println(noCheckinDays.multiply(daySalary));
                System.out.println(payableSalary);
            }else{
                payableSalary = salary.getSalary();
            }
            salary.setPayableSalary(payableSalary);
            salary.setStatus("0");
            System.out.println(salary);
            salaryMapper.save(salary);
        }
    }

    @Override
    public int updateSalaryById(Salary salary) {

        BigDecimal payableSalary = BigDecimal.valueOf(0.00);
        if(salary.getMonthDays() - salary.getCheckinDays() > 4){
            BigDecimal days = new BigDecimal(salary.getMonthDays());
            // 算出每日工资
            BigDecimal daySalary = salary.getSalary().divide(days, 4, BigDecimal.ROUND_HALF_UP);

            // 缺勤日期
            BigDecimal noCheckinDays = new BigDecimal(salary.getMonthDays() - salary.getCheckinDays());

            payableSalary = salary.getSalary().subtract(noCheckinDays.multiply(daySalary));
        }else{
            payableSalary = salary.getSalary();
        }
        salary.setPayableSalary(payableSalary);
        BigDecimal num = new BigDecimal(0);
        if (salary.getWithhold().compareTo(num)>0){
            salary.setPayableSalary(salary.getPayableSalary().subtract(salary.getWithhold()));
        }
        salary.setPayableSalary(salary.getPayableSalary().add(salary.getOtherSalary()));
        salary.setPayableSalary(salary.getPayableSalary().add(salary.getOvertimeSalary()));
        
        return salaryMapper.updateSalaryById(salary);
    }
}




