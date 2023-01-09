<template>

  <section class="tab-catalog-body">

    <!--展示视频的章节信息-->
    <!--
      v-for="(chapter, chapterIndex) in chapters：遍历视频的章节信息
    -->
    <article v-for="(chapter, chapterIndex) in chapters">

      <!--展示章信息-->
      <div class="chapter">第{{ computedIndex(chapterIndex + 1) }}章：{{ chapter['title'] }}</div>

      <!--展示每一章下，集的信息-->
      <!--
        v-for="(episode, episodeIndex) in chapter['episodes']"：遍历章中的集信息
      -->
      <div v-for="(episode, episodeIndex) in chapter['episodes']">

        <!--展示集-->
        <div class="episode">

          <!--每一集的标题-->
          <div class="episode-title">第{{ computedIndex(episodeIndex + 1) }}集：{{ episode['title'] }}</div>

          <!--每一集的状态：是否提示 "请购买后观看"-->
          <div class="episode-state">{{ chapterIndex === 0 && episodeIndex === 0 ? '[ 免费观看 ]' : '[ 请购买后观看 ]' }}</div>

          <!--清楚浮动样式-->
          <div style="clear:both;"></div>

        </div>

      </div>

    </article>

  </section>

</template>

<script setup>

import {computed} from "vue";

