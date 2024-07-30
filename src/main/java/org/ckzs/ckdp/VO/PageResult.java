package org.ckzs.ckdp.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T>{
    private List<T> data;
    private int total;         // 总记录数
    private int pageNum;        // 当前页码
    private int pageSize;
}
