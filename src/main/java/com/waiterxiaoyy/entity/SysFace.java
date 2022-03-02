package com.waiterxiaoyy.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/2 21:03
 * @Version 1.0
 */
@Data
public class SysFace extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "学号不能为空")
    private String studentId;

    private String faceUrl;
}
