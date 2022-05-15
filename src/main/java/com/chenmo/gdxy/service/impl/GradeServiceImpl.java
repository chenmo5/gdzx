package com.chenmo.gdxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Grade;
import com.chenmo.gdxy.mapper.GradeMapper;
import com.chenmo.gdxy.service.GradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenmo
 * @since 2022-05-14
 */
@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> pageParam, String gradeName) {
        QueryWrapper<Grade> queryWrapper=new QueryWrapper();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",gradeName);
        }
        queryWrapper.orderByDesc("id");

        Page<Grade> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }
}
