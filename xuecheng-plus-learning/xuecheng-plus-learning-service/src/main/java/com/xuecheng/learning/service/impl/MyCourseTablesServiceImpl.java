package com.xuecheng.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.ContentServiceClient;
import com.xuecheng.learning.mapper.XcChooseCourseMapper;
import com.xuecheng.learning.mapper.XcCourseTablesMapper;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;
import com.xuecheng.learning.service.MyCourseTablesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xb
 * @description TODO
 * @create 2023-06-27 15:55
 * @vesion 1.0
 */

@Service
public class MyCourseTablesServiceImpl implements MyCourseTablesService {

    @Autowired
    ContentServiceClient contentServiceClient;
    @Autowired
    XcChooseCourseMapper chooseCourseMapper;
    @Autowired
    XcCourseTablesMapper courseTablesMapper;

    @Transactional
    @Override
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId) {
        //查询课程发布表判断是否收费
        CoursePublish coursepublish = contentServiceClient.getCoursepublish(courseId);
        XcChooseCourse xcChooseCourse = null;
        if("201000".equals(coursepublish.getCharge())){//免费课程
            //向选课记录表添加数据
            xcChooseCourse = addFreeCoruse(userId, coursepublish);
            //向我的课程表添加数据
            addCourseTabls(xcChooseCourse);
        }else {
            //收费课程
             xcChooseCourse = addChargeCoruse(userId, coursepublish);
        }

        XcChooseCourseDto xcChooseCourseDto = new XcChooseCourseDto();
        BeanUtils.copyProperties(xcChooseCourse,xcChooseCourseDto);
        //获取学习资格
        XcCourseTablesDto learningStatus = getLearningStatus(userId, courseId);
        xcChooseCourseDto.setLearnStatus(learningStatus.getLearnStatus());
        return xcChooseCourseDto;
    }

    //查询学习资格
    @Override
    public XcCourseTablesDto getLearningStatus(String userId, Long courseId) {
        //学习资格，[{"code":"702001","desc":"正常学习"},{"code":"702002","desc":"没有选课或选课后没有支付"},{"code":"702003","desc":"已过期需要申请续期或重新支付"}]
        XcCourseTablesDto xcCourseTablesDto = new XcCourseTablesDto();

        //查询我的课程表
        XcCourseTables xcCourseTables = getXcCourseTables(userId, courseId);
        if(xcCourseTables== null){
            //没有选课或选课后没有支付
            xcCourseTablesDto.setLearnStatus("702002");
            return xcCourseTablesDto;
        }

        LocalDateTime validtimeEnd = xcCourseTables.getValidtimeEnd();
        if(!LocalDateTime.now().isBefore(validtimeEnd)){
            //已过期
            xcCourseTablesDto.setLearnStatus("702003");
            return xcCourseTablesDto;
        }

        //正常学习
        BeanUtils.copyProperties(xcCourseTables,xcCourseTablesDto);
        xcCourseTablesDto.setLearnStatus("702001");
        return xcCourseTablesDto;
    }



    //添加免费课程,免费课程加入选课记录表、我的课程表
    public XcChooseCourse addFreeCoruse(String userId, CoursePublish coursepublish) {
        //查询选课记录表是否存在免费且选课成功的订单
        LambdaQueryWrapper<XcChooseCourse> wrapper = new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, coursepublish.getId())
                .eq(XcChooseCourse::getOrderType, "700001")//免费课程
                .eq(XcChooseCourse::getStatus, "701001");//选课成功
        List<XcChooseCourse> xcChooseCourses = chooseCourseMapper.selectList(wrapper);
        if(xcChooseCourses.size()>0){
            return xcChooseCourses.get(0);
        }

        //添加选课记录信息
        XcChooseCourse xcChooseCourse = new XcChooseCourse();
        xcChooseCourse.setCourseId(coursepublish.getId());
        xcChooseCourse.setCourseName(coursepublish.getName());
        xcChooseCourse.setUserId(userId);
        xcChooseCourse.setCompanyId(coursepublish.getCompanyId());
        xcChooseCourse.setOrderType("700001");//免费课程
        xcChooseCourse.setCreateDate(LocalDateTime.now());
        xcChooseCourse.setCoursePrice(coursepublish.getPrice());
        xcChooseCourse.setValidDays(365);//课程有效期默认365天
        xcChooseCourse.setStatus("701001");//设置课程状态为成功
        xcChooseCourse.setValidtimeStart(LocalDateTime.now());
        xcChooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));

        int insert = chooseCourseMapper.insert(xcChooseCourse);
        if(insert < 1){
            XueChengPlusException.cast("添加选课记录失败");
        }
        return xcChooseCourse;
    }

    //添加收费课程
    public XcChooseCourse addChargeCoruse(String userId,CoursePublish coursepublish){
        //查询选课记录表是否存在收费且状态未待支付的订单
        LambdaQueryWrapper<XcChooseCourse> wrapper = new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, coursepublish.getId())
                .eq(XcChooseCourse::getOrderType, "700002")//收费课程
                .eq(XcChooseCourse::getStatus, "701002");//待支付
        List<XcChooseCourse> xcChooseCourses = chooseCourseMapper.selectList(wrapper);
        if(xcChooseCourses.size()>0){
            return xcChooseCourses.get(0);
        }

        //添加选课记录信息
        XcChooseCourse xcChooseCourse = new XcChooseCourse();
        xcChooseCourse.setCourseId(coursepublish.getId());
        xcChooseCourse.setCourseName(coursepublish.getName());
        xcChooseCourse.setUserId(userId);
        xcChooseCourse.setCompanyId(coursepublish.getCompanyId());
        xcChooseCourse.setOrderType("700002");//收费课程
        xcChooseCourse.setCreateDate(LocalDateTime.now());
        xcChooseCourse.setCoursePrice(coursepublish.getPrice());
        xcChooseCourse.setValidDays(365);//课程有效期默认365天
        xcChooseCourse.setStatus("701002");//设置课程状态为待支付
        xcChooseCourse.setValidtimeStart(LocalDateTime.now());
        xcChooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));

        int insert = chooseCourseMapper.insert(xcChooseCourse);
        if(insert < 1){
            XueChengPlusException.cast("添加选课记录失败");
        }
        return xcChooseCourse;
    }

    //添加到我的课程表
    public XcCourseTables addCourseTabls(XcChooseCourse xcChooseCourse){
        //选课状态为成功才可添加
        if(!"701001".equals(xcChooseCourse.getStatus())){
            XueChengPlusException.cast("选课未成功，无法添加至课程表");
        }
        //查询我的课程表
        XcCourseTables xcCourseTables = getXcCourseTables(xcChooseCourse.getUserId(), xcChooseCourse.getCourseId());
        if(xcCourseTables!=null){
            return xcCourseTables;
        }

        //添加我的课程表记录
        XcCourseTables courseTables = new XcCourseTables();
        courseTables.setChooseCourseId(xcChooseCourse.getId());
        courseTables.setUserId(xcChooseCourse.getUserId());
        courseTables.setCourseId(xcChooseCourse.getCourseId());
        courseTables.setCompanyId(xcChooseCourse.getCompanyId());
        courseTables.setCourseName(xcChooseCourse.getCourseName());
        courseTables.setCreateDate(xcChooseCourse.getCreateDate());
        courseTables.setValidtimeStart(xcChooseCourse.getValidtimeStart());
        courseTables.setValidtimeEnd(xcChooseCourse.getValidtimeEnd());
        courseTables.setCourseType(xcChooseCourse.getOrderType());
        int insert = courseTablesMapper.insert(courseTables);
        if(insert < 1){
            XueChengPlusException.cast("添加我的课记录失败");
        }
        return courseTables;
    }

    /**
     * 根据用户id和课程id查询我的课程表的一门课程
     * @param userId
     * @param courseId
     * @return
     */
    public XcCourseTables getXcCourseTables(String userId,Long courseId){
        LambdaQueryWrapper<XcCourseTables> wrapper = new LambdaQueryWrapper<XcCourseTables>()
                .eq(XcCourseTables::getCourseId, courseId)
                .eq(XcCourseTables::getUserId, userId);
        XcCourseTables xcCourseTables = courseTablesMapper.selectOne(wrapper);
        return xcCourseTables;
    }

}
