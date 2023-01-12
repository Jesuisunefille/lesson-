package com.cindy.service.impl;

import com.cindy.entity.VideoOrder;
import com.cindy.mapper.OrderMapper;
import com.cindy.mapper.UserMapper;
import com.cindy.mapper.VideoOrderMapper;
import com.cindy.param.VideoOrderDeleteParam;
import com.cindy.service.VideoOrderService;
import com.cindy.util.NullUtil;
import com.cindy.vo.OrderPageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Cindy
 */
@Slf4j
@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Override
    public OrderPageVo pageDetailByUserId(Integer userId, Integer page, Integer size) {

        if (null == userMapper.selectById(userId)) {
            throw new RuntimeException("用户不存在");
        }

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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteByVideoOrderId(VideoOrderDeleteParam videoOrderDeleteParam) {

        Integer videoOrderId = videoOrderDeleteParam.getVideoOrderId();
        Integer orderId = videoOrderDeleteParam.getOrderId();

        // 检查必要参数
        if(NullUtil.hasNull(videoOrderId, orderId)){
            throw new RuntimeException("必要参数为空");
        }

        // 按 [视频订单中间表] 主键删除记录，删除失败则抛出异常
        if (videoOrderMapper.deleteByVideoOrderId(videoOrderId) <= 0) {
            throw new RuntimeException("中间表删除失败");
        }

        // 通过 [订单表] 主键查询 [视频订单中间表] 记录
        List<VideoOrder> videoOrders = videoOrderMapper.selectByOrderId(orderId);

        // 若该订单下已经不存在任何 [视频订单中间表] 记录，则删除该订单
        if (videoOrders.isEmpty()) {
            if (orderMapper.deleteByOrderId(orderId) <= 0) {
                throw new RuntimeException("订单表删除失败");
            }
        }

        return 1;
    }
}
