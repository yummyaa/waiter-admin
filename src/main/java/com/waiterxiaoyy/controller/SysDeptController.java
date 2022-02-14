package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysDept;
import com.waiterxiaoyy.entity.SysMenu;
import com.waiterxiaoyy.entity.SysRoleMenu;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/14 18:40
 * @Version 1.0
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends BaseController {

    @GetMapping("/getlist")
    @PreAuthorize("hasAuthority('sys:dept:list')")
    public Result getList() {
        return sysDeptService.getDeptTree();
    }

    @PostMapping("/createcollege")
    @PreAuthorize("hasAuthority('sys:dept:add')")
    public Result createCollege(@RequestBody SysDept sysDept) {
        sysDept.setCreated(LocalDateTime.now());
        sysDept.setCollegeId(0L);
        sysDeptService.save(sysDept);
        return Result.succ(sysDept);
    }

    /**
     *  创建专业或者班级
     * @param sysDept
     * @return
     */
    @PostMapping("/createclass")
    @PreAuthorize("hasAuthority('sys:dept:add')")
    public Result createClass(@RequestBody SysDept sysDept) {
        sysDept.setCreated(LocalDateTime.now());
        sysDeptService.save(sysDept);
        return Result.succ(sysDept);
    }


    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:dept:update')")
    public Result update(@Validated @RequestBody SysDept sysDept) {
        sysDept.setUpdated(LocalDateTime.now());
        sysDeptService.updateById(sysDept);
        return Result.succ(sysDept);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize(("hasAuthority('sys:dept:delete')"))
    public Result delete(@PathVariable("id") Long id) {
        int count = sysDeptService.count(new QueryWrapper<SysDept>().eq("college_id", id));
        if (count > 0) {
            return Result.fail("请先删除下级组织");
        }


        sysDeptService.removeById(id);

        // 同步删除中间关联表(记得在此处处理）


        return Result.succ("");
    }


}
