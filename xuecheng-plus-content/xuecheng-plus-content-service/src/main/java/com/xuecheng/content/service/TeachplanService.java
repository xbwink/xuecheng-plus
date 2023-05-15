package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMedia;

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

    /**
     * @description 教学计划绑定媒资
     * @param bindTeachplanMediaDto
     * @return com.xuecheng.content.model.po.TeachplanMedia
     * @author Mr.M
     * @date 2022/9/14 22:20
     */
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

}
