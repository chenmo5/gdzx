package com.chenmo.gdxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Clazz;
import com.chenmo.gdxy.service.ClazzService;
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
@Api(tags = "班级控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    // sms/clazzController/getClazzs
    @ApiOperation("获取班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> list = clazzService.list();
        return Result.ok(list);
    }


    //sms/clazzController/deleteClazz
    @ApiOperation("删除单个或多个班级")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(@ApiParam("要删除的所有班级的id的集合") @RequestBody List<Integer> ids){
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    //sms/clazzController/saveOrUpdateClazz
    @PostMapping("/saveOrUpdateClazz")
    @ApiOperation("新增或修改班级信息，有id属性是修改，没有则是增加")
    public Result saveOrUpdateClazz(@ApiParam("Json的Clazz对象")@RequestBody Clazz clazz){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    // sms/clazzController/getClazzsByOpr/1/3
    @ApiOperation("根据班级名称查询，带分页")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("分页查询的页码数")@PathVariable("pageNo")Integer pageNo,
            @ApiParam("分页查询的页大小")@PathVariable("pageSize")Integer pageSize,
            @ApiParam("分页查询的查询条件")Clazz clazz
    ){
        Page<Clazz> page=new Page<>(pageNo,pageSize);
        IPage<Clazz> iPage=clazzService.getClazzByOpr(page,clazz);
        return Result.ok(iPage);
    }
}
