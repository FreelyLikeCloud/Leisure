package com.gjj.springbootdemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo {
    // 闲置ID
    private Integer gid;
    // 闲置名称
    private String name;
    // 闲置描述
    private String info;
    // 闲置图片
    private String pictures;
    // 闲置封面
    private String coverImage;
    // 闲置图片数组
    private List<String> goodsDetailImgsList;
    // 闲置库存
    private Integer inventory;
    // 审核结果
    private String auditResults;
    // 闲置原价格
    private BigDecimal originalPrice;
    // 闲置价格
    private BigDecimal currentPrice;
    // 用户ID
    private Integer uid;
    // 闲置状态
    private String status;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
    // 用户头像
    private String userAvatar;
    // 用户名
    private String userName;
}
