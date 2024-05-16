package com.gjj.springbootdemo.service;

import com.gjj.springbootdemo.vo.UserVo;

public interface UserService {
    UserVo login(String userAccount, String userPassword);

    Boolean signIn(String userAccount, String userPassword);
}
