package service;

import com.cindy.UserBackgroundApp;
import com.cindy.param.VideoOrderDeleteParam;
import com.cindy.service.VideoOrderService;
import com.cindy.vo.OrderPageVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class VideoOrderServiceTest {

    @Autowired
    private VideoOrderService videoOrderService;

    @Test
    public void testPageDetailByUserId() {
        OrderPageVo orderPageVo = videoOrderService.pageDetailByUserId(1,1,5);
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
    public void testDeleteByVideoOrderId() {
        VideoOrderDeleteParam videoOrderDeleteParam = new VideoOrderDeleteParam();
        videoOrderDeleteParam.setVideoOrderId(8);
        videoOrderDeleteParam.setOrderId(8);
        System.out.println(videoOrderService.deleteByVideoOrderId(videoOrderDeleteParam));
    }
}
