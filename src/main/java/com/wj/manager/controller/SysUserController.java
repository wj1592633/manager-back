package com.wj.manager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.Enum.LogTypeEnum;
import com.wj.manager.common.anotation.BusinessLog;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.dto.QueryParams;
import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.log.constance.LogConst;
import com.wj.manager.common.thread.LogThreadData;
import com.wj.manager.common.util.ToolUtil;
import com.wj.manager.common.util.ValidErrorUtil;
import com.wj.manager.controller.warpper.SysUserWarpper;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.SecurityKit;
import com.wj.manager.service.SysUserService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author Wj
 * @since 2019-02-13
 */
@Controller
@RequestMapping("/sysUser")
@Api(value = "系统用户数据")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据条件分页查询系统用户")
    @PreAuthorize("hasAuthority('/mgr')")
    public ResponseResult selectUserList(@ApiParam(name = "queryParams",value = "查询参数",required = true) @ModelAttribute QueryParams<SysUser> queryParams){
        IPage<Map<String, Object>> iPage = sysUserService.selectUserList(queryParams);
        if(iPage != null && iPage.getRecords() != null && iPage.getRecords().size() > 0){
            //封装下，把密码清空
            Object warpper = new SysUserWarpper(iPage).warpper();
            return ResponseResult.success(warpper);
        }
        return ResponseResult.fail("没有你想要的数据");
    }

    @ApiOperation(value = "登陆入口->已作废，暂时留着",notes = "注意！！时间格式为：2019-03-23 10:25:52")
    @PostMapping("/login")
    @ResponseBody
    //@BusinessLog(value = "测试",dict = LogConst.SYS_USER_DICT,type = LogTypeEnum.UPDATE)
    //@ApiImplicitParam(value = "loginUser", paramType = "query")
    public ResponseResult login(@RequestBody SysUser loginUser, HttpServletRequest request){
       //把loginUser存在ThreadLocal中
        //ThreadData.instance().setData(loginUser);
        System.out.println(request instanceof ServletRequest);
        System.out.println(request instanceof HttpServletRequest);
        System.out.println(request.getClass());
        System.out.println(111);
        SysUser sysUser = new SysUser();
        sysUser.setId(1);
        sysUser.setPassword("1111");
        sysUser.setName("张三");
        sysUser.setAvatar("头像");
        sysUser.setSex(1);
        sysUser.setBirthday(new Date(System.currentTimeMillis() - 10000));
        LogThreadData.instance().setData(sysUser);

       int i = 1/0;

//[角色名称]->旧值:,新值:;;  [性别]->旧值:男,新值:女;;  [部门名称]->旧值:null,新值:;;  [头像]->旧值:头像,新值:aa;;  [null]->旧值:1111,新值:aa;;  [电话]->旧值:null,新值:aa;;  [名字]->旧值:张三,新值:aa;;  [账号]->旧值:null,新值:aa;;  [电子邮件]->旧值:null,新值:aa;;

     /*   if(loginUser == null || StringUtils.isBlank(loginUser.getAccount())|| StringUtils.isBlank(loginUser.getPassword())){
            throw new CustomException(ExceptionEnum.NOT_EXIST);
        }
        try {
            List<SysUser> list = sysUserService.getUserListByAccount(loginUser.getAccount());
            if(list.size() >0 && list.get(0) != null){
                SysUser user = list.get(0);
                String loginPassword = ToolUtil.HashPassword(loginUser.getPassword(), user.getSalt());
                if(StringUtils.equals(loginPassword,user.getPassword())){
                    String token = JWTUtil.sign(user.getAccount(), user.getPassword());
                    System.out.println(token);
                    return ResponseResult.success(token);
                }
            }else {
                throw new UnauthorizedException();
            }

        }catch (UnknownAccountException e){
            return ResponseResult.fail("请输入正确的账号!");
        }catch (IncorrectCredentialsException e){
            //model.addAttribute("msg","密码错误!");
            return ResponseResult.fail("密码错误!");
        }*/
        return ResponseResult.fail("登陆失败");
    }
    @ApiOperation("文件上传,用户上传头像")
    @ResponseBody
    @PostMapping(value = "/addPic")
    public ResponseResult uploadAvatar(@RequestParam("avatar") MultipartFile avatar) throws IOException {
        String fileName = sysUserService.uploadAvatar(avatar);
        if (StringUtils.isNoneBlank(fileName)){
            return ResponseResult.success(fileName);
        }
        return  ResponseResult.fail("文件上传失败");
    }

    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation("添加用户")
    @PreAuthorize("hasAuthority('/mgr/add')")
    @BusinessLog(value = "添加用户",dict = LogConst.SYS_USER_DICT,type = LogTypeEnum.OTHER)
    public ResponseResult addSysUser(@ModelAttribute @Valid SysUser sysUser, BindingResult bindingResult){
        //账号不填写,给提示
        if(bindingResult.hasErrors()){
            throw new CustomException(ExceptionEnum.ACC_PWD_NO_NULL);
        }
        Map user = sysUserService.addUser(sysUser);
        Object warpper = new SysUserWarpper(user).warpper();
        if(warpper != null) {
            return ResponseResult.success(warpper);
        }
        return ResponseResult.fail(ResponseResult.OPERATE_ERROR);
    }

    /*@RequestMapping("/gopage/{page}")
    public String gotoPage(@PathVariable("page") String page){
        return "/"+page +".html";
    }*/

    @ResponseBody
    @PostMapping("/updata")
    @PreAuthorize("hasAuthority('/mgr/edit')")
    @BusinessLog(value = "修改用户",dict = LogConst.SYS_USER_DICT,type = LogTypeEnum.UPDATE)
    @ApiOperation(value = "修改用户信息",notes = "超管可以修改其他人，非管理员只能修改自己的")
    public ResponseResult updataUser(@ModelAttribute SysUser sysUser){
        if (sysUser == null){
            return ResponseResult.fail("请填写要修改的信息!");
        }
        //sysUser.setPassword(null);
        Map<String,Object> map = sysUserService.updataUser(sysUser);
        if(map != null){
            return ResponseResult.success(map);
        }
        return ResponseResult.fail(ResponseResult.OPERATE_ERROR);
    }


    @PostMapping("/password/reset")
    @ResponseBody
    @PreAuthorize("hasAuthority('/mgr/reset')")
    @ApiOperation("重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true,paramType = "query"),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "string", paramType = "query",required = false)
    })
    @BusinessLog(value = "重置用户密码",dict = LogConst.SYS_USER_DICT,type = LogTypeEnum.UPDATE,isWarpper = false)
    public ResponseResult resetPassword(Integer userid,@RequestParam(required = false) String password){
        if(StringUtils.isBlank(password)){
            password = ConstantFactory.instance().DEFAULT_PWD;
        }
        String s = sysUserService.resetPassword(userid, password);
        if(StringUtils.isNoneBlank(s)){
            return ResponseResult.success("密码重置成功！");
        }
        return  ResponseResult.fail("密码重置失败!");
    }

    @PostMapping("/password/update")
    @ResponseBody
    @ApiOperation("修改自己的密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPwd", value = "用户原来的密码", dataType = "string", paramType = "query",required = true),
            @ApiImplicitParam(name = "newPwd", value = "用户新的密码", dataType = "string", paramType = "query",required = true)
    })
    public ResponseResult changePassword(String oldPwd, String newPwd){
        if(StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)){
            throw new CustomException(ExceptionEnum.ACC_PWD_NO_NULL);
        }
        Integer userId = SecurityKit.getUserId();
        if(userId.intValue() == 45){
            throw new CustomException(ExceptionEnum.NOT_UPDATE_BOSS_PWD);
        }
        Integer row = sysUserService.updatePassword(userId, oldPwd, newPwd);
        if(row.intValue() == 1){
            return ResponseResult.success("密码修改成功！");
        }
        return  ResponseResult.fail("密码修改失败!");
    }

    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('/mgr/delete')")
    @ApiOperation("删除用户")
    @BusinessLog(value = "删除用户",dict = LogConst.SYS_USER_DICT,type = LogTypeEnum.OTHER,isWarpper = false)
    public ResponseResult deleteUser(@ApiParam(name="userId",value = "用户id") Integer userid){
        Integer row = sysUserService.updataUserStatus(userid, ConstantFactory.instance().USER_DISABLE);
        if(row == 1){
            return ResponseResult.success("删除用户成功!");
        }
        return ResponseResult.fail("删除用户失败!");
    }

    @ResponseBody
    @RequestMapping(value="/freeze",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('/mgr/freeze')")
    @ApiOperation("冻结用户")
    @BusinessLog(value = "冻结用户",dict = LogConst.SYS_USER_DICT,type = LogTypeEnum.UPDATE,isWarpper = false)
    public ResponseResult freezeUser(@ApiParam(name="userId",value = "用户id") Integer userid){

        Integer row = sysUserService.updataUserStatus(userid, ConstantFactory.instance().USER_FREEZE);
        if(row == 1){
            return ResponseResult.success("冻结用户成功!");
        }
        return ResponseResult.fail("冻结用户失败!");
    }

    @ResponseBody
    @RequestMapping(value = "/unfreeze",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('/mgr/unfreeze')")
    @ApiOperation("解除用户冻结状态")
    @BusinessLog(value = "解除用户冻结",dict = LogConst.SYS_USER_DICT,type = LogTypeEnum.UPDATE,isWarpper = false)
    public ResponseResult unfreezeUser(@ApiParam(name="userId",value = "用户id") Integer userid){
        Integer row = sysUserService.updataUserStatus(userid, ConstantFactory.instance().USER_UNFREEZE);
        if(row == 1){
            return ResponseResult.success("激活用户成功!");
        }
        return ResponseResult.fail("激活用户失败!");
    }

    @ResponseBody
    @RequestMapping(value = "/getUser",method = RequestMethod.POST)
    @ApiOperation("查询单个用户")
    public ResponseResult getSysUser(@ModelAttribute @RequestParam(required = false) SysUser user){
        SysUser sysUser = sysUserService.getSysUser(user);
        if(sysUser != null && sysUser.getId() != null){
            return ResponseResult.success(new SysUserWarpper(ToolUtil.bean2Map(sysUser)).warpper());
        }
        throw new CustomException(ExceptionEnum.NOT_EXIST);
    }

}

