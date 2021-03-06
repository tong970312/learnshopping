package com.neuedu.dao;

import com.neuedu.bean.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shipping
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shipping
     *
     * @mbg.generated
     */
    int insert(Shipping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shipping
     *
     * @mbg.generated
     */
    Shipping selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shipping
     *
     * @mbg.generated
     */
    List<Shipping> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shipping
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Shipping record);

    /**
     * 删除地址
     * @param userId
     * @param shippingId
     * @return
     */
    int deleteByUserIdAndShippingId(@Param("userId") Integer userId,
                                    @Param("shippingId") Integer shippingId);

    /**
     * 更新地址
     * @param shipping
     * @return
     */
    int updateBySelectiveKey(Shipping shipping);
}