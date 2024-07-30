package org.ckzs.ckdp.StartTask;

import lombok.extern.slf4j.Slf4j;
import org.ckzs.ckdp.Service.ShopService;
import org.ckzs.ckdp.VO.PageResult;
import org.ckzs.ckdp.pojo.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisPreheat implements CommandLineRunner {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ShopService shopService;
    @Override
    public void run(String... args) throws Exception {
        log.info("redis预热中...");
        for(int i=1;i<3;i++){
            PageResult<Shop> pageResult = shopService.getShopList(i, 5);
        }
    }
}