// props: 接收父组件传递过来的值
let props = defineProps({
  chapters: {
    type: Array,
    required: true,
    default: [
      {
        "id": 1,
        "title": "Java学前准备",
        "info": "",
        "episodes": [
          {
            "id": 1,
            "title": "我为何选择Java",
            "url": "EP1-1.mp4",
            "info": "",
            "cover-image": "EP1-1-cover.jpg",
            "index-in-chapter": 1,
            "chapter-id": 1,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 2,
            "title": "我该如何学Java",
            "url": "EP1-2.mp4",
            "info": "",
            "cover-image": "EP1-2-cover.jpg",
            "index-in-chapter": 2,
            "chapter-id": 1,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 3,
            "title": "何时开始学Java",
            "url": "EP1-3.mp4",
            "info": "",
            "cover-image": "EP1-3-cover.jpg",
            "index-in-chapter": 3,
            "chapter-id": 1,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          }
        ],
        "index-in-video": 1,
        "video-id": 1,
        "create-time": "2022-12-29 06:23:05",
        "last-modify": "2022-12-29 06:23:05"
      },
      {
        "id": 2,
        "title": "Java环境搭建",
        "info": "",
        "episodes": [
          {
            "id": 4,
            "title": "安装JDK和JRE",
            "url": "EP2-1.mp4",
            "info": "",
            "cover-image": "EP2-1-cover.jpg",
            "index-in-chapter": 1,
            "chapter-id": 2,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 5,
            "title": "配置Path变量",
            "url": "EP2-2.mp4",
            "info": "",
            "cover-image": "EP2-2-cover.jpg",
            "index-in-chapter": 2,
            "chapter-id": 2,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 6,
            "title": "配置JAVA_HOME变量",
            "url": "EP2-3.mp4",
            "info": "",
            "cover-image": "EP2-3-cover.jpg",
            "index-in-chapter": 3,
            "chapter-id": 2,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 7,
            "title": "开发HelloWorld程序",
            "url": "EP2-4.mp4",
            "info": "",
            "cover-image": "EP2-4-cover.jpg",
            "index-in-chapter": 4,
            "chapter-id": 2,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 8,
            "title": "详解HelloWorld代码",
            "url": "EP2-5.mp4",
            "info": "",
            "cover-image": "EP2-5-cover.jpg",
            "index-in-chapter": 5,
            "chapter-id": 2,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          }
        ],
        "index-in-video": 2,
        "video-id": 1,
        "create-time": "2022-12-29 06:23:05",
        "last-modify": "2022-12-29 06:23:05"
      },
      {
        "id": 3,
        "title": "集成开发环境",
        "info": "",
        "episodes": [
          {
            "id": 9,
            "title": "IDEA环境安装",
            "url": "EP3-1.mp4",
            "info": "",
            "cover-image": "EP3-1-cover.jpg",
            "index-in-chapter": 1,
            "chapter-id": 3,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 10,
            "title": "IDEA创建项目",
            "url": "EP3-2.mp4",
            "info": "",
            "cover-image": "EP3-2-cover.jpg",
            "index-in-chapter": 2,
            "chapter-id": 3,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 11,
            "title": "IDEA基础配置",
            "url": "EP3-3.mp4",
            "info": "",
            "cover-image": "EP3-3-cover.jpg",
            "index-in-chapter": 3,
            "chapter-id": 3,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 12,
            "title": "IDEA插件安装",
            "url": "EP3-4.mp4",
            "info": "",
            "cover-image": "EP3-4-cover.jpg",
            "index-in-chapter": 4,
            "chapter-id": 3,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          }
        ],
        "index-in-video": 3,
        "video-id": 1,
        "create-time": "2022-12-29 06:23:05",
        "last-modify": "2022-12-29 06:23:05"
      },
      {
        "id": 4,
        "title": "使用Maven工具",
        "info": "",
        "episodes": [
          {
            "id": 13,
            "title": "安装Maven工具",
            "url": "EP4-1.mp4",
            "info": "",
            "cover-image": "EP4-1-cover.jpg",
            "index-in-chapter": 1,
            "chapter-id": 4,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 14,
            "title": "搭建Maven仓库",
            "url": "EP4-2.mp4",
            "info": "",
            "cover-image": "EP4-2-cover.jpg",
            "index-in-chapter": 2,
            "chapter-id": 4,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 15,
            "title": "整合Maven到IDEA",
            "url": "EP4-3.mp4",
            "info": "",
            "cover-image": "EP4-3-cover.jpg",
            "index-in-chapter": 3,
            "chapter-id": 4,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 16,
            "title": "创建Maven父项目",
            "url": "EP4-4.mp4",
            "info": "",
            "cover-image": "EP4-4-cover.jpg",
            "index-in-chapter": 4,
            "chapter-id": 4,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 17,
            "title": "创建Maven子项目",
            "url": "EP4-5.mp4",
            "info": "",
            "cover-image": "EP4-5-cover.jpg",
            "index-in-chapter": 5,
            "chapter-id": 4,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 18,
            "title": "使用Junit单元测试",
            "url": "EP4-6.mp4",
            "info": "",
            "cover-image": "EP4-6-cover.jpg",
            "index-in-chapter": 6,
            "chapter-id": 4,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 19,
            "title": "三方依赖配置详解",
            "url": "EP4-7.mp4",
            "info": "",
            "cover-image": "EP4-7-cover.jpg",
            "index-in-chapter": 7,
            "chapter-id": 4,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          }
        ],
        "index-in-video": 4,
        "video-id": 1,
        "create-time": "2022-12-29 06:23:05",
        "last-modify": "2022-12-29 06:23:05"
      },
      {
        "id": 5,
        "title": "使用Git和GitEE",
        "info": "",
        "episodes": [
          {
            "id": 20,
            "title": "Git存储分区介绍",
            "url": "EP5-1.mp4",
            "info": "",
            "cover-image": "EP5-1-cover.jpg",
            "index-in-chapter": 1,
            "chapter-id": 5,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 21,
            "title": "安装并整合Git服务端",
            "url": "EP5-2.mp4",
            "info": "",
            "cover-image": "EP5-2-cover.jpg",
            "index-in-chapter": 2,
            "chapter-id": 5,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 22,
            "title": "整合GitEE代码托管中心",
            "url": "EP5-3.mp4",
            "info": "",
            "cover-image": "EP5-3-cover.jpg",
            "index-in-chapter": 3,
            "chapter-id": 5,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 23,
            "title": "赵四分享项目到GitEE",
            "url": "EP5-4.mp4",
            "info": "",
            "cover-image": "EP5-4-cover.jpg",
            "index-in-chapter": 4,
            "chapter-id": 5,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 24,
            "title": "赵四邀请刘能加入团队",
            "url": "EP5-5.mp4",
            "info": "",
            "cover-image": "EP5-5-cover.jpg",
            "index-in-chapter": 5,
            "chapter-id": 5,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 25,
            "title": "刘能克隆项目到本地",
            "url": "EP5-6.mp4",
            "info": "",
            "cover-image": "EP5-6-cover.jpg",
            "index-in-chapter": 6,
            "chapter-id": 5,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          },
          {
            "id": 26,
            "title": "刘能修改代码并Push",
            "url": "EP5-7.mp4",
            "info": "",
            "cover-image": "EP5-7-cover.jpg",
            "index-in-chapter": 7,
            "chapter-id": 5,
            "create-time": "2022-12-29 06:23:06",
            "last-modify": "2022-12-29 06:23:06"
          }
        ],
        "index-in-video": 5,
        "video-id": 1,
        "create-time": "2022-12-29 06:23:05",
        "last-modify": "2022-12-29 06:23:05"
      }
    ],
  }
})

// computed: 计算索引，将小于10的索引补充 "0" 作为前缀
let computedIndex = computed(() => index => index < 10 ? "0" + index : "" + index);

</script>

<style lang="scss" scoped>

.tab-catalog-body {

  text-align: left; // 水平居左
  padding-bottom: 50px; // 下内边距

  /*章*/
  .chapter {
    padding: 10px; // 内边距
    font-weight: bolder; // 字体加粗
  }

  /*集*/
  .episode {

    padding-left: 20px; // 左内边距
    color: white; // 前景色
    margin: 10px; // 外边距

    /*集的标题*/
    .episode-title {
      float: left; // 左浮动
    }

    /*集的状态提示*/
    .episode-state {
      float: right; // 右浮动
      padding-right: 20px; // 右内边郡
      color: #ff792f; // 前景色
      font-size: small; // 字号小一级
    }

  }

}

</style>