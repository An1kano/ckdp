package org.ckzs.ckdp.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.ckzs.ckdp.Mapper.OrderMapper;
import org.ckzs.ckdp.Mapper.SecKillMapper;
import org.ckzs.ckdp.Service.SecKillService;
import org.ckzs.ckdp.pojo.Order;
import org.ckzs.ckdp.pojo.Result;
import org.ckzs.ckdp.pojo.SecKillEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SecKIllServiceImpl implements SecKillService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private SecKillMapper secKillMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private StringRedisTemplate redisTemplateString;

    private static final DefaultRedisScript<Long> secScript;
    static {
        secScript = new DefaultRedisScript<>();
        secScript.setLocation(new ClassPathResource("Lua/seckill.lua"));
        secScript.setResultType(Long.class);
    }
    public List<SecKillEvent> loadTodayEvents(LocalDateTime today) {
        List<SecKillEvent> secKillEvents = secKillMapper.getTodayEvents(today);
        secKillEvents.forEach(secKillEvent -> {
            String cacheKey=new String("EventId:"+secKillEvent.getId());
            redisTemplate.opsForValue().set("StockOf"+cacheKey,secKillEvent.getStock());
            redisTemplate.expire("StockOf"+cacheKey,60*60*24, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(cacheKey,secKillEvent);
            redisTemplate.expire(cacheKey,60*60*24, TimeUnit.SECONDS);
        });
        String key=new String("EventList:"+today);
        redisTemplateString.opsForValue().set(key,JSON.toJSONString(secKillEvents));
        redisTemplateString.expire(key,60*60*48, TimeUnit.SECONDS);

        return secKillEvents;
    }
    public List<SecKillEvent> getEventsList(LocalDateTime today) {
        String cacheKey=new String("EventList:"+today);
        if(redisTemplateString.hasKey(cacheKey)){
            //log.info("缓存命中:{}",cacheKey);
            String eventList=redisTemplateString.opsForValue().get(cacheKey);
            return JSON.parseObject(eventList,new TypeReference<List<SecKillEvent>>(){});
        }
        List<SecKillEvent> secKillEvents = this.loadTodayEvents(today);
        redisTemplateString.opsForValue().set(cacheKey,JSON.toJSONString(secKillEvents));
        redisTemplateString.expire(cacheKey,60*60*48, TimeUnit.SECONDS);

        return secKillEvents;
    }
    public SecKillEvent getEventById(int eventId) {
        String cacheKey= "EventId:" + eventId;
        if(redisTemplate.hasKey(cacheKey)){
            //log.info("缓存命中:{}",cacheKey);
            return (SecKillEvent) redisTemplate.opsForValue().get(cacheKey);
        }
        SecKillEvent secKillEvent = secKillMapper.getEventById(eventId);
        redisTemplate.opsForValue().set(cacheKey,secKillEvent);
        redisTemplateString.expire(cacheKey,60*60*24, TimeUnit.SECONDS);
        return secKillEvent;
    }
    public Result<String> buy(SecKillEvent secKillEvent,int userId){
        String orderKey= "Order:" + userId + "." + secKillEvent.getId() + "." + secKillEvent.getProductId();
        String stockKey= "StockOf" + "EventId:" + secKillEvent.getId();

        // 执行 Lua 脚本
        List<String> keys = List.of(orderKey, stockKey);
        Long result = redisTemplate.execute(secScript, keys);
        if(result==1){
            log.info("您已经抢购过了，不可重复购买(ΩДΩ)！！！");
            return Result.error("您已经抢购过了，不可重复购买(ΩДΩ)！！！");
        }else if(result==0){
            //log.info("抢购失败，库存不足(ΩДΩ)！！！");
            return Result.error("抢购失败，库存不足(ΩДΩ)！！！");
        }

        rocketMQTemplate.asyncSend("seckillTopic", orderKey, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                //log.info("订单消息发送成功！！！");
            }
            @Override
            public void onException(Throwable throwable) {
                log.error("订单消息发送发送失败",throwable);
            }
        });
        String testkey="user:"+userId;
        log.info("成功，用户id:{}",testkey);
        return Result.success("抢购成功(p≧w≦q)！！！订单号:"+orderKey);
    }

    @Transactional
    public void updateStock(String OrderKey) {
        String orderId=OrderKey.split(":")[1];
        int userId= Integer.parseInt(orderId.split("\\.")[0]);
        int eventId= Integer.parseInt(orderId.split("\\.")[1]);
        int productId= Integer.parseInt(orderId.split("\\.")[2]);
        LocalDateTime createTime=LocalDateTime.now();

        Order order=new Order(orderId,userId,productId,createTime);
        try {
            //log.info("尝试插入订单");
            secKillMapper.decrementStock(eventId);
            orderMapper.insert(order);
        }catch (Exception e){
            log.error("订单插入失败：{}", e.getMessage());
            throw new RuntimeException("订单插入失败！！！");
        }

    }
}
