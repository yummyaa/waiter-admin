package com.waiterxiaoyy.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/6 12:25
 * @Version 1.0
 */
@Data
public class SysCourse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "课程名不能为空")
    private String courseName;

    private Long termId;

    private Long collegeId;

    private String description;


    private String type;

}
