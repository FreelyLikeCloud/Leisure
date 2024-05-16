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
import java.util.Date;

/**
 * 
 * @TableName order_main
 */
@TableName(value ="order_main")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMain implements Serializable {
    // 主订单主键ID
    @TableId(type = IdType.AUTO)
    private Integer oid;
    // 用户ID
    private Integer uid;
    // 订单状态
    private Integer status;
    // 金额合计
    private BigDecimal totalAmount;
    // 创建时间
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}