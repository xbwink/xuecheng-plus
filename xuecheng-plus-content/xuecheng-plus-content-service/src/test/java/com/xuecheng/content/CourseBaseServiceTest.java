package com.xuecheng.content;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xb
 * @description TODO
 * @create 2023-03-13 19:45
 * @vesion 1.0
 */
@SpringBootTest
public class CourseBaseServiceTest {

    @Autowired
    CourseBaseInfoService service;

    @Test
    void testCourseBaseService(){
        QueryCourseParamsDto queryCourseParamsDto = new QueryCourseParamsDto();
        queryCourseParamsDto.setCourseName("java");
        queryCourseParamsDto.setAuditStatus("202004");
        PageParams pageParams = new PageParams(1L,3L);
        PageResult<CourseBase> courseBasePageResult = service.queryCourseBaseList(queryCourseParamsDto, pageParams);
        System.out.println(courseBasePageResult);


    }

}
