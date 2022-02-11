package com.waiterxiaoyy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.waiterxiaoyy.common.dto.PythonDto;
import com.waiterxiaoyy.common.dto.RecognizeDto;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.service.AttendanceService;
import com.waiterxiaoyy.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/1/22 20:34
 * @Version 1.0
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    HttpClientUtil httpClientUtil;

    @Override
    public Result recognize(RecognizeDto recognizeDto) {
        String url = "http://192.168.1.101:8899/url";
        Map map = new HashMap();
        map.put("username", recognizeDto.getUsername());
        map.put("nowimage", recognizeDto.getNowimage());
        List<PythonDto> jsonArray = JSONArray.parseArray(HttpClientUtil.doPost(url, map), PythonDto.class);

        if(jsonArray.size() <= 0) {
            return Result.fail("服务器错误");
        }
        PythonDto pythonDto = jsonArray.get(0);
        if(pythonDto.getCode() == 200) {
            return Result.succ("同一个人");
        } else if(pythonDto.getCode() == 201) {
            return Result.fail("不是同一个人");
        } else {
            return Result.fail("服务器错误");
        }
    }

    public static void main(String[] args) {
    }
}
