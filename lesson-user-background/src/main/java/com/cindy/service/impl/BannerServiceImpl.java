package com.cindy.service.impl;

import com.cindy.entity.Banner;
import com.cindy.mapper.BannerMapper;
import com.cindy.service.BannerService;
import com.cindy.util.GuavaUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Cindy
 */
@Service
public class BannerServiceImpl implements BannerService {
    /**
     * Guava缓存的Key值
     */
    private static final String BANNER_GUAVA_KEY = "banners";

    @Autowired
    private BannerMapper bannerMapper;

    @SneakyThrows
    @Override
    public List<Banner> list() {

        // 缓存未命中时，从数据库获取banner数据并备份到缓存中
        Object cache = GuavaUtil.getTenMinCache().get(BANNER_GUAVA_KEY, () -> bannerMapper.list());

        // 缓存未命中时，将缓存强转为目标类型并返回
        if (cache instanceof List) {
            List<Banner> result = new ArrayList<>();
            for (Object obj : (List<?>) cache) {
                result.add((Banner) obj);
            }
            return result;
        }

        return Collections.emptyList();
    }
}
