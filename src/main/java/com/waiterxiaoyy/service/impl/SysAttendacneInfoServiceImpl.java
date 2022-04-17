package com.waiterxiaoyy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.dto.PythonDto;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysAttendanceInfo;
import com.waiterxiaoyy.entity.SysHomeworkInfo;
import com.waiterxiaoyy.mapper.SysAttendanceInfoMapper;
import com.waiterxiaoyy.mapper.SysHomeworkInfoMapper;
import com.waiterxiaoyy.service.SysAttendanceInfoService;
import com.waiterxiaoyy.service.SysHomeworkInfoService;
import com.waiterxiaoyy.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/17 16:49
 * @Version 1.0
 */
@Service
public class SysAttendacneInfoServiceImpl extends ServiceImpl<SysAttendanceInfoMapper, SysAttendanceInfo> implements SysAttendanceInfoService {

    @Autowired
    SysAttendanceInfoMapper sysAttendanceInfoMapper;

    @Override
    public Result postAttendance(SysAttendanceInfo sysAttendanceInfo) {
        try {
            String url = "http://172.27.184.8:8899/recognize";
            Map map = new HashMap();
            map.put("username", sysAttendanceInfo.getStudentId());
            map.put("nowimage", sysAttendanceInfo.getImage());
            List<PythonDto> jsonArray = JSONArray.parseArray(HttpClientUtil.doPost(url, map), PythonDto.class);

            if(jsonArray.size() <= 0) {
                return Result.fail("服务器错误");
            }
            PythonDto pythonDto = jsonArray.get(0);
            if(pythonDto.getCode() == 200) {
                sysAttendanceInfo.setStatu(1);
                sysAttendanceInfo.setCreated(LocalDateTime.now());
                sysAttendanceInfoMapper.update(sysAttendanceInfo,
                        new QueryWrapper<SysAttendanceInfo>()
                                .eq("attendance_id", sysAttendanceInfo.getAttendanceId())
                                .eq("student_id", sysAttendanceInfo.getStudentId()));
                return Result.succ(200, "同一个人" ,null);
            } else if(pythonDto.getCode() == 201) {
                return Result.succ(201, "人脸不匹配，考勤失败", null);
            } else if(pythonDto.getCode() == 203) {
                return Result.succ(201, "考勤失败，未识别到人脸", null);
            } else {
                return Result.fail("服务器错误");
            }
        } catch (Exception e) {
            System.out.println(e);
            return Result.fail("服务器错误");
        }
    }
}
