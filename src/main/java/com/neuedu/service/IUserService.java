package com.neuedu.service;

import com.neuedu.bean.UserInfo;
import com.neuedu.common.ServerResponse;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;

public interface IUserService {

    /**
     *注册接口
     *
     */
    public ServerResponse register(UserInfo userInfo);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public ServerResponse login( String username, String password);

    /**
     * 检查用户名和邮箱是否有效
     * @param str
     * @param type
     * @return
     */
    public ServerResponse check_valid(String str,String type);

    /**
     *根据用户名找回密保问题
     * @param username
     * @return
     */
    ServerResponse forget_get_question(String username);

    /**
     * 提交问题答案接口
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse forget_get_answer(String username,String question,String answer);

    /**
     * 忘记密码的重置密码
     * @param username
     * @param passwordNew
     * @return
     */
    ServerResponse forget_reset_password(String username,String passwordNew,String forgetToken);

    /**
     * 登录状态下修改密码
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    ServerResponse reset_password(UserInfo userInfo,String passwordOld, String passwordNew);

}
