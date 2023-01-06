<template>

  <header class="video-list-header">

    <!--搜索视频表单-->
    <!--
      ref="searchForm"：表单名称，校验的时候使用
      :inline="true"：行内表单模式
      :model="searchForm"：绑定表单数据对象
      :rules="searchRule"：绑定表单验证规则
    -->
    <el-form ref="searchForm"
             :inline="true"
             :model="searchFormData"
             :rules="searchFormRule">

      <!--存放单行文本框的表单项-->
      <!--
        prop="title"：若该表单项需要校验，则必须使用设置prop属性
      -->
      <el-form-item prop="title">

        <!--单行文本框控件-->
        <!--
          v-model="searchFormData['title']"：双向绑定表单数据中title值
          placeholder="请输入视频标题进行搜索.."：背景提示字
        -->
        <el-input type="text"
                  v-model="searchFormData['title']"
                  placeholder="请输入视频标题进行搜索.."/>

      </el-form-item>

      <!--存放按钮的表单项-->
      <el-form-item>

        <!--搜索按钮-->
        <!--
          type="primary"：蓝色
          @click="searchVideo"：点击该按钮时触发searchVideo方法
        -->
        <el-button type="primary" @click="searchVideo">搜索视频</el-button>

        <!--清空搜索-->
        <!--
          type="danger"：红色
          @click="clearSearchVideo"：点击该按钮时触发clearSearchVideo方法
        -->
        <el-button type="danger" @click="clearSearchVideo">清空搜索</el-button>

      </el-form-item>

    </el-form>

  </header>

  <section class="video-list-body">

    <!--展示视频列表-->
    <el-row>

      <!--根据屏幕大小展示视频列表-->
      <!--
        :xs="24"	当屏幕小于768px时，切分24份，即每行显示1个视频
        :sm="12"	当屏幕大于等于768px时，切分12份，即每行显示2个视频
        :md="8"	  当屏幕大于等于992px时，切分8份，即每行显示3个视频
        :lg="6"	  当屏幕大于等于1200px时，切分6份，即每行显示4个视频
        :xl="4"	  当屏幕大于等于1920px时，切分4份，即每行显示6个视频
        v-for="video in videos"：遍历视频列表
      -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4"
              v-for="video in videos">

        <!--点击视频封面时可进入对应视频的视频详情组件-->
        <!--
          <router-link :to="{..}">：点击切换组件
              path: '/video-detail'：点击进入VideoDetail组件
              query: {'id': video['id']}：向目标组件传递视频ID值
        -->
        <router-link :to="{ path: '', query: {'id': video['id']} }">

          <!--视频封面图-->
          <!--
            :src="nginxSrc(video['cover-image'])"：计算视频封面图的Nginx代理地址
            :title="video['info']"：鼠标悬停在图片上时展示视频的介绍
          -->
          <el-image :src="nginxSrc(video['cover-image'])"
                    :title="video['info']"
                    class="cover-image"/>

        </router-link>

        <!--视频简介-->
        <article class="video-info">

          <!--展示视频的标题和作者-->
          <!--
            在布局中直接使用 {{ 变量名 }} 展示变量
          -->
          <p class="video-title-and-author">{{ video['title'] }} - {{ video['author'] }}</p>

          <!--展示视频的价格-->
          <p class="video-price">￥ {{ video['price'] }} RMB</p>

        </article>

      </el-col>

    </el-row>

  </section>

  <footer class="video-list-footer">

    <!--大屏幕时展示此容器：当视口在xs尺寸时隐藏-->
    <article class="hidden-xs-only">

      <!--分页按钮-->
      <!--
        background：为分页按钮添加背景色
        layout="total, sizes, prev, pager, next, jumper"：组件布局，子组件名用逗号分隔：
            total  总条目数
            sizes  每页显示个数选择器
            prev   上一页按钮
            pager  数字列表
            next   下一页按钮
            jumper 跳转到指定页
        :total="videoPageInfo['total']"：绑定总条目数
        :page-sizes="[2, 6, 8]"：每页显示个数选择器的选项设置
        :page-size="videoPageInfo['page-size']"：绑定每页显示条目个数
        :current-page="videoPageInfo['page-num']"：绑定当前页数
        @current-change="changePage"：当前页数发生改变时触发 `changePage` 方法
        @size-change="changeSize"：每页显示条目个数发生改变时触发 `changeSize` 方法
      -->
      <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                     :total="videoPageInfo['total']"
                     :page-sizes="[2, 6, 8]"
                     :page-size="videoPageInfo['page-size']"
                     :current-page="videoPageInfo['page-num']"
                     @current-change="changePage"
                     @size-change="changeSize"/>

    </article>

    <!--小屏幕时展示此容器：当视口在sm及以上尺寸时隐藏-->
    <article class="hidden-sm-and-up">

      <!--分页按钮-->
      <!--
        background：为分页按钮添加背景色
        layout="prev, pager, next"：组件布局，子组件名用逗号分隔：
            prev   上一页按钮
            pager  数字列表
            next   下一页按钮
        small="small"：使用小型分页样式
        :total="videoPageInfo['total']"：绑定总条目数
        :page-size="videoPageInfo['page-size']"：绑定每页显示条目个数
        :current-page="videoPageInfo['page-num']"：绑定当前页数
        @current-change="changePage"：当前页数发生改变时触发 `changePage` 方法
      -->
      <el-pagination background layout="prev, pager, next" small="small"
                     :total="videoPageInfo['total']"
                     :page-size="videoPageInfo['page-size']"
                     :current-page="videoPageInfo['page-num']"
                     @current-change="changePage"/>

    </article>

  </footer>

