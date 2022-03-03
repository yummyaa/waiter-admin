package com.waiterxiaoyy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waiterxiaoyy.entity.SysMajorTeacher;
import com.waiterxiaoyy.entity.SysTeacher;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/3 22:32
 * @Version 1.0
 */
public interface MemMajorTeacherMapper extends BaseMapper<SysMajorTeacher> {

    Page<SysTeacher> getMajorTeacherList(Page page, Integer majorId, String teacherName);
}
