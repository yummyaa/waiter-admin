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
 * @Date 2022/3/5 22:06
 * @Version 1.0
 */
@Data
public class SysTerm extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "名称不能为空")
    private String term;

    /**
     * 排序
     */
    @TableField("orderNum")
    private Integer orderNum;
}
