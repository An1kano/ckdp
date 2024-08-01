package org.ckzs.ckdp.MQ;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.ckzs.ckdp.Service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "seckillTopic", consumerGroup = "ckdp-consumer")
public class OrderConsumer implements RocketMQListener<MessageExt> {
    @Autowired
    private SecKillService secKillService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public void onMessage(MessageExt message) {
        String orderKey = new String(message.getBody());
        String orderId=orderKey.split(":")[1];
        int productId= Integer.parseInt(orderId.split("\\.")[2]);
        long threadId=Thread.currentThread().getId();
        while(true){
            Boolean flag=redisTemplate.opsForValue().setIfAbsent("lock:"+productId, Long.toString(threadId), 10, java.util.concurrent.TimeUnit.SECONDS);
            if (flag){
                try{
                    secKillService.updateStock(orderKey);
                    log.info("消费成功！！！");
                    return;
                }catch (Exception e){
                    log.error("消费失败！！！:{}",e.getMessage());
                    return;
                }finally {
                    redisTemplate.delete("lock:"+productId);
                }
            }else {
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}
