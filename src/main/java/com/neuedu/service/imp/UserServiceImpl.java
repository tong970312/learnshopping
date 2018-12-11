package com.neuedu.service.imp;

import com.neuedu.bean.UserInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.TokenCache;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.service.IUserService;
import com.neuedu.util.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/*标识业务逻辑层  类交给容器处理*/
@Service
public class UserServiceImpl implements IUserService {
    /*通过类型找*/
    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * 注册方法
     * @param userInfo;
     * @return
     */
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //1.非空校验
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_ENPTY.getStatus(),ResponseCode.PARAM_ENPTY.getMsg());
        }
        //2.判断用户名是否存在
//        int count = userInfoMapper.checkUsername(userInfo.getUsername());
//        if (count>0){
//            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg()    );
//        }
        //使用check_valid方法校验
        ServerResponse username_serverResponse = check_valid(userInfo.getUsername(),Const.USERNAME);

        if (!username_serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
        }

        //3.判断邮箱是否存在
//        int result = userInfoMapper.checkEmail(userInfo.getEmail());
//        if (result>0){
//            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
//        }
        //使用check_valid方法校验
        ServerResponse email_serverResponse = check_valid(userInfo.getEmail(),Const.EMAIL);
        if (!email_serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
        }

        //4.注册(MD5密码加密设置)
        /*设置普通用户*/
        userInfo.setRole(Const.USER_ROLE);
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int insertResult = userInfoMapper.insert(userInfo);

        //5.返回处理结果
        if (insertResult>0){
            return ServerResponse.createServerResponseBySuccess("注册成功");
        }
        return ServerResponse.createServerResponseByError("注册失败");

    }

    /**
     * 登陆方法
     * @param username;
     * @param password;
     * @return
     */
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
            /*int result = userInfoMapper.checkUsername(username);
            if (result<=0){
                return  ServerResponse.createServerResponseByError("用户名不存在");
            }*/
            //使用check_valid方法校验
            ServerResponse serverResponse = check_valid(username,Const.USERNAME);
            if (serverResponse.isSuccess()){
                return ServerResponse.createServerResponseByError(ResponseCode.NOT_EXISTS_USERNAME.getStatus(),ResponseCode.NOT_EXISTS_USERNAME.getMsg());
            }

            //3.查询用户   登陆部分进行密码加密
            UserInfo userInfo=userInfoMapper.selectUserByUsernameAndPassword(username, MD5Utils.getMD5Code(password));
            if (userInfo==null){
                return ServerResponse.createServerResponseByError("密码错误");
            }

            //4.处理结果并返回
            userInfo.setPassword("");
        return ServerResponse.createServerResponseBySuccess(null,userInfo);
    }

    @Override
    public ServerResponse check_valid(String str, String type) {
        //1.参数非空校验
        if (StringUtils.isBlank(str)||StringUtils.isBlank(type)){
            return ServerResponse.createServerResponseByError("参数不能为空--");
        }
        //2.判断用户名
        if (type.equals(Const.USERNAME)){
           int username_result= userInfoMapper.checkUsername(str);
            if (username_result>0){
                return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
            }
            return ServerResponse.createServerResponseBySuccess("成功");
            /*校验邮箱*/
        }else  if (type.equals(Const.EMAIL)){
                int email_result = userInfoMapper.checkEmail(str);
                if (email_result>0){
                    return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
                }
             return ServerResponse.createServerResponseBySuccess("成功");
        }
        //3.返回结果

        return ServerResponse.createServerResponseByError("type参数传递有误");
    }

    /**
     * 根据用户名查密保问题
     * @param username;
     * @return
     */
    @Override
    public ServerResponse forget_get_question(String username) {
        //1.参数非空校验
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_ENPTY.getStatus(),ResponseCode.PARAM_ENPTY.getMsg());
        }
        //2.校验username
       ServerResponse serverResponse = check_valid(username,Const.USERNAME);
        if (serverResponse.getStatus()!=ResponseCode.EXISTS_USERNAME.getStatus()){
            return ServerResponse.createServerResponseByError(ResponseCode.NOT_EXISTS_USERNAME.getStatus(),ResponseCode.NOT_EXISTS_USERNAME.getMsg());

        }
        //3.查找密保问题
         String question = userInfoMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)){
            return ServerResponse.createServerResponseByError("密保问题为空");
        }
        System.out.println("question = " + question);
        return ServerResponse.createServerResponseBySuccess(question);
    }

    @Override
    public ServerResponse forget_get_answer(String username, String question, String answer) {

        //1.参数的非空校验
        if (StringUtils.isBlank(username)||StringUtils.isBlank(question)||StringUtils.isBlank(answer)){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //2.校验答案
        int count=userInfoMapper.checkAnswerByUsernameAndQuestion(username,question,answer);
            if (count<=0){
                return ServerResponse.createServerResponseByError("答案错误");
            }

        //返回用户的唯一标识 --->username -->token
        //随机字符串
        String user_token = UUID.randomUUID().toString();
       TokenCache.put(username,user_token);

        //3、返回结果

        return ServerResponse.createServerResponseBySuccess(user_token);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew,String forgetToken) {

        //1.参数的非空校验
        if (StringUtils.isBlank(username)||StringUtils.isBlank(passwordNew)||StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createServerResponseByError("参数不能为空");
        }

        //校验token

       String token = TokenCache.get(username);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createServerResponseByError("token不存在或过期");
        }

        if (token.equals(forgetToken)){
            return ServerResponse.createServerResponseByError("token不一致");
        }

        //2.根据用户名更新密码
        int count=userInfoMapper.updatePasswordByUsername(username,MD5Utils.getMD5Code(passwordNew));

        if (count<=0){
            return  ServerResponse.createServerResponseByError("密码修改失败");
        }
        //.返回结果
        return ServerResponse.createServerResponseBySuccess();
    }
    @Override
    public  ServerResponse reset_password(UserInfo userInfo,String passwordOld, String passwordNew){
        //1 参数的非空校验
        if (StringUtils.isBlank(passwordOld)||StringUtils.isBlank(passwordNew)){
            return  ServerResponse.createServerResponseByError("参数不能为空");
        }
        //2 校验旧密码是否正确(防止用户横向越权)
        UserInfo userInfoOld=  userInfoMapper.selectUserByUsernameAndPassword(userInfo.getUsername(),MD5Utils.getMD5Code(passwordOld));
        if (userInfoOld==null){
                return ServerResponse.createServerResponseByError("旧密码错误");
        }

        //3 修改密码
       int count= userInfoMapper.updatePasswordByUsername(userInfo.getUsername(),MD5Utils.getMD5Code(passwordNew));
        //4 返回结果
        if (count<=0){
            return ServerResponse.createServerResponseByError("密码修改失败");
        }
        return ServerResponse.createServerResponseBySuccess("密码修改成功");
    }
}
