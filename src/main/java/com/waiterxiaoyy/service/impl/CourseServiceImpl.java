package com.waiterxiaoyy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.entity.SysCourse;
import com.waiterxiaoyy.mapper.CourseMapper;
import com.waiterxiaoyy.service.CourseService;
import org.springframework.stereotype.Service;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/6 15:35
 * @Version 1.0
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, SysCourse> implements CourseService {
}
