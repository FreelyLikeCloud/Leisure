package com.gjj.springbootdemo.controller;

import com.gjj.springbootdemo.common.Result;
import com.gjj.springbootdemo.service.impl.FavoriteServiceImpl;
import com.gjj.springbootdemo.vo.FavoriteVo;
import com.gjj.springbootdemo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    FavoriteServiceImpl favoriteService;

    // 根据用户UID，获取收藏信息（前台）
    @GetMapping("/favorites/{userId}")
    public Result getFavorites(@PathVariable("userId") Integer userId){
        List<FavoriteVo> favoritesList = favoriteService.getFavoritesByUID(userId);
        if(favoritesList != null){
            return Result.success(favoritesList);
        }
        return Result.error("500", "商品查询出错");
    }
    // 更具用户UID，获取收藏信息（前台）
    @GetMapping("/favoritesIndex/{userId}")
    public Result getFavoritesIndex(@PathVariable("userId") Integer userId){
        List<Integer> favoritesIndexList = favoriteService.getFavoritesIndexByUID(userId);
        if(favoritesIndexList != null){
            return Result.success(favoritesIndexList);
        }
        return Result.error("500", "商品查询出错");
    }
    // 添加收藏（前台）
    @PostMapping("/favorite")
    public Result addFavorite(@RequestBody FavoriteVo favoriteVo){
        Boolean isInsert = favoriteService.insertFavorite(favoriteVo);
        if(isInsert){
            return Result.success("收藏成功");
        }
        return Result.error("500", "后台异常");
    }
    // 批量删除收藏（前台）
    @DeleteMapping("/favorites/{favIdList}")
    public Result deleteBatchById(@PathVariable("favIdList") List<Integer> favIdList){
        Integer size = favoriteService.deleteBatchById(favIdList);
        if(size < 1){
            return Result.error("500", "删除购物车闲置异常");
        }
        return Result.success("移除购物车成功");
    }
}
