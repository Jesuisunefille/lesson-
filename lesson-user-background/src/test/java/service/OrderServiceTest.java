package service;

import com.cindy.UserBackgroundApp;
import com.cindy.entity.VideoOrder;
import com.cindy.param.OrderDeleteParam;
import com.cindy.param.OrderInsertParam;
import com.cindy.service.OrderService;
import com.cindy.vo.OrderPageVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testInsert() {
        OrderInsertParam orderInsertParam = new OrderInsertParam();
        orderInsertParam.setVideoIds(new Integer[]{13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31});
        orderInsertParam.setUserId(6);
        orderInsertParam.setTotalFee(6500.00);
        System.out.println(orderService.insert(orderInsertParam) > 0 ? "成功" : "失败");
    }

    @Test
    public void testSelectDetailByUserId() {
        List<VideoOrder> videoOrders = orderService.selectDetailByUserId(1);
        if (videoOrders.isEmpty()) {
            System.out.println("暂无中间表数据");
        } else {
            videoOrders.forEach(videoOrder -> {
                System.out.println("videoOrder: " + videoOrder);
                System.out.println("video: " + videoOrder.getVideo());
                System.out.println("user: " + videoOrder.getUser());
                System.out.println("order: " + videoOrder.getOrder());
            });
        }
    }

    @Test
    public void testDeleteById() {
        OrderDeleteParam orderDeleteParam = new OrderDeleteParam();
        orderDeleteParam.setOrderId(2);
        System.out.println(orderService.deleteById(orderDeleteParam) > 0 ? "成功" : "失败");
    }

    @Test
    public void testPageDetailByUserId() {
        OrderPageVo orderPageVo = orderService.pageDetailByUserId(1,1,5);
        if (orderPageVo.getTotal() <= 0) {
            System.out.println("该用户暂无订单记录");
        } else {
            System.out.println("total: " + orderPageVo.getTotal());
            System.out.println("pageNum: " + orderPageVo.getPageNum());
            System.out.println("pageSize: " + orderPageVo.getPageSize());
            orderPageVo.getVideoOrders().forEach(videoOrder -> {
                System.out.println("videoOrder: " + videoOrder);
                System.out.println("video: " + videoOrder.getVideo());
                System.out.println("user: " + videoOrder.getUser());
                System.out.println("order: " + videoOrder.getOrder());
            });
        }
    }

    @Test
    public void testDeleteByOrderId() {
        OrderDeleteParam orderDeleteParam = new OrderDeleteParam();
        orderDeleteParam.setOrderId(9);
        System.out.println(orderService.deleteByOrderId(orderDeleteParam) > 0 ? "成功" : "失败");
    }
}
