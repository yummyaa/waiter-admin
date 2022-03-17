package com.waiterxiaoyy.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysClassHomework;
import com.waiterxiaoyy.entity.SysHomeworkInfo;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.mapper.SysTermCourseMapper;
import com.waiterxiaoyy.service.SysClassHomeworkService;
import com.waiterxiaoyy.service.SysHomeworkInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/17 16:51
 * @Version 1.0
 */
@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    SysHomeworkInfoService sysHomeworkInfoService;

    @Autowired
    SysClassHomeworkService sysClassHomeworkService;

    @Autowired
    SysTermCourseMapper sysTermCourseMapper;

    @GetMapping("/getHomeWorkById/{id}")
    public Result getHomeWorkById(@PathVariable("id") Long homeworkId) {
        SysClassHomework sysClassHomeworks = sysClassHomeworkService.getById(homeworkId);
        return Result.succ(sysClassHomeworks);
    }

    @GetMapping("/getHomeworkByClassId")
    public Result getHomeworkByClassId(Long classId, String title) {
        List<SysClassHomework> sysClassHomeworks
                = sysClassHomeworkService.list(new QueryWrapper<SysClassHomework>()
                .eq("class_id", classId)
                .like(StrUtil.isNotBlank(title), "title", title));
        return Result.succ(sysClassHomeworks);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('course:home:edit')")
    public Result save(@RequestBody SysClassHomework sysClassHomework) {
        sysClassHomework.setCreated(LocalDateTime.now());
        sysClassHomeworkService.save(sysClassHomework);
        List<SysStudent> sysStudentList = sysTermCourseMapper.getClassStudent(sysClassHomework.getClassId());
        List<SysHomeworkInfo> sysHomeworkInfoList = new ArrayList<>();
        SysHomeworkInfo sysHomeworkInfo = new SysHomeworkInfo();
        sysStudentList.forEach((sysStudent -> {
            sysHomeworkInfo.setStatu(0);
            sysHomeworkInfo.setHomeworkId(sysClassHomework.getId());
            sysHomeworkInfo.setStudentId(sysStudent.getStudentId());
            sysHomeworkInfo.setCreated(LocalDateTime.now());
            sysHomeworkInfoList.add(sysHomeworkInfo);
        }));
        sysHomeworkInfoService.saveBatch(sysHomeworkInfoList);
        return Result.succ(sysClassHomework);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('course:home:edit')")
    public Result update(@RequestBody SysClassHomework sysClassHomework) {
        sysClassHomework.setUpdated(LocalDateTime.now());
        sysClassHomeworkService.updateById(sysClassHomework);
        return Result.succ(sysClassHomework);
    }
}
