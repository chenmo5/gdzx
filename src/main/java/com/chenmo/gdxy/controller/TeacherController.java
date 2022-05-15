package com.chenmo.gdxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Teacher;
import com.chenmo.gdxy.service.TeacherService;
import com.chenmo.gdxy.util.MD5;
import com.chenmo.gdxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chenmo
 * @since 2022-05-14
 */
@Api(tags = "教师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // sms/teacherController/deleteTeacher
    @ApiOperation("删除单个或多个教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("要删除的老师的id的集合") @RequestBody List<Integer>ids
            ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }

    // sms/teacherController/saveOrUpdateTeacher
    @ApiOperation("添加或修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("Json的teacher对象")@RequestBody Teacher teacher
    ){

        if (teacher.getId() == null || teacher.getId() == 0) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    // sms/teacherController/getTeachers/1/3?name&clazzName
    @ApiOperation("条件查询教师信息，带分页")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo")Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize")Integer pageSize,
            @ApiParam("分页查询的条件") Teacher teacher
    ){
        Page<Teacher> page=new Page<>(pageNo,pageSize);
        IPage<Teacher> Ipage=teacherService.getTeacherByOpr(page,teacher);
        return Result.ok(Ipage);
    }



}
