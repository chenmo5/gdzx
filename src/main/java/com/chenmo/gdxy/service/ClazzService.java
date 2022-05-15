package com.chenmo.gdxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Clazz;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenmo
 * @since 2022-05-14
 */
public interface ClazzService extends IService<Clazz> {

    IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz);
}
