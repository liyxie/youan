package com.bobochang.warehouse.service;

import com.bobochang.warehouse.entity.Checkin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.page.PageCheckinDto;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @author HuihuaLi
* @description 针对表【checkin】的数据库操作Service
* @createDate 2023-10-20 15:37:44
*/
public interface CheckinService extends IService<Checkin> {
    // 用于判断是否可以签到
    Result validCanCheckIn(int userId, String date);

    // 签到方法
    Result checkinByFace(HashMap param);

    // 创建人脸模型
    void createFaceModel(String path, int userId);

    // 判断是否有人脸模型
    String haceFace(int userId);

    // 查询用户当天的签到情况
    HashMap searchTodayCheckin(int userId);

    //    查询用户签到的所有记录
    Result searchCheckinDays(int userId);

    // 查询用户签到的总天数
    long searchCheckinDaysCount(int userId);

    // 查询一周的考勤记录
    ArrayList<HashMap> searchWeekCheckin(HashMap param);

    // 查询一月的考勤记录
    ArrayList<HashMap> searchMonthCheckin(HashMap param);

    PageCheckinDto queryChekinPage(PageCheckinDto page, Checkin checkin);

    Result checkin(Checkin checkin);
}
