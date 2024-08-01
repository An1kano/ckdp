package org.ckzs.ckdp.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ckzs.ckdp.pojo.SecKillEvent;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SecKillMapper {
    List<SecKillEvent> getTodayEvents(LocalDateTime today);
    SecKillEvent getEventById(int id);

    void decrementStock(int eventId);
}
