package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.service.MemClassStudentService;
import com.waiterxiaoyy.service.MemStudentService;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
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
public class MemStudentController extends BaseController {

    @Autowired
    MemStudentService studentService;

    @Autowired
    MemClassStudentService memClassStudentService;

    @GetMapping("/getcctree")
    @PreAuthorize("hasAuthority('mem:stu:list')")
    public Result getCollegeClassTree() {
        return studentService.getCollegeClassTree();
    }

    @GetMapping("/getClassStuList")
    @PreAuthorize(("hasAuthority('mem:stu:list')"))
    public Result getClassStudentList(Integer classId, String studentName) {
        Page<SysStudent> pageData = memClassStudentService.getClassStudentList(getPage(), classId, studentName);

        return Result.succ(pageData);

    }


    @PostMapping("/addStuInClass")
    @PreAuthorize(("hasAuthority('mem:stu:add')"))
    public Result addStudentInClass(@RequestBody List<SysStudent> sysStudentList) {
        if(sysStudentList.size() == 1) {
            if(memClassStudentService.selectOne(sysStudentList.get(0)) != null) {
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
        } else if(sysStudentList.size() >= 1) {
            studentService.saveBatch(sysStudentList);
            List<SysClassStudent> sysClassStudents = new ArrayList<>();
            for(int i = 0; i < sysStudentList.size(); i++) {
                SysClassStudent sysClassStudent = new SysClassStudent();
                sysClassStudent.setStudentId(sysStudentList.get(i).getStudentId());
                sysClassStudent.setClassId(sysStudentList.get(i).getClassId());
                sysClassStudents.add(sysClassStudent);
            }
            memClassStudentService.saveBatch(sysClassStudents);
            return Result.succ("导入学生名单成功");
        }
        return Result.succ("添加学生成功");
    }


    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('mem:stu:add')")
    public Result upload(@RequestParam("file") MultipartFile[] multipartFiles, @RequestParam Long classId) throws Exception {

        if(multipartFiles.length <= 0) {
            return Result.fail("请上传文件");
        }

        return memClassStudentService.upload(multipartFiles[0],classId);
    }

    @PostMapping("/existStu")
    public Result existStudent(@RequestBody SysStudent sysStudent) {
        if(studentService.getOne(new QueryWrapper<SysStudent>().eq("student_id", sysStudent.getStudentId())) != null) {
            return Result.fail("已存在此名学生信息");
        }
        return Result.succ("学生信息合法");
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody List<SysStudent> sysStudentList) {
        for (int i = 0; i < sysStudentList.size(); i++) {
            studentService.remove(new QueryWrapper<SysStudent>().eq("student_id", sysStudentList.get(i).getStudentId()));
            memClassStudentService.remove(new QueryWrapper<SysClassStudent>().eq("student_id", sysStudentList.get(i).getStudentId()).eq("class_id", sysStudentList.get(i).getClassId()));
        }
        return Result.succ("删除成功");
    }
}
