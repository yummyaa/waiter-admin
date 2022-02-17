package com.waiterxiaoyy.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.mapper.MemClassStudentMapper;
import com.waiterxiaoyy.mapper.MemStudentMapper;
import com.waiterxiaoyy.service.MemClassStudentService;
import com.waiterxiaoyy.service.MemStudentService;
import com.waiterxiaoyy.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/16 19:38
 * @Version 1.0
 */
@Service
public class MemClassStudentServiceImpl extends ServiceImpl<MemClassStudentMapper, SysClassStudent> implements MemClassStudentService {

    @Autowired
    MemClassStudentMapper memClassStudentMapper;

    @Autowired
    MemStudentMapper memStudentMapper;

    @Override
    public SysClassStudent selectOne(SysStudent sysStudent) {
        return memClassStudentMapper.selectOne(new QueryWrapper<SysClassStudent>().eq("class_id", sysStudent.getClassId()).eq("student_id", sysStudent.getStudentId()));
    }

    @Override
    public Page<SysStudent> getClassStudentList(Page page, Integer classId, String studentName) {
        Page<SysStudent> pageData = memClassStudentMapper.getCLassStudentList(page, classId, studentName);
        return pageData;
    }

    @Override
    public Result upload(MultipartFile multipartFile, Long classId) throws Exception {
        List<SysStudent> sysStudentList = POIUtils.getClassStudentList(multipartFile);

        List<SysStudent> newList = new ArrayList<>();
        List<SysStudent> existList = new ArrayList<>();
        sysStudentList.forEach(s -> {

            if(memStudentMapper.selectOne(new QueryWrapper<SysStudent>().eq("student_id", s.getStudentId())) == null) {
                newList.add(s);
            } else {
                existList.add(s);
            }
        });
        return Result.succ(new MapUtil().builder().put("newList", newList).put("existList", existList).build());
    }
}
