package com.gjj.springbootdemo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private Integer uid;
    // 账号
    private String account;
    // 密码
    private String password;
    //头像url
    private String avatar;
    //用户名
    private String name;
    // 用户角色: user普通/admin管理员
    private String role;
    // 电子钱包
    private BigDecimal purse;
    // 手机号或者微信号等联系方式
    private String phone;
    // 地址
    private String address;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
}
