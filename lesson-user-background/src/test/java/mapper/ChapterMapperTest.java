package mapper;

import com.cindy.UserBackgroundApp;
import com.cindy.mapper.ChapterMapper;
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
public class ChapterMapperTest {

    @Autowired
    private ChapterMapper chapterMapper;

    @Test
    public void testSelectDetailByVideoId() {
        chapterMapper.selectDetailByVideoId(1).forEach(chapter -> {
            System.out.println("chapter: " + chapter);
            chapter.getEpisodes().forEach(episode -> System.out.println(("\tepisode: " + episode)));
        });
    }
}