package com.xuecheng.content.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author xb
 * @description 课程预览数据模型
 * @create 2023-05-22 15:07
 * @vesion 1.0
 */
@Data
public class CoursePreviewDto {

    //课程基本信息,课程营销信息
    CourseBaseInfoDto courseBase;


    //课程计划信息
    List<TeachplanDto> teachplans;

    //师资信息暂时不加...

}
