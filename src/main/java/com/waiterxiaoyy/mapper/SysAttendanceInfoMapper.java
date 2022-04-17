package com.waiterxiaoyy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.waiterxiaoyy.entity.SysAttendanceInfo;
import com.waiterxiaoyy.entity.SysHomeworkInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/17 16:49
 * @Version 1.0
 */
@Repository
public interface SysAttendanceInfoMapper extends BaseMapper<SysAttendanceInfo> {
    List<SysAttendanceInfo> getAttendanceInfo(Long attendanceId);
}
