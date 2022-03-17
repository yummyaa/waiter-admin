package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.*;
import com.waiterxiaoyy.service.MemMajorTeacherService;
import com.waiterxiaoyy.service.MemTeacherService;
import com.waiterxiaoyy.service.SysTeacherClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    SysTeacherClassService sysTeacherClassService;

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

        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < sysTeacher.getIdentity().size(); i++) {
            stringBuffer.append(sysTeacher.getIdentity().get(i));
            if(i != sysTeacher.getIdentity().size() - 1) {
                stringBuffer.append(",");
            }
            sysTeacher.setIdentityStr(stringBuffer.toString());
        }


        memTeacherService.save(sysTeacher);
        memMajorTeacherService.save(sysMajorTeacher);
        return Result.succ(sysTeacher);
    }
    @GetMapping("/getTeacById/{teacherId}")
    @PreAuthorize("hasAuthority('mem:teac:update')")
    public Result getUserById(@PathVariable("teacherId") String teacherId) {

        SysTeacher sysTeacher = memTeacherService.getOne(new QueryWrapper<SysTeacher>().eq("teacher_id", teacherId));
        if(sysTeacher.getIdentityStr() != null) {
            String[] strArray = sysTeacher.getIdentityStr().split(",");
            List<String> stringList = new ArrayList<>();
            for(String str: strArray) {
                stringList.add(str);
            }
            sysTeacher.setIdentity(stringList);
        }
        return Result.succ(sysTeacher);
    }

    @PostMapping("/updateTeac")
    @PreAuthorize("hasAuthority('mem:stu:update')")
    public Result updateTeac(@RequestBody SysTeacher sysTeacher) {
        if(memTeacherService.getOne(new QueryWrapper<SysTeacher>().eq("teacher_id", sysTeacher.getTeacherId()))== null) {
            return Result.fail("不存在此老师，请保证教师ID准确");
        }
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < sysTeacher.getIdentity().size(); i++) {
            stringBuffer.append(sysTeacher.getIdentity().get(i));
            if(i != sysTeacher.getIdentity().size() - 1) {
                stringBuffer.append(",");
            }
            sysTeacher.setIdentityStr(stringBuffer.toString());
        }
        sysTeacher.setUpdated(LocalDateTime.now());
        memTeacherService.update(sysTeacher, new QueryWrapper<SysTeacher>().eq("teacher_id", sysTeacher.getTeacherId()));
        return Result.succ("更新成功");
    }


    @PostMapping("/delete")
    public Result delete(@RequestBody List<SysTeacher> sysTeacherList) {
        for (int i = 0; i < sysTeacherList.size(); i++) {
            memTeacherService.remove(new QueryWrapper<SysTeacher>().eq("teacher_id", sysTeacherList.get(i).getTeacherId()));
            memMajorTeacherService.remove(new QueryWrapper<SysMajorTeacher>().eq("teacher_id", sysTeacherList.get(i).getTeacherId()).eq("major_id", sysTeacherList.get(i).getMajorId()));
        }
        return Result.succ("删除成功");
    }

    @PreAuthorize("hasAuthority('mem:teac:class')")
    @GetMapping("/dist/{id}")
    public Result info(@PathVariable("id") Long id) {

        SysTeacher sysTeacher = memTeacherService.getById(id);

        // 获取角色相关联的菜单id
        List<SysTeacherClass> sysTeacherClasses = sysTeacherClassService.list(new QueryWrapper<SysTeacherClass>().eq("teacher_id", id));
        List<Long> classIds = sysTeacherClasses.stream().map(p -> p.getClassId()).collect(Collectors.toList());

        sysTeacher.setClassIds(classIds);
        return Result.succ(sysTeacher);
    }

    @Transactional
    @PostMapping("/dist/submit/{teacherId}")
    @PreAuthorize("hasAuthority('mem:teac:class')")
    public Result info(@PathVariable("teacherId") Long teacherId, @RequestBody Long[] classIds) {

        List<SysTeacherClass> sysTeacherClasses = new ArrayList<>();

        Arrays.stream(classIds).forEach(classId -> {
            SysTeacherClass sysTeacherClass = new SysTeacherClass();
            sysTeacherClass.setClassId(classId);
            sysTeacherClass.setTeacherId(teacherId);

            sysTeacherClasses.add(sysTeacherClass);
        });

        // 先删除原来的记录，再保存新的
        sysTeacherClassService.remove(new QueryWrapper<SysTeacherClass>().eq("teacher_id", teacherId));
        sysTeacherClassService.saveBatch(sysTeacherClasses);


        return Result.succ(classIds);
    }

    @GetMapping("/getTeacherByClassId/{classId}")
    public Result getTeacherInfoByClassId(@PathVariable("classId") Long classId) {
        SysTeacherClass sysTeacherClass = sysTeacherClassService.getOne(new QueryWrapper<SysTeacherClass>().eq("class_id", classId));
        SysTeacher sysTeacher = memTeacherService.getById(sysTeacherClass.getTeacherId());
        return Result.succ(sysTeacher);

    }

}
