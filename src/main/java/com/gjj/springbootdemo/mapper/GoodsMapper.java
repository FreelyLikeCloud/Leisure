package com.gjj.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjj.springbootdemo.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    //sql -> 查询首页闲置展示信息
    @Select("SELECT * FROM goods WHERE status=#{goodsStatus} AND inventory>0")
    List<Goods> selectShelfGoods(@Param("goodsStatus") Integer goodsStatus);

    //sql -> 根据闲置id和闲置状态查询闲置信息
    @Select("SELECT * FROM goods WHERE uid=#{userId}")
    List<Goods> selectGoodsByUserId(@Param("userId") Integer userId);

    //sql -> 查询所有闲置信息（管理员）
    @Select("SELECT * FROM goods")
    List<Goods> selectAllGoods();

    //sql -> 根据闲置Id更新闲置信息
    @Update("UPDATE goods SET inventory=#{inventory},status=#{goodsStatus} WHERE gid=#{goodsId}")
    Boolean updateInventoryAndStatusById(@Param("inventory") Integer inventory,@Param("goodsStatus") Integer goodsStatus,@Param("goodsId") Integer goodsId);

    //xml -> 根据闲置Id批量查询闲置信息
    List<Goods> selectGoodsListById(List<Integer> goodsIdList);

    //xml -> 根据闲置Id批量更新商品状态
    Boolean updateBatchGoodsStatusById(@Param("list") List<Integer> goodsId,@Param("goodsStatus") Integer goodsStatus);

    //xml -> 分页查询
    List<Goods> goodsLimit(@Param("goods") Goods goods,@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);

    //xml -> 查询总数
    Integer goodsTotal(@Param("goods") Goods goods);

    //xml->批量删除
    Integer deleteBatchById(List<Integer> goodsIdList);
}
