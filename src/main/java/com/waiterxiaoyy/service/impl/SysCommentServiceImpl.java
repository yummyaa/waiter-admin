package com.waiterxiaoyy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.dto.SysCommentDto;
import com.waiterxiaoyy.common.dto.SysMenuDto;
import com.waiterxiaoyy.entity.SysComment;
import com.waiterxiaoyy.entity.SysMenu;
import com.waiterxiaoyy.mapper.SysCommentMapper;
import com.waiterxiaoyy.service.SysCommentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/4/12 9:12
 * @Version 1.0
 */
@Service
public class SysCommentServiceImpl extends ServiceImpl<SysCommentMapper, SysComment> implements SysCommentService {
    @Override
    public List<SysCommentDto> getClassComment(Long classId) {
        List<SysComment> sysCommentList = this.list(new QueryWrapper<SysComment>().eq("class_id", classId));

        // 转树状结构
        List<SysComment> commentTree = buildTreeMenu(sysCommentList);

        // 实体转dto
        return convert(commentTree);
    }


    /**
     * 递归构建树形菜单
     * @param commentTree
     * @return
     */
    private List<SysCommentDto> convert(List<SysComment> commentTree) {
        List<SysCommentDto> commentDtos = new ArrayList<>();

        commentTree.forEach(m -> {
            SysCommentDto dto = new SysCommentDto();
            dto.setId(m.getId());
            dto.setClassId(m.getClassId());
            dto.setUserId(m.getUserId());
            dto.setContent(m.getContent());
            dto.setParentid(m.getParentId());
            dto.setStatu(m.getStatu());
            dto.setCreated(m.getCreated().toString());

            if (m.getChildren().size() > 0) {

                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            commentDtos.add(dto);
        });

        return commentDtos;
    }

    /**
     * 分离出一级菜单
     * @param sysCommentList
     * @return
     */
    private List<SysComment> buildTreeMenu(List<SysComment> sysCommentList) {

        List<SysComment> finalComments = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (SysComment comment : sysCommentList) {

            for (SysComment e : sysCommentList) {

                if (comment.getId() == e.getParentId()) {
                    comment.getChildren().add(e);
                }

            }

            // 提取出父节点
            if (comment.getParentId() == 0L) {
                finalComments.add(comment);
            }
        }

        return finalComments;
    }
}
