package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.common.dto.SysCommentDto;
import com.waiterxiaoyy.entity.SysComment;

import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/4/12 9:12
 * @Version 1.0
 */
public interface SysCommentService extends IService<SysComment> {
    List<SysCommentDto> getClassComment(Long classId);
}
