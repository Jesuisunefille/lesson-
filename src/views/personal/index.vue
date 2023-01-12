<template>

  <header class="personal-header">

    <!--使用通用头子组件-->
    <common-header title="个人信息"/>

  </header>

  <section class="personal-body">

    <!--用户已登陆时展示此容器-->
    <article v-if="loginFlag">

      <!--用户基本信息：包括头像，昵称和描述-->
      <article class="header-head">

        <!--用户头像-->
        <!--
          :src="ossSrc(user['avatar']): 计算头像在OSS服务器中的真实地址
          :preview-src-list="getPreviewList(user['avatar'])": 计算该头像所有大图预览地址
        -->
        <el-image :src="ossSrc(user['avatar'])"
                  :preview-src-list="getPreviewList(user['avatar'])"
                  class="avatar-image"/>

        <!--用户昵称-->
        <p class="nick-name">{{ user["nick-name"] }}</p>

        <!--用户描述-->
        <p class="user-info">{{ user["info"] }}</p>

      </article>

      <!--功能按钮-->
      <article class="header-body">

        <el-button type="primary" class="opera-btn" @click="">查询积分</el-button>
        <el-button type="primary" class="opera-btn" @click="router.push('/order-list')">查询订单</el-button>
        <el-button type="warning" class="opera-btn" @click="router.push('/user-update')">修改信息</el-button>
        <el-button type="warning" class="opera-btn" @click="router.push('/user-update-avatar')">修改头像</el-button>
        <el-button type="warning" class="opera-btn" @click="router.push('/user-update-password')">重置密码</el-button>
        <el-button type="warning" class="opera-btn" @click="ElMessage.info('暂未开通服务')">重置手机</el-button>
        <el-button type="danger" class="opera-btn" @click="logout">注销账户</el-button>
        <el-button type="danger" class="opera-btn" @click="logout">退出登录</el-button>

      </article>

      <!--      <article class="header-foot">-->
      <!--        <order-list></order-list>-->
      <!--      </article>-->

    </article>

    <!--用户未登陆时展示此容器-->
    <article v-else>

      <!--用户基本信息：包括默认头像和未登录提示-->
      <article class="header-head">

        <!--用户讲哦人头像-->
        <el-image :src="require('@/assets/default-avatar.jpg')" class="avatar-image"/>

        <!--未登录提示-->
        <p>您暂未登录..</p>

      </article>

      <!--功能按钮-->
      <article class="header-body">

        <!--按钮-登录-->
        <!--
          @click="router.push('/login')": 点击时跳入Login组件
        -->
        <el-button @click="router.push('/login')" type="danger" class="login-btn">立刻登录</el-button>

      </article>

    </article>

  </section>

  <footer class="personal-footer">

    <!--使用通用头脚组件-->
    <common-footer/>

  </footer>

</template>

<script setup>
import CommonFooter from "@/components/common-footer";
import CommonHeader from "@/components/common-header";
import {USER_DELETE_BY_USER_ID_API, USER_SELECT_BY_USER_ID_API} from "@/api";
import router from "@/router";
import {useStore} from 'vuex';
import {computed, onMounted, ref} from "vue";
import {ossUserAvatar} from "@/global_variable";
import {ElMessage} from "element-plus";

import OrderList from "@/views/personal/order-list";

// data: Vuex实例
const vuex = useStore();

// 用户ID
const userId = sessionStorage.getItem('user-id');

// data: 用户登录状态
const loginFlag = vuex.state['loginFlag'];

// 用户实例
let user = ref({});

// computed: 拼接OSS用户头像目录前缀
let ossSrc = computed(() => src => ossUserAvatar + src);

// computed: 头像大图预览地址
let getPreviewList = computed(() => src => [ossUserAvatar + src]);

// method: 按 `用户ID` 查询一条 `用户` 记录
let selectByUserId = async (userId) => {
  try {
    const resp = await USER_SELECT_BY_USER_ID_API(userId);
    if (resp["data"]["code"] > 0) {
      user.value = resp["data"]["data"];
    } else console.error(resp["data"]["message"]);
  } catch (e) {
    console.error(e)
  }
}

// method: 当点击 `登出` 按钮的时候触发
let logout = async () => {

  // 修改登录状态
  await vuex.dispatch('setLoginFlag', false);

  // 清除sessionStorage中的token值
  sessionStorage.removeItem("token");

  // 清除sessionStorage中的user-id值
  sessionStorage.removeItem("user-id");

  // 刷新页面
  location.reload();

}

// method: 注销个人账号
let deleteByUserId = () => {

  // 危险操作保护
  if (!confirm("即将注销个人账号，确定吗？")) {
    return false;
  }

  // 调用对应API接口
  USER_DELETE_BY_USER_ID_API({
    'user-id': userId
  }).then(resp => {
    if (resp["data"]["code"] > 0) {
      ElMessage("账号注销成功");
      logout();
    } else {
      console.error(resp["data"]["message"]);
      ElMessage("账号注销失败");
    }
  }).catch(e => console.error(e));
}

// mounted: 页面加载完毕后，立刻调用 `selectByUserId()` 方法
onMounted(() => {
  if(loginFlag) selectByUserId(userId);
});

</script>

<style lang="scss" scoped>

/*顶部头像区域*/
.personal-body {

  /*用户基本信息*/
  .header-head {

    height: 200px; // 高度
    padding: 50px 0; // 上下内边距 左右内边距
    background-color: yellow; // 背景色
    text-align: center; // 内容居中

    /*头像图片*/
    .avatar-image {
      width: 100px; // 宽度
      height: 100px // 高度
    }

    /*用户昵称*/
    .nick-name {
      font-family: 楷体, serif; // 字体
      font-size: 22px; // 字号
    }

    /*用户介绍*/
    .user-info {
      color: gray; // 前景色
      font-size: 12px; // 字号
    }
  }

  /*功能按钮*/
  .header-body {

    padding-top: 20px; // 上内边距

    /* 功能按钮 */
    .opera-btn {

      display: inline-block; // 区块
      width: 40%; // 宽度
      height: 40px; // 高度
      margin: 10px; // 外边距
      color: #fff; // 前景色
      border-radius: 5px; // 圆角
      font-size: 0.9em; // 字号
      letter-spacing: 5px; // 子间距
    }

  }
}


</style>