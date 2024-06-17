package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.page.Page;

import java.util.List;

/**
* @author magic'book
* @description 针对表【customer】的数据库操作Service
* @createDate 2023-11-21 09:46:37
*/
public interface CustomerService extends IService<Customer> {

    List<Customer> getAll();

    Page queryCustomerPage(Page page, Customer customer);

    Result updateCustomer(Customer customer);

    Result addCustomer(Customer customer);
}
