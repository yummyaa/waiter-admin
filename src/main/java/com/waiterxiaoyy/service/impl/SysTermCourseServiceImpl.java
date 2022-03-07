package com.waiterxiaoyy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysDept;
import com.waiterxiaoyy.entity.SysTermCourse;
import com.waiterxiaoyy.mapper.SysTermCourseMapper;
import com.waiterxiaoyy.service.SysTermCourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/7 19:16
 * @Version 1.0
 */
@Service
public class SysTermCourseServiceImpl extends ServiceImpl<SysTermCourseMapper, SysTermCourse> implements SysTermCourseService {


    @Override
    public Result getTermCourseClass(String query) {
        List<SysTermCourse> sysTermCourseList = this.list(new QueryWrapper<SysTermCourse>().orderByAsc("orderNum").like(StrUtil.isNotBlank(query), "name", query));
        List<SysTermCourse> tree = builTree(sysTermCourseList);
        return Result.succ(200, "", tree);
    }



    //建立树形结构
    public List<SysTermCourse> builTree(List<SysTermCourse> sysTermCourseList){
        List<SysTermCourse> tree =new ArrayList<SysTermCourse>();
        for(SysTermCourse sysTermCourse : getRootNode(sysTermCourseList)) {
            sysTermCourse=buildChilTree(sysTermCourse, sysTermCourseList);
            tree.add(sysTermCourse);
        }
        return tree;
    }

    //递归，建立子树形结构
    private SysTermCourse buildChilTree(SysTermCourse pNode, List<SysTermCourse> sysTermCourseList){
        List<SysTermCourse> chil =new ArrayList<SysTermCourse>();
        for(SysTermCourse sysTermCourse : sysTermCourseList) {
            if(sysTermCourse.getParentId() == pNode.getId()) {
                chil.add(buildChilTree(sysTermCourse, sysTermCourseList));
            }
        }
        pNode.setChildren(chil);
        return pNode;
    }

    //获取根节点
    private List<SysTermCourse> getRootNode(List<SysTermCourse> sysTermCourseList) {
        List<SysTermCourse> sysTermCourses =new  ArrayList<SysTermCourse>();
        for(SysTermCourse sysTermCourse : sysTermCourseList) {
            if(sysTermCourse.getParentId()==0) {
                sysTermCourses.add(sysTermCourse);
            }
        }
        return sysTermCourses;
    }
}
