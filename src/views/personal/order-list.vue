<template>

  <section class="order-list-header">

    <!--引入通用头组件-->
    <common-header title="我购买的视频"/>

  </section>

  <section class="order-list-body">

    <!--当用户有订单时展示此容器-->
    <!--
      v-if="videoOrders.length > 0": 判断当前用户是否有订单记录
      v-for="videoOrder in videoOrders": 遍历当前用户的订单记录
    -->
    <article class="order-item"
             v-if="videoOrders.length > 0"
             v-for="videoOrder in videoOrders" >

      <!--订单号-->
      <div class="order-number">订单号: {{ videoOrder['order']['number'] }}</div>

      <el-row>

        <el-col :span="8">

          <!--视频封面图-->
          <!--
            :src="nginxSrc(videoOrder['video']['cover-image'])": 封面图的Nginx代理地址
          -->
          <el-image :src="nginxSrc(videoOrder['video']['cover-image'])" class="cover-image"/>

        </el-col>

        <el-col :span="16">

          <div class="info-head">标题：{{ videoOrder['video']['title'] }}</div>
          <div class="info-body">作者：{{ videoOrder['video']['author'] }}</div>
          <div class="info-body">评分：{{ videoOrder['video']['star'] }} 分</div>
          <div class="info-body">单价：{{ videoOrder['video']['price'] }} RMB</div>
          <div class="info-body">时间：{{ videoOrder['order']['create-time'] }}</div>
          <div class="info-foot">

            <!--播放视频按钮-->
            <!--
              @click="playVideo(videoOrder['video']['id'])": 当点击时触发playVideo方法
            -->
            <el-link type="warning" class="play-btn" @click="playVideo(videoOrder['video']['id'])">播放视频</el-link>

            <!--从订单中删除视频-->
            <!--
              @click="deleteOrder(videoOrder['order']['id'])": 当点击时触发deleteVideoOrder方法
            -->
            <el-link type="danger" class="remove-btn"
                     @click="deleteVideoOrder(videoOrder['order']['id'], videoOrder['id'])">[删除视频]
            </el-link>

            <!--删除订单-->
            <!--
              @click="deleteOrder(videoOrder['order']['id'])": 当点击时触发deleteOrder方法
            -->
            <el-link type="danger" class="remove-btn" @click="deleteOrder(videoOrder['order']['id'])">删除订单</el-link>

          </div>

        </el-col>

      </el-row>

      <!--分割线-->
      <el-divider/>

    </article>

    <!--当用户无订单时展示此容器-->
    <article v-else>

      <p class="no-order-tips">您暂未购买课程</p>

    </article>

  </section>

  <section class="order-list-footer">

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
                     :total="orderPageInfo['total']"
                     :page-sizes="[2, 6, 8]"
                     :page-size="orderPageInfo['page-size']"
                     :current-page="orderPageInfo['page-num']"
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
                     :total="orderPageInfo['total']"
                     :page-size="orderPageInfo['page-size']"
                     :current-page="orderPageInfo['page-num']"
                     @current-change="changePage"/>

    </article>

  </section>

</template>

<script setup>

import CommonHeader from "@/components/common-header";
import {ORDER_DELETE_BY_ORDER_API, ORDER_PAGE_DETAIL_BY_USER_ID_API} from "@/api";
import {nginxVideoCover} from '@/global_variable';
import router from "@/router";
import {onMounted, ref, computed, shallowReactive} from "vue";
import {ElMessage} from "element-plus";
import {useStore} from "vuex";

// data: vuex实例
const vuex = useStore();

// data: 用户登录状态
const loginFlag = vuex.state['loginFlag'];

// data: 从sessionStorage中取出用户主键
const userId = sessionStorage.getItem('user-id');

// data: 用户订单列表
let videoOrders = ref([]);

// data: 用户订单列表分页数据
let orderPageInfo = shallowReactive({
  'page-num': 1,
  'page-size': 6,
  'total': 0
});

