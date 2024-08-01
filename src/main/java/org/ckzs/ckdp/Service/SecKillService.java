package org.ckzs.ckdp.Service;

import org.ckzs.ckdp.pojo.Result;
import org.ckzs.ckdp.pojo.SecKillEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface SecKillService {
    List<SecKillEvent> loadTodayEvents(LocalDateTime today);
    List<SecKillEvent> getEventsList(LocalDateTime today);
    SecKillEvent getEventById(int eventId);
    Result<String> buy(SecKillEvent secKillEvent,int userId);
    void updateStock(String orderKey);
}
