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


public enum OrderStatusEnum{
        ORDER_CANCELED(0,"已取消"),
        ORDER_UN_PAY(10,"未付款"),
        ORDER_PAY(20,"已付款"),
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_CLOSE(60,"交易关闭")
    ;

        private int code;
        private String desc;
    OrderStatusEnum(int code, String desc) {
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

    /**
     * 遍历枚举
     * 订单状态描述
     * @param code
     * @return
     */
        public static OrderStatusEnum codeOf(Integer code){
            for (OrderStatusEnum orderStatusEnum:values()) {
                if (code==orderStatusEnum.code){
                    return orderStatusEnum;
                }
            }
            return null;
        }
    }

    public enum PayTypeEnum{
        ONLINE(1,"线上支付")
        ;

        private int code;
        private String desc;
        PayTypeEnum(int code, String desc) {
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
        /**
         * 遍历枚举
         * 支付类型描述
         * @param code
         * @return
         */
        public static PayTypeEnum codeOf(Integer code){
            for (PayTypeEnum payTypeEnum:PayTypeEnum.values()) {
                if (code==payTypeEnum.code){
                    return payTypeEnum;
                }
            }
            return null;
        }
    }




}
