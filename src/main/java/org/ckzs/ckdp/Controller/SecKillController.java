package org.ckzs.ckdp.Controller;

import io.swagger.v3.oas.annotations.Operation;
import org.ckzs.ckdp.Service.SecKillService;
import org.ckzs.ckdp.pojo.BaseContext;
import org.ckzs.ckdp.pojo.Result;
import org.ckzs.ckdp.pojo.SecKillEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/event")
public class SecKillController {
    AtomicInteger userIdAt=new AtomicInteger(0);
    @Autowired
    private SecKillService secKillService;
    @Operation(summary = "获取当天所有秒杀活动")
    @GetMapping("/list")
    public Result<List<SecKillEvent>> getEventsList()
    {
        List<SecKillEvent> eventsList = secKillService.getEventsList(LocalDate.now().atStartOfDay());
        return Result.success(eventsList);
    }
    @Operation(summary = "通过ID查询秒杀活动")
    @GetMapping("/info")
    public Result<SecKillEvent> getEventById(@RequestParam int eventId)
    {
        SecKillEvent event = secKillService.getEventById(eventId);
        return Result.success(event);
    }
    @GetMapping("/buy")
    public Result<String> buy(@RequestParam int eventId)
    {
        SecKillEvent secKillEvent=secKillService.getEventById(eventId);
        LocalDateTime nowTime=LocalDateTime.now();
        if (nowTime.isBefore(secKillEvent.getStartTime())){
            return Result.error("秒杀活动未开始！！！");
        }else if(nowTime.isAfter(secKillEvent.getEndTime())){
            return Result.error("秒杀活动已结束！！！");
        }
        //int userId= BaseContext.getCurrentUserId();
        int userId=userIdAt.incrementAndGet();
        return secKillService.buy(secKillEvent,userId);
    }

}
