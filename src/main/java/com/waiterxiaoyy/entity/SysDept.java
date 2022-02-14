package com.waiterxiaoyy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：学院组织
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/14 16:02
 * @Version 1.0
 */

@Data
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 学院ID，学院父级为0
     */
    @NotNull(message = "二级学院不能为空")
    private Long collegeId;

    @NotBlank(message = "名称不能为空")
    private String name;


    /**
     * 类型     0：学院   1：专业   2：班级
     */
    @NotNull(message = "类型不能为空")
    private Integer type;


    /**
     * 排序
     */
    @TableField("orderNum")
    private Integer orderNum;


    @TableField(exist = false)
    private String index;

    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<>();
}
