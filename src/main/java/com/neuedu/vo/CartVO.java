package com.neuedu.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车实体类VO
 */
public class CartVO implements Serializable {
    //购物信息集合
    private List<CartProductVO> cartProductVOS;
    //是否全选
    private boolean isallchecked;
    //总价格
    private BigDecimal cartprice;

    public List<CartProductVO> getCartProductVOS() {
        return cartProductVOS;
    }

    public void setCartProductVOS(List<CartProductVO> cartProductVOS) {
        this.cartProductVOS = cartProductVOS;
    }

    public boolean isIsallchecked() {
        return isallchecked;
    }

    public void setIsallchecked(boolean isallchecked) {
        this.isallchecked = isallchecked;
    }

    public BigDecimal getCartprice() {
        return cartprice;
    }

    public void setCartprice(BigDecimal cartprice) {
        this.cartprice = cartprice;
    }

    @Override
    public String toString() {
        return "CartVO{" +
                "cartProductVOS=" + cartProductVOS +
                ", isallchecked=" + isallchecked +
                ", cartprice=" + cartprice +
                '}';
    }
}
