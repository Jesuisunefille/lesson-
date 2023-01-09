package com.cindy.service;

import com.cindy.document.VideoDoc;
import com.cindy.entity.Episode;
import com.cindy.entity.Video;
import com.cindy.param.VideoPageParam;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface VideoService {

    /**
     * <h2>视频分页查询业务</h2>
     * <p> 01. 检查必填参数：若包含null值则直接抛出参数异常。
     * <p> 02. 启动分页：发送SQL前使用 `PageHelper.startPage(page, size)` 启动分页。
     * <p> 03. 进行全查：调用Mapper接口全查Video数据，该代码必须紧随启动分页的代码之后。
     * <p> 04. 封装结果：使用 `new PageInfo<Video>(全查结果)` 将全查结果进行封装并返回。
     *
     * @param videoPageParam 视频分页业务实体参数
     * @return 部分Video记录
     */
    PageInfo<Video> page(VideoPageParam videoPageParam);

    /**
     * <h2>视频详情查询业务</h2>
     *
     * <p> 01. 调用Mapper接口按视频主键查询Video详情，包括章集信息。
     *
     * @param id Video表主键
     * @return 查询成功返回该条Video记录，查询失败直接返回null值
     */
    Video selectDetailById(Integer id);

    /**
     * 添加一条VideoDoc记录到ES中
     *
     * @param videoDoc Video的Doc实体
     * @return 成功加入的一条VideoDoc记录
     */
    VideoDoc save(VideoDoc videoDoc);

    /**
     * 根据主键从ES中查询一条VideoDoc记录
     * @param id Video主键
     * @return 对应主键的一条VideoDoc记录
     */
    VideoDoc getById(String id);

    /**
     * 根据主键从ES中删除一条VideoDoc记录
     * @param id Video主键
     * @return 删除的VideoDoc记录的主键，若记录不存在则直接返回null值
     */
    String deleteById(String id);


    /**
     * 根据视频标题搜索符合条件的VideoDoc记录的数量
     * @param title 视频标题
     * @return 符合条件的VideoDoc记录的数量
     */
    long countByTitle(String title);


    /**
     * 按标题从ES中搜索相关视频
     *
     * @param title Video表标题
     * @return 符合条件的Video记录，若记录为空，返回id为0的Video记录
     */
    List<Video> searchByTitle(String title);


    /**
     * 按视频标题从ES中计数符合条件的VideoDoc记录
     * @param title Video表标题
     * @return 符合条件的VideoDoc记录的数量
     */
    long countByPhraseTitle(String title);


    /**
     *
     * @param id
     * @return
     */
    Episode selectFirstByVideoId(Integer id);
}
