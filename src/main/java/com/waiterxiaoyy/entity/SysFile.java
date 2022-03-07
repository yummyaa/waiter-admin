package com.waiterxiaoyy.entity;

import lombok.Data;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/6 12:30
 * @Version 1.0
 */
@Data
public class SysFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String name;

    private String url;

    private Long belongId;

    private int type;

}
