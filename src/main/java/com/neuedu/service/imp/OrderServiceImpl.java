package com.neuedu.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.bean.*;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.*;
import com.neuedu.service.IOrderService;
import com.neuedu.util.BigDecimalUtils;
import com.neuedu.util.DateUtils;
import com.neuedu.util.PropertiesUtils;
import com.neuedu.vo.CartOrderItemVo;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import com.neuedu.vo.ShippingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ShippingMapper shippingMapper;
    /**
     * 创建订单
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //非空判断
        if (shippingId==null){
            return ServerResponse.createServerResponseByError("收货地址参数有误");
        }
        //查询购物车已选中的商品 List<Cart>
        List<Cart> cartList = cartMapper.findCartListByUserIdAndChecked(userId);
        //List<Cart> --> List<OrderItem>
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if (!serverResponse.isSuccess()){
            return  serverResponse;
        }
        //创建订单order并保存到数据库
        //计算总价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        //取出serverResponse 中存在的订单明细
        List<OrderItem> orderItems= (List<OrderItem>)serverResponse.getData();

        if (orderItems==null||orderItems.size()==0){
            return ServerResponse.createServerResponseByError("购物车为空");
        }
        //订单总价
        orderTotalPrice= getOrderTotalPrice(orderItems);
        //创建订单Order并添加到数据库
        Order order= create(userId,shippingId,orderTotalPrice);
        //将List<OrderItem> 保存
        if (order==null){
            return ServerResponse.createServerResponseByError("订单创建失败");
        }
        for (OrderItem orderItem:orderItems) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        //批量插入
         orderItemMapper.insertBatch(orderItems);
        //扣库存
        reduceProductStock(orderItems);
        //清空购物车已下单的商品
        cleanCart(cartList);
        //返回OrderVO
        OrderVO orderVO = createOrderVO(order,orderItems,shippingId);
        return ServerResponse.createServerResponseBySuccess("OrderVO",orderVO);
    }

    /**
     * 取消订单
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {
        //参数非空校验
        if (orderNo==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //根据userId和orderNo查询订单
        Order order = orderMapper.findOrderByUserIdAndOrderNo(userId,orderNo);
        if (order==null){
            return ServerResponse.createServerResponseByError("订单不存在");
        }
        //判断订单状态
        if (order.getStatus()!=Const.OrderStatusEnum.ORDER_UN_PAY.getCode()){
            return ServerResponse.createServerResponseByError("订单不可取消");
        }
        order.setStatus(Const.OrderStatusEnum.ORDER_CANCELED.getCode());
        int result = orderMapper.updateByPrimaryKey(order);
        if (result>0){
            return ServerResponse.createServerResponseBySuccess("取消成功");
        }
        //返回结果
        return ServerResponse.createServerResponseByError("订单取消失败");
    }

    /**
     * 查询订单明细
     * @param userId
     * @return
     */
    @Override
    public ServerResponse get_order_cart_product(Integer userId) {
        //查询购物车
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        //List<Cart> -->List<OrderItem>
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        //组装VO
        CartOrderItemVo cartOrderItemVo = new CartOrderItemVo();
        cartOrderItemVo.setImageHost(PropertiesUtils.readByKey("imageHost"));
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        if (orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByError("购物车为空");
        }
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        for (OrderItem orderItem:orderItemList) {
            orderItemVOList.add( assembleOrderItemVO(orderItem));
        }
        cartOrderItemVo.setOrderItemVOList(orderItemVOList);

        cartOrderItemVo.setTotalPrice(getOrderTotalPrice(orderItemList));
        //返回结果
        return ServerResponse.createServerResponseBySuccess("查询购物车订单明细",cartOrderItemVo);
    }

    /**
     * 订单列表
     * @param userId
     * @return
     */
    @Override
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = Lists.newArrayList();
        if (userId==null){
            //查询所有
            orderList=orderMapper.selectAll();
        }else{
            //查询当前用户的
            orderList = orderMapper.findOrderByUserId(userId);
        }
        if (orderList==null||orderList.size()==0){
            return ServerResponse.createServerResponseByError("未查询到订单");
        }
        List<OrderVO> orderVOList = Lists.newArrayList();
        for (Order order:orderList) {
           List<OrderItem> orderItemList = orderItemMapper.findByOrderNo(order.getOrderNo());
           OrderVO orderVO=createOrderVO(order,orderItemList,order.getShippingId());
            orderVOList.add(orderVO);
        }
        PageInfo pageInfo  = new PageInfo(orderVOList);
        return ServerResponse.createServerResponseBySuccess("订单列表",pageInfo);
    }

    /**
     * 订单详情
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse detail(Long orderNo) {
        //参数非空校验
        if (orderNo==null){
            return ServerResponse.createServerResponseByError("订单不存在");
        }
        //查询订单
        Order order = orderMapper.findOrderByOrderNo(orderNo);
        if (order==null){
            return ServerResponse.createServerResponseByError("订单不存在");
        }
        //获取orderVo
        List<OrderItem> orderItemList = orderItemMapper.findByOrderNo(orderNo);
        OrderVO orderVO = createOrderVO(order,orderItemList,order.getShippingId());
        //返回结果
        return ServerResponse.createServerResponseBySuccess("订单详情",orderVO);
    }


    /**
     * 构建OrderVO
     * @param order
     * @param orderItemList
     * @param shippingId
     * @return
     */
    private OrderVO createOrderVO(Order order,List<OrderItem> orderItemList,Integer shippingId){
           OrderVO orderVo = new OrderVO();
           List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        //遍历订单项并转换为VO对象
        for (OrderItem orderItem:orderItemList) {
               OrderItemVO orderItemVO = assembleOrderItemVO(orderItem);
               orderItemVOList.add(orderItemVO);
        }
        //将订单明细前端对象传到订单前端对象中

        orderVo.setOrderItemVOList(orderItemVOList);
        orderVo.setImageHost(PropertiesUtils.readByKey("imageHost"));

        //与地址相关的赋值
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping!=null){
            orderVo.setShippingId(shippingId);
            //获取shippingvo对象
            ShippingVO shippingVO = assembleShippingVo(shipping);
            orderVo.setShippingVO(shippingVO);
            orderVo.setReceicerName(shipping.getReceiverName());
        }
        //设置状态以及状态详情
        //涉及到枚举遍历
        orderVo.setStatus(order.getStatus());
        Const.OrderStatusEnum orderStatusEnum= Const.OrderStatusEnum.codeOf(order.getStatus());
        if (orderStatusEnum!=null){
            orderVo.setStatusDesc(orderStatusEnum.getDesc());
        }
        //设置支付状态以及状态详情
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        Const.PayTypeEnum payTypeEnum = Const.PayTypeEnum.codeOf(order.getPaymentType());
        if (payTypeEnum!=null){
            orderVo.setGetPaymentTypeDesc(payTypeEnum.getDesc());
        }
        orderVo.setPostage(order.getPostage());
        orderVo.setOrderNo(order.getOrderNo());
        return orderVo;
    }

    /**
     * 获取shippingVo
     * @param shipping
     * @return
     */
    private ShippingVO assembleShippingVo(Shipping shipping){
        ShippingVO shippingVO = new ShippingVO();
        if (shipping!=null){
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
        return shippingVO;
    }
    /**
     * 生成OrderItemVO
      * @param orderItem
     * @return
     */
    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        if (orderItem!=null){
            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateToString(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setOrderNO(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        }
        return orderItemVO;
    }

    /**
     *清空购物车已选中的商品
     */
    private void cleanCart(List<Cart> cartList){
        if (cartList!=null&&cartList.size()>0){
            cartMapper.deleteBatch(cartList);
        }
    }

    /**
     * 扣库存
      */
    private void reduceProductStock(List<OrderItem> orderItemList){
        if (orderItemList!=null||orderItemList.size()>0){
            //遍历商品明细
            for (OrderItem orderItem:orderItemList) {
                    //获取每种商品的id以及购买数量
                    Integer productId = orderItem.getProductId();
                    Integer quantity = orderItem.getQuantity();
                    //先查找,在用库存减去购买数量来更新库存数据
                    Product product = productMapper.selectByPrimaryKey(productId);
                    product.setStock(product.getStock()-quantity);
                    productMapper.updateByPrimaryKey(product);
            }
        }
    }
    /**
     * 生成订单
     * @param userId
     * @param shippingId
     * @return
     */
    private Order create(Integer userId, Integer shippingId, BigDecimal orderTotalPrice){
        Order order = new Order();
        order.setOrderNo(createOrderNo());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setStatus(Const.OrderStatusEnum.ORDER_UN_PAY.getCode());
        order.setPayment(orderTotalPrice);
        order.setPaymentType(Const.PayTypeEnum.ONLINE.getCode());
        order.setPostage(0);
        int result= orderMapper.insert(order);
        if (result>0){
            return order;
        }
        return null;
    }
    /**
     * 生成订单号
     * @return
     */
    private Long createOrderNo(){

        return System.currentTimeMillis()+new Random().nextInt(100);
    }
    /**
     * 获取购物车订单项
     * @param userId
     * @param cartList
     * @return
     */
    private ServerResponse getCartOrderItem( Integer userId,List<Cart> cartList){
        if (cartList==null||cartList.size()==0){
            return ServerResponse.createServerResponseByError("购物车为空");
        }
        List<OrderItem> orderItemList = Lists.newArrayList();
        for (Cart cart:cartList) {
            OrderItem orderItem  = new OrderItem();
            orderItem.setUserId(userId);
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product==null){
                return ServerResponse.createServerResponseByError("商品id为"+cart.getProductId()+"不存在");
            }
            if (product.getStatus()!=Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
                return ServerResponse.createServerResponseByError("商品id为"+product.getId()+"已下架");
            }
            if (product.getStock()<cart.getQuantity()){
                return  ServerResponse.createServerResponseByError("商品id为"+product.getId()+"库存不足");
            }
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createServerResponseBySuccess("购物车清单明细",orderItemList);
    }

    /**
     * 计算总价格
     * @param orderItemList
     * @return
     */
    private  BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal bigDecimal = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
         bigDecimal= BigDecimalUtils.add(orderItem.getTotalPrice().doubleValue(),orderItem.getQuantity().doubleValue());
        }
        return bigDecimal;
    }
}
