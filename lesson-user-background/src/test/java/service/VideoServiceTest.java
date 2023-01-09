package service;

import com.cindy.UserBackgroundApp;
import com.cindy.document.VideoDoc;
import com.cindy.entity.Video;
import com.cindy.param.VideoPageParam;
import com.cindy.service.VideoService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Test
    public void testSelectDetailById() {
        Video video = videoService.selectDetailById(1);
        System.out.println("video: " + video);
        video.getChapters().forEach(chapter -> {
            System.out.println("\tchapter: " + chapter);
            chapter.getEpisodes().forEach(episode -> System.out.println("\t\tepisode: " + episode));
        });
    }

    @Test
    public void testPage() {
        VideoPageParam videoPageParam = new VideoPageParam();
        videoPageParam.setPage(2);
        videoPageParam.setSize(5);
        PageInfo<Video> pageVideos = videoService.page(videoPageParam);
        System.out.println("当前显示第几页: " + pageVideos.getPageNum());
        System.out.println("每页显示多少条: " + pageVideos.getPageSize());
        System.out.println("一共多少条数据: " + pageVideos.getTotal());
        System.out.println("一共多少页数据: " + pageVideos.getPages());
        System.out.println("导航页码的列表: " + Arrays.toString(pageVideos.getNavigatepageNums()));
        pageVideos.getList().forEach(System.out::println);
    }

    @Test
    public void testSave(){
        VideoDoc videoDoc = new VideoDoc();
        videoDoc.setId(99);
        videoDoc.setTitle("测试标题");
        videoDoc.setAuthor("测试作者");
        videoDoc.setInfo("测试描述");
        videoDoc.setSummaryImage("测试摘要图.jpg");
        videoDoc.setCoverImage("测试封面图.jpg");
        videoDoc.setPrice(777.77);
        videoDoc.setStar(5);
        videoDoc.setCreateTime(new Date());
        videoDoc.setLastModify(new Date(99999999999L));
        System.out.println(videoService.save(videoDoc));
    }

    @Test
    public void testGetById(){
        System.out.println(videoService.getById("99"));
        System.out.println(videoService.getById("999"));
    }

    @Test
    public void testDeleteById(){
        System.out.println(videoService.deleteById("99"));
        System.out.println(videoService.deleteById("999"));
    }

    @Test
    public void testSaveDataFromDb(){
        VideoPageParam videoPageParam = new VideoPageParam();
        videoPageParam.setPage(1);
        videoPageParam.setSize(999);
        PageInfo<Video> videoPage = videoService.page(videoPageParam);
        List<Video> videos = videoPage.getList();
        System.out.println(videos);
        List<VideoDoc> videoDocs = new ArrayList<>();
        for (Video video : videos) {
            VideoDoc videoDoc = new VideoDoc();
            BeanUtils.copyProperties(video, videoDoc);
            videoDocs.add(videoDoc);
        }
        System.out.println(videoDocs);
        for (VideoDoc videoDoc : videoDocs) {
            videoService.save(videoDoc);
        }

    }

    @Test
    public void testCount(){
        System.out.println(videoService.countByTitle("框架"));
    }

    @Test
    public void testCountByTitle(){
        System.out.println(videoService.countByPhraseTitle("服务器"));
    }

    @Test
    public void testSelectFirstByVideoId() {
        System.out.println("1号视频的第一集: " + videoService.selectFirstByVideoId(1));
        System.out.println("2号视频的第一集: " + videoService.selectFirstByVideoId(2));
        System.out.println("3号视频的第一集: " + videoService.selectFirstByVideoId(3));
    }

}
