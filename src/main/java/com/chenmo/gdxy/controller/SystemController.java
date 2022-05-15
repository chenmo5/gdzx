package com.chenmo.gdxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenmo.gdxy.entity.Admin;
import com.chenmo.gdxy.entity.LoginForm;
import com.chenmo.gdxy.entity.Student;
import com.chenmo.gdxy.entity.Teacher;
import com.chenmo.gdxy.service.AdminService;
import com.chenmo.gdxy.service.StudentService;
import com.chenmo.gdxy.service.TeacherService;
import com.chenmo.gdxy.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    // sms/system/updatePwd/{oldPwd}/{newPwd}
    @ApiOperation("修改密码的处理器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @RequestHeader("token")String token,
            @PathVariable("oldPwd")String oldPwd,
            @PathVariable("newPwd")String newPwd
    ){
        //校验token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.fail().message("token已过期，请重新登陆在修改密码");
        }
        //获取用户ID和类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd= MD5.encrypt(oldPwd);
        newPwd=MD5.encrypt(newPwd);

        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper1=new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue());
                queryWrapper1.eq("password",oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);
                if (admin!=null){
                    //修改密码
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("原密码有误！");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapper2=new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if (student!=null){
                    //修改密码
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("原密码有误！");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3=new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue());
                queryWrapper3.eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if (teacher!=null){
                    //修改密码
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("原密码有误！");
                }
                break;
        }

        return Result.ok();
    }

    // sms/system/headerImgUpload
    @ApiOperation("/文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
           @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile

    ){
        //获取文件名
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String newFileName=uuid+originalFilename.substring(index);

        //保存文件 真实开发环境：将文件发送到第三方/独立的图片服务器上
        String portraitPath="D:/learn/Java学习/尚硅谷/ideaProject/project/gdxy/target/classes/public/upload/"+newFileName;
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path="upload/"+newFileName;
        //响应图片路径
        return Result.ok(path);
    }

    @ApiOperation("通过token获取当前登录的用户信息的方法")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@ApiParam("token口令")@RequestHeader("token") String token){
        //判断token是否有效
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){   //token过期了
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //解析token 获取用户id和用户类型
        Integer userType = JwtHelper.getUserType(token);
        Long userId = JwtHelper.getUserId(token);
        Map<String,Object>map=new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("登录的方法")
    @PostMapping("/login")
    public Result login(@ApiParam("登录提交的form表单")@RequestBody LoginForm loginForm,HttpServletRequest request){
        //验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();

        if ("".equals(sessionVerifiCode)||null==sessionVerifiCode){
            return Result.fail().message("验证码失效，请刷新后重试");
        }

        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)){
            return Result.fail().message("验证码有误，请刷新后重试");
        }
        //从session中移除现有验证码
        session.removeAttribute("verifiCode");

        //分用户类型进行校验
        //创建集合用来存放传送的信息
        Map<String,Object>map=new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin=adminService.login(loginForm);
                    if(admin!=null){
                        //用户类型和用户id生成一个密文，并以token的名称向客户端反馈
                        map.put("token",JwtHelper.createToken(admin.getId().longValue(), 1));
                    }else{
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail(e.getMessage());
                }

            case 2:
                try {
                    Student student= studentService.login(loginForm);
                    if(student!=null){
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token",token);
                    }else{
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail(e.getMessage());
                }

            case 3:
                try {
                    Teacher teacher= teacherService.login(loginForm);
                    if(teacher!=null){
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token",token);
                    }else{
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail(e.getMessage());
                }
        }

        return Result.fail().message("查无此用户");
    }

    @ApiOperation("获取验证码")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();

        //获取图片上的验证码
        String verifiCode=new String( CreateVerifiCodeImage.getVerifiCode());

        //将验证码文本放入session域，为下次验证做准备
        HttpSession session=request.getSession();
        session.setAttribute("verifiCode",verifiCode);

        //将验证码图片响应给浏览器
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(verifiCodeImage,"JPEG",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
