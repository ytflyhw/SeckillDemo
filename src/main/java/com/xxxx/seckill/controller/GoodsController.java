package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodVo;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 商品列表页面跳转
     */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){

        // Redis 中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String)valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        // 如果为空， 手动渲染， 存入 Redis 并返回
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodVo());
//        return "goodsList";
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }
    /**
     * 商品详情页面跳转
     */
//    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String toDetail2(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){
//
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        String html = (String) valueOperations.get("goodsDetails:" + goodsId);
//        if(!StringUtils.isEmpty(html)){
//            return html;
//        }
//
//        model.addAttribute("user", user);
//        GoodVo goodVo = goodsService.findGoodVoByGoodsId(goodsId);
//        Date startTime = goodVo.getStartTime();
//        Date endTime = goodVo.getEndTime();
//        Date nowTime = new Date();
//        // 秒杀状态
//        int secKillStatus = 0;
//        // 秒杀倒计时
//        int remainSeconds = 0;
//        if(nowTime.before(startTime)){
//             remainSeconds = (int)((startTime.getTime() - nowTime.getTime()) / 1000);               // 秒杀未开始
//        }
//        else if(nowTime.after(endTime)){
//            secKillStatus = 2;                                  // 秒杀已结束
//            remainSeconds = -1;
//        }
//        else {
//            secKillStatus = 1;                                 // 秒杀进行中
//            remainSeconds = 0;
//        }
//        model.addAttribute("secKillStatus", secKillStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//        model.addAttribute("goods", goodVo);
//        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
//        if(!StringUtils.isEmpty(html)){
//            valueOperations.set("goodsDetails:"+goodsId, html, 60, TimeUnit.SECONDS);
//        }
//        return html;
//    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId){
        GoodVo goodVo = goodsService.findGoodVoByGoodsId(goodsId);
        Date startTime = goodVo.getStartTime();
        Date endTime = goodVo.getEndTime();
        Date nowTime = new Date();
        // 秒杀状态
        int secKillStatus = 0;
        // 秒杀倒计时
        int remainSeconds = 0;
        if(nowTime.before(startTime)){
            remainSeconds = (int)((startTime.getTime() - nowTime.getTime()) / 1000);               // 秒杀未开始
        }
        else if(nowTime.after(endTime)){
            secKillStatus = 2;                                  // 秒杀已结束
            remainSeconds = -1;
        }
        else {
            secKillStatus = 1;                                 // 秒杀进行中
            remainSeconds = 0;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodVo(goodVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);

        return RespBean.success(detailVo);
    }
}
