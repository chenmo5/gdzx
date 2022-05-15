package com.chenmo.gdxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenmo.gdxy.entity.Admin;
import com.chenmo.gdxy.entity.Clazz;
import com.chenmo.gdxy.service.AdminService;
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
@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //sms/adminController/deleteAdmin
    @ApiOperation(("删除单个或多个管理员"))
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("要删除的管理员的id的集合") @RequestBody List<Integer> ids
            ){
        adminService.removeByIds(ids);
        return Result.ok();
    }

    //sms/adminController/saveOrUpdateAdmin
    @ApiOperation("添加或修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("Json的Admin对象")@RequestBody Admin admin
    ){
        //如果为新增管理员需要对密码进行加密
        Integer id=admin.getId();
        if (id==null||id==0){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    // sms/adminController/getAllAdmin/1/3?adminName=a
    @ApiOperation("管理员条件查询，带分页")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("条件查询的页码") @PathVariable("pageNo")Integer pageNo,
            @ApiParam("条件查询的页大小") @PathVariable("pageSize")Integer pageSize,
            @ApiParam("条件查询的查询条件")String adminName
    ){
        Page<Admin>page=new Page<>(pageNo,pageSize);
        IPage<Admin> iPage=adminService.getAdminByOpr(page,adminName);
        return Result.ok(iPage);
    }

}
