package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 封装返回前端的高复用对象
 * @param <T>
 *     int 型的status状态码, 泛型 类型的data数据， String 型的信息
 */
/*定义成泛型类*/
    /*筛选非空的数据进行显示*/
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {
    /*状态码*/
    private int status;
    /*返回的接口数据*/
    private  T  data;
    /*接口提示信息*/
    private  String msg;

    private ServerResponse(){}

    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status, String msg){
        this.status=status;
        this.msg = msg;
    }
    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }




    /**
     * 判断接口是否调用成功
     *返回到前台,json会把这个方法当做get方法区扫描,所以返回的数据后会带有success
     */
    @JsonIgnore
    public  boolean isSuccess(){
        return this.status==Const.SUCCESS_CODE;
    }
    /**
     *成功
     * */

    public static ServerResponse createServerResponseBySuccess(){

        return new ServerResponse(Const.SUCCESS_CODE);
    }

    public static ServerResponse createServerResponseBySuccess(String msg){
        return new ServerResponse(Const.SUCCESS_CODE,msg);
    }

    public static <T> ServerResponse createServerResponseBySuccess(String msg, T data){

        return new ServerResponse(Const.SUCCESS_CODE,msg,data);
    }




    /**
     * 失败
     *
     */
    public static ServerResponse createServerResponseByError(){
        return new ServerResponse(Const.SUCCESS_ERROR);
    }
    public static ServerResponse createServerResponseByError(String msg){
        return new ServerResponse(Const.SUCCESS_ERROR,msg);
    }
    public static ServerResponse createServerResponseByError(int status){
        return new ServerResponse(status);
    }
    public static ServerResponse createServerResponseByError(int status, String msg){
            return new ServerResponse(status,msg);
    }





    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
