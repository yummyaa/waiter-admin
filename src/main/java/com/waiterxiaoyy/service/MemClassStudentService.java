package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysClassStudent;
import com.waiterxiaoyy.entity.SysStudent;
import org.springframework.web.multipart.MultipartFile;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/16 19:37
 * @Version 1.0
 */
public interface MemClassStudentService extends IService<SysClassStudent> {

    SysClassStudent selectOne(SysStudent sysStudent);

    Page<SysStudent> getClassStudentList(Page page, Integer classId, String studentName);

    Result upload(MultipartFile multipartFile, Long classId) throws Exception;
}
