package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xb
 * @description 课程师资编辑接口
 * @create 2023-03-30 19:30
 * @vesion 1.0
 */
@Api(value = "课程师资编辑接口",tags = "课程师资编辑接口")
@RestController
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

    @GetMapping ("/courseTeacher/list/{courseId}")
    @ApiOperation("查询课程教师")
    public List<CourseTeacher> getCourseTeacherList(@PathVariable Long courseId){
        return courseTeacherService.getCourseTeacherList(courseId);
    }

    @PostMapping("/courseTeacher")
    @ApiOperation("添加、修改教师信息")
    public void saveCourseTeacher(@RequestBody CourseTeacher courseTeacher){
         courseTeacherService.saveCourseTeacher(courseTeacher);
    }

    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    @ApiOperation("删除教师信息")
    public void deleteCourseTeacher(@PathVariable Long courseId,@PathVariable Long teacherId){
         courseTeacherService.deleteCourseTeacher(courseId,teacherId);
    }


}
