<template>

  <header class="tab-summary-header">

    <!--若视频存在摘要图，则展示此容器-->
    <article v-if="summaryImage">

      <!--视频摘要图-->
      <!--
        :src="nginxSrc(summaryImage)"：计算视频摘要图的Nginx代理地址
      -->
      <el-image :src="nginxSrc(summaryImage)" class="summary-image"/>

    </article>

    <!--若视频不存在摘要图，则展示此容器-->
    <article v-else>该视频暂无摘要图</article>

  </header>

</template>

<script setup>
import {computed} from "vue";
import {nginxVideoSummary} from "@/global_variable";

// props: 接收父组件传递过来的值
let props = defineProps({
  summaryImage: {
    type: String,
    required: true,
    default: 'default-summary.jpg'
  }
})

// computed: 拼接Nginx视频摘要图目录前缀
const nginxSrc = computed(() => src => nginxVideoSummary + src);

</script>

<style lang="scss" scoped>

.tab-summary-header {

  .summary-image {
    width: 100%; // 宽度
  }

}

</style>