package org.ckzs.ckdp.Controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.ckzs.ckdp.pojo.BaseContext;
import org.ckzs.ckdp.pojo.JwtProfile;
import org.ckzs.ckdp.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class LogoutController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result logout(@RequestHeader(JwtProfile.TOKEN_KEY) String token){
        redisTemplate.opsForValue().set(token,"logout",JwtProfile.TIME_LIMIT, TimeUnit.MILLISECONDS);
        int id= BaseContext.getCurrentUserId();
        log.info("用户登出：{}",id);
        BaseContext.clear();
        return Result.success();
    }
}
