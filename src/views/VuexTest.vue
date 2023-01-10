<template>
  <div>
    子组件1：
    <vuex-test1></vuex-test1>
    <hr/>
    子组件2：
    <vuex-test2></vuex-test2>
  </div>
  <div>
    <el-button @click="toggleLoginFlag">切换登录状态</el-button>
  </div>
</template>

<script setup>
import VuexTest1 from '@/views/VuexTest1'
import VuexTest2 from '@/views/VuexTest2'
import {useStore} from "vuex";

// data: vuex实例
const vuex = useStore();

let toggleLoginFlag = () => {
  if (vuex.state['loginFlag']) {
    sessionStorage.setItem('loginFlag', 'false');
    vuex.dispatch('setLoginFlag', false);
  } else {
    sessionStorage.setItem('loginFlag', 'true');
    vuex.dispatch('setLoginFlag', true);
  }
  console.log('sessionLoginFlag: ' + sessionStorage.getItem('loginFlag'))
}


</script>

<style scoped>

</style>