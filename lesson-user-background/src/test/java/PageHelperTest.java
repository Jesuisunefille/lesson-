import com.cindy.UserBackgroundApp;
import com.cindy.entity.Video;
import com.cindy.mapper.VideoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class PageHelperTest {

    @Autowired
    private VideoMapper videoMapper;

    @Test
    public void testPageHelper() {
        PageHelper.startPage(2, 4);
        List<Video> list = videoMapper.list();
        System.out.println(new PageInfo<>(list));
    }
}
