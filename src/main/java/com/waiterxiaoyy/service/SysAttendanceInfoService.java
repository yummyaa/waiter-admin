package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysAttendanceInfo;
import com.waiterxiaoyy.entity.SysHomeworkInfo;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/17 16:49
 * @Version 1.0
 */
public interface SysAttendanceInfoService extends IService<SysAttendanceInfo> {
    Result postAttendance(SysAttendanceInfo sysAttendanceInfo);
}
