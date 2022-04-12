package com.waiterxiaoyy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waiterxiaoyy.common.dto.SysCommentDto;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysComment;
import com.waiterxiaoyy.entity.SysUser;
import com.waiterxiaoyy.mapper.SysCommentMapper;
import com.waiterxiaoyy.service.SysCommentService;
import com.waiterxiaoyy.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/4/12 9:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/course/comment")
public class CommentController {
    @Autowired
    SysCommentService sysCommentService;

    @Autowired
    SysCommentMapper sysCommentMapper;

    @Autowired
    SysUserService sysUserService;

    @GetMapping("/getClassComment/{classId}")
    public Result getClassComment(@PathVariable("classId") Long classId) {
        List<SysCommentDto> sysCommentList = sysCommentService.getClassComment(classId);
        for(int i = 0; i < sysCommentList.size(); i++) {
            SysUser sysUser = sysUserService.getById(sysCommentList.get(i).getUserId());
            sysCommentList.get(i).setUsername(sysUser.getUsername());
            for(int j = 0; j < sysCommentList.get(i).getChildren().size(); j++) {
                SysUser sysUser1 = sysUserService.getById(sysCommentList.get(i).getChildren().get(j).getUserId());
                sysCommentList.get(i).getChildren().get(j).setUsername(sysUser1.getUsername());
            }
        }
        return Result.succ(sysCommentList);
    }

    @PostMapping("/submit")
    @PreAuthorize("hasAuthority('course:home:comment:submit')")
    public Result submitComment(@RequestBody SysComment sysComment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Long userId = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", username)).getId();
        sysComment.setUserId(userId);
        sysComment.setCreated(LocalDateTime.now());
        sysCommentService.save(sysComment);
        return Result.succ("发布评论成功");
    }

    @GetMapping("/deleteComment/{id}")
    @PreAuthorize("hasAuthority('course:home:comment:delete')")
    public Result deleteComment(@PathVariable("id") Long commentId) {
        sysCommentService.removeById(commentId);
        sysCommentService.remove(new QueryWrapper<SysComment>().eq("parent_id", commentId));
        return Result.succ("删除评论成功");
    }
}
