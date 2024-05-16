package com.gjj.springbootdemo.common;


public interface Constants {
    String CODE_200 = "200"; //成功
    String CODE_401 = "401";  // 权限不足
    String CODE_400 = "400";  // 参数错误
    String CODE_500 = "500"; // 系统错误
    String CODE_600 = "600"; // 其他业务异常
    String DOWN_GOODS = "未上架"; //闲置状态为未上架，对应数字 1
    String AUDIT_GOODS = "审核中"; //闲置状态为审核中，对应数字 2
    String AUDIT_Failed_GOODS = "未通过审核"; //闲置状态为未通过审核，对应数字 3
    String UP_GOODS = "已上架"; //闲置状态为已上架，对应数字 4
    String SOLD_OUT_GOODS = "已售空"; //闲置状态为已售空，对应数字 5
    String DELETE_GOODS = "已删除"; //闲置状态为已删除，对应数字 6
    Integer DOWN_GOODS_1 = 1;//闲置状态为未上架，对应数字 1
    Integer AUDIT_GOODS_2 = 2;//闲置状态为审核中，对应数字 2
    Integer AUDIT_Failed_GOODS_3 = 3; //闲置状态为未通过审核，对应数字 3
    Integer UP_GOODS_4 = 4;//闲置状态为已上架，对应数字 4
    Integer SOLD_OUT_GOODS_5 = 5;//闲置状态为已售空，对应数字 5
    Integer DELETE_GOODS_6 = 6;//闲置状态为已删除，对应数字 6
}
