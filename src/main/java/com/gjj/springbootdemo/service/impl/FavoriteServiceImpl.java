package com.gjj.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.springbootdemo.common.Constants;
import com.gjj.springbootdemo.common.Utils;
import com.gjj.springbootdemo.entity.Favorite;
import com.gjj.springbootdemo.entity.Goods;
import com.gjj.springbootdemo.exception.ServiceException;
import com.gjj.springbootdemo.service.FavoritesService;
import com.gjj.springbootdemo.mapper.FavoritesMapper;
import com.gjj.springbootdemo.vo.FavoriteVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author Lord
* @description 针对表【favorites】的数据库操作Service实现
* @createDate 2024-03-26 16:06:09
*/
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoritesMapper, Favorite>
    implements FavoritesService {
    @Autowired
    private FavoritesMapper favoriteMapper;
    @Autowired
    private GoodsServiceImpl goodsService;

    //根据用户Id获取收藏列表
    @Transactional
    public List<FavoriteVo> getFavoritesByUID(Integer userId){
        //查询用户购物车列表
        List<Favorite> favoriteList = favoriteMapper.selectFavoritesByUID(userId);
        if(favoriteList.size() < 1 || favoriteList == null){
            return null;
        }
        //购物车列表和闲置信息放入CartVo
        List<FavoriteVo> goodsList = new ArrayList<>();
        for(Favorite favorite : favoriteList){
            Goods goods = goodsService.getGoodsDetailById(favorite.getGid());
            // 处理闲置被删除了
            FavoriteVo favoriteVo = null;
            if(goods == null){
                favorite.setStatus(Constants.DELETE_GOODS_6);
                // 更新修改时间
                favorite.setUpdateTime(Utils.getNewTime());
                favoriteVo = toFavoritesVo(new Goods(), favorite);
            }else {
                favorite.setStatus(goods.getStatus());
                favoriteVo = toFavoritesVo(goods, favorite);
            }
            // 更新收藏的闲置状态
            int isUpdate = favoriteMapper.updateById(favorite);
            if(isUpdate < 1){
                //自定义异常
                throw new ServiceException(Constants.CODE_600, "后台异常");
            }
            if(favorite != null){
                goodsList.add(favoriteVo);
            }
        }
        if(goodsList.size() < 1 || goodsList == null){
            return null;
        }
        return goodsList;
    }

    //根据用户Id获取收藏列表下标
    public List<Integer> getFavoritesIndexByUID(Integer userId){
        List<Favorite> favoriteList = favoriteMapper.selectFavoritesByUID(userId);
        List<Integer> favoritesIndexList = new ArrayList<>();
        for(int i = 0; i < favoriteList.size(); i++){
            favoritesIndexList.add(i);
        }
        if(favoritesIndexList.size() > 0){
            return favoritesIndexList;
        }else return null;
    }

    public Boolean insertFavorite(FavoriteVo favoriteVo){
        Favorite favorite = favoriteVoToFavorite(favoriteVo);
        int insert = favoriteMapper.insert(favorite);
        return insert > 0;
    }

    // 批量移除收藏
    public Integer deleteBatchById(List<Integer> favIdList ){
        return favoriteMapper.deleteBatchById(favIdList);
    }
    private FavoriteVo toFavoritesVo(Goods goods, Favorite favorite){
        FavoriteVo favoriteVo = new FavoriteVo();
        BeanUtils.copyProperties(goods,favoriteVo);
        BeanUtils.copyProperties(favorite,favoriteVo);
        favoriteVo.setStatus(Utils.statusDescription(favorite.getStatus()));
        favoriteVo.setCreateTime(Utils.toDataString(favorite.getCreateTime()));
        favoriteVo.setUpdateTime(Utils.toDataString(favorite.getUpdateTime()));
        String goodsPicture = goods.getPictures();
        if(goodsPicture !=null && !goodsPicture.equals("")){
            String[] goodsPictures = goodsPicture.split(",");
            favoriteVo.setCoverImage(goodsPictures[0]);
//            if(goodsPictures.length > 0){
//                favoriteVo.setCoverImage(goodsPictures[0]);
//            }
        }
        return favoriteVo;
    }

    private Favorite favoriteVoToFavorite(FavoriteVo favoriteVo){
        Favorite favorite = new Favorite();
        BeanUtils.copyProperties(favoriteVo,favorite);
        favorite.setStatus(Utils.statusNum(favoriteVo.getStatus()));
        return favorite;
    }
}




