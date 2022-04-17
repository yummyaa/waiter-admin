package com.waiterxiaoyy.utils;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysFile;
import com.waiterxiaoyy.entity.SysHomeworkInfo;
import com.waiterxiaoyy.service.FileService;
import com.waiterxiaoyy.service.SysHomeworkInfoService;
import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @Autowired
    private SysHomeworkInfoService sysHomeworkInfoService;
    private static SysHomeworkInfoService studentHomeworkService;


    private static String localPath;



    @Value("${waiterxiaoyy.file.LocalPath}")
    public void setLocalPath(String localPath) {
        FileUtils.localPath = localPath;
    }

    @PostConstruct
    public void init(){
        fileService = this.tempFileService;
        studentHomeworkService = this.sysHomeworkInfoService;
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
     * 保存作业
     * @param homeworkFile
     * @param studentId
     * @param homeworkId
     * @return
     */
    public static Result saveHomework(MultipartFile homeworkFile, String studentId, Long homeworkId) {
        try {
            String tempPath = localPath + "/homework/" + homeworkId;
            File file = new File(tempPath);

            if(!file.exists()) {
                file.mkdir();
            }

            String fileName = homeworkFile.getOriginalFilename(); //获取文件名
            fileName = getFileName(fileName);

            String path = tempPath + "/" + fileName;
            file = new File(path);
            // 保存文件
            homeworkFile.transferTo(file);

            // 取出中间表的学生提交作业状态
            SysHomeworkInfo sysHomeworkInfo  = studentHomeworkService.getOne(new QueryWrapper<SysHomeworkInfo>().eq("homework_id", homeworkId).eq("student_id", studentId));


            SysFile sysFile = new SysFile();
            sysFile.setName(homeworkFile.getOriginalFilename());
            sysFile.setUrl(path);
            sysFile.setType(2);
            sysFile.setBelongId(homeworkId);
            sysFile.setCreated(LocalDateTime.now());
            sysFile.setStatu(1);
            fileService.save(sysFile);

            sysHomeworkInfo.setFileUrl(path);
            sysHomeworkInfo.setStatu(1);
            sysHomeworkInfo.setUpdated(LocalDateTime.now());

            // 更新中间表
            studentHomeworkService.updateById(sysHomeworkInfo);
            return Result.succ("提交作业成功");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("提交作业失败");
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

    private static final int BUFFER_SIZE = 2 * 1024;

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException {

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();

        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构, true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     *
     */

    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
            throws Exception {

        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 拷贝文件夹
     *
     * @param oldPath 原文件夹
     * @param newPath 指定文件夹
     */
    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        //文件名称列表
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + File.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath  + File.separator  + filePath[i], newPath  + File.separator + filePath[i]);
            }

            if (new File(oldPath  + File.separator + filePath[i]).isFile()) {
                copyFile(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

        }
    }

    /**
     * 拷贝文件
     *
     * @param oldPath 资源文件
     * @param newPath 指定文件
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);;

        byte[] buffer=new byte[2097152];

        while((in.read(buffer)) != -1){
            out.write(buffer);
        }
        in.close();
        out.close();
    }

    /**
     * 删除文件或文件夹
     * @param directory
     */
    public static void delAllFile(File directory){
        if (!directory.isDirectory()){
            directory.delete();
        } else{
            File [] files = directory.listFiles();

            // 空文件夹
            if (files.length == 0){
                directory.delete();
                System.out.println("删除" + directory.getAbsolutePath());
                return;
            }

            // 删除子文件夹和子文件
            for (File file : files){
                if (file.isDirectory()){
                    delAllFile(file);
                } else {
                    file.delete();
                    System.out.println("删除" + file.getAbsolutePath());
                }
            }

            // 删除文件夹本身
            directory.delete();
            System.out.println("删除" + directory.getAbsolutePath());
        }
    }

}
