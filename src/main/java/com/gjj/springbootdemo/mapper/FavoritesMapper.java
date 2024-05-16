package com.gjj.springbootdemo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjj.springbootdemo.entity.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Lord
* @description 针对表【favorites】的数据库操作Mapper
* @createDate 2024-03-26 16:06:09
* @Entity generator.domain.Favorites
*/
public interface FavoritesMapper extends BaseMapper<Favorite> {
    //sql->查询所有数据（管理员）
    @Select("SELECT * FROM Favorites")
    List<Favorite> findAll();

    //sql->根据用户id查询所有数据（用户）
    @Select("SELECT * FROM Favorites WHERE user_id=#{userId}")
    List<Favorite> findUserFavorites(@Param("userId") Integer userId);

    //sql->插入(用户添加闲置数据到购物车表)
    @Insert("INSERT INTO Favorites(user_id,goods_id) VALUES (#{userId},#{goodsId})")
    Boolean insertFavorites(Favorite favorite);

    //sql->删除
    @Delete("DELETE FROM Favorites WHERE fav_id=#{favId}")
    Integer deleteFavorites(Integer favId);

    //sql -> 根据用户Id查询收藏列表
    @Select("SELECT * FROM favorite WHERE uid=#{userId}")
    List<Favorite> selectFavoritesByUID(Integer userId);

    //xml -> 根据收藏Id批量移除收藏
    Integer deleteBatchById(List<Integer> favIdList);
}




