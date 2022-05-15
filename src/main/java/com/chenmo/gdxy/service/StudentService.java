package com.chenmo.gdxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.LoginForm;
import com.chenmo.gdxy.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenmo
 * @since 2022-05-14
 */
public interface StudentService extends IService<Student> {

    Student login(LoginForm loginForm);

    IPage<Student> getStudentByOpr(Page<Student> page, Student student);
}
