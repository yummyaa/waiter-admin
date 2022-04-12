package com.waiterxiaoyy.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class SysCommentDto implements Serializable {


	private Long id;
	private Long parentid;
	private Long userId;
	private Long classId;
	private String content;
	private String username;
	private int statu;
	private int type;
	private String created;
	private List<SysCommentDto> children = new ArrayList<>();

}
