package com.neuedu.controller.portal;

import com.neuedu.bean.UserInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    ICartService cartService;
    /**
     * 购物车中添加商品
     */
    @RequestMapping(value = "add.do")
    public ServerResponse add(HttpSession session, Integer productId, Integer count){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
            if (userInfo==null){
                return ServerResponse.createServerResponseByError("需要登录");
            }

        return cartService.add(userInfo.getId(),productId,count);
    }
}
