package mapper;

import com.cindy.UserBackgroundApp;
import com.cindy.entity.Video;
import com.cindy.mapper.VideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class VideoMapperTest {

    @Autowired
    private VideoMapper videoMapper;

    @Test
    public void testList() {
        videoMapper.list().forEach(video -> log.info("video: {}", video));
    }

    @Test
    public void testSelectById() {
        log.info("video: {}", videoMapper.selectById(1));
    }

    @Test
    public void testSelectDetailById() {
        Video video = videoMapper.selectDetailById(1);
        log.info("video: {}", video);
        video.getChapters().forEach(chapter -> {
            log.info("chapter: {}", chapter);
            chapter.getEpisodes().forEach(episode -> log.info("episode: {}", episode));
        });
    }

    @Test
    public void selectLikeTitle() {
        String title = "框架";
        videoMapper.selectLikeTitle(title).forEach(System.out::println);
    }
}