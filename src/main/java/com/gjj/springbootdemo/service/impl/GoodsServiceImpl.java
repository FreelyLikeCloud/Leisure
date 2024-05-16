package com.gjj.springbootdemo.service.impl;


import com.gjj.springbootdemo.common.Constants;
import com.gjj.springbootdemo.common.Utils;
import com.gjj.springbootdemo.entity.Goods;
import com.gjj.springbootdemo.entity.User;
import com.gjj.springbootdemo.exception.ServiceException;
import com.gjj.springbootdemo.mapper.GoodsMapper;
import com.gjj.springbootdemo.mapper.UserMapper;
import com.gjj.springbootdemo.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;
    //获取在售闲置（前台）
    public List<GoodsVo> getSaleGoods(){
        List<Goods> goodsList = goodsMapper
                .selectShelfGoods(Constants.UP_GOODS_4);
        ArrayList<GoodsVo> goodsVos = new ArrayList<>();
        for(Goods goods: goodsList){
            GoodsVo goodsVo = goodsToGoodsVo(goods);
            User user = userMapper.selectById(goods.getUid());
            goodsVo.setUserAvatar(user.getAvatar());
            goodsVo.setUserName(user.getName());
            goodsVos.add(goodsVo);
        }
        if (goodsVos.size() > 0 && goodsVos !=null){
            return goodsVos;
        }else {
            //自定义异常
            throw new ServiceException(Constants.CODE_600, "后台异常");
        }
    }
    //添加一个闲置
    public boolean addOneGoods(GoodsVo goodsVo) {
        //设置商品状态 2 表示审核中
        //只要是上架的商品都需要审核，一定要在 goodsVoToGoods 前设置
        //goodsVoToGoods 内部有对 status 的转化
        goodsVo.setStatus(Constants.AUDIT_GOODS);
        Goods goods = goodsVoToGoods(goodsVo);
        //设置发布用户的 ID
        goods.setUid(goodsVo.getUid());
        Integer insert = goodsMapper.insert(goods);
        if(insert > 0){
            return true;
        }else{
            //自定义异常
            throw new ServiceException(Constants.CODE_600, "后台异常");
        }
    }

    //批量改闲置状态
    public Boolean updateBatchGoodsStatusById(List<Integer> goodsIdList,Integer goodsStatus){
        return goodsMapper.updateBatchGoodsStatusById(goodsIdList, goodsStatus);
    }

    //通过闲置主键id和闲置状态获取商品详情 （有问题）
    public List<Goods> getGoodsDetailByUserId(Integer userId) {
        List<Goods> goodsList = goodsMapper.selectGoodsByUserId(userId);
        if(goodsList != null){
            return goodsList;
        }else {
            throw new ServiceException(Constants.CODE_600, "后台异常");//自定义异常
        }
    }

    // 通过闲置Id获取闲置详情，查询为空不用做异常处理。
    // 收藏业务会根据是否为空做处理
    public Goods getGoodsDetailById(Integer id) {
        Goods goodsDetail = goodsMapper.selectById(id);
        return goodsDetail;
    }

    //更新闲置信息
    public Boolean updateInventoryAndStatusById(Integer inventory,Integer goodsStatus,Integer goodsId){
        return goodsMapper.updateInventoryAndStatusById(inventory,goodsStatus,goodsId);
    }

    //根据闲置id更新闲置信息 方法重载
    public Boolean updateGoodsById(Goods goods){
        int i = goodsMapper.updateById(goods);
        return i > 0;
    }
    public Boolean updateGoodsById(GoodsVo goodsVo){
        Goods goods = goodsVoToGoods(goodsVo);
        int i = goodsMapper.updateById(goods);
        return i > 0;
    }

    //根据用户Id获取用户上传的闲置信息(有问题 和上面的方法疑似重叠)
    public List<GoodsVo> getOwnShelfGoods(Integer userId){
        List<Goods> idleList = goodsMapper.selectGoodsByUserId(userId);
        ArrayList<GoodsVo> goodsVos = new ArrayList<>();
        for (Goods goods: idleList){
            GoodsVo goodsVo = goodsToGoodsVo(goods);
            goodsVos.add(goodsVo);
        }
        if(goodsVos.size() > 0 && goodsVos != null){
            return goodsVos;
        }
        return null;
    }

    //批量删除闲置信息
    public Integer deleteBatchById(List<Integer> goodsIdList){
        return goodsMapper.deleteBatchById(goodsIdList);
    }

    //获取闲置Index
    public List<Integer> getGoodsIndexList(Integer userId){
        List<Goods> goods = goodsMapper.selectGoodsByUserId(userId);
        ArrayList<Integer> goodsIndexList = new ArrayList<>();
        for(int i = 0; i < goods.size(); i++){
            goodsIndexList.add(i);
        }
        if(goodsIndexList.size() > 0){
            return goodsIndexList;
        }else return null;
    }

    // 分页查询
    public List<GoodsVo> getGoodsLimit(Goods goods,Integer pageNum,Integer pageSize){
        List<Goods> goodsList = goodsMapper.goodsLimit(goods, (pageNum - 1) * pageSize, pageSize);
        ArrayList<GoodsVo> goodsVos = new ArrayList<>();
        for (Goods goods1 : goodsList){
            GoodsVo goodsVo = goodsToGoodsVo(goods1);
            goodsVos.add(goodsVo);
        }
        if(goodsVos.size() > 0 && goodsVos != null){
            return goodsVos;
        }
        return null;
    }
    //条件查询总数
    public Integer getGoodsTotal(Goods goods){
        return goodsMapper.goodsTotal(goods);
    }

    //获取闲置状态描述
