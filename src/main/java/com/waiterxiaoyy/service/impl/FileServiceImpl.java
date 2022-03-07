package com.waiterxiaoyy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.entity.SysFile;
import com.waiterxiaoyy.mapper.FileMapper;
import com.waiterxiaoyy.service.FileService;
import org.springframework.stereotype.Service;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/6 12:38
 * @Version 1.0
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, SysFile> implements FileService {
}
