package com.bobochang.warehouse.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.intern.InternUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.warehouse.config.SystemConstants;
import com.bobochang.warehouse.entity.Checkin;
import com.bobochang.warehouse.entity.FaceModel;
import com.bobochang.warehouse.entity.Result;
import com.bobochang.warehouse.mapper.*;
import com.bobochang.warehouse.page.PageCheckinDto;
import com.bobochang.warehouse.service.CheckinService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuihuaLi
 * @description 针对表【checkin】的数据库操作Service实现
 * @createDate 2023-10-20 15:37:44
 */
@Service
@Slf4j
public class CheckinServiceImpl extends ServiceImpl<CheckinMapper, Checkin>
        implements CheckinService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SystemConstants constants;

    @Autowired
    private HolidaysMapper holidaysMapper;

    @Autowired
    private WorkdayMapper workdayMapper;

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private FaceModelMapper faceModelMapper;

    @Value("${face.createFaceModelUrl}")
    private String createFaceModelUrl;

    @Value("${face.checkinUrl}")
    private String checkinUrl;

    @Value("${tian.api}")
    private String tianApi;

    @Value("${tian.url}")
    private String tianUrl;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result validCanCheckIn(int userId, String date) {
        Integer isNotWork = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(date + "-isNotWork")));

        String status = ""; // 考勤状态

        String type;
        if (DateUtil.date().isWeekend()) {
            type = "节假日";
        } else if (isNotWork == 1) {
            type = "节假日";
        } else {
            type = "工作日";
        }

        if (type.equals("节假日")) {
            status = "节假日不需要考勤";
        } else {
            HashMap map = new HashMap();
            map.put("userId", userId);
            map.put("date", date);
            if (checkinMapper.searchUserId(userId) == null) {
                Checkin checkin = new Checkin();
                checkin.setUserId(userId);
                checkin.setIdentification(0);
                checkin.setDate(DateUtil.parse(date));
                checkinMapper.insert(checkin);
            }
            Integer checkinIdentification = checkinMapper.haveCheckin(map);
            if (checkinIdentification == 1) {
                status = "今日已上班打卡";
            } else if (checkinIdentification == 2) {
                status = "今天已下班打卡";
            } else if (checkinIdentification == 0) {
                status = "当前可以考勤";
            }

        }
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("rankingWork",checkinMapper.searchRankingWorkByUserId(userId));
        map.put("rankingOff",checkinMapper.searchRankingOffByUserId(userId));
        return Result.ok(map);
    }


    // 签到方法(人脸)
    @Override
    public Result checkinByFace(HashMap param) {
        Date d1 = DateUtil.date();
        Date d2 = DateUtil.parse(DateUtil.today() + " " + constants.attendanceTime); // 上班考勤的开始时间
        Date d3 = DateUtil.parse(DateUtil.today() + " " + constants.attendanceEndTime);// 上班考勤的结束时间

        int status = 1;
        if (d1.compareTo(d2) <= 0) {
            // 早于上班时间
            status = 1;
        } else if (d1.compareTo(d2) > 0 && d1.compareTo(d3) < 0) {
            // 迟到了
            status = 2;
        } else {
            return Result.err(500, "超出考勤时间段，无法考勤");
        }

        // 判断是否存在人脸模型
        int userId = (Integer) param.get("userId");
        String faceModel = faceModelMapper.searchFaceModel(userId);
        if (faceModel == null) {
            return Result.err(500, "不存在人脸模型");
        } else {
            String path = (String) param.get("path"); // 上传的人脸图片地址

            if (check(path, userId)) {
//            if(1==1){
                String city = "测试地址";
                String district = "测试地址";
                String address = "测试地址";
                String country = "测试地址";
                String province = "测试地址";
                // 此处获得到微信小程序给的真实地址
                int risk = 1;
                //保存签到记录
                Checkin entity = new Checkin();
                entity.setUserId(userId);
                entity.setAddress(address);
                entity.setStatus((int) status);
                entity.setRisk(risk);
                entity.setDate(DateUtil.parse(DateUtil.today()));
                checkinMapper.insert(entity);
                return Result.ok("签到成功");
            } else {
                return Result.err(500, "人脸识别失败，签到失败");
            }
        }
    }

    public boolean check(String path, int userId) {
        try {
            // 设置API接口URL
            String url = checkinUrl;  // 替换为实际的 Flask 应用 URL

            RestTemplate restTemplate = new RestTemplate();

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 构建请求体
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("photo", new FileSystemResource(path));
            requestBody.add("userId", userId);
            requestBody.add("userName", "jiangwen");

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    String.class
            );

            log.info(String.valueOf(response.getBody()));

            // 处理响应结果
            if (response.getStatusCode().is2xxSuccessful()) {
                if (String.valueOf(response.getBody()).equals("jiangwen")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("HTTP请求错误");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("发生未知错误");
        }
    }

    @Override
    public Result checkin(Checkin checkin) {
        Date now = DateUtil.date();

        int userId = checkin.getUserId();
        Checkin entity = checkinMapper.searchUserId(userId);
        Integer checkinIdentification = entity.getIdentification();
        // 此处获得到微信小程序给的真实地址
        int risk = 1;
        //保存签到记录
        entity.setUserId(userId);
        // entity.setAddress(checkin.getAddress());
        entity.setRisk(risk);
        entity.setBrand(checkin.getBrand());
        if (checkinIdentification == 0) {
            entity.setWorkTime(now);
            entity.setIdentification(1);
            entity.setStatus(1);
            entity.setWorkAddr(checkin.getAddress());
        } else if (checkinIdentification == 1) {
            entity.setOffTime(now);
            entity.setStatus(3);
            entity.setIdentification(2);
            entity.setOffAddr(checkin.getAddress());
        } else {
            entity.setIdentification(0);
            entity.setStatus(2);
        }

        QueryWrapper<Checkin> checkinQueryWrapper = new QueryWrapper<>();
        checkinQueryWrapper.eq("user_id", userId);
        checkinQueryWrapper.eq("date", DateUtil.today());
        checkinMapper.update(entity, checkinQueryWrapper);
        return Result.ok("签到成功", checkinMapper.searchUserId(userId));
    }

    @Override
    public void createFaceModel(String path, int userId) {

        if (!uploadFaceModel(path, userId)) {
            throw new RuntimeException("创建模型失败！");
        } else {
            FaceModel entity = new FaceModel();
            entity.setUserId(userId);
            entity.setFaceModel("/images/" + userId + ".jpg");
//            faceModelDao.insert(entity);
            faceModelMapper.insert(entity);
        }
    }

    @Override
    public String haceFace(int userId) {
        boolean bool = faceModelMapper.haveFace(userId) != null;
        return bool ? "该用户已经有人脸模型了" : "可以为该用户创建人脸模型";
    }

    private boolean uploadFaceModel(String path, int userId) {
        try {
            // 设置API接口URL
            String url = createFaceModelUrl;  // 替换为实际的 Flask 应用 URL

            System.out.println(path);
            System.out.println(userId);
            System.out.println(url);
            RestTemplate restTemplate = new RestTemplate();

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 构建请求体
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("photo", new FileSystemResource(path));
            requestBody.add("userId", userId);

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    String.class
            );

            // 处理响应结果
            if (response.getStatusCode().is2xxSuccessful()) {
                String result = response.getBody();
                System.out.println(result);
                return true;
            } else {
                System.out.println("请求失败");
                return false;
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("HTTP请求错误");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("发生未知错误");
        }
    }

    @Override
    public HashMap searchTodayCheckin(int userId) {
        HashMap map = checkinMapper.searchTodayCheckin(userId);
        return map;
    }

    @Override
    public Result searchCheckinDays(int userId) {
        try {
            long days = checkinMapper.searchCheckinDays(userId);
            ArrayList<HashMap> list = checkinMapper.searchAllCheckin(userId);
            HashMap map = new HashMap<>();
            map.put("days", days);
            map.put("list", list);
            return Result.ok(map);
        } catch (Exception e) {
            return Result.err(500, String.valueOf(e));
        }
    }

    @Override
    public long searchCheckinDaysCount(int userId) {
        long days = checkinMapper.searchCheckinDays(userId);
        return days;
    }

    @Override
    public ArrayList<HashMap> searchWeekCheckin(HashMap param) {
        // 查看这一段时间的考勤天数
        ArrayList<HashMap<String, String>> checkinList = checkinMapper.searchWeekCheckin(param);

        // 查看这一段时间的特殊节假日
        String startDate = param.get("startDate").toString();
        String endDate = param.get("endDate").toString();
        String redisKey = "holidaysList:workdaysList:" + startDate + "-" + endDate;
        String result = redisTemplate.opsForValue().get(redisKey);
        if (result == null) {
            result = HttpUtil.get(tianUrl + tianApi + "&date=" + startDate + "~" + endDate + "&type=3");
            redisTemplate.opsForValue().set(redisKey, result);
        }
        JSONObject jsonObject = new JSONObject(result);
        JSONObject resultObject = jsonObject.getJSONObject("result");
        JSONArray listArray = resultObject.getJSONArray("list");
        List<String> holidaysList = listArray.stream()
                .map(item -> (JSONObject) item)
                .filter(listObj -> StrUtil.isNotEmpty(listObj.getStr("vacation")))
                .map(listObj -> listObj.getStr("vacation"))
                .flatMap(vacationStr -> JSONUtil.toList(vacationStr, String.class).stream())
                .distinct()
                .collect(Collectors.toList());
//        ArrayList holidaysList = holidaysMapper.searchHolidaysInRange(param);
//        查看这一段时间的特殊工作日
//        ArrayList workdayList = workdayMapper.searchWorkdayInRange(param);
        List<String> workdayList = listArray.stream()
                .map(item -> (JSONObject) item)
                .filter(listObj -> StrUtil.isNotEmpty(listObj.getStr("remark")))
                .map(listObj -> listObj.getStr("remark"))
                .flatMap(vacationStr -> JSONUtil.toList(vacationStr, String.class).stream())
                .distinct()
                .collect(Collectors.toList());
        // 获取开始日期和结束日期，构造一个集合
        DateTime startDateTime = DateUtil.parseDate(startDate);
        DateTime endDateTime = DateUtil.parseDate(endDate);
        DateRange range = DateUtil.range(startDateTime, endDateTime, DateField.DAY_OF_MONTH);

        // 判断一周内每日是否特殊工作日和特殊节假日
        ArrayList<HashMap> list = new ArrayList<>();
        Integer userId = (Integer) param.get("userId");
        range.forEach(one -> {
            QueryWrapper<Checkin> checkinQueryWrapper = new QueryWrapper<>();
            checkinQueryWrapper.eq("user_id", userId);

            String date = DateUtil.formatDate(one);
            String type = "工作日";
            // 判断是否是周末先，是周末就先定义为节假日
            if (one.isWeekend()) {
                type = "节假日";
            }

            // 判断当日是否是特殊节假日
            if (holidaysList != null && holidaysList.contains(date)) {
                type = "节假日";
            }

            // 判断当日是否是特殊工作日
            else if (workdayList != null && workdayList.contains(date)) {
                type = "工作日";
            }

            String status = ""; // 空字符串
            // 这里判断当天是否已经发生了，不是未来的日期
            // DateUtil.compare(获取到的这天,当天)<=0  <=0说明当天已经发生了
            if (type.equals("工作日") && DateUtil.compare(one, DateUtil.date()) <= 0) {
                status = "缺勤";
                // 用于控制for循环跳出
                // 从之前拿到的本周考勤天数，然后用当天和他比较，如果有就取当日的考勤状况
                for (HashMap<String, String> map : checkinList) {
                    if (map.get("date").equals(date)) {
                        status = map.get("status");
                        break;
                    }
                }
            }
            if (type.equals("节假日") && DateUtil.compare(one, DateUtil.date()) <= 0) {
                status = "节假日";
            }
            // 返回对象
            HashMap<String, Object> map = new HashMap<>();
            checkinQueryWrapper.eq("date", DateUtil.parse(date));
            Checkin checkin = checkinMapper.selectOne(checkinQueryWrapper);
            if (checkin != null) {
                // 从数据库取出的DateTime类型进行格式化
                map.putIfAbsent("workTime", DateUtil.formatTime(checkin.getWorkTime()));
                map.putIfAbsent("offTime", DateUtil.formatTime(checkin.getOffTime()));
                map.putIfAbsent("brand", checkin.getBrand());
                map.putIfAbsent("address", checkin.getAddress());
                map.putIfAbsent("workAddr", checkin.getWorkAddr());
                map.putIfAbsent("offAddr", checkin.getOffAddr());
            }
            map.put("date", date);
            map.put("status", status);
            map.put("type", type);
            map.put("day", one.dayOfWeekEnum().toChinese("周"));
            list.add(map);
        });
        return list;
    }

    @Override
    public ArrayList<HashMap> searchMonthCheckin(HashMap param) {
        return this.searchWeekCheckin(param);
    }

    @Override
    public PageCheckinDto queryChekinPage(PageCheckinDto page, Checkin checkin) {
        // 查询合同总行数
        int checkinCount = checkinMapper.selectCheckinCount(page);
        log.info(page.getStartDate());
        // 分页查询合同
        ArrayList<HashMap> list = checkinMapper.selectCheckinPage(page, checkin);

        // 将查询到的总行数和当前页数据组装到 Page 对象
        page.setTotalNum(checkinCount);
        page.setResultList(list);
        return page;
    }


}




