package org.ckzs.ckdp.pojo;

import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int productId;
    private String productName;
    private int shopId;
    private int price;
}
