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
 * @TableName favorites
 */
@TableName(value ="favorite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite implements Serializable {
    // 收藏表主键
    @TableId(type = IdType.AUTO)
    private Integer fid;
    // 收藏用户ID
    private Integer uid;
    // 收藏闲置ID
    private Integer gid;
    // 收藏闲置状态
    private Integer status;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}