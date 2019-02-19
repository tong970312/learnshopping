package com.neuedu.service;

import com.neuedu.bean.Shipping;
import com.neuedu.common.ServerResponse;

public interface IAddressService {
    /**
     * 添加地址
     * @param shipping
     * @return
     */
    ServerResponse add(Shipping shipping);

    /**
     * 删除地址
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse delete(Integer userId,Integer shippingId);

    /**
     * 更新地址
     * @param shipping
     * @return
     */
    ServerResponse update(Shipping shipping);

    /**
     * 查看
     * @param shippingId
     * @return
     */
    ServerResponse select(Integer shippingId);

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse list(Integer pageNum,Integer pageSize);
}
