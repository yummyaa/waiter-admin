package com.waiterxiaoyy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.*;
import com.waiterxiaoyy.mapper.SysTermCourseMapper;
import com.waiterxiaoyy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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

    @Autowired
    SysDistStudentService sysDistStudentService;

    @Autowired
    SysTeacherClassService sysTeacherClassService;

    @Autowired
    MemTeacherService memTeacherService;

    @Override
    public Result getTermCourseClass() {
        List<SysTermCourse> sysTermCourseList = this.list(new QueryWrapper<SysTermCourse>().orderByAsc("orderNum"));
        List<SysTermCourse> tree = builTree(sysTermCourseList);
        return Result.succ(200, "", tree);
    }

    @Override
    public Result getStuTermCourseList(String username) {
        List<SysTermCourse> sysTermCourseList = this.list(new QueryWrapper<SysTermCourse>().orderByAsc("orderNum"));
        List<SysDistStudent> sysDistStudents = sysDistStudentService.list(new QueryWrapper<SysDistStudent>().eq("student_id", username));
        HashSet<Long> classSet = new HashSet<>();
        for(SysDistStudent student : sysDistStudents) {
            classSet.add(student.getClassId());
        }
        Iterator<SysTermCourse> iterator = sysTermCourseList.iterator();
        while(iterator.hasNext()) {
            SysTermCourse sysTermCourse = iterator.next();
            if((sysTermCourse.getType() == 2 && !classSet.contains(sysTermCourse.getId())) || sysTermCourse.getStatu() == 0) {
                iterator.remove();
            }
        }
        List<SysTermCourse> tree = builTree(sysTermCourseList);
        return Result.succ(200, "", tree);
    }

    @Override
    public Result getTeacTermCourseList(String username) {
        List<SysTermCourse> sysTermCourseList = this.list(new QueryWrapper<SysTermCourse>().orderByAsc("orderNum"));
        SysTeacher sysTeacher = memTeacherService.getOne(new QueryWrapper<SysTeacher>().eq("teacher_id", username));
        List<SysTeacherClass> sysTeacherClasses = sysTeacherClassService.list(new QueryWrapper<SysTeacherClass>().eq("teacher_id", sysTeacher.getId()));
        HashSet<Long> classSet = new HashSet<>();
        for(SysTeacherClass teacher : sysTeacherClasses) {
            classSet.add(teacher.getClassId());
        }
        Iterator<SysTermCourse> iterator = sysTermCourseList.iterator();
        while(iterator.hasNext()) {
            SysTermCourse sysTermCourse = iterator.next();
            if((sysTermCourse.getType() == 2 && !classSet.contains(sysTermCourse.getId())) || sysTermCourse.getStatu() == 0) {
                iterator.remove();
            }
        }
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
