package com.waiterxiaoyy.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysDept;
import com.waiterxiaoyy.mapper.SysDeptMapper;
import com.waiterxiaoyy.service.SysDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/14 18:44
 * @Version 1.0
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public Result getDeptTree() {
        List<SysDept> sysDeptList = this.list(new QueryWrapper<SysDept>().orderByAsc("orderNum"));
        List<SysDept> treeDepts = builTree(sysDeptList);
        return Result.succ(200, "", treeDepts);
    }



    //建立树形结构
    public List<SysDept> builTree(List<SysDept> deptList){
        List<SysDept> treeDepts =new ArrayList<SysDept>();
        for(SysDept deptNode : getRootNode(deptList)) {
            deptNode=buildChilTree(deptNode, deptList);
            treeDepts.add(deptNode);
        }
        return treeDepts;
    }

    //递归，建立子树形结构
    private SysDept buildChilTree(SysDept pNode, List<SysDept> deptList){
        List<SysDept> chilMenus =new ArrayList<SysDept>();
        for(SysDept deptNode : deptList) {
            if(deptNode.getCollegeId() == pNode.getId()) {
                chilMenus.add(buildChilTree(deptNode, deptList));
            }
        }
        pNode.setChildren(chilMenus);
        return pNode;
    }

    //获取根节点
    private List<SysDept> getRootNode(List<SysDept> deptList) {
        List<SysDept> rootDeptList =new  ArrayList<SysDept>();
        for(SysDept deptNode : deptList) {
            if(deptNode.getCollegeId()==0) {
                rootDeptList.add(deptNode);
            }
        }
        return rootDeptList;
    }
}
