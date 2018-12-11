package com.neuedu.controller.portal;

import com.neuedu.bean.UserInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/portal/user/")
public class UserController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "login.do")
    public ServerResponse login(HttpSession session,String username, String password) {

        /*登陆成功,将内容保存到session*/
        ServerResponse serverResponse = userService.login(username,password);

            /*当 isSuccess 方法为true 设置session为当前登录用户的信息数据--->serverResponse.getData()*/
            /*serverResponse返回的是json,所以需要通过getData()获取数据 */
            if (serverResponse.isSuccess()){
                session.setAttribute(Const.CURRENTUSER,serverResponse.getData());
            }
        return serverResponse;
    }

    @RequestMapping(value = "register.do")
    public ServerResponse register(UserInfo userInfo) {

        return userService.register(userInfo);

    }

    /**
     * 检测用户名和邮箱是否有效
     */

    @RequestMapping(value = "check_valid.do")
    public ServerResponse check_valid(String str,String type){
      return  userService.check_valid(str,type);
    }

    /**
     * 获取用户信息（部分）
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        /*获取session中的数据*/
        Object obj = session.getAttribute(Const.CURRENTUSER);
        /*判断不为空且是UserInfo的实例*/
        if (obj!=null && obj instanceof UserInfo){
            UserInfo userInfo = (UserInfo) obj;
            UserInfo responseuserInfo = new UserInfo();
            responseuserInfo.setId(userInfo.getId());
            responseuserInfo.setUsername(userInfo.getUsername());
            responseuserInfo.setEmail(userInfo.getEmail());
            responseuserInfo.setCreateTime(userInfo.getCreateTime());
            responseuserInfo.setUpdateTime(userInfo.getUpdateTime());
            return  ServerResponse.createServerResponseBySuccess(null,responseuserInfo);
        }
        return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }

    /**
     * 获取用户数据(全部)
     * @param session;
     * @return
     */
    @RequestMapping(value = "get_information.do")
    public ServerResponse get_information(HttpSession session){
        /*获取session中的数据*/
        Object obj = session.getAttribute(Const.CURRENTUSER);
        /*判断不为空且是UserInfo的实例*/
        if (obj!=null && obj instanceof UserInfo){
         UserInfo responseuserInfo = (UserInfo) obj;
            return  ServerResponse.createServerResponseBySuccess(null,responseuserInfo);
        }
        return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }

    @RequestMapping(value = "forget_get_question")
    public ServerResponse forget_get_question(String username){

        ServerResponse serverResponse = userService.forget_get_question(username);

        return serverResponse;
    }

    /**
     * 提交问题答案接口
     */
    @RequestMapping(value = "forget_get_answer")
    public ServerResponse forget_get_answer(String username,String question,String answer){

        return  userService.forget_get_answer(username,question,answer);
    }


    /**
     * 修改密码
     */
    @RequestMapping(value = "forget_reset_password")
    public ServerResponse forget_reset_password(String username,String passwordNew,String forgetToken){
        return userService.forget_reset_password(username,passwordNew,forgetToken);
    }

    /**
     * 退出登录
     * 直接删除session
     */
    @RequestMapping(value = "logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.createServerResponseBySuccess();
    }

    /**
     * 登录状态下重置密码
     */

    @RequestMapping(value = "reset_password.do")
    public ServerResponse reset_password(HttpSession session ,String passwordOld,String passwordNew){
        Object o = session.getAttribute(Const.CURRENTUSER);
        if (o!=null && o instanceof UserInfo){
            UserInfo userInfo = (UserInfo) o;
           return userService.reset_password(userInfo,passwordOld,passwordNew);
        }
        return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());

    }












}