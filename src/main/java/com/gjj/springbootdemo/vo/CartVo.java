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
public class CartVo {
    // 购物车ID
    private Integer cid;
    // 用户ID
    private Integer uid;
    // 闲置ID
    private Integer gid;
    // 购买数量
    private Integer quantity;
    // 闲置状态
    private String status;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
    // 闲置名
    private String name;
    // 闲置信息
    private String info;
    // 闲置图片
    private String pictures;
    // 闲置封面
    private String coverImage;
    // 闲置图片组
    private List<String> goodsDetailImgsList;
    // 闲置库存
    private Integer inventory;
    // 闲置原价
    private BigDecimal originalPrice;
    // 闲置现价
    private BigDecimal currentPrice;
}
