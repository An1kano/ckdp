package org.ckzs.ckdp.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ckzs.ckdp.pojo.Product;
import org.ckzs.ckdp.pojo.Shop;

import java.util.List;

@Mapper
public interface ShopMapper {
    List<Shop> getShopList();
    Shop getShopById(int shopId);
    List<Product> getShopProduct(int shopId);

}
