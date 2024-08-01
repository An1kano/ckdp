package org.ckzs.ckdp.Scheduler;

import jakarta.annotation.PostConstruct;
import org.ckzs.ckdp.Service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SecKillScheduler {
    @Autowired
    private SecKillService secKillService;
    @Scheduled(cron = "0 0 0 * * ?")
    public void loadTodayEvents()
    {
        secKillService.loadTodayEvents(LocalDateTime.now());
    }
    @PostConstruct
    public void init()
    {
        this.loadTodayEvents();
    }

}
