package com.gjj.springbootdemo.controller;

import com.gjj.springbootdemo.common.Result;
import com.gjj.springbootdemo.entity.Cart;
import com.gjj.springbootdemo.service.impl.CartServiceImpl;
import com.gjj.springbootdemo.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("cart")
@ResponseBody
public class CartController {
    @Autowired
    CartServiceImpl cartService;
    // 闲置添加到购物车（前台）
    @PostMapping("/cart")
    public Result addCart(@RequestBody CartVo cartVo){
        Boolean isAdd = cartService.addCart(cartVo);
        if(isAdd){
            return Result.success("添加成功");
        }
        return Result.error("500", "闲置添加购物车异常");
    }
    // 获取购物车信息（前台）
    @GetMapping("/carts/{userId}")
    public Result getCartList(@PathVariable("userId") Integer userId){
        List<CartVo> cartVoList = cartService.getCartByUserId(userId);
        if(cartVoList == null){
            return Result.error("500", "获取购物车闲置异常");
        }
        return Result.success(cartVoList);
    }
    // 获取购物车下标信息（前台）
    @GetMapping("/cartsIndex/{userId}")
    public Result getCartListId(@PathVariable("userId") Integer userId){
        List<Integer> cartIdList = cartService.getCartIndexByUserId(userId);
        if(cartIdList == null){
            return Result.error("500", "获取购物车闲置异常");
        }
        return Result.success(cartIdList);
    }
    // 批量删除购物车信息（前台）
    @DeleteMapping ("/carts")
    public Result deleteBatchById(@RequestBody List<Integer> cartIdList){
        Boolean isDelete = cartService.deleteBatchById(cartIdList);
        if(isDelete){
            return Result.success("移除成功");
        }
        return Result.error("500", "删除购物车闲置异常");
    }
}
