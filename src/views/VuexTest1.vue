<template>

  <h1>VuexTest1.vue</h1>
  <p>vuex: {{ vuex.state['loginFlag'] }}</p>
  <p>sessionStorage: {{ sessionLoginFlag }}</p>
</template>

<script setup>


import {useStore} from "vuex";
import {shallowRef} from "vue";

// data: vuex实例
const vuex = useStore();
const sessionLoginFlag = shallowRef(sessionStorage.getItem('loginFlag'));

</script>

<style scoped>

</style>