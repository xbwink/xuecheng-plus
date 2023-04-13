package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @author xb
 * @description 课程计划相关接口
 * @create 2023-03-27 19:45
 * @vesion 1.0
 */
public interface TeachplanService {

    /**
     * 查询课程计划树形结构
     * @param courseId 课程id
     * @return
     */
    List<TeachplanDto> findTeachplayTree(Long courseId);

    /**
     * 新增/修改/保存课程计划
     * @param saveTeachplanDto 课程计划dto
     */
    void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    /**
     * 删除课程计划
     * @param teachPlanId 课程计划id
     */
    void deleteTeachplan(Long teachPlanId);

    /**
     * 课程计划上移/下移
     * @param index 移动方向：moveup上移 movedown下移
     * @param teachPlanId 课程计划id
     */
    void teachPlanMoveUp(String index,Long teachPlanId);



}
