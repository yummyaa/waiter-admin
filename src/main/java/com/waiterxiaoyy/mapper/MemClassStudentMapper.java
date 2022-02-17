package com.waiterxiaoyy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysStudent;
import org.springframework.stereotype.Repository;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/11 20:42
 * @Version 1.0
 */
@Repository
public interface MemClassStudentMapper extends BaseMapper<SysClassStudent> {
    Page<SysStudent> getCLassStudentList(Page page, Integer classId, String studentName);
}
