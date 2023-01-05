package server;

import com.cindy.UserBackgroundApp;
import com.cindy.server.PointsServer;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class PointsServerTest {

    @Autowired
    private PointsServer pointsServer;

    @SneakyThrows
    @Test
    public void testIncrPoints() {
        Future<String> future = pointsServer.incrByPoints(1, 6000);
        while (true) {
            if (future.isDone()) {
                System.out.println(future.get());
                break;
            }
        }
    }
}
