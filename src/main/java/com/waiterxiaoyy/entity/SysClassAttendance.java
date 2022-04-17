package com.waiterxiaoyy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/4/13 11:03
 * @Version 1.0
 */
@Data
public class SysClassAttendance extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private static final long serialVersionUID = 1L;

    private Long classId;

    private String title;
    private String beginTime;
    private String endTime;
}
