package org.ckzs.ckdp.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.ckzs.ckdp.Mapper.ShopMapper;
import org.ckzs.ckdp.Service.ShopService;
import org.ckzs.ckdp.VO.PageResult;
import org.ckzs.ckdp.VO.ShopVO;
import org.ckzs.ckdp.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.ckzs.ckdp.pojo.Shop;

import java.util.List;
@Slf4j
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ShopMapper shopMapper;
    private PageResult<Shop> pageSelect(int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        log.info("pageSize:{},pageNum:{}",pageSize,pageNum);
        List<Shop> shops=shopMapper.getShopList();
        PageInfo<Shop> pageInfo=new PageInfo<>(shops);
        return new PageResult<>(pageInfo.getList(), (int) pageInfo.getTotal(),pageInfo.getPageNum(),pageInfo.getPageSize());
    }
    public PageResult<Shop> getShopList(int pageNum, int pageSize){
        String cacheKey="shopPage:"+pageNum+":"+pageSize;

        if (redisTemplate.hasKey(cacheKey)){
            log.info("缓存命中:{}",cacheKey);
            String shopListStr=redisTemplate.opsForValue().get(cacheKey);
            return JSON.parseObject(shopListStr, new TypeReference<PageResult<Shop>>(){});
        }

        PageResult<Shop> shopPage=pageSelect(pageNum,pageSize);
        log.info("shopPage:{}",shopPage);
        redisTemplate.opsForValue().set(cacheKey,JSON.toJSONString(shopPage));

        return shopPage;
    }


    public ShopVO getShopById(int shopId){
        String cacheKey="shopId:"+shopId;

        if (redisTemplate.hasKey(cacheKey)){
            log.info("缓存命中:{}",cacheKey);
            return JSON.parseObject(redisTemplate.opsForValue().get(cacheKey),ShopVO.class);
        }

        Shop shop=shopMapper.getShopById(shopId);
        if(shop==null){
            redisTemplate.opsForValue().set(cacheKey,JSON.toJSONString(null));
            return null;
        }
        List<Product> productList=shopMapper.getShopProduct(shopId);
        ShopVO shopVO=new ShopVO(shopId,shop.getShopName(),productList);

        redisTemplate.opsForValue().set(cacheKey,JSON.toJSONString(shopVO));

        return shopVO;
    }


}
