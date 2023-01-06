<template>

  <header class="video-list-header">

    <!--搜索视频表单-->
    <el-form :inline="true" ref="searchFormRef" :model="searchForm" :rules="searchRule">
      <el-form-item prop="title">
        <el-input type="text" v-model="searchForm['title']"
                  placeholder="请输入视频标题进行搜索.."/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchVideo">搜索视频</el-button>
        <el-button type="danger" @click="clearSearchVideo">清空搜索</el-button>
      </el-form-item>
    </el-form>

  </header>

  <section class="video-list-body">

    <!--展示视频列表-->
    <el-row>
      <!--根据屏幕大小展示视频列表-->
      <!--
        :xs="24" :sm="12" :md="8" :lg="6" :xl="4"：响应式布局
        v-for="video in videos"：遍历视频列表
      -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4" v-for="video in videos">

        <!--视频封面：点击进入VideoDetail视频详情组件，并向其传递视频id值-->
        <router-link :to="{path: '/video-detail', query: {'id': video['id']}}">

          <!--视频封面图-->
          <!--
            :src="nginxSrc(video['cover-image'])"：计算视频封面图的真实地址
            :title="video['info']"：鼠标悬停在图上上时展示视频的描述
          -->
          <el-image :src="nginxSrc(video['cover-image'])" :title="video['info']" class="cover-image"/>

        </router-link>

        <!--视频简介-->
        <div class="video-info">

          <!--展示视频的标题和作者-->
          <!--
            在布局中可以直接使用 {{ }} 展示JS中的数据
          -->
          <p class="video-title">{{ video['title'] }} - {{ video['author'] }}</p>

          <!--展示视频的价格-->
          <p class="video-price">￥ {{ video['price'] }} RMB</p>

        </div>

      </el-col>

    </el-row>

  </section>

  <footer class="video-list-footer">

    <!--大屏幕时展示此容器-->
    <div class="hidden-xs-only">
      <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                     :total="videoPageInfo['total']"
                     :page-sizes="[2, 6, 8]"
                     :page-size="videoPageInfo['page-size']"
                     :current-page="videoPageInfo['page-num']"
                     @current-change="changePage"
                     @size-change="changeSize"/>
    </div>

    <!--大屏幕时展示此容器-->
    <div class="hidden-sm-and-up">
      <el-pagination background layout="prev, pager, next" small="small"
                     :total="videoPageInfo['total']"
                     :page-size="videoPageInfo['page-size']"
                     :current-page="videoPageInfo['page-num']"
                     @current-change="changePage"/>
    </div>

  </footer>

</template>

<script setup>

import {computed, onMounted, shallowReactive, shallowRef} from "vue";
import {VIDEO_PAGE_API, VIDEO_SEARCH_BY_TITLE_API} from '@/api'
import {nginxCoverImage} from "@/global_variable.js";

// data: 视频数据列表
let videos = shallowRef([]);

// data: 视频分页数据
let videoPageInfo = shallowReactive({
  'page-size': 6,
  'total': 0,
  'page-num': 0
});

// data: 搜索用的
let searchFormRef = shallowRef([]);
let searchForm = shallowReactive({
  title: ''
});
let searchRule = shallowReactive({
  title: [{
    required: true,
    message: '视频标题必填',
    trigger: 'blur'
  }]
});

// computed: 拼接Nginx视频封面目录前缀
let nginxSrc = computed(() => src => nginxCoverImage + src);

// method: 异步分页查询视频列表
let pageVideo = async (page, size) => {
  try {
    const resp = await VIDEO_PAGE_API(page, size);
    if (resp['data']['code'] > 0) {
      let data = resp['data']['data'];
      videos.value = data['videos'];
      videoPageInfo['page-size'] = data['page-size'];
      videoPageInfo['total'] = data['total'];
      videoPageInfo['page-num'] = data['page-num'];
    }
  } catch (e) {
    console.error(e);
  }
}

// method: 翻页时触发
let changePage = page => {
  videoPageInfo['page-num'] = page;
  pageVideo(page, videoPageInfo['page-size']);
};

// method: 改变每页大小时触发
let changeSize = size => {
  videoPageInfo['page-size'] = size;
  pageVideo(videoPageInfo['page-num'], size);
};

// method: 搜索视频
let searchVideo = () => {
  searchFormRef.value.validate(valid => {
    if (valid) {
      VIDEO_SEARCH_BY_TITLE_API(searchForm['title']).then(resp => {
        videos.value = resp['data']['data'];
      })
    }
  });
}

// method: 清空搜索
let clearSearchVideo = () => {
  pageVideo(1, videoPageInfo['page-size']);
  searchForm['title'] = '';
};

// mounted: 页面加载完毕后，立刻调用 `pageVideo()` 方法
onMounted(() => pageVideo(1, videoPageInfo['page-size']));

</script>

<style lang="scss" scoped>

.video-list-header {
  padding: 20px 0;

  .el-form-item {
    margin: 5px;
  }

}

.video-list-body {

  padding: 0 20px 20px; // 上内边距 左右内边距 下内边距

  /*视频封面*/
  .cover-image {
    margin: 5px; // 外边距
    border: 5px solid black; // 边框
    border-radius: 5px; // 圆角
    box-sizing: border-box; // 中和边框和内边距
  }

  /*视频简介*/
  .video-info {

    /*视频标题*/
    .video-title {
      margin: 10px auto; // 上下外边距 左右居中
      font-size: 13px; // 字号
    }

    /*视频价格*/
    .video-price {
      margin: 10px auto; // 上下外边距 左右居中
      font-size: 11px; // 字号
      color: #d93f30; // 前景色
    }
  }
}

.video-list-footer {
  .el-pagination {
    justify-content: center;
  }

  padding: 20px 0;
}

</style>