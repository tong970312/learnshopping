package com.neuedu.service.imp;

import com.neuedu.bean.UserInfo;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/*标识业务逻辑层  类交给容器处理*/
@Service
public class UserServiceImpl implements IUserService {
    /*通过类型找*/
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public ServerResponse register(UserInfo userInfo) {

     int count= userInfoMapper.insert(userInfo);
        if (count >0) {
            return ServerResponse.createServerResponseBySuccess();
        }

        return ServerResponse.createServerResponseByError();
    }

    @Override
    public ServerResponse login(String username, String password) {

            //1.参数的非空校验
            if (StringUtils.isBlank(username)){
                return ServerResponse.createServerResponseByError("用户名不能为空");
            }
            if (StringUtils.isBlank(password)){
                return ServerResponse.createServerResponseByError("密码不能为空");
            }
            //2.用户名是否存在
            int result = userInfoMapper.checkUsername(username);
            if (result<=0){
                return  ServerResponse.createServerResponseByError("用户名不存在");
            }
            //3.查询用户
            UserInfo userInfo=userInfoMapper.selectUserByUsernameAndPassword(username, password);
            if (userInfo==null){
                return ServerResponse.createServerResponseByError("密码错误");
            }

            //4.处理结果并返回
            userInfo.setPassword("");
        return ServerResponse.createServerResponseBySuccess(null,userInfo);
    }
}
