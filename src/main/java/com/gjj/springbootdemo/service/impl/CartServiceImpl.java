package com.gjj.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.springbootdemo.common.Constants;
import com.gjj.springbootdemo.common.Utils;
import com.gjj.springbootdemo.entity.Cart;
import com.gjj.springbootdemo.entity.Goods;
import com.gjj.springbootdemo.exception.ServiceException;
import com.gjj.springbootdemo.mapper.GoodsMapper;
import com.gjj.springbootdemo.service.CartService;
import com.gjj.springbootdemo.mapper.CartMapper;
import com.gjj.springbootdemo.vo.CartVo;
import com.gjj.springbootdemo.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @author Lord
* @description 针对表【cart】的数据库操作Service实现
* @createDate 2024-03-26 16:06:09
*/
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
    implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private GoodsServiceImpl goodsService;
    @Autowired
    private GoodsMapper goodsMapper;

    //查询
    public List<Cart> findAll(){
        return cartMapper.findAll();
    }

    //根据用户id查询购物车
    public List<CartVo> getCartByUserId(Integer userId){
        //查询用户购物车列表
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        if(cartList.size() < 1 || cartList == null){
            return null;
        }
        //购物车列表和闲置信息放入CartVo，更新购物车对应闲置的状态
        List<CartVo> cartVoList = new ArrayList<>();
        for(Cart cart : cartList){
            Goods goods = goodsMapper.selectById(cart.getGid());
            // 若闲置已删除，购物车闲置状态更新为已删除，记录更新时间
            if(goods == null){
                cart.setStatus(Constants.DELETE_GOODS_6);
                cart.setUpdateTime(Utils.getNewTime());
            }else {
            // 闲置未删除，同步闲置状态，记录了更新时间
                cart.setStatus(goods.getStatus());
                cart.setUpdateTime(Utils.getNewTime());
            }
            CartVo cartVo = toCartVo(cart, goods);
            cartVoList.add(cartVo);
            // 更新购物车信息
            int i = cartMapper.updateById(cart);
            if(i < 1){
                throw new ServiceException(Constants.CODE_600, "购物车信息更新失败");
            }
        }
        if(cartVoList.size() < 1 || cartVoList == null){
            return null;
        }
        return cartVoList;
    }

    //根据用户id查询购物车列表下标
    public List<Integer> getCartIndexByUserId(Integer userId){
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<Integer> cartIdList = new ArrayList<>();
        for(int i = 0; i < cartList.size(); i++){
            cartIdList.add(i);
        }
        if(cartIdList.size() > 0){
            return cartIdList;
        }else return null;
    }

    //闲置插入购物车
    public Boolean addCart(CartVo cartVo){
        Cart cart = cartVoToCart(cartVo);
        int insert = cartMapper.insert(cart);
        return insert > 0;
    }

    //闲置移除购物车
    public Boolean deleteBatchById(List<Integer> cartIdList){
        int i = cartMapper.deleteBatchIds(cartIdList);
        return i > 0;
    }

    public CartVo cartToCartVo(Goods goods,Cart cart){
        CartVo cartVo = new CartVo();
        //复制 goods 属性到 CartVo
        BeanUtils.copyProperties(goods,cartVo);
        //复制 cart 购买数量到 CartVo
        cartVo.setQuantity(cart.getQuantity());
        //复制 cartId 到 CartVo
        cartVo.setCid(cart.getCid());
        //从闲置 goodsPictures 图片列表字符串，分离闲置封面
        String goodsPicture = goods.getPictures();
        String[] goodsPictures = goodsPicture.split(",");
        ArrayList<String> goodsPicturesList = null;
        if(goodsPictures.length > 0){
            cartVo.setCoverImage(goodsPictures[0]);
            goodsPicturesList = new ArrayList<>(Arrays.asList(goodsPictures));
            cartVo.setGoodsDetailImgsList(goodsPicturesList);
        }
        return cartVo;
    }

    public Cart cartVoToCart(CartVo cartVo){
        Cart cart = new Cart();
        BeanUtils.copyProperties(cartVo,cart);
        cart.setStatus(Utils.statusNum(cartVo.getStatus()));
        return cart;
    }
    public CartVo toCartVo(Cart cart, Goods goods){
        CartVo cartVo = new CartVo();
        // cart 复制要在 goods前面，在后面会把 goods 的发布人覆盖成用户Id
        // 影响点击购物车闲置图片时跳转页面功能
        BeanUtils.copyProperties(cart,cartVo);
        if(goods != null){
            BeanUtils.copyProperties(goods,cartVo);
            //从闲置 goodsPictures 图片列表字符串，分离闲置封面
            String goodsPicture = goods.getPictures();
            String[] goodsPictures = goodsPicture.split(",");
            ArrayList<String> goodsPicturesList = null;
            if(goodsPictures.length > 0){
                cartVo.setCoverImage(goodsPictures[0]);
                goodsPicturesList = new ArrayList<>(Arrays.asList(goodsPictures));
                cartVo.setGoodsDetailImgsList(goodsPicturesList);
            }
        }

        cartVo.setStatus(Utils.statusDescription(cart.getStatus()));
        return cartVo;
    }
}




