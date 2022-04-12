package com.waiterxiaoyy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.waiterxiaoyy.common.dto.PythonDto;
import com.waiterxiaoyy.common.dto.FaceDto;
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

/**
 * Java调用Python接口
 * @param faceDto
 * @return
 */
@Override
public Result recognize(FaceDto faceDto) {
    try {
        String url = "http://172.27.184.8:8899/url";
        Map map = new HashMap();
        map.put("username", faceDto.getStudentId());
        map.put("nowimage", faceDto.getNowImage());
        List<PythonDto> jsonArray = JSONArray.parseArray(HttpClientUtil.doPost(url, map), PythonDto.class);

        if(jsonArray.size() <= 0) {
            return Result.fail("服务器错误");
        }
        PythonDto pythonDto = jsonArray.get(0);
        if(pythonDto.getCode() == 200) {
            return Result.succ(200, "同一个人" ,null);
        } else if(pythonDto.getCode() == 201) {
            return Result.succ(200, "不是同一个人", null);
        } else {
            return Result.fail("服务器错误");
        }
    } catch (Exception e) {
        return Result.fail("服务器错误");
    }
}


}
