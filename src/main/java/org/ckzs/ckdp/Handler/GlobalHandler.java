package org.ckzs.ckdp.Handler;

import lombok.extern.slf4j.Slf4j;
import org.ckzs.ckdp.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalHandler {
    @ExceptionHandler(RuntimeException.class)
    public Result exceptionHandler(RuntimeException e){
        log.error("错误:{}",e.getMessage());
        return Result.error(e.getMessage());
    }
}
