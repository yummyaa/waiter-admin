package com.waiterxiaoyy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/16 18:22
 * @Version 1.0
 */

@Data
public class SysStudent {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "学号不能为空")
    private String studentId;

    @NotBlank(message = "姓名不能为空")
    private String studentName;

    @TableField(exist = false)
    private Long classId;
}
