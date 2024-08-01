package org.ckzs.ckdp.pojo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    String orderId;
    int userId;
    int productId;
    LocalDateTime createTime;
}
