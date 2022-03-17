package com.waiterxiaoyy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.entity.SysTermCourse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/7 19:13
 * @Version 1.0
 */
@Repository
public interface SysTermCourseMapper extends BaseMapper<SysTermCourse> {
    List<SysStudent> getClassStudent(Long classId);
}
