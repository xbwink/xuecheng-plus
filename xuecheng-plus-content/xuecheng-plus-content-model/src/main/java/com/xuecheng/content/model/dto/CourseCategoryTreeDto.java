package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xb
 * @description 课程分类树型结点dto
 * @create 2023-03-16 14:34
 * @vesion 1.0
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {

    private List<CourseCategoryTreeDto> childrenTreeNodes;

}
