package com.neuedu.controller.manager;

import com.neuedu.bean.Product;
import com.neuedu.bean.UserInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
@RestController
@RequestMapping(value = "/manage/product")
public class ProductManagerController {

    @Autowired
    IProductService productService;

    /**
     * 新增或者更新
     */
    @RequestMapping(value = "saveOrderUpdate.do")
    public ServerResponse saveOrderUpdate(HttpSession session, Product product){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        if (userInfo.getRole()!= Const.USER_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
        }

        return productService.saveOrUpdate(product);

    }

    /**
     * 修改上下架状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "set_sale_status.do")
    public ServerResponse set_sale_status(HttpSession session,Integer productId,Integer status){
        //获取当前对象
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        System.out.println("userInfo = " + userInfo);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole()!= Const.USER_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
        }

        return productService.set_sale_status(productId,status);
    }

    /**
     * 查看商品详情
     * @param session;
     * @param productId;
     * @return
     */
    @RequestMapping(value = "detail.do")
    public ServerResponse detail(HttpSession session,Integer productId){
        //获取当前对象
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole()!= Const.USER_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
        }

        return productService.detail(productId);
    }

    /**
     * 分页查询
     * @param session
     * @param
     * @return
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        //获取当前对象
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole()!= Const.USER_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
        }

        return productService.list(pageNum,pageSize);
    }

    @RequestMapping(value = "search.do")
        public ServerResponse search(HttpSession session,
                                   @RequestParam(value = "productId",required = false)Integer productId,
                                   @RequestParam(value = "productName",required = false)String productName,
                                   @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                   @RequestParam(value = "pageSize",required = false)Integer pageSize){
            //获取当前对象
            UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
            if (userInfo==null){
                return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
            }
            //判断用户权限
            if (userInfo.getRole()!= Const.USER_ADMIN){
                return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
            }

            return productService.search(productId,productName,pageNum,pageSize);
        }

    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    @RequestMapping(value = "portal_detail.do")
        public ServerResponse detail(Integer productId){

            return productService.detail_portal(productId);
        }


}
