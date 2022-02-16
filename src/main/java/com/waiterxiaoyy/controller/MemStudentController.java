package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.service.MemClassStudentService;
import com.waiterxiaoyy.service.MemStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述：学生管理
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/13 20:31
 * @Version 1.0
 */

@RestController
@RequestMapping("/mem/stu")
public class MemStudentController {

    @Autowired
    MemStudentService studentService;

    @Autowired
    MemClassStudentService memClassStudentService;

    @GetMapping("/getcctree")
    @PreAuthorize("hasAuthority('mem:stu:list')")
    public Result getCollegeClassTree() {
        return studentService.getCollegeClassTree();
    }

    @PostMapping("/addStuInClass")
    @PreAuthorize(("hasAuthority('mem:stu:add')"))
    public Result addStudentInClass(@RequestBody List<SysStudent> sysStudentList) {
        if(sysStudentList.size() == 1) {
            if(memClassStudentService.selectOne(sysStudentList.get(0)) !=null) {
                return Result.fail("学生关联信息已存在");
            }
            if(studentService.getOne(new QueryWrapper<SysStudent>().eq("student_id", sysStudentList.get(0).getStudentId())) != null) {
                return Result.fail("学生信息已存在");
            }

            studentService.save(sysStudentList.get(0));
            SysClassStudent sysClassStudent = new SysClassStudent();
            sysClassStudent.setStudentId(sysStudentList.get(0).getStudentId());
            sysClassStudent.setClassId(sysStudentList.get(0).getClassId());
            memClassStudentService.save(sysClassStudent);
            return Result.succ("添加学生成功");
        }
        return Result.succ("添加学生成功");
    }




//    @PostMapping("/add")
//    @PreAuthorize("hasAuthority('mem:stu:add')")
//    public Result addStudent() {
//
//    }


}
