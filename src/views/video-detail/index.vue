<template>

  <header class="video-detail-header">

    <!--通用头子组件-->
    <common-header title="视频详情"/>

  </header>

  <section class="video-detail-body">

    <!--免费视频和视频信息介绍-->
    <article class="detail-info">

      <el-row>

        <!--免费视频-->
        <el-col :span="16" :xs="24">

          <!--免费视频子组件-->
          <free-video :firstEpisodeUrl="firstEpisodeUrl"/>

        </el-col>

        <!--视频信息：视口在xs时隐藏，即大屏幕时显示此容器-->
        <el-col :span="8" :xs="24" class="hidden-xs-only">

          <!--分隔线：视频标题作者-->
          <el-divider content-position="center">
            <Iphone class="icon"/>
            <span>视频标题作者</span>
          </el-divider>
          <span>{{ video['title'] }} - {{ video['author'] }}</span>

          <!--分隔线：视频创建日期-->
          <el-divider content-position="center">
            <Iphone class="icon"/>
            <span>视频创建日期</span>
          </el-divider>
          <span>{{ video['create-time'] }}</span>

          <!--分隔线：视频综合评分-->
          <el-divider content-position="center">
            <Star class="icon"/>
            <span>视频综合评分</span>
          </el-divider>
          <el-rate v-model="video['star']" class="rate"/>

          <!--分隔线：视频优惠价格-->
          <el-divider content-position="center">
            <Money class="icon"/>
            <span>视频优惠价格</span>
          </el-divider>
          <span>{{ video['price'] }} 元</span>

          <!--分隔线：视频详细介绍-->
          <el-divider content-position="center">
            <InfoFilled class="icon"/>
            <span>视频详细介绍</span>
          </el-divider>
          <span>{{ video['info'] === '' ? '该视频暂无介绍' : video['info'] }}</span>

        </el-col>

        <!--视频信息：视口在sm及以上时隐藏，即小屏幕时显示此容器-->
        <el-col :span="8" :xs="24" class="hidden-sm-and-up">
          <p class="video-msg">视频标题作者：<span class="msg-val">{{ video['title'] }} - {{ video['author'] }}</span></p>
          <p class="video-msg">视频创建日期：<span class="msg-val">{{ video['create-time'] }}</span></p>
          <p class="video-msg">视频综合评分：<span class="msg-val">{{ video['star'] }} 分</span></p>
          <p class="video-msg">视频优惠价格：<span class="msg-val">{{ video['price'] }} 元</span></p>
        </el-col>

      </el-row>

    </article>

    <!--视频信息选项卡-->
    <article class="detail-tab">

      <!--选项卡-->
      <!--
        stretch: 标签的宽度是否自撑开
        v-model="currentTabName"：双绑currentTabName变量，动态决定哪个选项卡为当前首选项
        @tab-click="changeTab"：点击触发changeTab方法
      -->
      <el-tabs stretch class="demo-tabs"
               v-model="currentTabName" @tab-click="changeTab">
        <el-tab-pane label="摘要" name="摘要"/>
        <el-tab-pane label="目录" name="目录"/>
      </el-tabs>

      <!--选项卡对应要显示的组件：使用动态组件来展示-->
      <!--:is="currentTabComponent"：属性 `:is` 的值是哪个组件的名称，就显示哪个组件-->
      <component :is="currentTabComponent"
                 :summaryImage="video['summary-image']"
                 :chapters="video['chapters']"/>

    </article>

  </section>

  <footer class="video-detail-footer">
    <el-button type="warning" @click="addToCart" class="add-to-cart-btn">加入购物车</el-button>
  </footer>

</template>

<script setup>

import CommonHeader from '@/components/common-header';
import FreeVideo from '@/views/video-detail/free-video';
import TabSummary from '@/views/video-detail/tab-summary';
import TabCatalog from '@/views/video-detail/tab-catalog';
import {VIDEO_SELECT_DETAIL_BY_VIDEO_ID, CART_INSERT_OR_UPDATE_API} from '@/api';
import {onMounted, shallowRef} from "vue";
import {Iphone, Star, Money, InfoFilled} from '@element-plus/icons-vue';
import router from '@/router';
import {ElMessage} from "element-plus";
import {useStore} from "vuex";

// data: vuex实例
const vuex = useStore();

// data: 从sessionStorage中获取当前登录的用户主键
const userId = sessionStorage.getItem('user-id');

// data: 从当前路由中获取视频主键
const videoId = router.currentRoute.value.query['video-id'];

// data: 当前选项卡的名称
let currentTabName = shallowRef('摘要');

// data: 当前选项卡展示的组件
let currentTabComponent = shallowRef(TabSummary);

// data: 视频对象
let video = shallowRef({});

// data: 视频第一集
let firstEpisodeUrl = shallowRef('');

// method: 当切换选项卡时触发
let changeTab = tab => currentTabComponent.value = tab.props.name === '摘要' ? TabSummary : TabCatalog;

// method: 根据视频主键查询视频详情
let selectDetailByVideoById = async (videoId) => {
  try {
    let resp = await VIDEO_SELECT_DETAIL_BY_VIDEO_ID(videoId);
    if (resp['data']['code'] > 0) {
      let data = resp['data']['data'];
      video.value = data;
      if (data['chapters'].size > 0) {
        firstEpisodeUrl.value = data['chapters'][0]['episodes'][0]['url'];
      }
    } else console.error(resp['data']['message']);
  } catch (e) {
    console.error(e)
  }
}

// method: 将视频添加到购物车
let addToCart = () => {

  // 未登录保护
  if (!vuex.state['token']) {
    ElMessage("请先登录！");
    setTimeout(() => router.push("/login"), 2000);
    return;
  }

  // 添加购物车
  CART_INSERT_OR_UPDATE_API({
    'user-id': userId,
    'video-id': videoId,
    'title': video.value['title'],
    'price': video.value['price'],
    'author': video.value['author'],
    'cover-image': video.value['cover-image']
  }).then(resp => {
    if (resp['data']['code'] > 0) {
      router.push({path: '/cart'});
    } else {
      console.log(resp['data']['message']);
      ElMessage('购物车添加失败！');
    }
  }).catch(error => console.log(error))
}

// mounted: 页面加载完毕后，立刻调用 `selectDetailByVideoById()` 方法
onMounted(() => selectDetailByVideoById(videoId));

</script>

<style lang="scss" scoped>

.video-detail-body {

  .detail-info {

    .el-col {
      padding: 10px; // 内边距
    }

    /*信息图标*/
    .icon {
      width: 1em; // 宽度
      height: 0.9em; // 高度
      margin-right: 8px; // 右外边距
    }

    /*视频信息*/
    .video-msg {

      text-align: left; // 内容居左
      margin: 10px 15px; // 上下外边距 左右外边距

      .msg-val {
        float: right; // 右浮动
        color: #ff792f; // 前景色
      }
    }
  }

  .detail-tab {
    margin: 20px 0; //上下外边距 左右忽略
  }

}

.video-detail-footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  padding: 8px 0;
  z-index: 999;

  .add-to-cart-btn {
    display: block;
    color: #fff;
    margin: 0 auto;
    background-color: #d93f30;
    height: 35px;
    border-radius: 15px;
    width: 90%;
    border: none;
    font-size: 15px;
    text-align: center;
  }
}

</style>