<template>

  <header class="player-header">

    <!--引入通用头子组件-->
    <common-header title="观看已购买的视频"/>

  </header>

  <section class="player-body">

    <el-row :gutter="20">

      <!--视频播放器和弹幕区-->
      <el-col :span="15" :xs="24">

        <!--当前正在播放的集的标题-->
        <h4 class="episode-title">当前播放: {{ currentEpisode['title'] }}</h4>

        <!--西瓜视频播放器容器-->
        <article id="video-content"></article>

        <!--弹幕区域-->
        <article class="danmu-art">

          <!--弹幕表单-->
          <!--
            :inline="true": 内联表单
          -->
          <el-form :inline="true">

            <!--弹幕表单项目: 包含输入控件和按钮-->
            <el-form-item class="danmu-form-item">

              <!--弹幕输入控件-->
              <!--
                :disabled="danmuDisabled": 由danmuDisabled决定该控件是否可用
                v-model="danMuOpt['text']": 双向绑定 danMuOpt['text'] 属性
              -->
              <el-input placeholder="在此输入弹幕.."
                        :disabled="danmuDisabled"
                        v-model="danMuOpt['text']">

                <!--后插槽-->
                <template #append>

                  <!--发送弹幕按钮-->
                  <!--
                    :disabled="danmuDisabled": 由danmuDisabled决定该控件是否可用
                    v-model="danMuOpt['text']": 双向绑定 danMuOpt['text'] 属性
                  -->
                  <el-button type="primary"
                             :disabled="danmuDisabled"
                             @click="sendDanMuToWebSocket">
                    发弹幕
                  </el-button>

                </template>

              </el-input>

            </el-form-item>

            <!--弹幕表单项目: 颜色选择器-->
            <el-form-item class="danmu-form-item">

              <!--颜色选择器控件-->
              <!--
                :disabled="danmuDisabled": 由danmuDisabled决定该控件是否可用
                v-model="danMuOpt['color']": 双向绑定 danMuOpt['color'] 属性
              -->
              <el-color-picker :disabled="danmuDisabled" v-model="danMuOpt['color']"/>

            </el-form-item>

          </el-form>

        </article>

      </el-col>

      <!--视频章集列表-->
      <el-col :span="9" :xs="24">

        <!--视频章集列表标题-->
        <h4 class="chapters-title">章集列表</h4>

        <!--树形结构-->
        <!--
          :data="video['chapters']": 数据来源
          :props="chapterTree": 节点结构，一级节点展示章的title值, 二级节点展示章内全部集的title值
          highlight-current: 对当前选中的父/子节点进行高亮
          @node-click="changeEpisode": 当点击子节点时触发changeEpisode方法
          accordion: 每次只展开一个父节点
        -->
        <el-tree :data="video['chapters']"
                 :props="chapterTree"
                 highlight-current
                 accordion
                 @node-click="changeEpisode" class="chapters-col"/>

      </el-col>

    </el-row>

  </section>

</template>

<script setup>

import CommonHeader from "@/components/common-header";
import {VIDEO_SELECT_DETAIL_BY_VIDEO_ID} from "@/api";
import {nginxVideo, nginxVideoCover, lessonDanmuServer} from "@/global_variable";
import XGPlayer from 'xgplayer';
import router from "@/router";
import {useStore} from 'vuex';
import {ref, onMounted, shallowReactive} from "vue";
import {ElMessage} from "element-plus";

// data: 用户登录状态
let loginFlag = useStore().state['loginFlag'];

// data: 视频ID
const videoId = router.currentRoute.value.query['video-id'];

// data: 视频实例
let video = ref({});

// data: Tree控件的配置选项
let chapterTree = shallowReactive({
  label: 'title', // 指定节点展示的内容为 `章` 节点对象的 `title` 属性值
  children: 'episodes' // 指定子树为 `章` 节点对象的 `episodes` 属性值
});

// data: 西瓜视频播放器实例
let xgPlayer = null;

// data: 当前播放的视频集
let currentEpisode = ref({});

// data: WebSocket实例
let webSocket = null;

// data: 用户ID
const userId = sessionStorage.getItem('user-id');

// data: 是否禁用弹幕相关控件，未播放时不允许发弹幕，已播放时允许发弹幕
let danmuDisabled = ref(true);

// data: 发送的弹幕配置：包括内容和颜色
let danMuOpt = shallowReactive({
  text: '',
  color: '#409EFF'
});

// method: 查询视频详细信息
let selectVideoDetailById = async (videoId) => {
  let resp = await VIDEO_SELECT_DETAIL_BY_VIDEO_ID(videoId);
  try {
    if (resp['data']['code'] > 0) {
      video.value = resp['data']['data'];
      currentEpisode.value = resp['data']['data']['chapters'][0]['episodes'][0];
      initXgPlayer();
    }
  } catch (e) {
    console.error(e)
  }
}

