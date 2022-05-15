package com.chenmo.gdxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Clazz;
import com.chenmo.gdxy.mapper.ClazzMapper;
import com.chenmo.gdxy.service.ClazzService;
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
@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {

    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> pageParam, Clazz clazz) {
        QueryWrapper<Clazz> queryWrapper=new QueryWrapper<>();
        String gradeName = clazz.getGradeName();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }

        String name = clazz.getName();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("id");
        Page<Clazz> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }
}
