package com.bobochang.warehouse.controller;

import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.entity.Customer;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.entity.Supply;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LI
 * @date 2023/11/21
 */
@RequestMapping("/customer")
@RestController
@Transactional
@BusLog(name = "客户管理")
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     * 查询所有的客户记录
     * @return
     */
    @GetMapping("/customer-list")
    public Result getAll(){
        List<Customer> customerList = customerService.getAll();
        return Result.ok(customerList);
    }

    @RequestMapping("/customer-page-list")
    public Result storePageList(Page page, Customer customer) {
        log.info(customer.getCustomerName());
        //执行业务
        page = customerService.queryCustomerPage(page, customer);
        //响应
        return Result.ok(page);
    }


    @RequestMapping("/customer-update")
    @BusLog(descrip = "更新客户")
    public Result updateStore(@RequestBody Customer customer) {
        //执行业务
        Result result = customerService.updateCustomer(customer);
        //响应
        return result;
    }

    @RequestMapping("/customer-add")
    @BusLog(descrip = "添加客户")
    public Result addStore(@RequestBody Customer customer) {
        //执行业务
        Result result = customerService.addCustomer(customer);
        //响应
        return result;
    }

    @RequestMapping("/customer-delete/{customerId}")
    @BusLog(descrip = "删除客户")
    public Result deleteStore(@PathVariable Integer customerId) {
        //执行业务
        if(customerService.removeById(customerId)){
            return Result.ok("客户删除成功！");
        }else{
            return Result.err(Result.CODE_ERR_BUSINESS, "客户删除失败！");
        }
    }
}
