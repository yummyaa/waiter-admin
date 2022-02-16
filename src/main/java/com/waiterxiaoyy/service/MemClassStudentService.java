package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysStudent;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/16 19:37
 * @Version 1.0
 */
public interface MemClassStudentService extends IService<SysClassStudent> {

    SysClassStudent selectOne(SysStudent sysStudent);
}
