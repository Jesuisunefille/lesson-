<template>

  <section class="common-footer-body">

    <!--选项卡-->
    <!--
      stretch: 标签的宽度是否自撑开
      tab-position="bottom": 选项卡所在位置，居于底部
      v-model="activeName"：绑activeName变量，动态决定哪个选项卡为当前首选项
      @tab-click="handleClick"：点击触发handleClick方法。
    -->
    <el-tabs stretch tab-position="bottom" class="demo-tabs"
             v-model="activeName"
             @tab-click="handleClick">

      <!--选项卡item项-->
      <!--
        :name="tab['name']"：设置不同的跳转路径
        v-for="tab in tabs"：遍历tabs数组
        :name="tab['name']"：与选项卡绑定值 value 对应的标识符，表示选项卡别名
      -->

      <el-tab-pane v-for="tab in tabs" :name="tab['name']">

        <template #label>

          <div class="custom-tabs-label">

            <!--选项卡中的图标：使用动态组件来展示图标-->
            <!--:is="tab['icon']"：属性 `:is` 的值是哪个组件的名称，就显示哪个组件-->
            <component :is="tab['icon']" class="icon"/>

            <!--选项卡中的文字-->
            <span class="label">{{ tab['label'] }}</span>

          </div>

        </template>

      </el-tab-pane>

    </el-tabs>

  </section>

</template>

<script setup>
import {HomeFilled, GoodsFilled, UserFilled} from '@element-plus/icons-vue';
import {shallowRef} from "vue";
import router from "@/router";

// data: 当前路由路径
let currentRouterPath = router.currentRoute.value.path;

// data: 选项卡可选值
const tabs = [
  {name: '/', label: '首页', icon: HomeFilled},
  {name: '/cart', label: '购物车', icon: GoodsFilled},
  {name: '/personal', label: '我的', icon: UserFilled}
];

// data: 当前选中的选项卡
let activeName = shallowRef(currentRouterPath);

// method: 点击选项卡时触发，tab参数表示点击的选项卡实例
let handleClick = currentTab => {

  // 若用户点击的不是当前路由，则进行跳转
  if (currentTab.props['name'] !== activeName.value) {
    router.push(currentTab.props['name']);
  }

}

</script>

<style lang="scss" scoped>

.common-footer-body {

  background-color: orangered; // 背景色
  position: fixed; // 固定定位
  bottom: 0; // 下坐标
  z-index: 999; // Z轴
  width: 100%; // 宽度
  border-top: 1px solid gray; // 上边框
  padding-bottom: 5px; // 下内边距
  margin-bottom: -5px; // 下外边距

  /*选项卡*/
  .custom-tabs-label {

    margin-top: -5px; // 上外边距

    /*选项卡图标*/
    .icon {
      width: 1.2em; // 宽度
      height: 1.1em; // 高度
      margin-right: 8px; // 右外边距
    }

    /*选项卡文字*/
    .label {
      font-size: 1.4em; // 字号
      font-weight: bold; // 加粗
      font-family: 楷体, serif; // 字体
      color: orange; // 前景色
    }

  }

}

</style>