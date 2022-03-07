package com.waiterxiaoyy.controller;

import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysFile;
import com.waiterxiaoyy.service.FileService;
import com.waiterxiaoyy.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/6 12:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("upload/image")
    public Result uploadImage(@RequestParam("image") MultipartFile[] file, @RequestParam Integer type, @RequestParam Long belongId) {

        if(file.length > 0) {
            Result result = FileUtils.saveImg(file[0], "/courseImage",  type, belongId);
            return result;
        }
        return Result.fail("保存图片失败");


    }
}
