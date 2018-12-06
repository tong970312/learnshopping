package com.neuedu.controller;


import com.neuedu.bean.UserInfo;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
/*返回的是json格式数据*/
@RequestMapping(value = "/portal/user/")
public class TestController {

    @Autowired
    IUserService iUserService;

    @RequestMapping(value = "UserInfo")
            /*RequestParam  前台取值，并赋值给username等...
            * value 值跟key相等
            * required  -->true|false-->必须传值|可传可不传
            * defaultValue --> 假如不传,设为默认值
            * value跟形参相等,可以不写  @RequestParam(value =" ***** ")
            * */
    public ServerResponse login(UserInfo userInfo){
            /*@RequestParam(value ="username") String username,
                          @RequestParam(value ="password") String password,
                          @RequestParam(value ="email") String email,
                          @RequestParam(value ="phone") String phone,
                          @RequestParam(value ="question") String question,
                          @RequestParam(value ="answer") String answer*/

        /*UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.setPassword(password);
            userInfo.setEmail(email);
            userInfo.setPhone(phone);
            userInfo.setQuestion(question);
            userInfo.setAnswer(answer);
            userInfo.setRole(1);
            userInfo.setCreateTime(new Date());
            userInfo.setUpdateTime(new Date());*/

            userInfo.setCreateTime(new Date());
            userInfo.setUpdateTime(new Date());



            return iUserService.register(userInfo);
    }


}
