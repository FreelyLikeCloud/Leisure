package com.gjj.springbootdemo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjj.springbootdemo.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Lord
* @description 针对表【order_details】的数据库操作Mapper
* @createDate 2024-03-26 16:06:09
* @Entity generator.domain.OrderDetails
*/
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    //sql->查询所有数据（管理员）
    @Select("SELECT * FROM OrderDetails")
    List<OrderDetail> findAll();

    //sql->根据订单id查询订单闲置详情数据（用户）
    @Select("SELECT * FROM OrderDetails WHERE order_id=#{orderId}")
    List<OrderDetail> findOrderDetails(@Param("orderId") Integer orderId);

    //sql->插入单个闲置商品到订单中(管理员)
    @Insert("INSERT INTO OrderDetails(order_id,goods_name,goods_info,goods_pictures,quantity,unit_price,total_price) " +
            "VALUES (#{orderId},#{goodsName},#{goodsInfo},#{goodsPictures},#{quantity},#{unitPrice},#{totalPrice})")
    Boolean insertOrderDetails(OrderDetail OrderDetails);

    //sql->删除订单中单个闲置(管理员)
    @Delete("DELETE FROM OrderDetails WHERE detail_id=#{detailId}")
    Integer deleteOrderDetails(Integer detailId);

    //sql->根据主订单Id查询订单详情
    @Select("SELECT * FROM Order_Detail WHERE oid=#{orderId}")
    List<OrderDetail> selectOrderDetailsByOrderId(@Param("orderId") Integer orderId);

    //xml -> 批量插入
    Boolean insertBatchOrderDetails(List<OrderDetail> orderDetailsList);
}




