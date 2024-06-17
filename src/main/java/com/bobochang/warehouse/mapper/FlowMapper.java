package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.Flow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author HuihuaLi
* @description 针对表【flow】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.Flow
*/
public interface FlowMapper extends BaseMapper<Flow> {
    void insertFlow(Flow flow);

    void updateFlow(Flow flow);

    Flow selectByInstanceId(String instanceId);

    int updateReasonByContract(Flow flow);
}




