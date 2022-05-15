package com.chenmo.gdxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenmo.gdxy.entity.Clazz;
import com.chenmo.gdxy.entity.LoginForm;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenmo
 * @since 2022-05-14
 */
public interface AdminService extends IService<Admin> {

    Admin login(LoginForm loginForm);

    IPage<Admin> getAdminByOpr(Page<Admin> page, String adminName);
}
