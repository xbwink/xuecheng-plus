package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xb
 * @description TODO
 * @create 2023-03-27 19:47
 * @vesion 1.0
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplayTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }


    //获取同级课程计划数量
    public Integer getSameTreeCounts(Long courseId, Long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId)
                .eq(Teachplan::getParentid, parentId);
        return teachplanMapper.selectCount(queryWrapper);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //根据id判断新增还是修改
        Long teachPlanId = saveTeachplanDto.getId();
        if (teachPlanId == null) { //新增课程计划
            //将dto数据拷贝到po对象
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);
            //确定排序规则(当前节点同级个数+1)
            Integer count = getSameTreeCounts(teachplan.getCourseId(), teachplan.getParentid());
            teachplan.setOrderby(count + 1);
            teachplanMapper.insert(teachplan);
            return;
        }

        //修改课程计划
        Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
        BeanUtils.copyProperties(saveTeachplanDto, teachplan);
        teachplanMapper.updateById(teachplan);
    }

    //查询当前课程计划的子节点数量
    public Integer getChildNodeCount(Long teachPlanId) {
        //sql:select count(1) from teachplan where parentid = ?;
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid, teachPlanId);
        return teachplanMapper.selectCount(queryWrapper);
    }

    @Transactional
    @Override
    public void deleteTeachplan(Long teachPlanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
        //判断是否为大章节
        if (teachplan.getParentid() == 0) {
            //删除第一级别的大章节时要求大章节下边没有小节即可删除
            Long id = teachplan.getId();
            Integer childNodeCount = getChildNodeCount(id);
            if (childNodeCount > 0) {
                XueChengPlusException.cast("改章节还存在子节点！");
            }
            //执行删除
            teachplanMapper.deleteById(teachPlanId);
            return;
        }

        //删除第二级别的小章节是同时需要将teach_media表关联的信息也删除
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getTeachplanId, teachPlanId);
        teachplanMediaMapper.delete(queryWrapper);
        teachplanMapper.deleteById(teachPlanId);

    }

    @Override
    public void teachPlanMoveUp(String index, Long teachPlanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachPlanId);

        //构建queryWrapper
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        //查询出同级的节点
        queryWrapper.eq(Teachplan::getCourseId, teachplan.getCourseId())
                .eq(Teachplan::getParentid, teachplan.getParentid())
                .lt(index.equals("moveup"), Teachplan::getOrderby, teachplan.getOrderby())
                .gt(index.equals("movedown"), Teachplan::getOrderby, teachplan.getOrderby())
                .orderByDesc(index.equals("moveup"),Teachplan::getOrderby)
                .last(" limit 1"); //表示只获取第一行数据
        //上方或者下方的课程计划
        Teachplan teachplan1 = teachplanMapper.selectOne(queryWrapper);
        if(teachplan1 == null){
            XueChengPlusException.cast("已经到达边界，不能再移动啦！");
        }
        //交换位置
        Integer o = teachplan.getOrderby();
        Integer o1 = teachplan1.getOrderby();
        teachplan.setOrderby(o1);
        teachplan1.setOrderby(o);
        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(teachplan1);
    }


}
