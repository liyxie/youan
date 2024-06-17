package com.bobochang.warehouse.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bobochang
 * @Description
 * @Date 2024/5/21 - 15:55
 */
@Component
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String tianUrl = "https://apis.tianapi.com/jiejiari/index?key=";
    private static final String tianApi = "f8d4debfd25853b09dff2b037b5bfd4a";

    @PostConstruct
    public void init() {
        fetchAndStoreHolidayInfo();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void fetchAndStoreHolidayInfo() {
        String date = DateUtil.today();

        try {
            String result = HttpUtil.get(tianUrl + tianApi + "&date=" + date + "&type=0");
            JSONObject jsonObject = new JSONObject(result);
            JSONObject resultObject = jsonObject.getJSONObject("result");
            JSONArray listArray = resultObject.getJSONArray("list");
            Integer isNotWork = listArray.getJSONObject(0).getInt("isnotwork");

            String redisKey = date + "-isNotWork";
            redisTemplate.opsForValue().set(redisKey, isNotWork.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
