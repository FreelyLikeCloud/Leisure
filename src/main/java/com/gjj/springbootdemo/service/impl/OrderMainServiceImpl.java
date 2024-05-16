package com.gjj.springbootdemo.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.springbootdemo.common.Constants;
import com.gjj.springbootdemo.common.Utils;
import com.gjj.springbootdemo.entity.Goods;
import com.gjj.springbootdemo.entity.OrderDetail;
import com.gjj.springbootdemo.entity.OrderMain;
import com.gjj.springbootdemo.entity.User;
import com.gjj.springbootdemo.exception.ServiceException;
import com.gjj.springbootdemo.mapper.GoodsMapper;
import com.gjj.springbootdemo.mapper.OrderDetailMapper;
import com.gjj.springbootdemo.mapper.OrderMainMapper;
import com.gjj.springbootdemo.mapper.UserMapper;
import com.gjj.springbootdemo.vo.CartVo;
import com.gjj.springbootdemo.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* @author Lord
* @description 针对表【order_main】的数据库操作Service实现
* @createDate 2024-03-26 16:06:09
*/
@Service
public class OrderMainServiceImpl extends ServiceImpl<OrderMainMapper, OrderMain> {
    @Autowired
    private OrderMainMapper orderMainMapper;
    @Autowired
    private OrderDetailServiceImpl orderDetailsService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private GoodsServiceImpl goodsService;
    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    // 添加订单主表；因为需要把主订单的 id，给订单详情表的外键绑定
    // 所以要返回订单的主键
    public Integer addOrderMain(OrderMain orderMain){
        Boolean isAdd = orderMainMapper.insertOrderMain(orderMain);
        if(isAdd){
            return orderMain.getOid();
        }
        return null;
    }
    //支付逻辑
    @Transactional
    public String buyGoods(OrderVo orderVo){
        //判断是否可以购买
        if(orderVo.getPurse().compareTo(orderVo.getTotalAmount()) == -1){
            return "余额不足";
        }
        //计算余额
        BigDecimal balance = orderVo.getPurse().subtract(orderVo.getTotalAmount());
        //生成主订单
        OrderMain orderMain = new OrderMain();
        orderMain.setUid(orderVo.getUid());
        orderMain.setTotalAmount(orderVo.getTotalAmount());
        Boolean isAdd = orderMainMapper.insertOrderMain(orderMain);
        if(!isAdd){
            throw new ServiceException(Constants.CODE_600, "创建主订单失败");//自定义异常
        }
        Integer orderId = orderMain.getOid();
        //生成详情订单
        isAdd = orderDetailsService.addOrderDetails(orderVo.getCartVos(), orderId);
        if(!isAdd){
            throw new ServiceException(Constants.CODE_600, "创建订单详情失败");//自定义异常
        }
        //更新余额
        Boolean isUpdate = userService.updateUserPurse(balance,orderVo.getUid());
        if(!isUpdate){
            throw new ServiceException(Constants.CODE_600, "更新余额失败");//自定义异常
        }
        //移除购物车
        ArrayList<Integer> idList = new ArrayList<>();
        for(CartVo cartVo : orderVo.getCartVos()){
            idList.add(cartVo.getCid());
        }
        Boolean isDelete = cartService.deleteBatchById(idList);
        if(isDelete){
            throw new ServiceException(Constants.CODE_600, "移除购物车失败");//自定义异常
        }
        //判断闲置商品库存数量是否足够，设置闲置状态为已下架
        idList.clear();
        Goods goods = new Goods();
        for(CartVo cartVo : orderVo.getCartVos()){
            Integer remainingInventory = cartVo.getInventory() - cartVo.getQuantity();
            if(remainingInventory < 0){
                continue;
            }
            if(remainingInventory == 0){
                idList.add(cartVo.getGid());
            }
            goods.setInventory(remainingInventory);
            goods.setGid(cartVo.getGid());
            isUpdate = goodsService.updateGoodsById(goods);
            if(!isUpdate){
                throw new ServiceException(Constants.CODE_600, "更新闲置库存失败");//自定义异常
            }
        }
        if(idList.size() > 0){
            isUpdate = goodsService.updateBatchGoodsStatusById(idList, 4);
            if(!isUpdate){
                throw new ServiceException(Constants.CODE_600, "更新闲置状态失败");//自定义异常
            }
        }
        return "支付成功";
    }


