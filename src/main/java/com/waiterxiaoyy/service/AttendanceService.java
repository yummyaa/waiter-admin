package com.waiterxiaoyy.service;

import com.waiterxiaoyy.common.dto.FaceDto;
import com.waiterxiaoyy.common.lang.Result;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/1/22 20:34
 * @Version 1.0
 */
public interface AttendanceService  {
    Result recognize(FaceDto faceDto);
}
