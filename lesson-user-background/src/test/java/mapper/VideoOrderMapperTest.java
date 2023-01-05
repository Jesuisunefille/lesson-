package mapper;

import com.cindy.UserBackgroundApp;
import com.cindy.entity.VideoOrder;
import com.cindy.mapper.VideoOrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class VideoOrderMapperTest {
    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Test
    public void testInsert() {
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setOrderId(1);
        videoOrder.setVideoId(1);
        videoOrder.setUserId(1);
        videoOrder.setInfo("测试描述");
        videoOrder.setCreateTime(new Date(999999999L));
        videoOrder.setLastModify(new Date());

        System.out.println(videoOrderMapper.insert(videoOrder) > 0 ? "成功" : "失败");
    }

    @Test
    public void testDeleteUserById() {
        System.out.println(videoOrderMapper.deleteByUserId(999) > 0 ? "成功" : "失败");
    }

    @Test
    public void testDeleteByOrderId() {
        System.out.println(videoOrderMapper.deleteByOrderId(999) > 0 ? "成功" : "失败");
    }

    @Test
    public void testSelectByUserId() {
        videoOrderMapper.selectByUserId(1).forEach(System.out::println);
    }

    @Test
    public void testSelectDetailById() {
        VideoOrder videoOrder = videoOrderMapper.selectDetailById(7);
        System.out.println("videoOrder: " + videoOrder);
        System.out.println("user: " + videoOrder.getUser());
        System.out.println("video: " + videoOrder.getVideo());
        System.out.println("order: " + videoOrder.getOrder());
    }

    @Test
    public void testSelectDetailByUserId() {
        List<VideoOrder> videoOrders = videoOrderMapper.selectDetailByUserId(1);
        if (videoOrders.isEmpty()) {
            System.out.println("暂无中间表数据");
        } else {
            videoOrders.forEach(System.out::println);
        }
    }
}
