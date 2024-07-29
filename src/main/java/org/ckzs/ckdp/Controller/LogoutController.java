package org.ckzs.ckdp.Controller;

import lombok.extern.slf4j.Slf4j;
import org.ckzs.ckdp.pojo.BaseContext;
import org.ckzs.ckdp.pojo.JwtProfile;
import org.ckzs.ckdp.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogoutController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @PostMapping("/logout")
    public Result logout(@RequestHeader(JwtProfile.TOKEN_KEY) String token){
        redisTemplate.opsForValue().set(token,"logout");
        int id= BaseContext.getCurrentUserId();
        log.info("用户登出：{}",id);
        BaseContext.clear();
        return Result.success();
    }
}
