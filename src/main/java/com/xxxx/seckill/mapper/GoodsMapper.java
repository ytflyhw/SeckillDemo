package com.xxxx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.vo.GoodVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ytflyhw
 * @since 2021-03-12
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取商品列表
     */
    List<GoodVo> findGoodsVo();

    /**
     * 获取商品详情
     * @param goodsId
     */
    GoodVo findGoodVoByGoodsId(Long goodsId);
}
