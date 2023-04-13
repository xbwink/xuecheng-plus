package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xb
 * @description 修改课程dto
 * @create 2023-03-23 16:06
 * @vesion 1.0
 */
@Data
@ApiModel(value="EditCourseDto", description="修改课程基本信息")
public class EditCourseDto extends AddCourseDto{

    @ApiModelProperty(value = "课程id", required = true)
    private Long id;

}
