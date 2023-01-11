package com.cindy.service.impl;

import com.cindy.entity.User;
import com.cindy.entity.Video;
import com.cindy.mapper.VideoMapper;
import com.cindy.service.TestService;
import com.cindy.util.GuavaUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.Cache;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cindy
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<User> testAop(String param) {
        System.out.println("执行业务方法");
        User user01 = new User();
        user01.setPassword("11111111111");
        User user02 = new User();
        user02.setPassword("22222222222");
        List<User> users = new ArrayList<>();
        users.add(user01);
        users.add(user02);
        return users;
    }

    @SneakyThrows
    @Override
    public String testGuava() {
        Cache<String, Object> cache = GuavaUtil.getTenMinCache();
        return (String) (cache.get("ID", () -> {
            System.out.println("模拟：正在从数据库中查询ID的值 ..");
            return "01";
        }));
    }


    @Override
    public PageInfo<Video> testPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(videoMapper.list());
    }
}
