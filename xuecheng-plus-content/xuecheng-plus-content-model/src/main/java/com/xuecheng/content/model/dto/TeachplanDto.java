package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author xb
 * @description 课程计划树型结构dto
 * @create 2023-03-26 19:37
 * @vesion 1.0
 */
@Data
@ToString
public class TeachplanDto extends Teachplan {

    //课程计划关联的媒资信息
    private TeachplanMedia teachplanMedia;

    //子节点
    private List<TeachplanDto> teachPlanTreeNodes;

}
