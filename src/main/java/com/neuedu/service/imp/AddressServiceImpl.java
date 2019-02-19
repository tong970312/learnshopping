package com.neuedu.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.bean.Shipping;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    ShippingMapper shippingMapper;
    /**
     * 添加地址
     * @param shipping
     * @return
     */
    @Override
    public ServerResponse add(Shipping shipping) {
        //非空校验
        if (shipping==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //添加
        shippingMapper.insert(shipping);
        //返回结果
        Map<String,Integer> map = Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createServerResponseBySuccess("添加地址成功",map);
    }

    /**
     * 删除地址
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {
        //非空校验
        if (shippingId==null) {
          return  ServerResponse.createServerResponseByError("参数错误");
        }
        //删除
        int result = shippingMapper.deleteByUserIdAndShippingId(userId,shippingId);
        //返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySuccess("删除成功");
        }
        return ServerResponse.createServerResponseByError("删除失败");
    }

    /**
     * 修改地址
     * @param shipping
     * @return
     */
    @Override
    public ServerResponse update(Shipping shipping) {
        //非空校验
        if (shipping==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //地址更新
        int result = shippingMapper.updateBySelectiveKey(shipping);
        //返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySuccess("更新成功");
        }
        return ServerResponse.createServerResponseByError("更新失败");
    }

    /**
     * 查看
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse select(Integer shippingId) {
        if (shippingId==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);

        return ServerResponse.createServerResponseBySuccess("地址列表",shipping);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectAll();
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createServerResponseBySuccess("分页查询",pageInfo);
    }


}
