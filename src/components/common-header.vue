<template>

  <header class="common-header-body">

    <!--页头-->
    <!--
      @back="router.back()"：当点击返回按钮时，会切换到上一次的路由
    -->
    <el-page-header @back="router.back()">

      <!--插槽: 插入页头标题-->
      <template #content>
        <span> {{ title }} </span>
      </template>

    </el-page-header>

  </header>

</template>

<script setup>
import router from "@/router";

// props: 接收父组件传递过来的值
defineProps({
  title: {
    type: String,
    require: true
  }
})

</script>

<style lang="scss" scoped>

.common-header-body {
  padding: 10px 20px;// 上下内边距 左右内边距
}

</style>