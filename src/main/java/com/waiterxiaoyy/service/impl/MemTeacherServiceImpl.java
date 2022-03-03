package com.waiterxiaoyy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.dto.CollegeClassDto;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysDept;
import com.waiterxiaoyy.entity.SysStudent;
import com.waiterxiaoyy.entity.SysTeacher;
import com.waiterxiaoyy.mapper.MemMajorTeacherMapper;
import com.waiterxiaoyy.mapper.MemTeacherMapper;
import com.waiterxiaoyy.mapper.SysDeptMapper;
import com.waiterxiaoyy.service.MemTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/3 22:23
 * @Version 1.0
 */
@Service
public class MemTeacherServiceImpl extends ServiceImpl<MemTeacherMapper, SysTeacher> implements MemTeacherService {

    @Autowired
    SysDeptMapper sysDeptMapper;

    @Autowired
    MemMajorTeacherMapper memMajorTeacherMapper;

    @Override
    public Result getColMajorTree() {

        List<SysDept> sysDeptList = sysDeptMapper.selectList(new QueryWrapper<SysDept>().orderByAsc("orderNum"));

        List<CollegeClassDto> collegeClassDtoList = new ArrayList<>();

        sysDeptList.forEach(sysDept -> {
            if(sysDept.getType() == 0 || sysDept.getType() == 1) {
                CollegeClassDto collegeClassDto = new CollegeClassDto();
                collegeClassDto.setStatu(sysDept.getStatu());
                collegeClassDto.setId(sysDept.getId());
                collegeClassDto.setLabel(sysDept.getName());
                collegeClassDto.setCollegeId(sysDept.getCollegeId());
                collegeClassDto.setType(sysDept.getType());
                collegeClassDtoList.add(collegeClassDto);
            }
        });

        List<CollegeClassDto> ansList = builTree(collegeClassDtoList);

        return Result.succ(ansList);
    }

    @Override
    public Page<SysTeacher> getMajorTeacherList(Page page, Integer majorId, String teacherName) {

        Page<SysTeacher> pageData = memMajorTeacherMapper.getMajorTeacherList(page, majorId, teacherName);
        return pageData;
    }

    //建立树形结构
    public List<CollegeClassDto> builTree(List<CollegeClassDto> collegeClassDtoList){
        List<CollegeClassDto> collegeClassDtos =new ArrayList<CollegeClassDto>();
        for(CollegeClassDto collegeClassDto : getRootNode(collegeClassDtoList)) {
            collegeClassDto=buildChilTree(collegeClassDto, collegeClassDtoList);
            collegeClassDtos.add(collegeClassDto);
        }
        return collegeClassDtos;
    }

    //递归，建立子树形结构
    private CollegeClassDto buildChilTree(CollegeClassDto pNode, List<CollegeClassDto> collegeClassDtos){
        List<CollegeClassDto> chilMenus =new ArrayList<CollegeClassDto>();
        for(CollegeClassDto ccNode : collegeClassDtos) {
            if(ccNode.getCollegeId() == pNode.getId()) {
                chilMenus.add(buildChilTree(ccNode, collegeClassDtos));
            }
        }
        pNode.setChildren(chilMenus);
        return pNode;
    }

    //获取根节点
    private List<CollegeClassDto> getRootNode(List<CollegeClassDto> collegeClassDtos) {
        List<CollegeClassDto> rootCCList =new  ArrayList<CollegeClassDto>();
        for(CollegeClassDto deptNode : collegeClassDtos) {
            if(deptNode.getCollegeId()==0) {
                rootCCList.add(deptNode);
            }
        }
        return rootCCList;
    }
}
