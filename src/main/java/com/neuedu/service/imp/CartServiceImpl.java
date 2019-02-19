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

    /**
     * 添加购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //参数校验
        if (productId==null||count==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("要添加的商品不存在");
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

    /**
     * 查询购物车信息
     * @param userId
     * @return
     */
    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO =  getCartVO(userId);
        return ServerResponse.createServerResponseBySuccess("购物车列表",cartVO);
    }

    /**
     * 更新商品数量
      * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        //非空判断
        if (productId==null&&count==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //查询商品
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart!=null){
            //更新数量
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        //返回结果
        return ServerResponse.createServerResponseBySuccess("更新成功",getCartVO(userId));
    }

     /**
     * 删除商品
     * @param userId
     * @param productIds
     * @return
     */
    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        //非空校验
        if (productIds==null||productIds.equals("")){
            return  ServerResponse.createServerResponseByError("参数不能为空");
        }
        //productIds-->List<Integer>
        //英文逗号
        String[] productIdsArr=productIds.split(",");
        List<Integer> productIdList =new ArrayList<>();
        if (productIdsArr!=null&&productIdsArr.length>0){
            for (String productIdStr:productIdsArr) {
                Integer productId = Integer.parseInt(productIdStr);
                productIdList.add(productId);
            }
        }
        //调用dao
        cartMapper.deleteByUserIdAndProductIds(userId,productIdList);
        //返回结果
        return ServerResponse.createServerResponseBySuccess("删除成功",getCartVO(userId));
    }

    /**
     * 选中某个商品
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer checked) {
        //非空校验
        /*if (productId==null){
            return  ServerResponse.createServerResponseByError("参数不能为空");
        }*/
        //dao接口
        cartMapper.selectOrUnSelectProduct(userId,productId,checked);
        //返回结果

        return ServerResponse.createServerResponseBySuccess("选中",getCartVO(userId));
    }

    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        int quantity =cartMapper.get_cart_product_count(userId);
        return ServerResponse.createServerResponseBySuccess("商品数量",quantity);
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
                //被选中的商品总价
                if (cartProductVO.getChecked()==Const.CartCheckEnum.PRODUCT_CHECKED.getCode()) {
                    carttotalprice = BigDecimalUtils.add(carttotalprice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());

                }
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
