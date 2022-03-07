package com.waiterxiaoyy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysFace;
import com.waiterxiaoyy.mapper.SysFaceMapper;
import com.waiterxiaoyy.service.SysFaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/2 21:05
 * @Version 1.0
 */
@Service
public class SysFaceServiceImpl extends ServiceImpl<SysFaceMapper, SysFace> implements SysFaceService {
    @Value("${waiterxiaoyy.file.LocalPath}")
    private String localPath;


    @Override
    public Result saveFace(MultipartFile uploadFile, String studentId) throws IOException {
        String tempPath = localPath + "/faceImg";
        File file = new File(tempPath);
        // Temp文件夹是否存在
        if (!file.exists()) {
            file.mkdir();
        }
        String path = tempPath + "/" + uploadFile.getOriginalFilename();
        file = new File(path);
        // 保存文件
        uploadFile.transferTo(file);

        SysFace sysFace = getOne(new QueryWrapper<SysFace>().eq("student_id", studentId));

        if(sysFace == null) {
            sysFace = new SysFace();
            sysFace.setStudentId(studentId);
            sysFace.setFaceUrl("/localPath/faceImg/" + uploadFile.getOriginalFilename());
            sysFace.setCreated(LocalDateTime.now());
            save(sysFace);
        } else {
            sysFace.setStudentId(studentId);
            sysFace.setFaceUrl("/localPath/faceImg/" + uploadFile.getOriginalFilename());
            sysFace.setUpdated(LocalDateTime.now());
            updateById(sysFace);
        }

        System.out.println(localPath);
        System.out.println(studentId);
        System.out.println(file);
        return Result.succ(sysFace);
    }
}
