package com.neuedu.service;

import com.neuedu.bean.Product;
import com.neuedu.common.ServerResponse;
import org.springframework.stereotype.Service;

public interface IProductService {
    /**
     * 修改或添加
     * @param product;
     * @return
     */
    ServerResponse saveOrUpdate(Product product);

    /**
     * 设置上下架
     * @param productId;
     * @param status;
     * @return
     */
    ServerResponse set_sale_status(Integer productId,Integer status);

    /**
     * 商品详情
     * @param productId;
     * @return
     */
    ServerResponse detail(Integer productId);

    /**
     * 分页查询商品
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse list(Integer pageNum,Integer pageSize);

    /**
     * 后台搜索商品
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */

    ServerResponse search(Integer productId,String productName,Integer pageNum,Integer pageSize);

}
