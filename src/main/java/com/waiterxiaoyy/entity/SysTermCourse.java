package com.waiterxiaoyy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/7 19:08
 * @Version 1.0
 */
@Data
public class SysTermCourse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 父ID，一级菜单为0
     */
    @NotNull(message = "上级不能为空")
    private Long parentId;

    @NotBlank(message = "名称不能为空")
    private String name;

    private Long collegeId;


    /**
     * 类型     0：学期   1：课程   2：班级
     */
    @NotNull(message = "类型不能为空")
    private Integer type;


    private String description;

    private String classId;

    private String courseId;

    private String category;

    private String image;
    /**
     * 排序
     */
    @TableField("orderNum")
    private Integer orderNum;


    @TableField(exist = false)
    private List<SysTermCourse> children = new ArrayList<>();

    @TableField(exist = false)
    private List<String> studentIdList = new ArrayList<>();

}
