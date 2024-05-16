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
 * @TableName goods
 */
@TableName(value ="goods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer gid;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商品详情
     */
    private String info;

    /**
     * 商品图片
     */
    private String pictures;

    /**
     * 商品库存
     */
    private Integer inventory;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品现价
     */
    private BigDecimal currentPrice;

    /**
     * 所属用户
     */
    private Integer uid;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     * 审核结果
     */
    private String auditResults;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}