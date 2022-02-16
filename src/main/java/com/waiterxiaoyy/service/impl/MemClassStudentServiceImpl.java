package com.waiterxiaoyy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.mapper.MemClassStudentMapper;
import com.waiterxiaoyy.service.MemClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public SysClassStudent selectOne(SysStudent sysStudent) {
        return memClassStudentMapper.selectOne(new QueryWrapper<SysClassStudent>().eq("class_id", sysStudent.getClassId()).eq("student_id", sysStudent.getStudentId()));
    }
}
