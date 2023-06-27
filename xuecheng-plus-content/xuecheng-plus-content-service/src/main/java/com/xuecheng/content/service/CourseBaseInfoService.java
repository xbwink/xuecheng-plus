package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author xb
 * @description 课程查询接口
 * @create 2023-03-13 19:34
 * @vesion 1.0
 */
public interface CourseBaseInfoService {

     /**
      * 查询课程列表
      * @param courseParamsDto 查询条件
      * @param pageParams 分页参数
      * @return
      */
     PageResult<CourseBase> queryCourseBaseList(QueryCourseParamsDto courseParamsDto, PageParams pageParams,Long companyId);

     /**
      * 添加课程基本信息
      * @param companyId 教学机构id
      * @param dto 课程基本信息
      * @return CourseBaseInfoDto
      */
     CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto);

     /**
      * 根据课程id查询课程基本信息，包括基本信息和营销信息
      * @param courseId 课程id
      * @return
      */
     CourseBaseInfoDto getCourseBaseInfo(Long courseId);

     /**
      * 修改课程基本信息
      * @param companyId 教学机构id
      * @param dto 课程基本信息
      * @return CourseBaseInfoDto
      */
     CourseBaseInfoDto editCourseBase(Long companyId, EditCourseDto dto);

     /**
      * 删除课程基本信息
      * @param courseId 课程id
      */
     void deleteCourseBase(Long companyId,Long courseId);

}
