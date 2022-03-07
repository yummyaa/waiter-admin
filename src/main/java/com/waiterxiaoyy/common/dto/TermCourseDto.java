package com.waiterxiaoyy.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/6 15:52
 * @Version 1.0
 */
@Data
public class TermCourseDto {
    private Long id;
    private String name;
    private int type;
    private int statu;
    private List<TermCourseDto> children = new ArrayList<>();
}
