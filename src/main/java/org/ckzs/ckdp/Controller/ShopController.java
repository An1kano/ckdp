package org.ckzs.ckdp.Controller;


import lombok.extern.slf4j.Slf4j;
import org.ckzs.ckdp.DTO.PageDTO;
import org.ckzs.ckdp.Service.ShopService;
import org.ckzs.ckdp.VO.PageResult;
import org.ckzs.ckdp.VO.ShopVO;
import org.ckzs.ckdp.pojo.Result;
import org.ckzs.ckdp.pojo.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    ShopService shopService;

    @PostMapping("/list")
    public Result<PageResult<Shop>> getShopList(@RequestBody PageDTO pageDTO){
        int pageNum = pageDTO.getPageNum();
        int pageSize = pageDTO.getPageSize();
        if(pageNum<=0||pageSize<=0){
            return Result.error("页数和每页数量不能小于1");
        }
        PageResult<Shop> shopList = shopService.getShopList(pageNum, pageSize);
        if (shopList.getData().isEmpty()){
            return Result.error("没有数据了！！！");
        }
        return Result.success();
    }
    @GetMapping("/info")
    public Result<ShopVO> getShopById(@RequestParam int shopId){
        log.info("查询店铺:{}",shopId);
        ShopVO shopVO = shopService.getShopById(shopId);
        if(shopVO==null){
            return Result.error("店铺不存在！！！");
        }
        return Result.success(shopVO);
    }

}
