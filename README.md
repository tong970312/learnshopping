***
测试：使用插件测试 Restlet Client(Google 应用商店)

    注册
	1.非空校验
		StringUtils.isBlank() 可以忽略空格
		StringUtils.isEmpty() 不可以忽略空格,会认为空格是字符串
		
	2.校验用户名、邮箱
		调用check_valid进行校验
		
	3.注册普通用户
		--1.先通过MD5Utils进行密码加密转换
		--2.然后设置Role为普通用户(1)
		--3.调用insert方法添加到数据库中进行注册
		---------------------------------------------------------------
		(登陆&注册代码重构)
		1.使用MD5进行密码加密（加密存储到数据库）,提高安全性
		2.MD5密码加密不可逆  例如：e10adc3949ba59abbe56e057f20f883e
		---------------------------------------------------------------
	4.返回处理结果
	（中间ServerResponse返回的信息,用枚举和字符串常量来封装完成）

    保存用户信息到session
	1.在login中加入session,当登陆成功（isSuccess方法返回true）,将用户信息存到session
	2.ServerResponse返回的是json,设置session是需要用getData()获取用户信息
    校验用户名和邮箱  
	check_valid()方法(校验用户名、邮箱是否合法(不为空)以及存在)
	type的username和email设置为常量(Const)
	
	1.非空校验：判断用户名、判断邮箱
	2.登录校验：检验用户名是否存在（正常是存在）调用dao层的checkusername进行检查
	3.注册校验：检验用户名是否存在，检验邮箱是否存在（正常是不存在（新注册））
	调用dao层的checkusername和checkemail进行检查
	
	获取用户信息以及详细信息（部分跟整体的区别）
	方法参数列表中加入HttpSession，获取登录时设置的session值，先判断是否为空以及
	是否属于UserInfo实例，满足条件ServerResponse返回数据，不满足则返回用户未登录
	
	用户信息：重新创建一个UserInfo对象通过set设置数据，之后通过ServerResponse返回
	详细信息：直接通过ServerResponse返回
	
 购物车模块
 
	1、购物车添加商品
	Contreller层:用户登录后才可添加商品,传入userId,productId,count
	Service:
		先进行参数非空校验,然后根据传入参数userId,productId查询当前用户在数据库购物车表中的购物车数据,当前用户
		在购物车中是否有当前商品编号的数据
		--例如选择新商品
		购物车为空,则执行insert添加购物车(创建一个购物车对象,并进行赋值,然后添加到数据库)
		--例如修改商品数量
		不为空,则执行更新操作(创建一个购物车对象,进行一一赋值,然后更新数据库购物车表中的对应数据)
		
		创建一个CartVO(购物车的前端对象,传入当前用户的userId,之后通过getCartVO方法返回当前用户的购物车信息cartList)
		getCartVO方法(返回的是CartVO购物车前端对象):
			创建一个CartVO,
			通过传入的userId查询当前用户的购物车数据(List集合存放),然后创建一个 List<CartProductVO>(返回前端商品对象)
			初始化购物车总价格 BigDecimal carttotalprice = new BigDecimal("0");使用BigDecimal字符串形式使运算精确
				判断cartList是否为空以及size>0,然后foreach进行遍历(将cartList中的数据转换成返回前段商品对象cartProductVO)
				(根据要求创建返回的前端对象并生成)
					给id,quantity(数量),userId,checked(当前状态)进行一一赋值
					根据productId查询商品,进行非空判断,给对象属性进行赋值
						stock(库存)和传入的quantity进行比较
						定义limitCount(限定数量) 
						stock>cart.getQuantity()-->限定数量=传入的数量-->返回 LIMIT_NUM_SUCCESS
						
						else 					-->限定数量=库存
												-->创建购物车Cart对象进行数量更新
												-->返回 LIMIT_NUM_FALL
						 cartProductVO.setQuantity(limitCount);设置数量为limitCount(数值取决于上面)
			
			计算购物车中某个商品总价格
			BigDecimalUtils为写好的封装计算工具类 doubleValue()         商品单价*购买个数
			cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cartProductVO.getQuantity().doubleValue()));
			将CartProductVO添加到 List<CartProductVO>中
			
			计算总价格  初始化的总价格(0) + 每个商品总价格
            carttotalprice =  BigDecimalUtils.add(carttotalprice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
			设置总价格以及 List<CartProductVO>
			
			判断是否全选(根据userid来查找数据中checked=0的个数(0为非全选))
			
			返回cartVO对象
			
地址模块

	1、添加地址
		shippingMapper文件中insert语句添加 useGeneratedKeys="true" keyProperty="id"
		作用：接口文档要求返回添加后的主键id, 添加以后可以把生成的主键值赋给shipping对象中的id

	2、分页查询
		 PageHelper.startPage(pageNum,pageSize);
       	 List<Shipping> shippingList = shippingMapper.selectAll();
         PageInfo pageInfo = new PageInfo(shippingList);
	其余略、、、	 
		
订单模块

	1、添加订单 
		 登录成功,获取session并传入shippingId(地址id)
			-->非空判断shippingId ,通过userId查询购物车中已选中的商品--findCartListByUserIdAndChecked(userId) 		
			-->将购物车数据转换为购物车清单明细数据 List<Cart>cartList --> List<OrderItem> orderItems
			自定义getCartOrderItem方法来转换
			ServerResponse serverResponse = getCartOrderItem(userId,cartList);
			-->校验cartList,创建一个List<OrderItem> orderItemList 通过foreach去遍历赋值进行对象的转换
				 再通过productId进行对应商品的查找,
				 对product对象、Status状态码、Stock库存的判断来返回商品是否存在、是否下架、库存是否充足等信息
			然后一一设置OrderItem的数据并添加到orderItemList集合中
			
		  计算订单总价
			自定义方法getOrderTotalPrice(orderItems) 传入之前取出的购物车清单数据(名字换了一个而已) orderItems
			-->初始化的总价格为 0
				BigDecimal bigDecimal = new BigDecimal("0");
			-->foreach遍历orderItems中单项商品的总价格以及购买数量并进行运算
			-->返回总价结果
		
		  创建Order(并非OrderVO)
			自定义方法create(userId,shippingId,orderTotalPrice) 	
			对属性进行赋值并调用dao层的insert将order添加到数据库中
				-->对订单编号的设置
						OrderNo为Long类型,自定义方法createOrderNo()
							-->当前毫秒数 + 随机数
							-->System.currentTimeMillis()+new Random().nextInt(100);
		   
		  (创建OrderVO时需要)foreach遍历对orderItems,将订单号OrderNo(order.getOrderNo())赋值
		   
			将订单中的每一项进行批量插入 orderItemMapper.insertBatch(orderItems)
			-->Mapper文件中,Mybatis的 SQL语句的动态形式
				  insert into order_item (order_no, user_id,
										  product_id, product_name, product_image,
										  current_unit_price, quantity, total_price,
										  create_time, update_time)
				  values
				  <foreach collection="list" item="orderItem" separator=",">
				   (#{orderItem.orderNo,jdbcType=BIGINT}, #{orderItem.userId,jdbcType=INTEGER},
				    #{orderItem.productId,jdbcType=INTEGER}, #{orderItem.productName,jdbcType=VARCHAR}, #{orderItem.productImage,jdbcType=VARCHAR},
				    #{orderItem.currentUnitPrice,jdbcType=DECIMAL}, #{orderItem.quantity,jdbcType=INTEGER}, #{orderItem.totalPrice,jdbcType=DECIMAL},
				    now(),now())
				  </foreach>
				collection：数据形式list    item:orderItem类型  separator：逗号分隔
					orderItem.*** -->进行数据对应
		  
		  扣除库存
			  foreach遍历商品明细, 获取productId以及quantity
			  -->通过productId 查找对应商品,使用set方法重新赋值-->product.getStock()-quantity
				 调用dao层的update更新库存数目

		  清空购物车
			  将购物车数据List<Cart>cartList 传入到自定义方法cleanCart
			  -->批量删除购物车数据  mybatis动态sql语句
				<delete id="cleanCart" parameteType="list">
					delete from cart
					<where>
					 id in  /*foreach进行主键id的遍历*/
					 <foreach>
						#{cart.id}
					 </foreach>
					</where>
				</delete>
				
		*****创建OrderVO -- >传入order(订单)、orderItems(订单明细)、shippingId(地址id)
				由orderItemVoList、shippingVo以及相关的属性组成
				
			OrderVO orderVO = createOrderVO(order,orderItems,shippingId);
				createOrderVO -->创建OrderVO对象以及OrderItemVo集合
					与订单明细orderitem相关
					-->foreach遍历将orderItem转换成orderitemVO并赋值给orderVO(orderItemVoList项)
					与地址相关
					-->Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
						通过主键shippingid查找对应的地址数据
						自定义方法将shipping转换为shippingVO进行属性赋值
						将shippingVO传到orderVO(shippingVo项)
					
				    与订单状态相关
					-->遍历枚举,通过传入的对应状态码返回对应的状态说明
					Const.OrderStatusEnum orderStatusEnum= Const.OrderStatusEnum.codeOf(order.getStatus());
					  /**
						* 遍历枚举
					    * 订单状态描述
						* @param code
						* @return
						* for (OrderStatusEnum orderStatusEnum:values())  注意***
						*/
						public static OrderStatusEnum codeOf(Integer code){
							for (OrderStatusEnum orderStatusEnum:values()) {
								if (code==orderStatusEnum.code){
									return orderStatusEnum;
								}
							}
							return null;
						}
					支付状态同上
				给orderVo其他属性进行对应赋值,最后返回orderVO
				
		测试结果:		
					"status": 0,
					"data":{
					"orderNo": 1550590816492,
					"payment": 4020,
					"paymentType": 1,
					"getPaymentTypeDesc": "线上支付",
					"postage": 0,
					"status": 10,
					"statusDesc": "未付款",
					"paymentTime": null,
					"sendTime": null,
					"endTime": null,
					"closeTime": null,
					"createTime": null,
					"orderItemVOList":[
									{"orderNO": 1550590816492,
									 "productId": 1,
									 "productName": "商品1",
									 "productImage": "主图",
									 "currentUnitPrice": 100,
									 "quantity": 15,
									 "totalPrice": 1500,
									 "createTime": "2019-02-19 23:40:16"},
									{"orderNO": 1550590816492, "productId": 2, "productName": "商品2", "productImage": "主图2",…},
									{"orderNO": 1550590816492, "productId": 5, "productName": "商品5", "productImage": "5",…}
									],
					"imageHost": "Http://localhost:8080/uploadpic",
					"shippingId": 4,
					"shippingVO":{
					"receiverName": "李通",
					"receiverPhone": "06625752719",
					"receiverMobile": "15822869305",
					"receiverProvince": "天津市",
					"receiverCity": "天津市",
					"receiverDistrict": "宝坻区",
					"receiverAddress": "宝坻区xxxx",
					"receiverZip": "100000"
					},
					"receicerName": "李通"
					},
					"msg": "OrderVO"
					}
	
***