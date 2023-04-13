package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @author xb
 * @description 课程师资服务
 * @create 2023-03-30 19:32
 * @vesion 1.0
 */
public interface CourseTeacherService {

    /**
     * 查询课程老师
     * @param courseId 课程id
     * @return
     */
    List<CourseTeacher> getCourseTeacherList(Long courseId);

    /**
     * 新增、修改课程教师信息
     * @param courseTeacher 教师信息
     */
    void saveCourseTeacher(CourseTeacher courseTeacher);

    /**
     * 删除教师信息
     * @param courseId  课程id
     * @param teacherId 教师id
     */
    void deleteCourseTeacher(Long courseId,Long teacherId);

}
