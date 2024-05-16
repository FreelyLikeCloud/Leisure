package com.gjj.springbootdemo.service;

import com.gjj.springbootdemo.entity.Goods;
import com.gjj.springbootdemo.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    List<Goods> selectByGoodsStatus(String goodsStatus);
//    List<GoodsDTO> getAllShelfGoods();
//    GoodsDTO getOneGoodsDetail(Integer id);
    boolean addOneGoods(GoodsVo goodsVo);
}
