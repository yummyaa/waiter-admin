package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.dto.FaceDto;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysFace;
import com.waiterxiaoyy.mapper.SysFaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

}
