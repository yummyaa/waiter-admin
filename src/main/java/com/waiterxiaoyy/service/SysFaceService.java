package com.waiterxiaoyy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waiterxiaoyy.common.dto.FaceDto;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysFace;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/2 21:05
 * @Version 1.0
 */
public interface SysFaceService extends IService<SysFace> {

    Result saveFace(MultipartFile file, String studentId) throws IOException;
}
