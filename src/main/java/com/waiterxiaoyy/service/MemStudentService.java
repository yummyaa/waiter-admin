package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysStudent;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/13 20:35
 * @Version 1.0
 */
public interface MemStudentService extends IService<SysStudent> {

    Result getCollegeClassTree();

}
