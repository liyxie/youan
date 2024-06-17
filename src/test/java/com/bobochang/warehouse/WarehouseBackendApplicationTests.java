package com.bobochang.warehouse;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bobochang.warehouse.entity.Contract;
import com.bobochang.warehouse.entity.CurrentUser;
import com.bobochang.warehouse.entity.ProductMaterial;
import com.bobochang.warehouse.mapper.ContractMapper;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.CheckinService;
import com.bobochang.warehouse.service.ContractService;
import com.bobochang.warehouse.service.UserInfoService;
import com.bobochang.warehouse.utils.DigestUtil;
import org.apache.el.parser.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class WarehouseBackendApplicationTests {

    @Test
    void contextLoads() {
        List<String> urlList = new ArrayList<>();
        urlList.add("/contract/download-image/.*");

        if (urlList.contains("/contract/download-image/t01.jpg")) {
            System.out.println('t');
        } else {
            System.out.println('f');
        }


        String targetUrl = "/contract/download-image/t01.jpg";

        Pattern pattern = Pattern.compile(urlList.get(0));
        Matcher matcher = pattern.matcher(targetUrl);
        Matcher matcher1 = Pattern.compile("/contract/download-image/.*").matcher(targetUrl);
        if (Pattern.compile("/contract/download-image/.*").matcher(targetUrl).matches()) {
            System.out.println('t');
        } else {
            System.out.println('f');
        }
    }
    
    @Autowired
    private ContractMapper contractMapper;
    
    @Autowired
    private ContractService contractService;
    @Test
    void test() {
//        LambdaQueryWrapper<ProductMaterial> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ProductMaterial::getProductId, 37);
//        queryWrapper.select(ProductMaterial::get)
        Contract contract = contractService.getBaseMapper().selectById(109);
        System.out.println(contract);
        
    }
}
