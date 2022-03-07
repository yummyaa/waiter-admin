package com.waiterxiaoyy.utils;

import cn.hutool.core.map.MapUtil;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysFile;
import com.waiterxiaoyy.service.FileService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/6 14:16
 * @Version 1.0
 */
@Component
public class FileUtils {

    @Autowired
    private FileService tempFileService;
    private static FileService fileService;


    private static String localPath;

    @Value("${waiterxiaoyy.file.LocalPath}")
    public void setLocalPath(String localPath) {
        FileUtils.localPath = localPath;
    }

    @PostConstruct
    public void init(){
        fileService = this.tempFileService;
    }


    /**
     * 根据所给路径保存图片
     * @param image
     * @param folderPath
     * @param type
     * @param belongId
     * @return
     */
    public static Result saveImg(MultipartFile image, String folderPath, Integer type, Long belongId) {

        try {
            String tempPath = localPath + folderPath;
            File file = new File(tempPath);

            if(!file.exists()) {
                file.mkdir();
            }

            String fileName = image.getOriginalFilename(); //获取文件名
            fileName = getFileName(fileName);

            String path = tempPath + "/" + fileName;
            file = new File(path);
            // 保存文件
            image.transferTo(file);

            SysFile sysFile = new SysFile();
            sysFile.setName(image.getOriginalFilename());
            sysFile.setUrl("/localPath" +  folderPath + "/" + fileName);
            sysFile.setType(type);
            sysFile.setBelongId(belongId);
            sysFile.setCreated(LocalDateTime.now());
            sysFile.setStatu(1);
            if(fileService.save(sysFile)) {
               return Result.succ(sysFile);
            }

            return Result.fail("保存文件失败");
        } catch (Exception e) {
            e.printStackTrace();

            return Result.fail("保存文件失败");
        }
    }

    /**
     * 文件名后缀前添加一个时间戳
     */
    private static String getFileName(String fileName) {
        int index = fileName.lastIndexOf(".");
        final SimpleDateFormat sDateFormate = new SimpleDateFormat("yyyymmddHHmmss");  //设置时间格式
        String nowTimeStr = sDateFormate.format(new Date()); // 当前时间
        fileName =  nowTimeStr + fileName.substring(index);
        return fileName;
    }

}
