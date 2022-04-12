package com.waiterxiaoyy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/4/12 9:09
 * @Version 1.0
 */
@Data
public class SysComment extends BaseEntity  {

    private static final long serialVersionUID = 1L;


    private Long parentId;

    private Long userId;
    private Long classId;

    private String content;

    private int type;

    @TableField(exist = false)
    private List<SysComment> children = new ArrayList<>();

    @TableField(exist = false)
    private String username;
}
