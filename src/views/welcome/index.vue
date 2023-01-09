<template>

  <header class="welcome-header">

    <!--大标题-->
    <h1 class="title">{{ title }}</h1>

    <!--小标题-->
    <h3 class="sub-title">{{ subTitle }}</h3>

  </header>

  <section class="welcome-body">

    <!--插入轮播图子组件-->
    <article class="welcome-body-banner">
      <banner/>
    </article>

    <!--插入视频列表子组件-->
    <article class="welcome-body-video-list">
      <video-list/>
    </article>

  </section>

  <footer class="welcome-footer">

    <!--引入通用脚子组件-->
    <article class="welcome-footer-common-footer">
      <common-footer/>
    </article>

  </footer>

</template>

<script setup>
import Banner from "@/views/welcome/banner";
import VideoList from '@/views/welcome/video-list';
import CommonFooter from '@/components/common-footer';

// data: 首页大标题
const title = '绝对精品课 - 在线课堂';

// data: 首页小标题
const subTitle = 'Welcome To Lesson Project';

</script>

<style lang="scss" scoped>

.welcome-header {

  margin: 20px; // 内边距

  .sub-title {
    color: #807c74; // 前景色
  }
}

.welcome-body {

  .welcome-body-banner {
    margin: 20px; // 外边距
  }

  .welcome-body-video-list {
    margin: 0 20px 50px; // 上外边距 左右外边距 下外边距
  }

}

.welcome-footer {

  .welcome-footer-common-footer {
    margin: 20px 0; // 上下外边距 左右外边距
  }

}

</style>