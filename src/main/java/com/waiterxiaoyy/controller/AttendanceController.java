package com.waiterxiaoyy.controller;

import com.waiterxiaoyy.common.dto.RecognizeDto;
import com.waiterxiaoyy.common.lang.Result;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

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

    @RequestMapping(value = "/recognize", method = RequestMethod.POST,produces="application/json")
    public Result recognize(@RequestBody RecognizeDto recognizeDto) {
        System.out.println(recognizeDto);
        Result result = attendanceService.recognize(recognizeDto);
        return  result;
    }

}
