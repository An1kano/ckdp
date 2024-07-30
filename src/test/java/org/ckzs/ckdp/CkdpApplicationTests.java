package org.ckzs.ckdp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.ckzs.ckdp.pojo.JwtProfile;
import org.ckzs.ckdp.pojo.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CkdpApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Test
    public void testRedis() {
        // 设置值
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set("testKey", "testValue");

        // 获取值
        String value = valueOps.get("testKey");
        System.out.println(value);
    }
    @Test
    public void test1(){
        String password="75863588";
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println(password);
    }
    @Test
    public void redisKeyTest(){
        redisTemplate.opsForValue().set("testKey","testValue");
        if(redisTemplate.hasKey("testKey")){
            System.out.println("存在");
        }

    }
    @Test
    public void test2(){
        Shop shop=new Shop();
        shop.setShopId(1);
        shop.setShopName("CKZS");
        System.out.println(shop);
    }
    @Test
    public void tokenTest(){
        String token="";
        Map<String, Object> claims = new HashMap<>();
        claims.put("username","ckzs");
        claims.put("password","75863588");

        token= Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JwtProfile.SECRET_KEY)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+JwtProfile.TIME_LIMIT))
                .compact();
        System.out.println(token);
        Claims reclaims= Jwts.parser()
                .setSigningKey(JwtProfile.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        String username= (String) reclaims.get("username");
        String password= (String) reclaims.get("password");
        System.out.println("用户名："+username);
        System.out.println("密码："+password);
    }
}

