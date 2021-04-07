package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.mapper.GoodsMapper;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.vo.GoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ytflyhw
 * @since 2021-03-12
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    /**
     * 获取商品列表
     */
    @Override
    public List<GoodVo> findGoodVo() {
        return goodsMapper.findGoodsVo();
    }

    /**
     * 获取商品详情
     */
    @Override
    public GoodVo findGoodVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodVoByGoodsId(goodsId);
    }
}
