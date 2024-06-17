package com.bobochang.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.entity.ProductType;
import com.bobochang.warehouse.mapper.ProductTypeMapper;
import com.bobochang.warehouse.service.ProductTypeService;
import org.springframework.stereotype.Service;

/**
* @author HuihuaLi
* @description 针对表【product_type(商品分类表)】的数据库操作Service实现
* @createDate 2023-10-20 15:37:44
*/
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType>
    implements ProductTypeService {

}




