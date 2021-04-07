package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.vo.GoodVo;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ytflyhw
 * @since 2021-03-12
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MQSender mqSender;

    /**
     * 秒杀
     */
    @Transactional
    @Override
    public Order seckill(User user, GoodVo goodVo) {
        // 秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("good_id", goodVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        // 判断是否更新成功
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count - 1")
                .eq("good_id", goodVo.getId()).gt("stock_count", 0));
        if(!result){
            return null;
        }
        // 生成订单
        Order order = new Order();
        order.setId(123L);
        order.setUserId(user.getId());
        order.setGoodId(goodVo.getId());
        order.setDeliverAddrId(0L);
        order.setGoodName(goodVo.getGoodName());
        order.setGoodCount(1);
        order.setGoodPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setCreatData(new Date());
        order.setStatus(0);
        orderMapper.insert(order);
//        orderService.save(order);
//        mqSender.sendDirect02(order);
        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodId(goodVo.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrderService.save(seckillOrder);
//        mqSender.sendDirect01(seckillOrder);

        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goodVo.getId(), order,10*60, TimeUnit.SECONDS);

        return order;
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if(null == orderId){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIT);
        }
        Order order = orderMapper.selectById(orderId);
        GoodVo goodVo = goodsService.findGoodVoByGoodsId(order.getGoodId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodVo(goodVo);
        return orderDetailVo;
    }
}
