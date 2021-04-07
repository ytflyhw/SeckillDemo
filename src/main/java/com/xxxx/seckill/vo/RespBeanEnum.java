package com.xxxx.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 公共返回对象枚举
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    // 通用
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务器异常"),
    // 登录模块 5002xx
    LOGIN_ERROR(500210, "用户名或密码错误"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    BIND_ERROR(500212, "参数校验异常"),
    SESSION_ERROR(500213, "用户不存在"),
    // 秒杀模块 5005xx
    EMPTY_STOCK(500500, "手慢了"),
    REPEATE_ERROR(500501, "您已经抢过了"),
    // 用户信息 5006xx
    MOBILE_NOT_EXIT(500601,"用户不存在"),
    PASSWORD_UPDATE_FAIL(500602, "更新密码失败"),
    // 订单模块 5003xx
    ORDER_NOT_EXIT(500300, "订单不存在");

    private final Integer code;     // 状态码
    private final String message;   // 对应信息

}
