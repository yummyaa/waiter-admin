package com.waiterxiaoyy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/11 20:37
 * @Version 1.0
 */
@Data
public class SysCollegeClass {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private static final long serialVersionUID = 1L;

    private Long collegeId;

    private Long classId;
}
