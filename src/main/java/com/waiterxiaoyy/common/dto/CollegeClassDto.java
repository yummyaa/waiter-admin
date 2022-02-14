package com.waiterxiaoyy.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/13 21:09
 * @Version 1.0
 */
@Data
public class CollegeClassDto {
    private Long id;
    private Long collegeId;
    private String label;
    private List<CollegeClassDto> children;
    private int type;
    private int statu;
}
