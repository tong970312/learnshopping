package com.neuedu.service;

import com.neuedu.bean.UserInfo;
import com.neuedu.common.ServerResponse;
import org.apache.ibatis.annotations.Param;

public interface IUserService {

    /**
     *注册接口
     *
     */
    public ServerResponse register(UserInfo userInfo);

    public ServerResponse login( String username, String password);
}
