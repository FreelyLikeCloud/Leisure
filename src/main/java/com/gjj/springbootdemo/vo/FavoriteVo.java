package com.gjj.springbootdemo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteVo {
    // 收藏表主键
    private Integer fid;
    // 收藏用户ID
    private Integer uid;
    // 收藏闲置ID
    private Integer gid;
    // 收藏闲置状态
    private String status;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
    // 闲置名
    private String name;
    // 闲置封面
    private String coverImage;
    // 闲置价格
    private BigDecimal currentPrice;
}