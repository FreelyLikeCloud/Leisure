package com.gjj.springbootdemo.vo;

import com.gjj.springbootdemo.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {
    // 订单主表ID
    private Integer oid;
    // 用户ID （买家）
    private Integer uid;
    // 订单状态
    private String status;
    // 金额合计
    private BigDecimal totalAmount;
    // 创建时间
    private String createTime;
    // 买什么、数量、卖家
    private List<CartVo> cartVos;
    // 订单详情数组，记录购买的过个闲置信息
    private List<OrderDetail> orderDetails;
    // 当前用户余额
    private BigDecimal purse;
    // 购物车购买后需要移除的购物车信息ID
    private List<Integer> cartIdList;
}
