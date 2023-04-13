package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xb
 * @description 课程分类相关接口
 * @create 2023-03-16 14:38
 * @vesion 1.0
 */
@Api(value = "课程分类相关接口",tags = "课程分类相关接口")
@RestController
public class CourseCategoryController {

    @Autowired
    private CourseCategoryService categoryService;

    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes(){
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = categoryService.queryTreeNodes("1");
        return courseCategoryTreeDtos;
    }

}