// computed: 拼接Nginx视频封面目录前缀
const nginxSrc = computed(() => src => nginxVideoCover + src);

// method: 获取订单列表
let pageDetailByUserId = async (userId, page, size) => {
  try {
    const resp = await ORDER_PAGE_DETAIL_BY_USER_ID_API(userId, page, size);
    if (resp['data']['code'] > 0) {
      let data = resp['data']['data'];
      videoOrders.value = data['video-orders'];
      orderPageInfo['page-num'] = data['page-num']
      orderPageInfo['page-size'] = data['page-size']
      orderPageInfo['total'] = data['total']
    } else {
      console.error(resp['data']['message']);
    }
  } catch (e) {
    console.error(e);
  }
}

// method: 删除订单中的某个视频
let deleteVideoOrder = async (orderId, videoOrderId) => {

  try {

    // 危险操作保护
    if (!confirm("您将要删除这个视频，确定吗？")) return false;

    // 准备请求参数
    let params = {
      "video-order-id": videoOrderId,
      "order-id": orderId
    };

    // 异步调用接口：按 `订单ID` 和 `视频订单中间表ID` 删除 `视频订单中间表` 记录
    const resp = await VIDEO_ORDER_DELETE_BY_VIDEO_ORDER_API(params);
    if (resp['data']['code'] > 0) {
      ElMessage('订单删除成功');

      // 当删除成功后，重新调用分页查询方法
      await pageDetailByUserId(userId, orderPageInfo['page-num'], orderPageInfo['page-size']);

    } else console.error(resp['data']['message']);
  } catch (e) {
    console.error(e);
  }
}

// method: 删除订单
let deleteOrder = async (orderId) => {

  if (!confirm("确定要删除订单吗？")) {
    return false;
  }

  try {
    let params = {"order-id": orderId};
    const resp = await ORDER_DELETE_BY_ORDER_API(params);
    if (resp['data']['code'] > 0) {
      ElMessage('订单删除成功');
      videoOrders.value = videoOrders.value.filter(videoOrder => videoOrder['order']['id'] !== orderId);
    } else {
      console.log(resp['data']['message']);
    }
  } catch (e) {
    console.error(e);
  }
}

// method: 播放视频
let playVideo = videoId => {
  router.push({
    'path': '/player',
    'query': {'video-id': videoId}
  })
}

// method: 当前显示第几页被改变时触发
let changePage = page => {
  orderPageInfo['page-num'] = page;
  pageDetailByUserId(userId, page, orderPageInfo['page-size']);
};

// method: 每页显示多少条被改变时触发：
let changeSize = size => {
  orderPageInfo['page-size'] = size;
  pageDetailByUserId(userId, orderPageInfo['page-num'], size);
};

// mounted: 页面加载完毕后，判断用户是否登录，若登录，则立刻调用 `pageDetailByUserId()` 方法
onMounted(() => {

  // 未登录保护
  if (!loginFlag) {

    ElMessage('请先登录！');

    // 两秒之后跳入登录组件
    setTimeout(() => router.push("/login"), 2000);

    return;
  }

  pageDetailByUserId(userId, 1, 6);

});

</script>

<style lang="scss" scoped>

.order-list-body {

  padding: 20px;
  text-align: left;

  .order-number {
    color: gray;
    padding: 10px 0;
    font-size: small;
  }

  .cover-image {
    border: 1px solid black;
    width: 100px;
    height: 100px;
    box-sizing: border-box;
  }

  .info-head {
    height: 30px;
  }

  .info-body {
    font-family: 楷体, serif;
    color: gray;
  }

  .info-foot {
    padding-top: 20px;
    text-align: right;

    .play-btn {
      padding-right: 10px;
    }

    .remove-btn {
      padding-right: 5px;
    }
  }

  .no-order-tips {
    color: gray;
    text-align: center;
  }
}

.order-list-footer {

  padding: 20px 0; // 上下外边距 左右外边距

  /*分页按钮*/
  .el-pagination {
    justify-content: center; // Flex布局下，子元素主轴方向居中
  }

}

</style>