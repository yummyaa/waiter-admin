package com.waiterxiaoyy.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/1/22 20:08
 * @Version 1.0
 */
@Data
public class FaceDto implements Serializable {

    public String studentId;
    public String nowImage;
}
