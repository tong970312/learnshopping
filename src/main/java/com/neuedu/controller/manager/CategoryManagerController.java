package com.neuedu.controller.manager;

import com.alibaba.druid.sql.visitor.functions.Concat;
import com.neuedu.bean.UserInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManagerController {
    @Autowired
    ICategoryService categoryService;
    /**
     * 获取品类子节点(平级)
     */
    @RequestMapping(value = "get_category.do")
    public ServerResponse get_category(HttpSession session, Integer categoryId){
        //session获取用户信息
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限  管理员才可以操作后台
        if (userInfo.getRole()!=Const.USER_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
        }


        return categoryService.get_category(categoryId);
    }

    /**
     * 增加节点
     * @param session
     * @param parentId
     * @param categoryName
     * @return
     */
     @RequestMapping(value = "add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       Integer parentId,
                                       String categoryName){
        //session获取用户信息
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限  管理员才可以操作后台
        if (userInfo.getRole() != Const.USER_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
        }

        return categoryService.add_category(parentId,categoryName);
    }

    /**
     * 修改节点
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */

    @RequestMapping(value = "set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,
                                            Integer categoryId,
                                            String categoryName){
        //session获取用户信息
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限  管理员才可以操作后台
        if (userInfo.getRole() != Const.USER_ADMIN){
            return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
        }
        return categoryService.set_category_name(categoryId,categoryName);
    }

    /**
     * 获取当前分类id以及递归子节点
     * @param session
     * @param categoryId
     * @return
     */
     @RequestMapping(value = "get_deep_category.do")
        public ServerResponse get_deep_category(HttpSession session,
                                                Integer categoryId){
            //session获取用户信息
            UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
            if (userInfo==null){
                return ServerResponse.createServerResponseByError(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
            }
            //判断用户权限  管理员才可以操作后台
            if (userInfo.getRole() != Const.USER_ADMIN){
                return ServerResponse.createServerResponseByError(ResponseCode.NO_PRIVILECE.getStatus(),ResponseCode.NO_PRIVILECE.getMsg());
            }
            return categoryService.get_deep_category(categoryId);
        }


}
