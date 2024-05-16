package com.gjj.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.springbootdemo.common.Constants;
import com.gjj.springbootdemo.common.Utils;
import com.gjj.springbootdemo.entity.User;
import com.gjj.springbootdemo.exception.ServiceException;
import com.gjj.springbootdemo.mapper.UserMapper;
import com.gjj.springbootdemo.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author Lord
* @description 针对表【users】的数据库操作Service实现
* @createDate 2024-03-26 16:06:10
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> {

    @Autowired
    UserMapper userMapper;

    // 用户登陆
    public UserVo login(String account, String password) {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            //自定义异常
            throw new ServiceException(Constants.CODE_600, "后台异常");
        }
        User user = userMapper.login(account);
        // 比对密码
        if(user != null && user.getPassword().equals(password)){
            return userToUserVo(user);
        }
        return null;
    }

    //注册用户
    @Transactional
    public Boolean registered(String account, String password) {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            //自定义异常
            throw new ServiceException(Constants.CODE_600, "后台异常");
        }
        //根据账号查询，查看是否已经注册
        User user = userMapper.selectUserByAccount(password);
        //没有重名，正常注册
        if(user != null){
            return false;
        }
        //注册只需要账号和密码就行，其他字段数据库非必填，或者有默认值
        User registeredUser = new User();
        registeredUser.setAccount(account);
        registeredUser.setPassword(password);
        return userMapper.insertUsers(registeredUser);
    }

    // 余额充值
    public Boolean updateUserPurse(BigDecimal purse,Integer userId){
        return userMapper.updateUserPurse(purse, userId);
    }

    //查询单个用户信息
    public UserVo selectUserById(Integer userId) {
        User user = userMapper.selectById(userId);
        UserVo userVo = userToUserVo(user);
        if(userVo != null){
            return userVo;
        }
        return null;
    }

    //获取所有用户信息
    public List<UserVo> getUserLimit(String account, Integer pageNum, Integer pageSize){
        List<User> users = userMapper.selectUserLimit(account,pageNum,pageSize);
        ArrayList<UserVo> userVos = new ArrayList<>();
        for (User user: users){
            UserVo userVo = userToUserVo(user);
            userVos.add(userVo);
        }
        if(userVos.size() <= 0 || userVos == null){
            return null;
        }
        return userVos;
    }

    // 新增用户
    public Boolean insertUsers(UserVo userVo){
        // 设置创建时间&更新时间，防止 userVoToUsers() 转化异常
        userVo.setCreateTime(Utils.toDataString(new Date()));
        userVo.setUpdateTime(Utils.toDataString(new Date()));
        User user = userVoToUsers(userVo);
        int insert = userMapper.insert(user);
        return insert > 0;
    }

    //更新用户信息
    public String updateUser(UserVo userVo){
        // 判断手机号是否已被使用
        String account = userVo.getAccount();
        Integer uid = userVo.getUid();
        User userOld = userMapper.selectById(uid);
        if(account != null && account != ""
                && !userOld.getAccount().equals(userVo.getAccount())){
            User userChange = userMapper.selectUserByAccount(account);
            if(userChange != null){
                return "手机号已被使用";
            }
        }
        User user = userVoToUsers(userVo);
        int i = userMapper.updateById(user);
        if(i>0){
            return "修改成功";
        }
        return null;
    }

    //根据收索条件获取用户记录总条数
    public Integer getUserLimitTotal(String name){
        return userMapper.userTotal(name);
    }

    //批量删除用户信息
    public Integer deleteBatchById(List<Integer> userIdList){
        return userMapper.deleteBatchById(userIdList);
    }

    //修改密码
    public String changePassword(Integer uid, String oldPassword, String newPassword){
        if(oldPassword.equals(newPassword)){
            return "新密码不能和原密码相同";
        }
        User user = userMapper.selectById(uid);
        if(user == null || !user.getPassword().equals(oldPassword)){
            return null;
        }
        user.setPassword(newPassword);
        int i = userMapper.updateById(user);
        if(i > 0){
            return "修改成功";
        }
        return "后台异常";
    }

    private UserVo userToUserVo(User user){
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        userVo.setCreateTime(Utils.toDataString(user.getCreateTime()));
        userVo.setUpdateTime(Utils.toDataString(user.getUpdateTime()));
        return userVo;
    }

    private User userVoToUsers(UserVo userVo){
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        user.setCreateTime(Utils.formatData(userVo.getCreateTime()));
        user.setUpdateTime(Utils.formatData(userVo.getUpdateTime()));
        return user;
    }
}




