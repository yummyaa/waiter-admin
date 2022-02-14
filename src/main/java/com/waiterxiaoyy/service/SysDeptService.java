package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysDept;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/14 18:44
 * @Version 1.0
 */
public interface SysDeptService extends IService<SysDept> {

    Result getDeptTree();

}
