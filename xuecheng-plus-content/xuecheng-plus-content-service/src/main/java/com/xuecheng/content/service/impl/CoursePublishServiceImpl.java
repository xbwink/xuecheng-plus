package com.xuecheng.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.utils.StringUtils;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xb
 * @description TODO
 * @create 2023-05-22 15:09
 * @vesion 1.0
 */
@Slf4j
@Service
public class CoursePublishServiceImpl implements CoursePublishService {

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    @Autowired
    TeachplanService teachplanService;

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CoursePublishPreMapper coursePublishPreMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper categoryMapper;

    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        //课程基本信息、营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        coursePreviewDto.setCourseBase(courseBaseInfo);
        //课程计划信息
        List<TeachplanDto> teachplayTree = teachplanService.findTeachplayTree(courseId);
        coursePreviewDto.setTeachplans(teachplayTree);

        return coursePreviewDto;
    }

    @Transactional
    @Override
    public void commitAudit(Long companyId, Long courseId) {
        //约束校验
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        //课程审核状态
        String auditStatus = courseBase.getAuditStatus();
        //当前审核状态为已提交不允许再次提交
        if("202003".equals(auditStatus)){
            XueChengPlusException.cast("当前为等待审核状态，审核完成可以再次提交。");
        }
        //本机构只允许提交本机构的课程
//        if(!courseBase.getCompanyId().equals(companyId)){
//            XueChengPlusException.cast("不允许提交其它机构的课程。");
//        }

        //课程图片是否填写
        if(StringUtils.isEmpty(courseBase.getPic())){
            XueChengPlusException.cast("提交失败，请上传课程图片");
        }

        //添加课程预发布记录
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        //课程基本信息加部分营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        //查询分类信息，将分类名称放入courseBaseInfo对象
        CourseCategory mt = categoryMapper.selectById(courseBase.getMt());
        courseBaseInfo.setMtName(mt.getName()); //大分类名称
        CourseCategory st = categoryMapper.selectById(courseBase.getSt());
        courseBaseInfo.setStName(st.getName()); //小分类名称
        BeanUtils.copyProperties(courseBaseInfo,coursePublishPre);

        //课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //转为json
        String courseMarketJson = JSON.toJSONString(courseMarket);
        //将课程营销信息json数据放入课程预发布表
        coursePublishPre.setMarket(courseMarketJson);

        //查询课程计划信息
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplayTree(courseId);
        if(teachplanTree.size()<=0){
            XueChengPlusException.cast("提交失败，还没有添加课程计划");
        }
        //转json
        String teachplanTreeString = JSON.toJSONString(teachplanTree);
        coursePublishPre.setTeachplan(teachplanTreeString);

        //设置预发布记录状态,已提交
        coursePublishPre.setStatus("202003");
        //教学机构id
        coursePublishPre.setCompanyId(companyId);
        //提交时间
        coursePublishPre.setCreateDate(LocalDateTime.now());
        //查询课程预发布记录表 存在则更新不存在则插入
        CoursePublishPre coursePublishPreUpdate = coursePublishPreMapper.selectById(courseId);
        if(coursePublishPreUpdate == null){
            //添加课程预发布记录
            coursePublishPreMapper.insert(coursePublishPre);
        }else{
            coursePublishPreMapper.updateById(coursePublishPre);
        }



        //更新课程基本表的审核状态
        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);
    }

}