</template>

<script setup>

import {computed, onMounted, shallowReactive, shallowRef} from "vue";
import {VIDEO_PAGE_API, VIDEO_SEARCH_BY_TITLE_API} from '@/api'
import {nginxCoverImage} from "@/global_variable";
import {ElMessage} from "element-plus";

// data: 视频数据列表
let videos = shallowRef();

// data: 视频分页数据
let videoPageInfo = shallowReactive({
  'page-size': 6,
  'total': 0,
  'page-num': 1
});

// data: 搜索表单对象
let searchForm = shallowRef('');

// data: 存放搜索表单的数据
let searchFormData = shallowReactive({
  title: ''
});

// data: 存放搜索表单的校验规则
let searchFormRule = shallowReactive({
  title: [{
    required: true,
    message: '您要搜索的视频标题不能为空',
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

// method: 当前显示第几页被改变时触发
let changePage = page => {
  videoPageInfo['page-num'] = page;
  pageVideo(page, videoPageInfo['page-size']);
};

// method: 每页显示多少条被改变时触发：
let changeSize = size => {
  videoPageInfo['page-size'] = size;
  pageVideo(videoPageInfo['page-num'], size);
};

// method: 搜索视频
let searchVideo = () => {

  // 检查一下是否表单中所有的控件都校验通过了
  searchForm.value.validate(async valid => {

    // 全部控件校验成功时，`valid` 参数才为true，否则为false
    if (valid) {
      try {
        let resp = await VIDEO_SEARCH_BY_TITLE_API(searchFormData['title']);
        if (resp['data']['code'] > 0) {
          videos.value = resp['data']['data'];
        } else console.error(resp['data']['message'])
      } catch (e) {
        console.error(e);
      }
    } else {

      // EL提示组件
      ElMessage('表单校验失败，请检查字段');
    }

  });
}

// method: 清空搜索
let clearSearchVideo = () => {
  pageVideo(1, videoPageInfo['page-size']);
  searchFormData['title'] = '';
};

// mounted: 页面加载完毕后，立刻调用 `pageVideo()` 方法
onMounted(() => pageVideo(1, 6));

</script>

<style lang="scss" scoped>

.video-list-header {

  padding: 20px 0; // 上下内边距 左右内边距

  /*表单项*/
  .el-form-item {
    margin: 5px; // 外边距
  }
}

.video-list-body {

  margin: 20px; // 上内边距 左右内边距 下内边距

  /*视频封面图片*/
  .cover-image {
    width: 300px; // 宽度
    height: 200px; // 高度
    margin: 5px; // 外边距
    border: 5px solid black; // 边框
    border-radius: 5px; // 圆角
    box-sizing: border-box; // 中和边框和内边距
  }

  /*视频简介*/
  .video-info {

    /*视频标题和作者*/
    .video-title-and-author {
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

  padding: 20px 0; // 上下外边距 左右外边距

  /*分页按钮*/
  .el-pagination {
    justify-content: center; // Flex布局下，子元素主轴方向居中
  }

}

</style>