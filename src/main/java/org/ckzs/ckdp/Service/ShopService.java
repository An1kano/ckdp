package org.ckzs.ckdp.Service;


import org.ckzs.ckdp.VO.PageResult;
import org.ckzs.ckdp.VO.ShopVO;
import org.ckzs.ckdp.pojo.Shop;

import java.lang.reflect.Array;
import java.util.List;

public interface ShopService {
    PageResult<Shop> getShopList(int pageNum, int pageSize);
    ShopVO getShopById(int shopId);

}
