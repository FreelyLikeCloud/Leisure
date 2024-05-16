package com.gjj.springbootdemo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjj.springbootdemo.entity.OrderMain;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Lord
* @description 针对表【order_main】的数据库操作Mapper
* @createDate 2024-03-26 16:06:09
* @Entity generator.domain.OrderMain
*/
public interface OrderMainMapper extends BaseMapper<OrderMain> {
    //sql->查询所有数据（管理员）
    @Select("SELECT * FROM OrderMain")
    List<OrderMain> findAll();

    //sql->根据订单id查询数据（用户）
    @Select("SELECT * FROM OrderMain WHERE order_id=#{orderId}")
    OrderMain findUserOrderMain(@Param("orderId") Integer orderId);

    //sql->根据用户Id获取用户订单
    @Select("SELECT * FROM Order_Main WHERE uid=#{userId}")
    List<OrderMain> selectOrderMainByUserId(@Param("userId")Integer userId);

    //sql->插入(用户添加闲置数据到购物车表)
//    @Insert("INSERT INTO OrderMain(user_id,create_time,order_status,total_amount) VALUES (#{userId},#{createTime},#{orderStatus},#{totalAmount})")
    Boolean insertOrderMain(OrderMain orderMain);

    //mybatis的xml方式用sql->批量插入

    //sql->删除
    @Delete("DELETE FROM OrderMain WHERE order_id=#{orderId}")
    Integer deleteOrderMain(Integer orderId);

    //mybatis的xml方式用sql->批量删除

    //sql->查询分页总数
    Integer orderMainTotal(@Param("orderMain") OrderMain orderMain,@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);
    List<OrderMain> orderMainLimit(@Param("orderMain") OrderMain orderMain,@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);
}




