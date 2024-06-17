package com.bobochang.warehouse.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * SystemConstants
 * @author LI
 * 记录考勤时间的反射类
 */
@Data
@Component
public class SystemConstants {
    public String attendanceStartTime; // 上班考勤开始时间
    public String attendanceTime; // 上班时间
    public String attendanceEndTime; // 上班考勤结束时间
    public String closingStartTime; // 下班考勤开始时间
    public String closingTime; // 下班时间
    public String closingEndTime; // 下班考勤结束时间
}
