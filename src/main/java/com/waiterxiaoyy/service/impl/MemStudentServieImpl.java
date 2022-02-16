package com.waiterxiaoyy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.dto.CollegeClassDto;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.*;
import com.waiterxiaoyy.mapper.SysDeptMapper;
import com.waiterxiaoyy.mapper.MemStudentMapper;
import com.waiterxiaoyy.service.MemStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/13 20:36
 * @Version 1.0
 */
@Service
public class MemStudentServieImpl extends ServiceImpl<MemStudentMapper, SysStudent> implements MemStudentService {


    @Autowired
    SysDeptMapper sysDeptMapper;

    @Override
    public Result getCollegeClassTree() {
        List<SysDept> sysDeptList = sysDeptMapper.selectList(new QueryWrapper<SysDept>().orderByAsc("orderNum"));

        List<CollegeClassDto> collegeClassDtoList = new ArrayList<>();

        sysDeptList.forEach(sysDept -> {
           CollegeClassDto collegeClassDto = new CollegeClassDto();
           collegeClassDto.setStatu(sysDept.getStatu());
           collegeClassDto.setId(sysDept.getId());
           collegeClassDto.setLabel(sysDept.getName());
           collegeClassDto.setCollegeId(sysDept.getCollegeId());
           collegeClassDto.setType(sysDept.getType());
           collegeClassDtoList.add(collegeClassDto);
        });

        List<CollegeClassDto> ansList = builTree(collegeClassDtoList);

        return Result.succ(ansList);
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
