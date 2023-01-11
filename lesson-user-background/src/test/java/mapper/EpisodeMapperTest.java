package mapper;

import com.cindy.UserBackgroundApp;
import com.cindy.mapper.EpisodeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class EpisodeMapperTest {

    @Autowired
    private EpisodeMapper episodeMapper;

    @Test
    public void testSelectByChapterId() {
        episodeMapper.selectByChapterId(1).forEach(System.out::println);
    }

    @Test
    public void testSelectFirstByVideoId() {
        System.out.println("1号视频的第一集: " + episodeMapper.selectFirstByVideoId(1));
        System.out.println("2号视频的第一集: " + episodeMapper.selectFirstByVideoId(2));
    }
}