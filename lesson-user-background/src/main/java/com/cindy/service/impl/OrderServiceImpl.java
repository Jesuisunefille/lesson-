package com.cindy.service.impl;

import com.cindy.entity.Order;
import com.cindy.entity.User;
import com.cindy.entity.VideoOrder;
import com.cindy.mapper.OrderMapper;
import com.cindy.mapper.UserMapper;
import com.cindy.mapper.VideoMapper;
import com.cindy.mapper.VideoOrderMapper;
import com.cindy.param.OrderDeleteParam;
import com.cindy.param.OrderInsertParam;
import com.cindy.server.SmsServer;
import com.cindy.service.OrderService;
import com.cindy.util.BuildUtil;
import com.cindy.util.NullUtil;
import com.cindy.vo.OrderPageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Cindy
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private SmsServer smsServer;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insert(OrderInsertParam orderInsertParam) {

        Integer[] videoIds = orderInsertParam.getVideoIds();
        Integer userId = orderInsertParam.getUserId();
        Double totalFee = orderInsertParam.getTotalFee();
        String info = orderInsertParam.getInfo();

        // 检查必填参数：若包含null值则直接抛出参数异常
        if (NullUtil.hasNull(videoIds, userId, totalFee)) {
            throw new RuntimeException("必要参数为空");
        }

        // 调用Mapper接口检查下单用户是否存在，不存在直接抛异常，触发回滚
        if (null == userMapper.selectById(userId)) {
            throw new RuntimeException("用户不存在");
        }

        // 根据videoIds检查要购买的视频是否全部存在，有一个不存在就抛出异常，触发回滚
        if (videoMapper.selectByIds(videoIds).size() < videoIds.length) {
            throw new RuntimeException("至少有一个视频不存在");
        }

        // 检查用户是否重复购买视频，重复购买直接抛异常
        if (!videoOrderMapper.selectByUserIdAndVideoIds(userId, videoIds).isEmpty()) {
            throw new RuntimeException("不允许重复购买视频");
        }

        // 组装Order实体
        Order order = new Order();
        order.setNumber(BuildUtil.buildUuid());
        order.setState(1);
        order.setTotalFee(totalFee);
        order.setInfo(StringUtils.isBlank(info) ? "该订单暂无描述信息" : info);
        order.setCreateTime(new Date());
        order.setLastModify(new Date());

        // 调用数据接口添加Order表记录，若添加失败则抛出异常触发回滚
        if (orderMapper.insert(order) <= 0) {
            throw new RuntimeException("Order表添加异常");
        }

        // 调用Mapper接口添加VideoOrder记录
        this.insertVideoOrder(userId, videoIds, order);

        //this.sendSmsAfterBuying(user.getPhone());
        return 1;
    }

    @Override
    public List<VideoOrder> selectDetailByUserId(Integer userId) {
        this.checkUserIsExists(userId);
        return videoOrderMapper.selectDetailByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteById(OrderDeleteParam orderDeleteParam) {
        int orderId = orderDeleteParam.getOrderId();
        this.checkOrder(orderId);
        this.deleteVideoOrder(orderId);
        this.deleteOrder(orderId);
        return 1;
    }

    /**
     * 检查用户是否存在，不存在直接抛出异常
     *
     * @param userId 用户表实体
     * @return 若存在，则返回该条User记录
     */
    private User checkUserIsExists(Integer userId) {
        User user = userMapper.selectById(userId);
        if (null == user) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    /**
     * 调用数据接口按主键查询订单记录，若订单不存在则抛异常
     *
     * @param orderId Order表主键
     */
    private void checkOrder(Integer orderId) {
        if (null == orderMapper.selectById(orderId)) {
            throw new RuntimeException("订单不存在");
        }
    }

    /**
     * 向VideoOrder表添加视频订单中间表记录，若添加失败则抛出异常
     *
     * @param userId   User表主键
     * @param videoIds Video表主键数组
     * @param order    Order实体
     */
    private void insertVideoOrder(Integer userId, Integer[] videoIds, Order order) {

        // 获取Order表主键
        Integer orderId = order.getId();

        if (null == orderId) {
            throw new RuntimeException("Order表主键为空：可能是回注异常");
        }

        // 循环添加VideoOrder中间表数据
        for (Integer videoId : videoIds) {

            // 准备VideoOrder数据
            VideoOrder videoOrder = new VideoOrder();
            videoOrder.setVideoId(videoId);
            videoOrder.setOrderId(orderId);
            videoOrder.setUserId(userId);
            videoOrder.setInfo(userId + "号用户在" + orderId + "号订单中购买了" + videoId + "号视频");
            videoOrder.setCreateTime(new Date());
            videoOrder.setLastModify(new Date());

            // 调用数据接口循环添加VideoOrder表记录，若添加失败则抛出异常触发回滚
            if (videoOrderMapper.insert(videoOrder) <= 0) {
                throw new RuntimeException("VideoOrder表添加异常");
            }
        }
    }

    /**
     * 下单成功后向用户发送短信通知
     */
    @SneakyThrows
    private void sendSmsAfterBuying(String phone) {
        if (!NullUtil.hasBlank(phone)) {
            Future<String> future = smsServer.sendOrderSms(phone);
            while (true) {
                if (future.isDone()) {
                    log.info(future.get());
                    break;
                }
            }
        }
    }

    /**
     * 调用数据接口删除该订单的Order记录
     *
     * @param orderId Order表主键
     */
    private void deleteOrder(Integer orderId) {
        if (orderMapper.deleteById(orderId) <= 0) {
            throw new RuntimeException("Order删除失败");
        }
    }

    /**
     * 调用数据接口删除该订单的所有VideoOrder表记录
     *
     * @param orderId Order表主键
     */
    private void deleteVideoOrder(Integer orderId) {
        if (videoOrderMapper.deleteByOrderId(orderId) <= 0) {
            throw new RuntimeException("VideoOrder删除失败");
        }
    }

    @Override
    public OrderPageVo pageDetailByUserId(Integer userId, Integer page, Integer size){

        this.checkUserExists(userId);
        // 分页查询VideoOrder记录
        PageHelper.startPage(page, size);
        PageInfo<VideoOrder> videoPageInfo = new PageInfo<>(videoOrderMapper.selectDetailByUserId(userId));

        // 组装VO
        OrderPageVo orderPageVo = new OrderPageVo();
        orderPageVo.setTotal(videoPageInfo.getTotal());
        orderPageVo.setPageNum(videoPageInfo.getPageNum());
        orderPageVo.setPageSize(videoPageInfo.getPageSize());
        orderPageVo.setPages(videoPageInfo.getPages());
        orderPageVo.setVideoOrders(videoPageInfo.getList());

        // 返回VO
        return orderPageVo;
    }


    /**
     * 检查用户是否存在，不存在直接抛出异常
     *
     * @param userId User主键
     */
    private void checkUserExists(Integer userId) {
        if (null == userMapper.selectByUserId(userId)) {
            throw new RuntimeException("用户不存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteByOrderId(OrderDeleteParam orderDeleteParam) {
        int orderId = orderDeleteParam.getOrderId();
        this.checkOrderExists(orderId);
        this.deleteVideoOrder(orderId);
        this.deleteOrder(orderId);
        return 1;
    }


    /**
     * 调用数据接口按主键查询订单记录，若订单不存在则抛异常
     *
     * @param orderId Order表主键
     */
    private void checkOrderExists(Integer orderId) {
        if (null == orderMapper.selectByOrderId(orderId)) {
            throw new RuntimeException("订单不存在");
        }
    }
}
