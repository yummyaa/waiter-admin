package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysClassHomework;
import com.waiterxiaoyy.entity.SysFile;
import com.waiterxiaoyy.entity.SysHomeworkInfo;
import com.waiterxiaoyy.service.FileService;
import com.waiterxiaoyy.service.SysClassHomeworkService;
import com.waiterxiaoyy.service.SysHomeworkInfoService;
import com.waiterxiaoyy.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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

    @Autowired
    SysClassHomeworkService sysClassHomeworkService;

    @Autowired
    SysHomeworkInfoService sysHomeworkInfoService;

    @PostMapping("upload/image")
    public Result uploadImage(@RequestParam("image") MultipartFile[] file, @RequestParam Integer type, @RequestParam Long belongId) {

        if(file.length > 0) {
            Result result = FileUtils.saveImg(file[0], "/courseImage",  type, belongId);
            return result;
        }
        return Result.fail("保存图片失败");

    }

    @PostMapping("upload/homework")
    public Result uploadHomework(@RequestParam("homework") MultipartFile[] file, @RequestParam String  studentId, @RequestParam Long homeworkId) {

        studentId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if(file.length > 0) {
            Result result = FileUtils.saveHomework(file[0], studentId, homeworkId);
            return result;
        }
        return Result.fail("保存图片失败");
    }

    @PostMapping("/download/homework")
    public Result downloadHomework(@RequestBody SysHomeworkInfo sysHomeworkInfo, HttpServletResponse response) {

        File file = new File(sysHomeworkInfo.getFileUrl());
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            //文件是否存在
            if (file.exists()) {
                //设置响应
                response.setContentType("application/octet-stream;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                while(bis.read(buffer) != -1){
                    os.write(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(bis != null) {
                    bis.close();
                }
                if(os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result.succ("");
        }

    }

    @GetMapping("/downloadFile")
    public ResponseEntity<Object> downloadFile(@RequestParam("taskId") Long taskId, HttpServletResponse response) {
        SysHomeworkInfo sysHomeworkInfo = sysHomeworkInfoService.getById(taskId);
        if (sysHomeworkInfo == null) {
            return null;
        }
        File file = new File(sysHomeworkInfo.getFileUrl());
        // 文件下载
        if (file.isFile()) {
            return downloadFile(taskId);
        }
        // 文件夹压缩成zip下载
        if (file.isDirectory()) {
            String parent = file.getParent();
            // 创建临时存放文件夹
            File temDir = new File(parent + "/" + taskId);
            if (!temDir.exists()) {
                temDir.mkdirs();
            }
            // 将需要下载的文件夹和内容拷贝到临时文件夹中
            try {
                FileUtils.copyDir(sysHomeworkInfo.getFileUrl(), parent + "/" + taskId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 设置头部格式
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename="+taskId+".zip");
            // 调用工具类，下载zip压缩包
            try {
                FileUtils.toZip(temDir.getPath(), response.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 删除临时文件夹和内容
            FileUtils.delAllFile(new File(parent + "/" + taskId));
        }
        return null;

    }

    public ResponseEntity<Object> downloadFile(Long taskId) {
        SysHomeworkInfo sysHomeworkInfo = sysHomeworkInfoService.getById(taskId);
        if (sysHomeworkInfo == null) {
            return null;
        }
        File file = new File(sysHomeworkInfo.getFileUrl());
        String fileName = file.getName();
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment;filename=\"%s", fileName));
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
        return responseEntity;
    }

}
