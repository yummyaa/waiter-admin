package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysMajorTeacher;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.entity.SysTeacher;
import com.waiterxiaoyy.service.MemMajorTeacherService;
import com.waiterxiaoyy.service.MemTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/3 21:15
 * @Version 1.0
 */
@RestController
@RequestMapping("/mem/teac")
public class MemTeacherController extends BaseController {

    @Autowired
    MemTeacherService memTeacherService;

    @Autowired
    MemMajorTeacherService memMajorTeacherService;

    @GetMapping("/getColMajorTree")
    @PreAuthorize("hasAuthority('mem:teac:list')")
    public Result getColMajorTree() {
        return memTeacherService.getColMajorTree();
    }

    @GetMapping("/getMajorTeacList")
    @PreAuthorize(("hasAuthority('mem:teac:list')"))
    public Result getMajorTeacherList(Integer majorId, String teacherName) {
        Page<SysTeacher> pageData = memTeacherService.getMajorTeacherList(getPage(), majorId, teacherName);

        return Result.succ(pageData);
    }

    @PostMapping("/addTeacher")
    @PreAuthorize("hasAuthority('course:manage:add')")
    public Result addTeacher(@RequestBody SysTeacher sysTeacher) {
        SysMajorTeacher sysMajorTeacher = new SysMajorTeacher();
        sysMajorTeacher.setMajorId(sysTeacher.getMajorId());
        sysMajorTeacher.setTeacherId(sysTeacher.getTeacherId());

        sysTeacher.setCreated(LocalDateTime.now());


        memTeacherService.save(sysTeacher);
        memMajorTeacherService.save(sysMajorTeacher);
        return Result.succ(sysTeacher);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody List<SysTeacher> sysTeacherList) {
        for (int i = 0; i < sysTeacherList.size(); i++) {
            memTeacherService.remove(new QueryWrapper<SysTeacher>().eq("teacher_id", sysTeacherList.get(i).getTeacherId()));
            memMajorTeacherService.remove(new QueryWrapper<SysMajorTeacher>().eq("teacher_id", sysTeacherList.get(i).getTeacherId()).eq("major_id", sysTeacherList.get(i).getMajorId()));
        }
        return Result.succ("删除成功");
    }

}
