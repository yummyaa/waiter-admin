package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysTermCourse;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/7 19:15
 * @Version 1.0
 */
public interface SysTermCourseService extends IService<SysTermCourse> {
    Result getTermCourseClass();

    Result getStuTermCourseList(String username);

    Result getTeacTermCourseList(String username);
}
