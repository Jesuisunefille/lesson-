package com.cindy.service.impl;

import com.cindy.document.VideoDoc;
import com.cindy.entity.Chapter;
import com.cindy.entity.Episode;
import com.cindy.entity.Video;
import com.cindy.mapper.ChapterMapper;
import com.cindy.mapper.EpisodeMapper;
import com.cindy.mapper.VideoMapper;
import com.cindy.param.VideoPageParam;
import com.cindy.service.VideoService;
import com.cindy.util.NullUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private EpisodeMapper episodeMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public Video selectDetailById(Integer id) {
        return videoMapper.selectDetailById(id);
    }

    /**
     * 开发ES添加视频业务
     * @param videoDoc Video的Doc实体
     * @return 添加VideoDoc文档，返回添加的文档
     */
    @Override
    public VideoDoc save(VideoDoc videoDoc) {
        return elasticsearchRestTemplate.save(videoDoc);
    }

    /**
     * 开发ES查询视频业务
     * @param id Video主键
     * @return 按主键查询文档数据，不存在返回null
     */
    @Override
    public VideoDoc getById(String id) {
        return elasticsearchRestTemplate.get(id, VideoDoc.class);
    }

    @Override
    public PageInfo<Video> page(VideoPageParam videoPageParam) {
        Integer page = videoPageParam.getPage();
        Integer size = videoPageParam.getSize();
        if (NullUtil.hasNull(page, size)) {
            throw new RuntimeException("必要参数为空");
        }
        PageHelper.startPage(page, size);
        return new PageInfo<>(videoMapper.list());
    }


    /**
     * 开发ES删除视频业务
     * @param id Video主键
     * @return null
     */
    @Override
    public String deleteById(String id) {
        if (elasticsearchRestTemplate.exists(id, VideoDoc.class)) {
            return elasticsearchRestTemplate.delete(id, VideoDoc.class);
        }
        return null;
    }

    /**
     * 开发ES条件计数业务
     * @param title 视频标题
     * @return 根据本地搜索器进行条件计数
     */
    @Override
    public long countByTitle(String title) {
        //1.构建条件查询器（将‘微服务’当成短语模糊匹配title值）
        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", title);
        //2.构建本地搜索器（构建时绑定条件查询器）
        Query query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();
        //3.根据本地搜索器进行条件计数
        return elasticsearchRestTemplate.count(query, VideoDoc.class);
    }

    /**
     *开发ES条件搜索业务
     * @param title Video表标题
     * @return 失败时返回提示消息，成功时返回符合条件的视频
     */
    @Override
    public List<Video> searchByTitle(String title) {

        // 使用es进行搜索
        SearchHits<VideoDoc> searchHits = elasticsearchRestTemplate.search(new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", title))
                .withPageable(PageRequest.of(0, 999))
                .withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .withHighlightFields(new HighlightBuilder.Field("title"))
                .build(), VideoDoc.class);

        // 如果ES中未搜索到任何数据，则从数据库中获取
        if (searchHits.isEmpty()) {

            // 从数据库中模糊查询数据
            List<Video> videos = videoMapper.selectLikeTitle(title);
            if (videos.isEmpty()) {
                return Collections.emptyList();
            }

            // 将数据库中搜索到的数据备份到ES中
            List<VideoDoc> videoDocs = new ArrayList<>();
            for (Video video : videos) {
                VideoDoc videoDoc = new VideoDoc();
                BeanUtils.copyProperties(video, videoDoc);
                videoDocs.add(videoDoc);
            }
            elasticsearchRestTemplate.save(videoDocs);

            // 返回数据
            return videos;
        } else {

            List<Video> videos = new ArrayList<>();
            // 将结果中的全部VideoDoc实体类数据对位拷贝到Video实体类中
            for (SearchHit<VideoDoc> searchHit : searchHits) {
                VideoDoc videoDoc = searchHit.getContent();
                Video video = new Video();
                BeanUtils.copyProperties(videoDoc, video);
                videos.add(video);
            }

            // 返回数据
            return videos;
        }
    }

    @Override
    public long countByPhraseTitle(String title) {
        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", title);
        Query query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();
        return elasticsearchRestTemplate.count(query, VideoDoc.class);
    }

    @Override
    public Episode selectFirstByVideoId(Integer videoId) {
        Chapter firstChapter = chapterMapper.selectFirstByVideoId(videoId);
        if (null != firstChapter) {
            Integer firstChapterId = firstChapter.getId();
            return episodeMapper.selectFirstByChapterId(firstChapterId);
        }
        return null;
    }
}
