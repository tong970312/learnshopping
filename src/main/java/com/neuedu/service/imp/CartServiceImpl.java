package com.neuedu.service.imp;

import com.google.common.collect.Lists;
import com.neuedu.bean.Cart;
import com.neuedu.bean.Product;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.service.ICartService;
import com.neuedu.util.BigDecimalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //参数校验
        if (productId==null||count==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //根据productId 和userId查询购物信息
       Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
        //如果购物车为空,则执行添加,不为空,则更新商品数量
        if (cart==null){
            //添加
            Cart cart1 = new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }else{
            //更新
            //Cart{id=1, userId=1, productId=1, quantity=2, checked=1, createTime=Sat Feb 16 00:14:20 CST 2019, updateTime=Sat Feb 16 00:14:20 CST 2019}
            Cart cart1 = new Cart();
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.createServerResponseBySuccess("购物车信息",cartVO);
    }

    private CartVO getCartVO(Integer userId){

        CartVO cartVO = new CartVO();
        //根据用户Id查询所有购物车信息-->list<Cart>
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        //转换成购物车前端信息
        List<CartProductVO> cartProductVOS = Lists.newArrayList();
        //初始购物车总价格
        BigDecimal carttotalprice = new BigDecimal("0");
        //list<Cart>-->List<CartProductVo>
        if (cartList!=null&&cartList.size()>0){
            for (Cart cart:cartList){
                    CartProductVO cartProductVO = new CartProductVO();
                    cartProductVO.setId(cart.getId());
                    cartProductVO.setQuantity(cart.getQuantity());
                    cartProductVO.setUserId(userId);
                    cartProductVO.setProductChecked(cart.getChecked());
                //查询商品
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                //商品信息转换成购物车商品信息
                if (product!=null){
                    cartProductVO.setProductId(cart.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductChecked(product.getStatus());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    int stock = product.getStock();
                    int limitCount = 0;
                    //如果库存大于数量
                    if (stock>cart.getQuantity()){
                        limitCount = cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                    }else{
                        limitCount=stock;
                        //更新购物车中商品的数量
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setQuantity(stock);
                        cart1.setProductId(cart.getId());
                        cart1.setChecked(cart.getChecked());
                        cart1.setUserId(cart.getUserId());
                        cartMapper.updateByPrimaryKey(cart1);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FALL");
                    }
                    cartProductVO.setQuantity(limitCount);
                    //计算某个商品总价
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cartProductVO.getQuantity().doubleValue()));
                }
                cartProductVOS.add(cartProductVO);
                //计算总价格  总价格  不要写成商品价格
                carttotalprice =  BigDecimalUtils.add(carttotalprice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());


            }
        }
        //计算总价
         cartVO.setCartprice(carttotalprice);
        cartVO.setCartProductVOS(cartProductVOS);
        //判断是否全选
        int count = cartMapper.isCheckedAll(userId);
        if (count>0){
            cartVO.setIsallchecked(false);
        }else{
            cartVO.setIsallchecked(true);
        }

        return cartVO;
    }
}
