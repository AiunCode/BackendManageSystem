package com.aiun.order.service.impl;

import com.aiun.common.constant.OrderStatusEnum;
import com.aiun.common.constant.PaymentTypeEnum;
import com.aiun.common.constant.ProductStatusEnum;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.BigDecimalUtils;
import com.aiun.common.util.DateUtils;
import com.aiun.order.mapper.*;
import com.aiun.order.pojo.*;
import com.aiun.order.vo.OrderProductVO;
import com.aiun.product.pojo.Product;
import com.aiun.order.service.IOrderService;
import com.aiun.order.vo.OrderItemVO;
import com.aiun.order.vo.OrderVO;
import com.aiun.product.feign.ProductFeign;
import com.aiun.shipping.feign.ShippingFeign;
import com.aiun.shipping.pojo.Shipping;
import com.aiun.shipping.vo.ShippingVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 订单服务层实现类
 * @author lenovo
 */
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {
    @Value("${image.localhost}")
    private String mageHost;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductFeign productFeign;
    @Autowired
    private ShippingFeign shippingFeign;

    @Override
    public ServerResponse<OrderVO> createOrder(Integer userId, Integer shippingId) {
        //从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);

        //计算订单的总价
        ServerResponse serverResponse = getCartOrderItem(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        //计算订单总价
        BigDecimal payment = getOrderTotalPrice(orderItemList);
        //生成订单
        Order order = assembleOrder(userId, shippingId, payment);
        if (order == null) {
            return ServerResponse.createByErrorMessage("生成订单错误");
        }

        orderItemList.forEach(e->e.setOrderNo(order.getOrderNo()));

        //mybatis批量插入
        orderItemMapper.batchInsert(orderItemList);
        //生成成功，减少产品的库存
        reduceProductStock(orderItemList);
        //清空购物车
        cleanCart(cartList);

        //返回数据给前端
        OrderVO orderVo = assembleOrderVo(order, orderItemList);

        return ServerResponse.createBySuccess(orderVo);
    }

    @Override
    public ServerResponse<String> cancel(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该用户没有此订单");
        }
        if (order.getStatus() != OrderStatusEnum.NO_PAY.getCode()) {
            return ServerResponse.createByErrorMessage("订单已付款，无法取消！");
        }

        Order updateOrder = new Order();
        updateOrder.setId(order.getId())
                   .setStatus(OrderStatusEnum.CANCELED.getCode());

        int rowCount = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse getOrderCartProduct(Integer userId) {
        OrderProductVO orderProductVo = new OrderProductVO();
        //从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        ServerResponse serverResponse = getCartOrderItem(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0");

        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtils.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVOList.add(assembleOrderItemVo(orderItem));
        }

        //TODO Lambda 表达式引用外部变量问题
//        orderItemList.forEach(e->{
//            payment = BigDecimalUtil.add(payment.doubleValue(), e.getTotalPrice().doubleValue());
//            orderItemVoList.add(assembleOrderItemVo(e));
//        });

        orderProductVo.setProductTotalPrice(payment)
                      .setOrderItemVOList(orderItemVOList)
                      .setImageHost(mageHost);

        return ServerResponse.createBySuccess(orderProductVo);
    }

    @Override
    public ServerResponse<OrderVO> getOrderDetail(Integer userId, Long orderNo) {
        Order order;
        // userId == null 表示管理员
        if (userId == null) {
            order = orderMapper.selectByOrderNo(orderNo);
        } else {
            order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        }
        List<OrderItem> orderItemList;
        if (order != null) {
            orderItemList = (userId == null ? orderItemMapper.getByOrderNo(orderNo) : orderItemMapper.getByOrderNoAndUserId(orderNo, userId));
            OrderVO orderVo = assembleOrderVo(order, orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }

        return ServerResponse.createByErrorMessage("没有找到该订单");
    }

    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OrderVO> orderVOList;
        List<Order> orderList;
        // 管理员
        if (userId == null) {
            orderList = orderMapper.selectAllOrder();
            orderVOList = assembleOrderVoList(orderList, userId);
        } else { // 普通用户
            orderList = orderMapper.selectByUserId(userId);
            orderVOList = assembleOrderVoList(orderList, userId);
        }
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVOList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<String> manageSendGoods(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            if (order.getStatus() == OrderStatusEnum.PAID.getCode()) {
                order.setStatus(OrderStatusEnum.SHIPPED.getCode())
                     .setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerResponse.createBySuccessMessage("发货成功");
            }
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVO orderVo = assembleOrderVo(order, orderItemList);

            PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
            pageInfo.setList(Lists.newArrayList(orderVo));
            return ServerResponse.createBySuccess(pageInfo);
        }

        return ServerResponse.createByErrorMessage("订单不存在");
    }

    /**
     * 购物车内数据的封装
     * @param userId 用户id
     * @param cartList 该用户的购物车列表
     * @return 操作结果
     */
    private ServerResponse getCartOrderItem(Integer userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)) {
            ServerResponse.createByErrorMessage("购物车为空");
        }
        //校验购物车的数据，包括产品的状态和数量
        for(Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productFeign.findById(cartItem.getProductId());
            if (ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在线售卖状态");
            }

            //校验库存
            if (cartItem.getQuantity() > product.getStock()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存数量不足");
            }

            orderItem.setUserId(userId)
                     .setProductId(product.getId())
                     .setProductImage(product.getMainImage())
                     .setProductName(product.getName())
                     .setCurrentUnitPrice(product.getPrice())
                     .setQuantity(cartItem.getQuantity())
                     .setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), cartItem.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }
        //TODO 用 Lambda 表达式

        return ServerResponse.createBySuccess(orderItemList);
    }

    /**
     * 计算购物车内购买商品总价
     * @param orderItemList 用户所有购买的商品
     * @return 支付金额
     */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtils.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    /**
     * 生成订单
     * @param userId 用户id
     * @param shippingId 地址id
     * @param payment 支付金额
     * @return 订单
     */
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();
        long orderNo = generateOrderNo();
        order.setOrderNo(orderNo)
             .setStatus(OrderStatusEnum.NO_PAY.getCode())
             .setPostage(0)
             .setPaymentType(PaymentTypeEnum.ONLINE_PAY.getCode())
             .setPayment(payment)
             .setUserId(userId)
             .setShippingId(shippingId);
        //发货时间，付款时间，后面会填充
        int rowCount = orderMapper.insert(order);
        if (rowCount > 0) {
            return order;
        }
        return null;
    }

    /**
     * 生成订单号
     * @return 订单号
     */
    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        //并发量高的会造成订单重复
        return currentTime + new Random().nextInt(100);
    }

    /**
     * 减少商品库存
     * @param orderItemList 订单详情列表
     */
    private void reduceProductStock(List<OrderItem> orderItemList) {
        orderItemList.forEach(e->{
            Product product = productFeign.findById(e.getProductId());
            productFeign.updateStockById(e.getProductId(), product.getStock() - e.getQuantity());
        });
    }

    /**
     * 清空购物车
     * @param cartList 购物车列表
     */
    private void cleanCart(List<Cart> cartList) {
        cartList.forEach(e->{
            cartMapper.deleteByPrimaryKey(e.getId());
        });
    }

    /**
     * 封装订单数据
     * @param order 订单
     * @param orderItemList 订单详情列表
     * @return 封装的订单对象
     */
    private OrderVO assembleOrderVo(Order order, List<OrderItem> orderItemList) {
        OrderVO orderVo = new OrderVO();
        orderVo.setOrderNo(order.getOrderNo())
               .setPayment(order.getPayment())
               .setPaymentType(order.getPaymentType())
               .setPaymentTypeDesc(PaymentTypeEnum.codeOf(order.getPaymentType()).getValue())
               .setPostage(order.getPostage())
               .setStatus(order.getStatus())
               .setStatusDesc(OrderStatusEnum.codeOf(order.getStatus()).getValue())
               .setShippingId(order.getShippingId());
        Shipping shipping = shippingFeign.findById(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName())
                   .setShippingVo(assembleShippingVo(shipping));
        }
        orderVo.setPaymentTime(DateUtils.dateToString(order.getPaymentTime()))
               .setSendTime(DateUtils.dateToString(order.getSendTime()))
               .setCreateTime(DateUtils.dateToString(order.getCreateTime()))
               .setCloseTime(DateUtils.dateToString(order.getCloseTime()))
               .setEndTime(DateUtils.dateToString(order.getEndTime()))
               .setImageHost(mageHost);

        List<OrderItemVO> orderItemVOList = Lists.newArrayList();

        orderItemList.forEach(e->{
            OrderItemVO orderItemVo = assembleOrderItemVo(e);
            orderItemVOList.add(orderItemVo);
        });

        orderVo.setOrderItemVOList(orderItemVOList);
        return orderVo;
    }

    /**
     * 给前端返回的地址数据
     * @param shipping 地址实体类
     * @return 地址信息封装 VO
     */
    private ShippingVo assembleShippingVo(Shipping shipping) {
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverAddress(shipping.getReceiverAddress())
                  .setReceiverCity(shipping.getReceiverCity())
                  .setReceiverDistrict(shipping.getReceiverDistrict())
                  .setReceiverMobile(shipping.getReceiverMobile())
                  .setReceiverName(shipping.getReceiverName())
                  .setReceiverProvince(shipping.getReceiverProvince())
                  .setReceiverPhone(shipping.getReceiverPhone())
                  .setReceiverZip(shipping.getReceiverZip());

        return shippingVo;
    }

    /**
     * 封装订单详情前端数据
     * @param orderItem 订单详情
     * @return 订单详情信息封装 VO
     */
    private OrderItemVO assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVO orderItemVo = new OrderItemVO();
        orderItemVo.setOrderNo(orderItem.getOrderNo())
                   .setProductId(orderItem.getProductId())
                   .setProductName(orderItem.getProductName())
                   .setProductImage(orderItem.getProductImage())
                   .setTotalPrice(orderItem.getTotalPrice())
                   .setCurrentUnitPrice(orderItem.getCurrentUnitPrice())
                   .setQuantity(orderItem.getQuantity())
                   .setCreateTime(DateUtils.dateToString(orderItem.getCreateTime()));
        return orderItemVo;
    }

    /**
     * 封装订单 vo 信息
     * @param orderList 订单列表
     * @param userId 用户 id
     * @return 订单展示列表
     */
    private List<OrderVO> assembleOrderVoList(List<Order> orderList, Integer userId) {
        List<OrderVO> orderVOList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = Lists.newArrayList();
            if (userId == null) {
                //管理员不需要userId
                orderItemList = orderItemMapper.getByOrderNo(order.getOrderNo());
            } else {
                orderItemList = orderItemMapper.getByOrderNoAndUserId(order.getOrderNo(), userId);
            }
            OrderVO orderVo = assembleOrderVo(order, orderItemList);
            orderVOList.add(orderVo);
        }

        //TODO 用 Lambda 表达式

        return orderVOList;
    }
}
