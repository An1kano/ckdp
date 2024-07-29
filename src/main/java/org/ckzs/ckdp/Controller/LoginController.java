package org.ckzs.ckdp.Controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.ckzs.ckdp.DTO.UserDTO;
import org.ckzs.ckdp.Service.UserService;
import org.ckzs.ckdp.VO.UserVO;
import org.ckzs.ckdp.pojo.JwtProfile;
import org.ckzs.ckdp.pojo.Result;
import org.ckzs.ckdp.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;


    @PostMapping("")
    public Result<UserVO> login(@RequestBody UserDTO userDTO){

        log.info("用户登陆:{}",userDTO);

        User user=userService.login(userDTO);

        String token="";
        Map<String, Object> claims = new HashMap<>();
        claims.put("username",user.getUsername());
        claims.put("password",user.getPassword());
        claims.put("id",user.getId());

        token= Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JwtProfile.SECRET_KEY)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+JwtProfile.TIME_LIMIT))
                .compact();

        UserVO userVO=UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .build();

        return Result.success(userVO);
    }


    @PostMapping("/reg")
    public Result register(@RequestBody UserDTO userDTO){
        log.info("用户注册:{}",userDTO);
        userService.insert(userDTO);
        return Result.success();
    }

}
