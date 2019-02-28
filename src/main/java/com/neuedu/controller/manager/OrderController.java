package com.neuedu.controller.manager;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.bean.UserInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IOrderService;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    IOrderService orderService;
    /**
     * 创建订单
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping(value = "createOrder.do")
    public ServerResponse createOrder(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登录");
        }
        return  orderService.createOrder(userInfo.getId(),shippingId);
    }

    /**
     * 取消订单
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登录");
        }
        return  orderService.cancel(userInfo.getId(),orderNo);
    }

    /**
     * 查询购物车订单明细
     * @param session
     * @return
     */
    @RequestMapping(value = "get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登录");
        }
        return  orderService.get_order_cart_product(userInfo.getId());
    }

    /**
     * 查询订单
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登录");
        }
        return  orderService.list(userInfo.getId(),pageNum,pageSize);
    }

    /**
     * 订单明细
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登录");
        }
        return  orderService.detail(orderNo);
    }

    /**
     * 支付宝支付
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "pay.do")
    public ServerResponse pay(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登录");
        }

        return orderService.pay(userInfo.getId(),orderNo);
    }
    /**
     * 支付宝服务器回调应用服务器接口
     */
    @RequestMapping(value = "alipay_callback.do")
    public ServerResponse callback(HttpServletRequest request){
        System.out.println("=========支付宝服务器回调应用服务器接口===========");
        //取出数据
        Map<String,String[]>params = request.getParameterMap();
        Map<String,String> requestparams = Maps.newHashMap();
        Iterator<String>it = params.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            String[] strArr = params.get(key);
            String value = "";
            for (int i = 0; i <strArr.length ; i++) {
                value =(i==strArr.length-1)?value+strArr[i]:value+strArr[i]+",";
            }
            requestparams.put(key,value);
        }

        System.out.println("requestparams = " + requestparams);
        //支付宝验签(防止其它调用)
        try {
           //移除签名类型,否则无效
            requestparams.remove("sign_type");
            //参数  公钥  字符类型 签名类型

        System.out.println("移除后: requestparams = " + requestparams);

           boolean result=AlipaySignature.rsaCheckV2(requestparams, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
         System.out.println("Configs.getSignType() = " + Configs.getSignType());
           if (!result){
            return ServerResponse.createServerResponseByError("非法请求,验证不通过");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //处理业务逻辑
        return orderService.alipay_callback(requestparams);
    }

    /**
     * 查询订单支付状态
     */
    @RequestMapping(value = "query_order_pay_status.do")
    public ServerResponse query_order_pay_status(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登录");
        }

        return orderService.query_order_pay_status(orderNo);
    }
}
