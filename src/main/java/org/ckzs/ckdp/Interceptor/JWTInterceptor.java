package org.ckzs.ckdp.Interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ckzs.ckdp.pojo.BaseContext;
import org.ckzs.ckdp.pojo.JwtProfile;
import org.ckzs.ckdp.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = request.getHeader(JwtProfile.TOKEN_KEY);
        if(token==null){
            log.info("token为空，拦截请求：");
            response.setStatus(401);
            return false;
        }
        //判断token是否已经注销
        if(redisTemplate.hasKey(token)){
            log.info("token已注销，拦截请求");
            response.setStatus(401);
            return false;
        }

        try {
            //校验token
            log.info("解析token中...");
            Claims claims=Jwts.parser()
                    .setSigningKey(JwtProfile.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            int userId=Integer.valueOf(claims.get(User.USER_ID_KEY).toString());
            BaseContext.setCurrentUserId(userId);
            log.info("token解析成功，当前员工id：{}",userId);
            //放行
            return true;
        }catch (Exception e){
            log.error("token解析失败，原因：{}",e.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}
