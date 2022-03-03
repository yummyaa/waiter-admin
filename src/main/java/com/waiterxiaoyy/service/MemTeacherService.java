package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.entity.SysTeacher;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/3 21:48
 * @Version 1.0
 */
public interface MemTeacherService extends IService<SysTeacher> {
    Result getColMajorTree();

    Page<SysTeacher> getMajorTeacherList(Page page, Integer majorId, String teacherName);
}
