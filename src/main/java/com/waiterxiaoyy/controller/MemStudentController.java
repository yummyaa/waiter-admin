package com.waiterxiaoyy.controller;

import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.service.MemStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getcctree")
    @PreAuthorize("hasAuthority('mem:stu:list')")
    public Result getCollegeClassTree() {
        return studentService.getCollegeClassTree();
    }

}
