package com.gjj.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjj.springbootdemo.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author Lord
* @description 针对表【cart】的数据库操作Mapper
* @createDate 2024-03-26 16:06:09
* @Entity generator.domain.Cart
*/
public interface CartMapper extends BaseMapper<Cart> {
    //sql->查询所有数据（管理员）
    @Select("SELECT * FROM Cart")
    List<Cart> findAll();

    //sql->根据用户id查询所有数据（用户）
    @Select("SELECT * FROM cart WHERE uid=#{userId}")
    List<Cart> selectCartByUserId(@Param("userId") Integer userId);

    //sql->插入(用户添加闲置数据到购物车表)
//    @Insert("INSERT INTO Cart(user_id,goods_id,quantity) VALUES (#{userId},#{goodsId},#{quantity})")
//    Boolean insertCart(Cart cart);

    //sql->删除
    @Delete("DELETE FROM cart WHERE cid=#{cartId}")
    Integer deleteCartById(Integer cartId);
    //mybatis的xml批量删除
    Integer deleteBatchById(List<Integer> cartIdList);
}




