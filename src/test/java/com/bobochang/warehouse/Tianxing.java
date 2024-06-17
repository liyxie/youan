package com.bobochang.warehouse;

import cn.hutool.core.date.*;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bobochang
 * @Description
 * @Date 2024/5/6 - 11:23
 */
public class Tianxing {
    @Test
    public void test() {
        // 查看这一段时间的特殊节假日
        String tianApi = "f8d4debfd25853b09dff2b037b5bfd4a";
        String result = HttpUtil.get("https://apis.tianapi.com/jiejiari/index?key=" + tianApi + "&type=3" + "&date=" + "2024-09-14~2024-10-10");
        JSONObject jsonObject = new JSONObject(result);
        JSONObject resultObject = jsonObject.getJSONObject("result");
        JSONArray listArray = resultObject.getJSONArray("list");
        List<String> list = listArray.stream()
                .map(item -> (JSONObject) item)
                .filter(listObj -> StrUtil.isNotEmpty(listObj.getStr("remark")))
                .map(listObj -> listObj.getStr("remark"))
                .flatMap(vacationStr -> JSONUtil.toList(vacationStr, String.class).stream())
                .distinct()
                .collect(Collectors.toList());
        System.out.println(list);
       /* JSONArray vacation = listArray.getJSONArray(0).getJSONArray("vacation");
        List<String> vacationList = vacation.toList(String.class);
        System.out.println(vacationList);*/
    }

    @Test
    public void test1() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("grant_type", "client_credential");
        map.put("appid", "wx85419626edd58f69");
        map.put("secret", "a67db825f0d7b40455cab34fff273e27");
        String resultOfAccessToken = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", map);
        // 获取 resultOfAccessToken 字符串中的access_token
        String accessToken = JSONUtil.parseObj(resultOfAccessToken).getStr("access_token");
        // 根据 access_token 和 code 获取手机号信息
        HashMap<String, Object> getPhoneNumberInfo = new HashMap<>();
        getPhoneNumberInfo.put("access_token",accessToken);
        getPhoneNumberInfo.put("code","940dd09e2b87c6e9616c16a544508d03b690db835b1e79367107846c9693221a");
        String resultOfPhoneNumber = HttpUtil.post("https://api.weixin.qq.com/wxa/business/getuserphonenumber", getPhoneNumberInfo);
        // 获取 resultOfPhoneNumber 字符串中的phone_info
        String phoneNumber = JSONUtil.parseObj(resultOfPhoneNumber).getJSONObject("phone_info").getStr("phoneNumber");
    }
}
