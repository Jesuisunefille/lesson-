package service;

import com.cindy.UserBackgroundApp;
import com.cindy.param.VideoOrderDeleteParam;
import com.cindy.service.VideoOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Cindy
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class VideoOrderServiceTest {

    @Autowired
    private VideoOrderService videoOrderService;

    @Test
    public void testDeleteByVideoOrderId() {
        VideoOrderDeleteParam videoOrderDeleteParam = new VideoOrderDeleteParam();
        videoOrderDeleteParam.setVideoOrderId(1);
        videoOrderDeleteParam.setOrderId(1);
        System.out.println(videoOrderService.deleteByVideoOrderId(videoOrderDeleteParam));
    }
}