    // 核心购买逻辑
    @Transactional
    public String buy(OrderVo orderVo){
        // 先判断简单的购买逻辑，购买总金额和用户余额经行怕判断
        Integer uid = orderVo.getUid();
        User user = userMapper.selectById(uid);
        if(user.getPurse().compareTo(orderVo.getTotalAmount()) == -1){
            return "余额不足，请充值";
        }
        // 比对购买数量和闲置库存，购买数量不能大于闲置库存
        //（购物车可以添加同一个闲置多次，每一个可购买的上限为库存数）
        Map<Integer, Integer> map = new HashMap<>();
        // 1 统计每种闲置购买的数量，2 查询该种商品的库存
        List<CartVo> cartVos = orderVo.getCartVos();
        for(CartVo cartVo: cartVos){
            map.put(cartVo.getGid(), map.getOrDefault(cartVo.getGid(), 0) + cartVo.getQuantity());
        }
        // 等待添加的订单详情
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        // 等待更新的闲置信息
        ArrayList<Goods> goodsList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            // 判断库存是否足够
            Goods goods = goodsMapper.selectById(entry.getKey());
            if(goods == null){
                throw new ServiceException(Constants.CODE_600, "闲置不存在");
            }
            if(goods.getInventory() < entry.getValue()){
                return goods.getName() + "库存不足";
            }
            // 封装订单详情对象
            User goodsOwer = userMapper.selectById(goods.getUid());
            if(goodsOwer == null){
                throw new ServiceException(Constants.CODE_600, "用户已注销");
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setGoodsName(goods.getName());
            orderDetail.setGoodsInfo(goods.getInfo());
            orderDetail.setGoodsPictures(goods.getPictures().split(",")[0]);
            orderDetail.setUnitPrice(goods.getCurrentPrice());
            orderDetail.setQuantity(entry.getValue());
            orderDetail.setTotalPrice(goods.getCurrentPrice().multiply(new BigDecimal(entry.getValue())));
            orderDetail.setUid(goodsOwer.getUid());
            orderDetail.setUserAccount(goodsOwer.getAccount());
            orderDetail.setUserName(goodsOwer.getName());
            orderDetail.setUserPhone(goodsOwer.getPhone());
            orderDetail.setUserAddress(goodsOwer.getAddress());
            // 放入待生成订单详情列表
            orderDetails.add(orderDetail);
            // 更新闲置库存，闲置状态，更新时间
            goods.setInventory(goods.getInventory() - entry.getValue());
            if(goods.getInventory() == 0){
                goods.setStatus(Constants.SOLD_OUT_GOODS_5);
            }
            goods.setUpdateTime(Utils.getNewTime());
            // 放入待更新的闲置列表
            goodsList.add(goods);
        }

        // 生成主订单
        OrderMain orderMain = new OrderMain();
        orderMain.setUid(orderVo.getUid());
        orderMain.setTotalAmount(orderVo.getTotalAmount());
        Boolean isAdd = orderMainMapper.insertOrderMain(orderMain);
        if(!isAdd){
            throw new ServiceException(Constants.CODE_600, "创建主订单失败");//自定义异常
        }
        Integer oid = orderMain.getOid();
        // 生成订单详情表
        for(OrderDetail orderDetail :orderDetails){
            orderDetail.setOid(oid);
            int insert = orderDetailMapper.insert(orderDetail);
            if(insert < 1){
                throw new ServiceException(Constants.CODE_600, "创建订单详情失败");//自定义异常
            }
        }
        // 更新闲置库存，闲置状态，更新时间
        for (Goods goods :goodsList){
            int i = goodsMapper.updateById(goods);
            if(i < 1){
                throw new ServiceException(Constants.CODE_600, "更新闲置失败");
            }
        }
        // 更新用户余额，更新时间
        user.setPurse(user.getPurse().subtract(orderVo.getTotalAmount()));
        user.setUpdateTime(Utils.getNewTime());
        int i = userMapper.updateById(user);
        if(i < 1){
            throw new ServiceException(Constants.CODE_600, "更新用户余额失败");
        }
        // 移除购物车中的闲置信息
        if(orderVo.getCartIdList() != null && orderVo.getCartIdList().size() > 0){
            Boolean isDelete = cartService.deleteBatchById(orderVo.getCartIdList());
            if(!isDelete){
                throw new ServiceException(Constants.CODE_600, "闲置移除购物车失败");
            }
        }
        return "支付成功";
    }
    //分页条件查询
    public List<OrderVo> getOrdersLimit(OrderMain orderMain,Integer
            pageNum,Integer pageSize){
        List<OrderMain> orderMains = orderMainMapper
                .orderMainLimit(orderMain, pageNum, pageSize);
        if(orderMains.size() < 0 || orderMains == null){
            return null;
        }
        ArrayList<OrderVo> orderVos = new ArrayList<>();
        for (OrderMain order : orderMains){
            Integer orderId = order.getOid();
            List<OrderDetail> orderDetailsList = orderDetailsService
                    .getOrderDetailsByOrderId(orderId);
            if(orderDetailsList != null){
                OrderVo orderVo = orderMainToOrderVo(order);
                orderVo.setOrderDetails(orderDetailsList);
                orderVos.add(orderVo);
            }
        }
        if(orderVos.size() > 0 && orderVos != null){
            return orderVos;
        }
        return null;
    }
    //查询数据总数
    public Integer getOrderMainsTotal(OrderMain orderMain,Integer pageNum,Integer pageSize){
        Integer total = orderMainMapper.orderMainTotal(orderMain, pageNum, pageSize);
        return total;
    }

    public List<OrderVo> getOrders(Integer userId){
        // 获取主订单信息
        List<OrderMain> orderMains = orderMainMapper
                .selectOrderMainByUserId(userId);
        if(orderMains.size() < 0 || orderMains == null){
            return null;
        }
        ArrayList<OrderVo> orderVos = new ArrayList<>();
        // 获取子订单信息
        for (OrderMain orderMain : orderMains){
            Integer orderId = orderMain.getOid();
            List<OrderDetail> orderDetailsList = orderDetailsService
                    .getOrderDetailsByOrderId(orderId);
            if(orderDetailsList != null){
                OrderVo orderVo = orderMainToOrderVo(orderMain);
                orderVo.setOrderDetails(orderDetailsList);
                orderVos.add(orderVo);
            }
        }
        if(orderVos.size() > 0 && orderVos != null){
            return orderVos;
        }
        return null;
    }

    private OrderVo orderMainToOrderVo(OrderMain orderMain){
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(orderMain,orderVo);
        //设置订单状态
        orderVo.setStatus("支付成功");
        //复制创建时间
        orderVo.setCreateTime(toDataString(orderMain.getCreateTime()));
        return orderVo;
    }

    private String toDataString(Date data){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(data);
        return dateString;
    }
}