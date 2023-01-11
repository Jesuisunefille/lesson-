package service;

import com.cindy.UserBackgroundApp;
import com.cindy.service.BannerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class BannerServiceTest {

    @Autowired
    private BannerService bannerService;

    @Test
    public void testList() {
        System.out.println(bannerService.list());
        System.out.println(bannerService.list());
        System.out.println(bannerService.list());
        System.out.println(bannerService.list());
    }
}
