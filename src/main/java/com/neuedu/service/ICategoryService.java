package com.neuedu.service;

import com.neuedu.common.ServerResponse;
public interface ICategoryService {
    /**
     *  获取品类子节点(平级)
     * @param categoryId
     * @return
     */
    ServerResponse get_category(Integer categoryId);

    /**
     * 增加节点
     * @param parentId
     * @param categoryName
     * @return
     */
    ServerResponse add_category(Integer parentId, String categoryName);

    /**
     * 修改节点
     * @param categoryId
     * @param categoryName
     * @return
     */
    ServerResponse set_category_name(Integer categoryId,String categoryName);

    /**
     * 获取当前分类id以及递归子节点
     * @param categoryId
     * @return
     */
    ServerResponse get_deep_category(Integer categoryId);
}
