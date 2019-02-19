#####测试插件-Restlet Client(Google 应用商店)

####登录
http://localhost:8080/portal/user/login.do?username=李通&password=123456
####购物车
***
查看购物车列表
http://localhost:8080/cart/list.do

更新购物车商品数量
http://localhost:8080/cart/update.do?productId=1&count=15

选中某个商品
http://localhost:8080/cart/select.do?productId=1

取消选中(某个商品)
http://localhost:8080/cart/un_select.do?productId=1

删除1-N个商品
http://localhost:8080/cart/delete_product.do?productIds=3,4

全选
http://localhost:8080/cart/select_all.do

获取购物车商品个数
http://localhost:8080/cart/get_cart_product_count.do
***
####订单
***
添加订单
http://localhost:8080/order/createOrder.do?shippingId=4
***