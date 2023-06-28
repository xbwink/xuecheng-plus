package com.xuecheng.learning.service;

import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;

/**
 * @author xb
 * @description 我的课程表service接口
 * @create 2023-06-27 15:53
 * @vesion 1.0
 */
public interface MyCourseTablesService {

    /**
     * 添加选课
     *
     * @param userId
     * @param courseId
     * @return
     */
    XcChooseCourseDto addChooseCourse(String userId, Long courseId);

    /**
     * 获取查询资格
     * @param userId
     * @param courseId
     * @return
     */
    XcCourseTablesDto getLearningStatus(String userId, Long courseId);

}
