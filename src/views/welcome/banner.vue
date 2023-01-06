<template>

  <!--当有轮播图数据的时候展示-->
  <!--
    v-if="banners.length > 0"：当有轮播图的时候展示此区域
  -->
  <header v-if="banners.length > 0" class="banner-header">

    <!--轮播图组件-->
    <!--
      :interval="5000"：5秒钟自动轮播一次
      arrow="always"：永远展示左右翻页箭头
      indicator-position="outside"：指示器在图片之外
    -->
    <el-carousel :interval="5000" arrow="always" indicator-position="outside">

      <!--轮播图组件item项-->
      <!--
        v-for="banner in banners"：遍历轮播图数据
      -->
      <el-carousel-item v-for="banner in banners">

        <!--展示图片：每张图片都可以点击跳转-->
        <!--
          :src="nginxSrc(banner['src'])"：计算轮播图的Nginx代理地址
          :title="banner['info']"：鼠标悬停在图片上时展示图片的介绍
        -->
        <a :href="banner['url']">
          <el-image :src="nginxSrc(banner['src'])" :title="banner['info']" class="image"/>
        </a>

      </el-carousel-item>

    </el-carousel>

  </header>

  <!--当无轮播图数据的时候展示-->
  <header v-else class="banner-header">

    <!--展示无轮播图数据的静态提示图片-->
    <!--
      :src="require('@/assets/no-banner-tips.png')"：展示本地资源时必须使用 require() 函数
    -->
    <el-image :src="require('@/assets/no-banner-tips.png')" class="image"/>

  </header>

</template>

<script setup>

import {computed, onMounted, shallowRef} from "vue";
import {BANNER_LIST_API} from "@/api";
import {nginxBanner} from "@/global_variable";

// data: 轮播图数据列表
let banners = shallowRef([]);

// computed: 拼接Nginx轮播图目录前缀
let nginxSrc = computed(() => src => nginxBanner + src);

// method: 异步查询轮播图列表
let listBanner = async () => {
  try {
    const resp = await BANNER_LIST_API();
    if (resp['data']['code'] > 0) {
      banners.value = resp['data']['data'];
    } else console.error(resp['data']['message'])
  } catch (e) {
    console.error(e)
  }
}

// mounted: 页面加载完毕后，立刻调用 `listBanner()` 方法
onMounted(() => listBanner());

</script>

<style lang="scss" scoped>

.banner-header {
  padding: 10px; // 外边距
}

</style>