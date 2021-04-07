package com.xxxx.seckill.vo;


import com.xxxx.seckill.pojo.Goods;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodVo extends Goods {

    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startTime;
    private Date endTime;
}
