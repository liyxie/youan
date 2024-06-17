package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.Customer;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.mapper.CustomerMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author magic'book
* @description 针对表【customer】的数据库操作Service实现
* @createDate 2023-11-21 09:46:37
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    
    @Override
    public List<Customer> getAll() {
        return customerMapper.getAll();
    }

    @Override
    public Page queryCustomerPage(Page page, Customer customer) {
        //查询客户总行数
        int Count = customerMapper.selectCustomerCount(customer);

        //分页查询客户
        List<Customer> customers = customerMapper.selectCustomerPage(page, customer);

        //将查到的总行数和当前页数据封装到Page对象
        page.setTotalNum(Count);
        page.setResultList(customers);

        return page;    
    }

    @Override
    public Result updateCustomer(Customer customer) {
        //根据仓库id修改仓库
        int i = customerMapper.updateCustomerById(customer);
        if(i>0){
            return Result.ok("客户修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "客户修改失败！");
    }

    @Override
    public Result addCustomer(Customer customer) {
        //添加仓库
        int i = customerMapper.insertCustomer(customer);
        if(i>0){
            return Result.ok("客户添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "客户添加失败！");    
    }

}




