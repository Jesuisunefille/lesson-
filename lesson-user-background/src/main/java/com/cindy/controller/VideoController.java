package com.cindy.controller;

import com.cindy.entity.Episode;
import com.cindy.entity.Video;
import com.cindy.param.VideoPageParam;
import com.cindy.service.VideoService;
import com.cindy.util.BindingResultUtil;
import com.cindy.util.Result;
import com.cindy.vo.VideoPageVo;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Cindy
 */
@Tag(name = "VideoController", description = "视频模块接口")
@Controller
@RequestMapping("/api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @ResponseBody
    @Operation(summary = "按视频主键单查视频记录详情", description = "无需token验证，查询的结果中包含视频中的章节记录")
    @GetMapping("/select-detail-by-id")
    public Result selectDetailById(@Parameter(description = "视频表主键")
                                   @RequestParam("video-id") Integer videoId) {
        Video video = videoService.selectDetailById(videoId);
        return null != video ?
                Result.ok(video) :
                Result.fail(0, "视频不存在");
    }

    @Operation(summary = "分页查询视频记录", description = "无需token验证")
    @GetMapping("/page")
    @ResponseBody
    public Result page(@Validated VideoPageParam videoPageParam,
                       BindingResult bindingResult) {
        BindingResultUtil.check(bindingResult);

        PageInfo<Video> page = videoService.page(videoPageParam);

        // 组装VO
        VideoPageVo videoPageVo = new VideoPageVo();
        BeanUtils.copyProperties(page, videoPageVo);
        videoPageVo.setVideos(page.getList());
        return Result.ok(videoPageVo);
    }


    @Operation(summary = "按视频标题计数视频", description = "无需token验证")
    @GetMapping("/count-by-phrase-title")
    @ResponseBody
    public Result countByPhraseTitle(@RequestParam @Parameter(description = "视频标题") String title) {
        return Result.ok(videoService.countByPhraseTitle(title));
    }

    @Operation(summary = "按视频标题搜索视频", description = "无需token验证")
    @GetMapping("/search-by-title")
    @ResponseBody
    public Result searchByTitle(@RequestParam @Parameter(description = "视频标题") String title) {
        List<Video> result = videoService.searchByTitle(title);
        return !result.isEmpty() ? Result.ok(result) :
                Result.fail(0, "无符合要求的视频");
    }
    @Operation(summary = "按视频主键查询该视频下的第一集", description = "无需token验证")
    @GetMapping("/select-first-episode-by-id")
    @ResponseBody
    public Result selectFirstEpisodeById(@Validated @RequestParam Integer id) {
        Episode episode = videoService.selectFirstByVideoId(id);
        return null != episode ?
                Result.ok(episode) :
                Result.fail(0, "该视频暂无章集信息，请联系运维人员");
    }
}
