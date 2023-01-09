<template>
  <section class="free-video-body">

    <!--西瓜视频播放器容器-->
    <article id="video-content"></article>
  </section>
</template>

<script setup>

import XGPlayer from 'xgplayer';
import {nginxVideo} from "@/global_variable";
import {onMounted, defineProps} from "vue";

// props: 接收父组件传递过来的值
let props = defineProps({
  firstEpisodeUrl: {
    type: String,
    required: true
  }
})

// data: 视频播放器
let xgPlayer;

// method: 初始化视频播放器
let initXgPlayer = () => {
  xgPlayer = new XGPlayer({
    id: "video-content", // 绑定视频容器
    url: nginxVideo + props.firstEpisodeUrl, // 视频对象
    poster: require('@/assets/free-video-cover.png'), // 视频封面
    fluid: true, // 使用流体布局
    playbackRate: [0.5, 1, 1.5, 2], // 倍速播放设置
    download: false, // 不允许下载视频
    pip: false, // 不开启画中画
  });
}

// mounted: 页面加载完毕后，立刻调用 `initPlayer()` 方法
onMounted(() => initXgPlayer());

</script>

<style lang="scss" scoped>

</style>