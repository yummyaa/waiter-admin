package com.waiterxiaoyy.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/11 20:32
 * @Version 1.0
 */
@Data
public class SysCollege extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "学院名称不能为空")
    private String college;

}
