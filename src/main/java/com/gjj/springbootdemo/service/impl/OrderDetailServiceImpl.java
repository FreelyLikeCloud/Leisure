package com.gjj.springbootdemo.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.springbootdemo.entity.OrderDetail;
import com.gjj.springbootdemo.mapper.OrderDetailMapper;
import com.gjj.springbootdemo.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @author Lord
* @description 针对表【order_details】的数据库操作Service实现
* @createDate 2024-03-26 16:06:09
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> {
    @Autowired
    OrderDetailMapper orderDetailMapper;
    public Boolean addOrderDetails(List<CartVo> cartVos, Integer orderMainId){
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        for (CartVo cartVo : cartVos){
            OrderDetail orderDetails = CartVoToOrderDetails(cartVo, orderMainId);
            orderDetailsList.add(orderDetails);
        }
        if(orderDetailsList.size() > 0) {
            return orderDetailMapper.insertBatchOrderDetails(orderDetailsList);
        }else {
            return false;
        }
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId){
        List<OrderDetail> orderDetailsList = orderDetailMapper.selectOrderDetailsByOrderId(orderId);
        if (orderDetailsList.size() > 0 && orderDetailsList != null){
            return orderDetailsList;
        }
        return null;
    }

    // goodsVo -> orderDetails
    private OrderDetail CartVoToOrderDetails(CartVo cartVo, Integer orderMainId){
        OrderDetail orderDetails = new OrderDetail();
        orderDetails.setOid(orderMainId);
        orderDetails.setGoodsName(cartVo.getName());
        orderDetails.setGoodsInfo(cartVo.getInfo());
        orderDetails.setGoodsPictures(cartVo.getCoverImage());
        orderDetails.setQuantity(cartVo.getQuantity());
        orderDetails.setUnitPrice(cartVo.getCurrentPrice());
        BigDecimal totalPrice = cartVo.getCurrentPrice().multiply(new BigDecimal(cartVo.getQuantity().toString()));
        orderDetails.setTotalPrice(totalPrice);
        return orderDetails;
    }
}




