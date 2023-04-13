package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @author xb
 * @description 课程分类接口
 * @create 2023-03-16 16:46
 * @vesion 1.0
 */
public interface CourseCategoryService {

    /**
     * 课程分类树形结构查询
     *
     * @return
     */
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);

}
