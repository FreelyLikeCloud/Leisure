package com.gjj.springbootdemo.controller;

import com.gjj.springbootdemo.common.Result;
import com.gjj.springbootdemo.entity.OrderMain;
import com.gjj.springbootdemo.service.OrderMainService;
import com.gjj.springbootdemo.service.impl.OrderMainServiceImpl;
import com.gjj.springbootdemo.vo.OrderVo;
import com.gjj.springbootdemo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("orderMain")
@ResponseBody
public class OrderMainController {
    @Autowired
    OrderMainServiceImpl orderMainService;
    // 购物车点击购买后走这个接口 （前台）
    @PostMapping("/orderMain")
    public Result addOrderMain(@RequestBody OrderVo orderVo){
//        String msg = orderMainService.buyGoods(orderVo);
        String msg = orderMainService.buy(orderVo);
        if(msg == null){
            return Result.error("500","支付失败");
        }
        return Result.success(msg);
    }
    //获取订单信息
    @GetMapping("/orders/{userId}")
    public Result getOrders(@PathVariable("userId") Integer userId){

        List<OrderVo> orders = orderMainService.getOrders(userId);
        if(orders != null){
            return Result.success(orders);
        }
        return Result.error("500","获取订单信息异常");
    }
    //
    @GetMapping("/orderMain")
    public Result userlimit(@RequestParam Integer pageNum,
                            @RequestParam Integer pageSize,
                            @RequestParam(required = false) Integer userId){
        OrderMain orderMain = new OrderMain();
        orderMain.setUid(userId);
        List<OrderVo> orderMainList = orderMainService.getOrdersLimit(orderMain,(pageNum - 1) * pageSize, pageSize);
        Integer total = orderMainService.getOrderMainsTotal(orderMain,(pageNum - 1) * pageSize, pageSize);
        HashMap<String, Object> res = new HashMap<>();
        res.put("orderMainList",orderMainList);
        res.put("total",total);
        if(res.size() > 0){
            return Result.success(res);
        }
        return Result.error("500", "后台异常");
    }
}
