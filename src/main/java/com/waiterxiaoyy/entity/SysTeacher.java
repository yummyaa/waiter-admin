package com.waiterxiaoyy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/3 22:20
 * @Version 1.0
 */
@Data
public class SysTeacher extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "教师ID不能为空")
    private String teacherId;

    @NotBlank(message = "姓名不能为空")
    private String teacherName;

    private String beginTime;

    private String identityStr;

    @TableField(exist = false)
    private List<String> identity;

    @TableField(exist = false)
    private Long majorId;

    @TableField(exist = false)
    private List<Long> classIds = new ArrayList<>();


}
