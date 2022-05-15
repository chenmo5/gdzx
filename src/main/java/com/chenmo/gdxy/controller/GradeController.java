package com.chenmo.gdxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Grade;
import com.chenmo.gdxy.service.GradeService;
import com.chenmo.gdxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chenmo
 * @since 2022-05-14
 */
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // sms/gradeController/getGrades
    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> list = gradeService.list();

        return Result.ok(list);
    }

    // sms/gradeController/deleteGrade
    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("要删除的所有的grade的id的json集合") @RequestBody List<Integer> ids){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    //sms/gradeController/getGrades/1/3?gradeName=%E4%B8%89
    @ApiOperation("根据年级名称查询，带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码数")@PathVariable("pageNo")Integer pageNo,
            @ApiParam("分页查询的页大小")@PathVariable("pageSize")Integer pageSize,
            @ApiParam("分页查询的模糊匹配")String gradeName
    ){
        //分页 带条件查询
        Page<Grade>page=new Page<>(pageNo,pageSize);
        //服务层完成查询
        IPage<Grade>pageRs=gradeService.getGradeByOpr(page,gradeName);

        //封装result并返回
        return Result.ok(pageRs);
    }

    // /sms/gradeController/saveOrUpdateGrade
    @ApiOperation("新增或修改grade，有id属性是修改，没有则是增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("JSON的Grade对象")@RequestBody Grade grade
    ){
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }


}
