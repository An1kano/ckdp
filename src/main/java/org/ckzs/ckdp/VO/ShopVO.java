package org.ckzs.ckdp.VO;


import lombok.*;
import org.ckzs.ckdp.pojo.Product;
import java.util.List;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopVO {
    private int id;
    private String shopName;
    private List<Product> productList;

}
