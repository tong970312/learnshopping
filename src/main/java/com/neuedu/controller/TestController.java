package com.neuedu.controller;


import com.neuedu.bean.UserInfo;

import com.neuedu.dao.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
/*返回的是json格式数据*/
public class TestController {

    @RequestMapping(value = "UserInfo")
    public UserInfo login(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("aaa");
        return userInfo;
    }


}
