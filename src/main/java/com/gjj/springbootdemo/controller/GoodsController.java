package com.gjj.springbootdemo.controller;

import com.gjj.springbootdemo.common.Result;
import com.gjj.springbootdemo.entity.Goods;
import com.gjj.springbootdemo.service.impl.GoodsServiceImpl;
import com.gjj.springbootdemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("goods")
@ResponseBody
public class GoodsController {
    @Autowired
    GoodsServiceImpl goodsService;

    //获取在售闲置信息（前台）
    @GetMapping("/getAllShelfGoods")
    public Result getSaleGoods(){
        List<GoodsVo> saleGoods = goodsService.getSaleGoods();
        if(saleGoods != null){
            return Result.success(saleGoods);
        }
        return Result.error("500", "商品查询出错");
    }

    //根据闲置商品的主键id获取商品的详细信息
    @GetMapping("/getOneShelfGoodsDetail/{goodsId}")
    public Result getOneGoodsDetail(@PathVariable("goodsId") Integer goodsId){
        Goods oneGoodsDetail = goodsService.getGoodsDetailById(goodsId);
        GoodsVo oneGoods = goodsService.goodsToGoodsVo(oneGoodsDetail);
        if(oneGoodsDetail != null){
            return Result.success(oneGoods);
        }
        return Result.error("500", "商品查询出错");
    }

    // 添加一个闲置（前台）
    @PostMapping("/goods")
    public Result addGoods(@RequestBody GoodsVo goodsVo){
        boolean isAdd = goodsService.addOneGoods(goodsVo);
        if(isAdd){
            return Result.success("发布成功");
        }else{
            return Result.error("200","发布失败");
        }
    }

    //根据用户Id获取发布闲置信息
    @GetMapping("/getOwnShelfGoods/{userId}")
    public  Result getOwnShelfGoods(@PathVariable("userId") Integer userId){
        List<GoodsVo> ownShelfGoods = goodsService.getOwnShelfGoods(userId);
        if(ownShelfGoods == null){
            return Result.error("200","获取用户闲置失败");
        }
        return Result.success(ownShelfGoods);
    }

    //根据用户Id获取用户上架闲置信息
    @GetMapping("/getGoodsByUserId/{userId}")
    public  Result getGoodsByUserId(@PathVariable("userId") Integer userId){
        List<Goods> goodsList = goodsService.getGoodsDetailByUserId(userId);
        if(goodsList != null){
            return Result.success(goodsList);
        }
        return Result.error("500", "商品查询出错");
    }

    // 修改闲置状态（前台）
    @PatchMapping("/goodsStatus/{goodsStatus}")
    public Result updateGoodsStatusByGoodsId(@RequestBody List<Integer> goodsIdList
            ,@PathVariable("goodsStatus") Integer goodsStatus) {
        Boolean isUpdate = goodsService.updateBatchGoodsStatusById(goodsIdList, goodsStatus);
        if (isUpdate) {
            return Result.success("修改闲置状态成功");
        }
        return Result.error("500", "修改闲置状态出错");
    }

    //获取闲置Index
    @GetMapping("/getGoodsIndexList/{userId}")
    public Result getGoodsIndexList(@PathVariable("userId") Integer userId){
        List<Integer> goodsIndexList = goodsService.getGoodsIndexList(userId);
        if(goodsIndexList != null){
            return Result.success(goodsIndexList);
        }
        return Result.error("500","获取闲置下标异常");
    }

    //分页查询（后台）
    @GetMapping("/goods")
    public Result goodsLimit(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize,
                             @RequestParam(required = false) Integer userId){
        Goods goods = new Goods();
        goods.setUid(userId);
        List<GoodsVo> goodsLimit = goodsService.getGoodsLimit(goods,pageNum, pageSize);
        Integer goodsTotal = goodsService.getGoodsTotal(goods);
        HashMap<String, Object> res = new HashMap<>();
        res.put("goodsList",goodsLimit);
        res.put("total",goodsTotal);
        if(res.size() > 0){
            return Result.success(res);
        }
        return Result.error("500","分页查询异常");
    }

    // 更新闲置信息（前台）
    @PutMapping("/goods")
    public Result editUser(@RequestBody GoodsVo goodsVo){
        Boolean isInsert = goodsService.updateGoodsById(goodsVo);
        if(isInsert != null){
            return Result.success("更新闲置成功");
        }
        return Result.error("500", "后台异常");
    }

    // 批量删除闲置（前台、后台）
    @DeleteMapping ("/goods")
    public Result deleteBatchById(@RequestBody List<Integer> goodsIdList){
        Integer size = goodsService.deleteBatchById(goodsIdList);
        if(size < 1){
            return Result.error("500", "删除用户异常");
        }
        return Result.success("删除用户成功");
    }
}