// method: 初始化视频播放器
let initXgPlayer = () => {

  // 欢迎弹幕
  let welcomeDanMu = {
    id: Math.random().toString(36).slice(-10), // 弹幕id，需要唯一，将随机小数转成36进制字符串并截取后10位
    start: 3000, // 弹幕在3秒后出现
    duration: 8000, // 弹幕在8秒后消失
    txt: "前方高能：一大堆弹幕即将来临..", // 弹幕内容
    mode: "scroll", // 弹幕出现方式
    style: { // 弹幕样式
      color: 'red',
      fontSize: '20px',
      padding: '20px'
    }
  }

  // 创建西瓜视频播放器实例
  xgPlayer = new XGPlayer({
    id: "video-content", // 视频容器的ID值
    url: nginxVideo + currentEpisode.value['url'], // 视频对象
    poster: nginxVideoCover + video.value['cover-image'], // 视频封面
    fluid: true, // 使用流体布局
    playbackRate: [0.5, 1, 1.5, 2], // 倍速播放设置
    download: false, // 不允许下载视频
    pip: false, // 不开启画中画
    danmu: {
      comments: [welcomeDanMu]
    }
  });

  // 当西瓜视频播放器播放结束时触发
  xgPlayer.on('ended', () => webSocket.close());

  // 当西瓜视频播放器播放时触发
  xgPlayer.on('play', () => {
    danmuDisabled.value = false;
    // 调用初始化WebSocket对象的方法
    initWebSocket();
  });

  // 当西瓜视频播放器暂停时触发
  xgPlayer.on('pause', () => danmuDisabled.value = true);

}

// method: 当点击每一集时，切换播放的视频
let changeEpisode = (node) => {

  // 若用户点击的是Chapter节点而不是Episode节点，则无需任何操作
  if (!node['url']) {
    return;
  }

  // 仅在当前视频播放器正在播放时允许切换视频集操作
  if (!xgPlayer['_hasStart']) {
    return;
  }

  // 切换西瓜视频播放器中的 `src` 属性
  xgPlayer.src = nginxVideo + node['url'];

  // 切换当前播放的视频集的标题内容
  currentEpisode.value['title'] = node['title'];

  // 播放器重新播放
  xgPlayer.replay();

};

// method: 发布用户弹幕到视频播放器
let publishDanMuToPlayer = (userMessage) => {

  // 配置用户弹幕
  let userDanMu = {
    id: Math.random().toString(36).slice(-10), // 弹幕id，需要唯一，将随机小数转成36进制字符串并截取后10位
    duration: 8000, // 弹幕在8秒后消失
    txt: userMessage['text'], // 弹幕内容
    mode: "scroll", // 弹幕出现方式
    style: { // 弹幕样式
      color: userMessage['color']
    }
  };

  // 发布用户弹幕到视频播放器上
  xgPlayer.danmu.sendComment(userDanMu);

}

// method: 初始化WebSocket实例
let initWebSocket = () => {

  // 判断浏览器是否支持WebSocket功能
  if (!WebSocket) {
    ElMessage.warning("您的浏览器不支持WebSocket！");
    return;
  }

  // 使用ws://协议连接WebSocket服务端并传递用户ID参数
  webSocket = new WebSocket(lessonDanmuServer + userId);

  // 成功连接WebSocket服务端时触发
  webSocket.onopen = () => console.log("WebSocket上线..");

  // 成功断开WebSocket服务端时触发
  webSocket.onclose = () => console.log("WebSocket下线..");

  // 异常连接WebSocket服务端时触发
  webSocket.onerror = () => console.error("WebSocket异常..");

  // 接收到WebSocket服务端消息时时触发
  webSocket.onmessage = resp => {

    // 调用弹幕发布方法
    publishDanMuToPlayer(JSON.parse(resp['data']));

    // 弹幕输入控件清空
    danMuOpt['text'] = "";

  };

}

// method: 发送弹幕到WebSocket服务器
let sendDanMuToWebSocket = () => {

  // 弹幕非空校验
  if ('' === danMuOpt['text']) {
    ElMessage.warning("弹幕不能为空！");
    return;
  }

  // 发送弹幕到WebSocket服务端
  webSocket.send(JSON.stringify(danMuOpt));
}

// mounted: 页面加载完毕后，判断用户是否登录，若登录，则立刻调用 `selectVideoDetailById()` 方法
onMounted(() => {

  // 未登录保护
  if (!loginFlag) {
    ElMessage.warning("请先登录！");
    setTimeout(() => router.push("/login"), 2000);
    return;
  }

  selectVideoDetailById(videoId);

});

</script>

<style lang="scss" scoped>

.player-body {

  padding: 20px; // 内边距

  /*章节树型结构*/
  .chapters-col {
    border: 1px solid orange; // 边框
    box-sizing: border-box; // 忽略边框和内边距
  }

  /*当前集的标题，树型结构中章的标题*/
  .episode-title, .chapters-title {
    text-align: left; // 内容居左
  }

  /*弹幕区*/
  .danmu-art {

    margin: 20px auto; // 上下外边距 左右自居中

    /*弹幕表单项*/
    .danmu-form-item {
      margin: 0; // 外边距
    }

  }

}

</style>