package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.mapper.GoodsMapper;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.vo.GoodVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ytflyhw
 * @since 2021-03-12
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     */
    List<GoodVo> findGoodVo();

    /**
     * 获取商品详情
     */
    GoodVo findGoodVoByGoodsId(Long goodsId);
}
