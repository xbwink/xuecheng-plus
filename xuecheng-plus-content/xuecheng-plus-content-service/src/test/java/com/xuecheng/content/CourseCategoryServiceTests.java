package com.xuecheng.content;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author xb
 * @description TODO
 * @create 2023-03-17 14:44
 * @vesion 1.0
 */
@SpringBootTest
public class CourseCategoryServiceTests {

    @Autowired
    CourseCategoryService categoryService;

    @Test
    void testqueryTreeNodes() {
        List<CourseCategoryTreeDto> categoryTreeDtos =
                categoryService.queryTreeNodes("1");
        System.out.println(categoryTreeDtos);
    }

}
