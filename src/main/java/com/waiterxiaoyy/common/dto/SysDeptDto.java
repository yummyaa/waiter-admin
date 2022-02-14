package com.waiterxiaoyy.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class SysDeptDto implements Serializable {

	private Long id;
	private String name;
	private String index;
	private int orderNum;
	private List<SysDeptDto> children = new ArrayList<>();

}
