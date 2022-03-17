package com.waiterxiaoyy.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/17 16:27
 * @Version 1.0
 */
@Data
public class SysClassHomework extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private static final long serialVersionUID = 1L;

    private Long classId;

    private String title;
    private String content;
    private String beginTime;
    private String endTime;
}
