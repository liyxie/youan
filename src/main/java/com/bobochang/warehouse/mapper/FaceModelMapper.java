package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.FaceModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author HuihuaLi
* @description 针对表【face_model】的数据库操作Mapper
* @createDate 2023-10-20 15:37:44
* @Entity generator.domain.FaceModel
*/
public interface FaceModelMapper extends BaseMapper<FaceModel> {
    // 查找用户的人脸模型
    public String searchFaceModel(int userId);

    // 判断是否有人脸模型
    public Integer haveFace(int userId);

    // 添加用户的人脸模型
//    public void insert(FaceModel faceModel);

    // 删除用户的人脸模型
    public int deleteFaceModel(int userId);
}




