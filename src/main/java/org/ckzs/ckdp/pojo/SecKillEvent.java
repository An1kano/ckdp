package org.ckzs.ckdp.pojo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SecKillEvent {
    private int id;
    private int productId;
    private int stock;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String eventName;
    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("productId",productId);
        map.put("stock",stock);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("eventName",eventName);
        return map;
    }
}
