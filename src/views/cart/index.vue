<template>

  <header class="cart-header">

    <!--引入通用头子组件-->
    <common-header title="我的购物车"/>

  </header>

  <section class="cart-body">

    <!--如果当前用户存在购物车数据则展示此容器-->
    <article v-if="carts.length > 0">

      <div v-for="cart in carts" class="cart-item">

        <el-row>

          <el-col :span="2">

            <!--多选按钮组-->
            <!--
              v-model="videos": 双向绑定videos属性
            -->
            <el-checkbox-group v-model="videos" class="checkbox-group">

              <!--多选按钮-->
              <!--
                :label="cart": 每个多选按钮的值都绑定了cart属性
                <br>: 用于隐藏label文字
              -->
              <el-checkbox :label="cart"><br></el-checkbox>

            </el-checkbox-group>
          </el-col>

          <el-col :span="8" v-if="cart['cover-image']">
            <el-image :src="nginxSrc(cart['cover-image'])" class="cover-image"/>
          </el-col>

          <el-col :span="14">

            <div class="info-head">{{ cart['title'] }}</div>

            <div class="info-body">{{ cart['author'] }} 老师</div>

            <div class="info-foot">

              <el-link type="primary" class="info-price">￥{{ cart['price'] }} RMB</el-link>

              <!--按钮: 移出购物车-->
              <!--
                @click="removeFromCart(cart['video-id'])": 点击时触发removeFromCart方法并传递视频ID
              -->
              <el-link type="danger" class="remove-btn" @click="removeFromCart(cart['video-id'])">移出购物车</el-link>

            </div>

          </el-col>

        </el-row>

        <!--分隔线-->
        <el-divider/>

      </div>

    </article>

    <!--如果当前用户不存在购物车数据则展示此容器-->
    <article v-else>购物车为空</article>

  </section>

  <footer class="cart-footer">

    <!--立即结算区域-->
    <article class="cart-pay">

      <!--购物车总金额-->
      <div class="total-fee">
        <span>合计: ￥{{ totalFee }}</span><br/>
        <small class="total-fee-discount">优惠减:￥0.00</small>
      </div>

      <!--立即结算按钮-->
      <!--
        @click="toPay": 点击时触发toPay方法
      -->
      <div class="pay-btn">
        <el-button type="danger" size="large" @click="toPay">立即结算</el-button>
      </div>

    </article>

    <!--引入通用脚子组件-->
    <common-footer/>

  </footer>

</template>

<script setup>

import CommonFooter from '@/components/common-footer';
import CommonHeader from '@/components/common-header';
import {CART_SELECT_BY_USER_ID_API, CART_DELETE_BY_USER_ID_AND_VIDEO_IDS_API, ORDER_INSERT_API} from "@/api";
import {onMounted, ref, computed} from "vue";
import {useStore} from 'vuex';
import {ElMessage} from "element-plus";
import {nginxVideoCover} from '@/global_variable';
import router from "@/router";

// data: Vuex实例
const vuex = useStore();

// 用户ID
let userId = sessionStorage.getItem('user-id');

// data: 用户登录状态
const loginFlag = vuex.state['loginFlag'];

// 购物车列表实例
let carts = ref([]);

// 当前选中的视频实例
let videos = ref([]);

// computed: 拼接Nginx视频封面代理目录前缀
const nginxSrc = computed(() => src => nginxVideoCover + src);

// computed: 计算当前金额
const totalFee = computed(() => {
  let result = 0;
  videos.value.forEach(video => result += video['price']);
  return result;
});

// method: 获取购物车列表
let selectCartByUserId = async (userId) => {
  const resp = await CART_SELECT_BY_USER_ID_API(userId);
  if (resp['data']['code'] > 0) {
    let data = resp['data']['data'];
    for (let key in data) {
      carts.value.push(JSON.parse(data[key]))
    }
  } else console.error(resp['data']['message']);
}

// method: 将选中的视频商品移出购物车
let removeFromCart = async (videoId) => {

  // 危险操作保护
  if (!confirm("选中的视频商品将被移出购物车，确定吗？")) return false;

  let params = {
    'video-ids': [videoId],
    'user-id': userId
  }
  let resp = await CART_DELETE_BY_USER_ID_AND_VIDEO_IDS_API(params);
  try {
    if (resp['data']['code'] > 0) {
      ElMessage.success('删除成功');
      location.reload();
    } else ElMessage.error(resp['data']['message']);
  } catch (e) {
    console.error(e)
  }

};

// method: 立即结算
let toPay = async () => {

  let videoIds = [];
  videos.value.forEach(value => videoIds.push(value['video-id']))

  let params = {
    'video-ids': videoIds,
    'total-fee': totalFee.value,
    'user-id': userId,
    'info': '测试订单备注'
  }

  try {
    const resp = await ORDER_INSERT_API(params);
    if (resp['data']['code'] > 0) {
      CART_DELETE_BY_USER_ID_AND_VIDEO_IDS_API(params).then(resp => {
        if (resp['data']['code'] > 0) {
          ElMessage.success('支付成功');
          router.push("/personal");
        } else ElMessage.error(resp['data']['message']);

      }).catch(e => console.error(e));
    } else {
      ElMessage.error(resp['data']['message']);
    }
  } catch (e) {
    console.error(e)
  }

}

// mounted: 页面加载完毕后，立刻调用 `selectCartByUserId()` 方法
onMounted(() => {

  // 未登录保护
  if (!loginFlag) {

    ElMessage.warning('请先登录！');

    // 两秒之后跳入登录组件
    setTimeout(() => router.push("/login"), 2000);

    return;
  }

  selectCartByUserId(userId);

})

</script>

<style lang="scss" scoped>

.cart-body {

  padding-top: 20px; // 上内边距
  margin-bottom: 100px; // 下外边距

  /*每个购物车展示项*/
  .cart-item {

    padding: 10px; // 内边距
    text-align: left; // 内容居左

    /*多选按钮组*/
    .checkbox-group {
      line-height: 115px; // 行高
    }

    /*视频封面图*/
    .cover-image {
      border: 1px solid black; // 边框
      width: 100px; // 宽度
      height: 100px; // 高度
      box-sizing: border-box; // 忽略边框和内边距
    }

    /*视频标题*/
    .info-head {
      height: 40px; // 高度
    }

    /*视频作者*/
    .info-body {
      font-family: 楷体, serif; // 字体
      color: gray; // 前景色
    }

    /*视频价格*/
    .info-foot {

      padding-top: 20px; // 上内边距

      /*移出按钮*/
      .remove-btn {
        float: right; // 右浮动
        padding-right: 5px; // 右内边距
      }

    }

  }

}

.cart-footer {

  /*立即结算区域*/
  .cart-pay {

    position: fixed; // 固定定位
    bottom: 60px; // 下左边
    width: 100%; // 宽度

    /*总金额*/
    .total-fee {

      text-align: left; // 内容局左
      padding-left: 20px; // 左内边距
      float: left; // 左浮动

      /*优惠金额*/
      .total-fee-discount {
        color: gray; // 前景色
      }

    }

    /*立即结算按钮*/
    .pay-btn {
      float: right; // 右浮动
      padding-right: 20px; // 左内边距
    }

  }

}

</style>