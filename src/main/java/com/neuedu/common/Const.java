package com.neuedu.common;
//常量
public class Const {

    public static final Integer SUCCESS_CODE=0;
    public static final Integer SUCCESS_ERROR=1;

    public static final Integer USER_ROLE=0;//普通用户
    public static final Integer USER_ADMIN=1;//管理员

    public static final String CURRENTUSER= "CURRENTUSER";//用于保存用户信息到session

    public static final String USERNAME= "username";
    public static final String EMAIL= "email";

    public  enum ProductStatusEnum{
        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OFFONLINE(2,"下架"),
        PRODUCT_DELETE(3,"删除")
        ;
        private int code;
        private String desc;
        private ProductStatusEnum(int code,String desc){
            this.code=code;
            this.desc=desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public enum CartCheckEnum{
        PRODUCT_CHECKED(1,"已勾选"),
        PRODUCT_UNCHECKED(0,"未勾选");

        private int code;
        private String desc;
        CartCheckEnum(int code, String desc) {
            this.code=code;
            this.desc=desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

}
