package com.chenmo.gdxy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Admin;
import com.chenmo.gdxy.entity.Student;
import com.chenmo.gdxy.service.AdminService;
import com.chenmo.gdxy.service.StudentService;
import com.chenmo.gdxy.service.TeacherService;
import com.chenmo.gdxy.util.JwtHelper;
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
@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;




    //sms/studentController/delStudentById
    @ApiOperation("删除单个或多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("要删除的所有学生的id的集合") @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    // sms/studentController/addOrUpdateStudent
    @ApiOperation(("添加或修改学生信息,有id属性是修改，没有则是增加"))
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
           @ApiParam("Json的学生对象") @RequestBody Student student
    ){
        Integer id = student.getId();
        if (null == id || 0 ==id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    // sms/studentController/getStudentByOpr/1/3?name&clazzName
    @ApiOperation("根据条件查询学生信息，分页查询")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo")Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize")Integer pageSize,
            @ApiParam("分页查询的条件") Student student
            ){
        Page<Student> page=new Page<>(pageNo,pageSize);
        IPage<Student> IPage=studentService.getStudentByOpr(page,student);
        return Result.ok(IPage);
    }
}
