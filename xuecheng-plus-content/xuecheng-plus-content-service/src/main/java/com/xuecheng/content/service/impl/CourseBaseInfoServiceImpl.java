package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xb
 * @description TODO
 * @create 2023-03-13 19:36
 * @vesion 1.0
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CourseCategoryMapper categoryMapper;
    @Autowired
    CourseMarketServiceImpl courseMarketService;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    CourseTeacherMapper courseTeacherMapper;


    @Override
    public PageResult<CourseBase> queryCourseBaseList(QueryCourseParamsDto courseParamsDto, PageParams pageParams,Long companyId) {
        //拼接查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据机构id查询
        queryWrapper.eq(CourseBase::getCompanyId,companyId);
        //根据课程名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName());
        //根据课程审核状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus());
        //根据课程发布状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()), CourseBase::getStatus, courseParamsDto.getPublishStatus());


        //创建page分页参数对象，参数：当前页码、每页显示记录数
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        //开始进行分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        //数据列表
        List<CourseBase> records = pageResult.getRecords();
        //总记录数
        long total = pageResult.getTotal();

        PageResult<CourseBase> courseBasePageResult = new PageResult<>(records, total, pageParams.getPageNo(), pageParams.getPageSize());

        return courseBasePageResult;
    }

    @Transactional
    @Override
    public void deleteCourseBase(Long companyId,Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (!companyId.equals(courseBase.getCompanyId()))
            XueChengPlusException.cast("只允许删除本机构的课程");
        //课程的审核状态为未提交时才可删除。
        if(!"202002".equals(courseBase.getAuditStatus())){
            XueChengPlusException.cast("只有当课程审核状态为未提交时才可删除");
        }
        //删除课程需要删除课程相关的基本信息、营销信息、课程计划、课程教师信息。
        //删除课程相关的营销信息
        courseMarketMapper.deleteById(courseId);
        //删除课程计划
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        teachplanMapper.delete(queryWrapper);
        //删除教师信息
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId,courseId);
        courseTeacherMapper.delete(wrapper);
        //删除课程基本信息
        courseBaseMapper.deleteById(courseId);

    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {

        //合法性校验
//        if (StringUtils.isBlank(dto.getName())) {
//            XueChengPlusException.cast("课程名称为空");
//        }
//        if (StringUtils.isBlank(dto.getMt())) {
//            XueChengPlusException.cast("课程分类为空");
//        }
//        if (StringUtils.isBlank(dto.getGrade())) {
//            XueChengPlusException.cast("课程等级为空");
//        }
//        if (StringUtils.isBlank(dto.getTeachmode())) {
//            XueChengPlusException.cast("教育模式为空");
//        }
//        if (StringUtils.isBlank(dto.getUsers())) {
//            XueChengPlusException.cast("适应人群为空");
//        }
//        if (StringUtils.isBlank(dto.getCharge())) {
//            XueChengPlusException.cast("收费规则为空");
//        }
        //构建新增课程对象
        CourseBase courseBase = new CourseBase();
        //将用户填写的课程信息赋值给新增对象
        BeanUtils.copyProperties(dto, courseBase);
        //设置审核状态(默认未提交)
        courseBase.setAuditStatus("202002");
        //设置发布状态(默认未发布)
        courseBase.setStatus("203001");
        //创建时间
        courseBase.setCreateDate(LocalDateTime.now());
        //机构id
        courseBase.setCompanyId(companyId);
        //插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBase);
        Long courseId = courseBase.getId();

        //课程营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);
        courseMarket.setId(courseId);
        //收费规则
        String charge = courseMarket.getCharge();
        //收费课程必须填写价格并且价格>0
        if (charge.equals("201001")) {
            BigDecimal price = dto.getPrice();
            if (price == null || price.floatValue() <= 0) {
                throw new XueChengPlusException("课程设置了收费价格不能为空且必须大于0");
            }
            courseMarket.setPrice(dto.getPrice().floatValue());
            courseMarket.setOriginalPrice(dto.getOriginalPrice().floatValue());
        }


        //插入课程营销信息
        int insert1 = courseMarketMapper.insert(courseMarket);
        if (insert <= 0 || insert1 <= 0) {
            XueChengPlusException.cast("新增课程基本信息失败");
        }

        //添加成功，返回添加的课程信息
        return getCourseBaseInfo(courseId);
    }

    //根据课程id查询课程基本信息，包含基本信息和营销信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        //先单独查询出课程基本信息和营销信息两张表
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

        if (courseBase == null) {
            return null;
        }
        //创建出返回对象
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }

        //设置分类
        CourseCategory bySt = categoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setMtName(bySt.getName());
        CourseCategory byMt = categoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(byMt.getName());
        return courseBaseInfoDto;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto editCourseBase(Long companyId, EditCourseDto dto) {
        //先查询得到课程
        CourseBase courseBase = courseBaseMapper.selectById(dto.getId());
        //机构验证
//        if(!companyId.equals(courseBase.getCompanyId())){
//            XueChengPlusException.cast("只允许修改本机构的课程");
//        }

        BeanUtils.copyProperties(dto,courseBase);
        //设置修改时间
        courseBase.setChangeDate(LocalDateTime.now());
        int update = courseBaseMapper.updateById(courseBase);
        if(update <= 0){
            XueChengPlusException.cast("修改失败");
        }

        //修改课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(dto.getId());
        if(courseMarket==null){
            courseMarket = new CourseMarket();
        }
        //收费规则
        String charge = dto.getCharge();
        //收费课程必须填写价格并且价格>0
        if (charge.equals("201001")) {
            BigDecimal price = dto.getPrice();
            if (price == null || price.floatValue() <= 0) {
                throw new XueChengPlusException("课程设置了收费价格不能为空且必须大于0");
            }
            courseMarket.setPrice(dto.getPrice().floatValue());
            courseMarket.setOriginalPrice(dto.getOriginalPrice().floatValue());
        }else {
            courseMarket.setPrice(new Float(0));
            courseMarket.setOriginalPrice(new Float(0));
        }

        //将dto中的课程营销信息拷贝至courseMarket对象中
        BeanUtils.copyProperties(dto,courseMarket);

        //修改课程营销信息
        boolean b = courseMarketService.saveOrUpdate(courseMarket);

        CourseBaseInfoDto courseBaseNew = getCourseBaseInfo(dto.getId());

        return courseBaseNew;
    }

}
