package com.neuedu.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartOrderItemVo {
    private List<OrderItemVO> orderItemVOList;
    private String imageHost;
    private BigDecimal totalPrice;

    @Override
    public String toString() {
        return "CartOrderItemVo{" +
                "orderItemVOList=" + orderItemVOList +
                ", imageHost='" + imageHost + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public List<OrderItemVO> getOrderItemVOList() {
        return orderItemVOList;
    }

    public void setOrderItemVOList(List<OrderItemVO> orderItemVOList) {
        this.orderItemVOList = orderItemVOList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
