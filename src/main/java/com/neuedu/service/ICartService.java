package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICartService {
    /**
     * 购物车添加商品
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse add(Integer userId,Integer productId,Integer count);

    /**
     * 查看购物车列表
     * @param userId
     * @return
     */
    ServerResponse list(Integer userId);

    /**
     * 更新商品数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse update(Integer userId,Integer productId,Integer count);

    /**
     * 删除1-n商品
     * @param userId
     * @param productIds
     * @return
     */
    ServerResponse delete_product(Integer userId,String productIds);

    /**
     * 更改选中状态
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    ServerResponse select(Integer userId,Integer productId,Integer checked);

    /**
     * 购物车中商品数量
     * @param userId
     * @return
     */
    ServerResponse get_cart_product_count(Integer userId);


}