//    private String goodsStatusDescription(Integer goodsStatus){
//        switch (goodsStatus){
//            case 1:
//                return Constants.DOWN_GOODS;
//            case 2:
//                return Constants.AUDIT_GOODS;
//            case 3:
//                return Constants.UP_GOODS;
//            case 4:
//                return Constants.SOLD_OUT_GOODS;
//            default:
//                return "";
//        }
//    }

//    private Integer goodsStatusNum(String goodsStatus){
//        switch (goodsStatus){
//            case Constants.DOWN_GOODS:
//                return Constants.DOWN_GOODS_1;
//            case Constants.AUDIT_GOODS:
//                return Constants.AUDIT_GOODS_2;
//            case Constants.UP_GOODS:
//                return Constants.UP_GOODS_3;
//            case Constants.SOLD_OUT_GOODS:
//                return Constants.SOLD_OUT_GOODS_4;
//            default:
//                return -1;
//        }
//    }

    public GoodsVo goodsToGoodsVo(Goods goods){
        GoodsVo goodsVo = new GoodsVo();
        //复制有相同属性相同类型的数据
        BeanUtils.copyProperties(goods,goodsVo);
        //分割图片集合字符串
        String goodsPicture = goods.getPictures();
        String[] goodsPictures = goodsPicture.split(",");
        ArrayList<String> goodsPicturesList = null;
        if(goodsPictures.length > 0){
            //获取图片数组中的第一张为闲置物品封面
            goodsVo.setCoverImage(goodsPictures[0]);
            //将图片列表放入goodsVo
            goodsPicturesList = new ArrayList<>(Arrays.asList(goodsPictures));
            goodsVo.setGoodsDetailImgsList(goodsPicturesList);
        }
        //将闲置状态的数值传化为文字
        goodsVo.setStatus(Utils.statusDescription(goods.getStatus()));
        //将时间类型转化为字符串
        goodsVo.setCreateTime(toDataString(goods.getCreateTime()));
        goodsVo.setUpdateTime(toDataString(goods.getUpdateTime()));
        return goodsVo;
    }

    private Goods goodsVoToGoods(GoodsVo goodsVo){
        Goods goods = new Goods();
        //将 GoodsVo 转化成 Goods
        BeanUtils.copyProperties(goodsVo,goods);
        //拼接图片链接，用','分开多张图片
        String goodsPictures = "";
        List<String> goodsDetailImgsList = goodsVo.getGoodsDetailImgsList();
        if(!goodsDetailImgsList.isEmpty() && goodsDetailImgsList != null){
            goodsPictures = goodsDetailImgsList.stream().collect(Collectors.joining(","));
        }
        goods.setPictures(goodsPictures);
        //复制闲置状态
        goods.setStatus(Utils.statusNum(goodsVo.getStatus()));
        //设置更新时间
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date(System.currentTimeMillis());
        goods.setUpdateTime(new Date());
        return goods;
    }

    private String toDataString(Date data){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(data);
        return dateString;
    }

    private Date formatData(String dateString){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
}
