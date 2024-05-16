package com.gjj.springbootdemo.controller;

import com.gjj.springbootdemo.common.Result;
import com.gjj.springbootdemo.entity.User;
import com.gjj.springbootdemo.service.impl.UserServiceImpl;
import com.gjj.springbootdemo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
@ResponseBody
public class UserController {
    @Autowired
    UserServiceImpl usersService;
    //@RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
    // 而最常用的使用请求体传参的无疑是POST请求了，所以使用@RequestBody接收数据时，
    // 一般都用POST方式进行提交。在后端的同一个接收方法里，
    // @RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个，
    // 而@RequestParam()可以有多个。
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> user){
        UserVo loginUser = usersService.login(user.get("userAccount"),user.get("userPassword"));
        if(loginUser!=null)
            return Result.success(loginUser);
        else
            return Result.error("500","账号或密码错误");
    }
    @PostMapping("/signIn")
    public Result signIn(@RequestBody Map<String,String> user){
        Boolean signed = usersService.registered(user.get("userAccount"),user.get("userPassword"));
        if(signed) {
            return Result.success("注册成功");
        }else {
            return Result.error("500", "用户已存在");
        }
    }

    // 通过用户ID，获取用户信息（前台）
    @GetMapping ("/user/{userId}")
    public Result getUser(@PathVariable("userId") Integer userId){
        UserVo userVo = usersService.selectUserById(userId);
        if(userVo == null){
            Result.error("500", "获取用户信息异常");
        }
        return Result.success(userVo);
    }

    @PutMapping("/setUserPurse")
    public Result setUserPurse(@RequestBody Map<String,String> userPurse){
        BigDecimal purse = new BigDecimal(userPurse.get("purse"));
        Integer userId = new Integer(userPurse.get("userId"));
        Boolean isUpdate = usersService.updateUserPurse(purse, userId);
        if(isUpdate){
            return Result.error("200", "充值成功");
        }else{
            return Result.error("500", "充值异常");
        }
    }
    // 更新用户密码（前台）
    @PutMapping("/user/{uid}")
    public Result changePassword(@PathVariable("uid") Integer uid
            , @RequestBody Map<String,String> password){
        String updateMsg = usersService.changePassword(uid,password.get("oldPassword"),password.get("newPassword"));
        if(updateMsg != null){
            return Result.success(updateMsg);
        }
        return Result.error("500", "后台异常");
    }

    //分页查询用户（后台）
    @GetMapping("/user")
    public Result getUserLimit(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String account){
        pageNum = (pageNum - 1) * pageSize;
        List<UserVo> users = usersService.getUserLimit(account,pageNum,pageSize);
        Integer total = usersService.getUserLimitTotal(account);
        HashMap<String, Object> res = new HashMap<>();
        res.put("users",users);
        res.put("total",total);
        if(res.size() > 0){
            return Result.success(res);
        }
        return Result.error("500", "后台异常");
    }

    // 新增用户（后台）
    @PostMapping("/user")
    public Result addUser(@RequestBody UserVo userVo){
        Boolean isInsert = usersService.insertUsers(userVo);
        if(isInsert){
            return Result.success("新增用户成功");
        }
        return Result.error("500", "后台异常");
    }

    // 更新用户（后台）
    @PutMapping("/user")
    public Result editUser(@RequestBody UserVo userVo){
        String updateMsg = usersService.updateUser(userVo);
        if(updateMsg != null){
            return Result.success(updateMsg);
        }
        return Result.error("500", "后台异常");
    }

    // 通过用户UID，批量删除用户信息（后台）
    @DeleteMapping ("/users")
    public Result deleteBatchById(@RequestBody List<Integer> userIdList){
        Integer size = usersService.deleteBatchById(userIdList);
        if(size < 1){
            return Result.error("500", "删除用户异常");
        }
        return Result.success("删除用户成功");
    }
}
