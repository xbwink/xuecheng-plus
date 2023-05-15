package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xb
 * @description 课程计划编辑接口
 * @create 2023-03-26 19:40
 * @vesion 1.0
 */
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
@RestController
public class TeachplanController {

    @Autowired
    TeachplanService teachplanService;


    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType
            = "Long",paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        return teachplanService.findTeachplayTree(courseId);
    }

    @PostMapping("/teachplan")
    @ApiOperation("课程计划创建或修改")
    public void saveTeachplan(@RequestBody SaveTeachplanDto saveTeachplanDto){
         teachplanService.saveTeachplan(saveTeachplanDto);
    }

    @DeleteMapping("/teachplan/{teachPlanId}")
    @ApiOperation("课程计划删除")
    public void deleteTeachplan(@PathVariable Long teachPlanId){
        teachplanService.deleteTeachplan(teachPlanId);
    }

    @PostMapping("/teachplan/{index}/{teachPlanId}")
    @ApiOperation("课程计划向上、下移动")
    public void teachPlanMoveUp(@PathVariable String index,@PathVariable Long teachPlanId){
        teachplanService.teachPlanMoveUp(index,teachPlanId);
    }

    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto){
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }



}
