package com.gjj.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName cart
 */
@TableName(value ="cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {
    // 购物车主键ID
    @TableId(type = IdType.AUTO)
    private Integer cid;
    // 用户ID
    private Integer uid;
    // 闲置ID
    private Integer gid;
    // 购买数量
    private Integer quantity;
    // 闲置状态
    private Integer status;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}