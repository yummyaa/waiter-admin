package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysTerm;
import com.waiterxiaoyy.mapper.SysTermMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/5 22:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    SysTermMapper sysTermMapper;

    @PostMapping("/addTerm")
    public Result addTerm(@RequestBody SysTerm sysTerm) {
        sysTerm.setCreated(LocalDateTime.now());
        if(sysTermMapper.insert(sysTerm) == 1) {
            return Result.succ(sysTerm);
        }
        return Result.fail("创建新学期失败");
    }

    @GetMapping("/getTermList")
    public Result getTermList() {
        List<SysTerm> sysTermList = sysTermMapper.selectList(new QueryWrapper<SysTerm>().orderByAsc("orderNum"));
        return Result.succ(sysTermList);
    }
}
