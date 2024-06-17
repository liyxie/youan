package com.bobochang.warehouse.mapper;

import com.bobochang.warehouse.entity.Checkin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobochang.warehouse.page.PageCheckinDto;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author HuihuaLi
 * @description 针对表【checkin】的数据库操作Mapper
 * @createDate 2023-10-20 15:37:44
 * @Entity generator.domain.Checkin
 */
public interface CheckinMapper extends BaseMapper<Checkin> {
    // 判断日期内是否有考勤记录
    public Integer haveCheckin(HashMap param);

    //    public void insert(Checkin checkin);
    public Checkin searchUserId(int userId);

    public HashMap searchTodayCheckin(int userId);

    public long searchCheckinDays(int userId);

    public ArrayList<HashMap> searchWeekCheckin(HashMap<String, String> param);

    public ArrayList<HashMap> searchAllCheckin(int userId);

    public int selectCheckinCount(PageCheckinDto page);

    // 分页查询合同
    public ArrayList<HashMap> selectCheckinPage(PageCheckinDto page, Checkin checkin);

    public String searchRankingWorkByUserId(int uerId);
    public String searchRankingOffByUserId(int uerId);
}




