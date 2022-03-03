package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.entity.SysTeacher;
import com.waiterxiaoyy.service.MemTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
