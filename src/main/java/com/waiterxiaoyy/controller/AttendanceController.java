package com.waiterxiaoyy.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.dto.FaceDto;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.*;
import com.waiterxiaoyy.mapper.SysAttendanceInfoMapper;
import com.waiterxiaoyy.mapper.SysFaceMapper;
import com.waiterxiaoyy.mapper.SysTermCourseMapper;
import com.waiterxiaoyy.service.SysAttendanceInfoService;
import com.waiterxiaoyy.service.SysClassAttendanceService;
import com.waiterxiaoyy.service.SysFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/1/22 20:03
 * @Version 1.0
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController extends BaseController {

    @Autowired
    SysFaceMapper sysFaceMapper;

    @Autowired
    SysClassAttendanceService sysClassAttendanceService;

    @Autowired
    SysAttendanceInfoService sysAttendanceInfoService;
    @Autowired
    SysTermCourseMapper sysTermCourseMapper;

    @Autowired
    SysAttendanceInfoMapper sysAttendanceInfoMapper;

    @Autowired
    SysFaceService sysFaceService;


    @GetMapping("/getStuFace/{studentId}")
    @PreAuthorize("hasAuthority('attendance:face:input')")
    public Result getStudentFace(@PathVariable("studentId") String studentId) {
        SysFace sysFace = sysFaceMapper.selectOne(new QueryWrapper<SysFace>().eq("student_id", studentId));
        if(sysFace == null) {
            sysFace = new SysFace();
            sysFace.setStudentId(studentId);
            return Result.succ(sysFace);
        }
        return Result.succ(sysFace);
    }

    @PostMapping("/saveFace")
    @PreAuthorize(("hasAuthority('attendance:face:input')"))
    public Result saveFace(MultipartFile file, String studentId) throws IOException {
        Result result = sysFaceService.saveFace(file, studentId);
        return result;
    }


    @RequestMapping(value = "/recognize", method = RequestMethod.POST,produces="application/json")
    @PreAuthorize("hasAuthority('attendance:face:recognize')")
    public Result recognize(@RequestBody FaceDto faceDto) {
        Result result = attendanceService.recognize(faceDto);
        return  result;
    }


    /*班级考勤*/


    @GetMapping("/course/getAttendanceList/")
    public Result getAttendanceList(Long classId, String title) {
        List<SysClassAttendance> sysClassAttendances
                = sysClassAttendanceService.list(new QueryWrapper<SysClassAttendance>()
                .eq("class_id", classId)
                .like(StrUtil.isNotBlank(title), "title", title));
        return Result.succ(sysClassAttendances);
    }

    @GetMapping("/course/getAttendanceById/{id}")
    public Result getHomeWorkById(@PathVariable("id") Long attendanceId) {
        SysClassAttendance sysClassAttendance = sysClassAttendanceService.getById(attendanceId);
        return Result.succ(sysClassAttendance);
    }

    /**
     * 教师发布考勤
     * @param sysClassAttendance
     * @return
     */
    @PostMapping("/course/addAttendance")
    @PreAuthorize("hasAuthority('attendance:course:add')")
    public Result addAttendance(@RequestBody SysClassAttendance sysClassAttendance) {
        sysClassAttendance.setCreated(LocalDateTime.now()); // 设置创建时间
        sysClassAttendanceService.save(sysClassAttendance); // 保存到本地
        // 在中间表中加入血神作业完成情况
        List<SysStudent> sysStudentList = sysTermCourseMapper.getClassStudent(sysClassAttendance.getClassId());
        List<SysAttendanceInfo> sysHomeworkInfoList = new ArrayList<>();
        SysAttendanceInfo sysAttendanceInfo = null;
        for(int i = 0; i < sysStudentList.size(); i++) {
            sysAttendanceInfo = new SysAttendanceInfo();
            sysAttendanceInfo.setStatu(0);
            sysAttendanceInfo.setAttendanceId(sysClassAttendance.getId());
            sysAttendanceInfo.setStudentId(sysStudentList.get(i).getStudentId());
            sysAttendanceInfo.setCreated(LocalDateTime.now());
            sysHomeworkInfoList.add(sysAttendanceInfo);
        }

        // 保存作业完成情况到本地
        sysAttendanceInfoService.saveBatch(sysHomeworkInfoList);
        return Result.succ(sysClassAttendance);
    }

    @PostMapping("/course/update")
    public Result update(@RequestBody SysClassAttendance sysClassAttendance) {
        sysClassAttendance.setUpdated(LocalDateTime.now());
        sysClassAttendanceService.updateById(sysClassAttendance);
        return Result.succ(sysClassAttendance);
    }

    @GetMapping("/course/delete/{id}")
    public Result deleteAttendance(@PathVariable("id") Long id) {
        sysAttendanceInfoService.remove(new QueryWrapper<SysAttendanceInfo>().eq("attendance_id", id));
        sysClassAttendanceService.removeById(id);
        return Result.succ("删除成功");
    }


    @GetMapping("/course/getAttendanceInfo/{id}")
    public Result getAttendanceInfo(@PathVariable("id") Long attendanceId) {
        List<SysAttendanceInfo> sysAttendanceInfos = sysAttendanceInfoMapper.getAttendanceInfo(attendanceId);
        return Result.succ(sysAttendanceInfos);
    }


    @PostMapping("/course/postAttendance")
    public Result postAttendance(@RequestBody SysAttendanceInfo sysAttendanceInfo) {
        SysFace sysFace = sysFaceService.getOne(new QueryWrapper<SysFace>().eq("student_id", sysAttendanceInfo.getStudentId()));
        if(sysFace == null) {
            return Result.succ(204, "该生人脸数据还未录入", null);
        }
        Result result = sysAttendanceInfoService.postAttendance(sysAttendanceInfo);
        return result;
    }

}
