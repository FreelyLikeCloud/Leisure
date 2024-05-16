package com.gjj.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName order_details
 */
@TableName(value ="order_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {
    // 订单详情ID
    @TableId(type = IdType.AUTO)
    private Integer did;
    // 主订单ID
    private Integer oid;
    // 闲置名
    private String goodsName;
    // 闲置信息
    private String goodsInfo;
    // 闲置封面
    private String goodsPictures;
    // 购买数量
    private Integer quantity;
    // 闲置单价
    private BigDecimal unitPrice;
    // 闲置总价
    private BigDecimal totalPrice;
    // 发布人ID
    private Integer uid;
    // 发布人手机号
    private String userAccount;
    // 发布人名称
    private String userName;
    // 发布人联系方式
    private String userPhone;
    // 发布人地址
    private String userAddress;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}