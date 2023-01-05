package service;

import com.cindy.UserBackgroundApp;
import com.cindy.service.TestService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cindy
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class TestServiceTest {

    @Autowired
    private TestService testService;

    @Test
    public void testAop() {
        System.out.println(testService.testAop(""));
//        System.out.println(testService.testAop(null));
    }

    @SneakyThrows
    @Test
    public void testGuava() {
        System.out.println("ID: " + testService.testGuava());
        System.out.println("ID: " + testService.testGuava());
        System.out.println("ID: " + testService.testGuava());
    }

    //测试分页
    @Test
    public void testPage() {
        System.out.println(testService.testPage(2, 3));
    }

}
