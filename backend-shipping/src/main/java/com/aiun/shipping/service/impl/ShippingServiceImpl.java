package com.aiun.shipping.service.impl;

import com.aiun.common.ServerResponse;
import com.aiun.shipping.mapper.ShippingMapper;
import com.aiun.shipping.pojo.Shipping;
import com.aiun.shipping.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lenovo
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map resultMap = Maps.newHashMap();
            resultMap.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功！", resultMap);
        }
        return ServerResponse.createByErrorMessage("新建地址失败！");
    }

    @Override
    public ServerResponse<String> del(Integer userId, Integer shippingId) {
        int result = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (result > 0) {
            return ServerResponse.createBySuccess("删除地址成功！");
        }
        return ServerResponse.createByErrorMessage("删除地址失败！");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功！");
        }
        return ServerResponse.createByErrorMessage("更新地址失败！");
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("收货地址未查询到");
        }
        return ServerResponse.createBySuccess("查询地址成功", shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public Shipping findById(Integer id) {
        return shippingMapper.selectByPrimaryKey(id);
    }
}
