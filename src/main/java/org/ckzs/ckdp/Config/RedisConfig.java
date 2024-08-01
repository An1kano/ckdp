package org.ckzs.ckdp.Config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.ckzs.ckdp.pojo.SecKillEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 使用FastJsonRedisSerializer替换默认序列化
        GenericFastJsonRedisSerializer fastJsonSerializer = new GenericFastJsonRedisSerializer();

        // 设置value的序列化规则和key的序列化规则
        template.setValueSerializer(fastJsonSerializer);
        template.setKeySerializer(new StringRedisSerializer());

        // 设置Hash key 和 value 的序列化规则
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(fastJsonSerializer);

        template.afterPropertiesSet();

        return template;
    }
}
