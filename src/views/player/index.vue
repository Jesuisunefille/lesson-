<template>

  <header class="player-header">

    <!--引入通用头子组件-->
    <common-header title="观看已购买的视频"/>

  </header>

  <section class="player-body">

    <el-row :gutter="20">

      <el-col :span="15" :xs="24">

        <!--当前正在播放的集的标题-->
        <h4 class="episode-title">当前播放: {{ currentEpisode.value ? currentEpisode.value['title'] : '无' }}</h4>

        <!--西瓜视频播放器容器-->
        <article id="video-content"></article>

        <!--弹幕区域-->
        <article class="danmu-art">

          <el-form :inline="true">

            <!--颜色选择控件-->
            <!--
              :disabled="playDisabled": 绑定playDisabled属性
              v-model="danMuOpt['value']": 双向绑定danMuOpt['value']属性
            -->

            <el-form-item class="danmu-form-item">
              <el-input :disabled="playDisabled" v-model="danMuOpt['value']" placeholder="在此输入弹幕..">
                <template #append>
                  <el-button :disabled="playDisabled" type="primary" @click="sendDanMu">发弹幕</el-button>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item class="danmu-form-item">
              <el-color-picker :disabled="playDisabled" v-model="danMuOpt['color']"/>
            </el-form-item>

          </el-form>

        </article>

      </el-col>

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
import {ref, onMounted, shallowReactive, reactive} from "vue";
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
let currentEpisode = reactive({});

// data: Socket实例
let socket = null;

// data: 用户ID
const userId = sessionStorage.getItem('user-id');

// data: 是否禁用弹幕相关控件，未播放时不允许发弹幕，已播放时允许发弹幕
let playDisabled = ref(true);

// data: 发送的弹幕配置：包括内容和颜色
let danMuOpt = shallowReactive({
  value: '',
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

  xgPlayer.on('ended', () => socket.close());
  xgPlayer.on('play', () => {
    playDisabled.value = false;
    initSocket();
  });
  xgPlayer.on('pause', () => playDisabled.value = true);

}

// method: 当点击每一集时，切换playUrl的值为当前集视频的地址
let changeEpisode = (episode) => {
  if (episode['url'] && xgPlayer['_hasStart']) {
    xgPlayer.src = nginxVideo + episode['url'];
    currentEpisode.value['title'] = episode['title'];
    xgPlayer.replay();
  }
};

// method: 初始化Socket
let initSocket = () => {

  // 初始化socket连接和事件监听
  if (!WebSocket) {
    ElMessage("您的浏览器不支持WebSocket！");
    return;
  }

  // 使用ws://协议连接S端，并传递用户的token过去，这里可以改为传递id
  socket = new WebSocket(lessonDanmuServer + userId);
  socket.onopen = () => console.log("socket成功上线..");
  socket.onclose = () => console.log("socket成功下线..");
  socket.onerror = () => console.log("socket通信异常..");
  socket.onmessage = resp => {
    let data = JSON.parse(resp['data']);
    player.danmu.sendComment({
      duration: 10000,
      id: Math.random().toString(36).slice(-10),
      txt: data['value'],
      style: {
        color: data['color']
      }
    });
    danMuOpt['value'] = "";
  };
}

// method: 发送弹幕时触发
let sendDanMu = () => {
  if (danMuOpt['value'] === '') {
    ElMessage("弹幕不能为空！");
    return;
  }
  socket.send(JSON.stringify(danMuOpt));
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

  .danmu-art {
    margin: 20px auto;

    .danmu-form-item {
      margin: 0;
    }
  }

}

</style>