<template>

  <header class="user-update-password-header">

    <!--引入通用头子组件-->
    <common-header title="修改用户密码"/>

  </header>

  <section class="user-update-password-body">

    <!--修改表单-->
    <!--
      ref="updatePasswordForm"：表单名称，校验的时候使用
      :model="updatePasswordFormData"：绑定表单数据对象
      :rules="updatePasswordFormRule"：绑定表单验证规则
    -->
    <el-form ref="updatePasswordForm"
             :model="updatePasswordFormData"
             :rules="updatePasswordFormRule">

      <!--表单项: 用户原密码-->
      <!--
        label="原密码": 展示的文字
         prop="old-password": 对应updatePasswordFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item label="旧的密码" prop="old-password">
        <el-input type="password" v-model="updatePasswordFormData['old-password']" placeholder="请输入原密码.." clearable></el-input>
      </el-form-item>

      <!--表单项: 用户原密码-->
      <!--
        label="新密码": 展示的文字
         prop="new-password": 对应updatePasswordFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item label="新的密码" prop="new-password">
        <el-input type="password" v-model="updatePasswordFormData['new-password']" placeholder="请输入新密码.." clearable></el-input>
      </el-form-item>

      <!--表单项: 确认新密码-->
      <!--
        label="新密码": 展示的文字
         prop="confirm": 对应updatePasswordFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item label="确认密码" prop="confirm">
        <el-input type="password" v-model="updatePasswordFormData['confirm']" placeholder="请确认新密码.." clearable></el-input>
      </el-form-item>

      <!--确认修改按钮-->
      <el-button class="update-btn" type="primary" @click="updatePasswordByUserId">确认修改</el-button>

    </el-form>

  </section>

</template>

<script setup>

import CommonHeader from "@/components/common-header";
import {USER_UPDATE_PASSWORD_BY_USER_ID_API} from '@/api'
import {useStore} from "vuex";
import router from "@/router";
import {onMounted, reactive, shallowReactive, shallowRef} from "vue";
import {ElMessage} from 'element-plus'

// data: vuex实例
const vuex = useStore();

// data: 用户登录状态
const loginFlag = vuex.state['loginFlag'];

// data: 用户主键
const userId = sessionStorage.getItem('user-id');

// data: 修改表单
let updatePasswordForm = shallowRef();

// data: 修改表单数据对象
let updatePasswordFormData = shallowReactive({
  'old-password': '',
  'new-password': '',
  'confirm': ''
});

// data: 修改表单校验规则
let updatePasswordFormRule = reactive({
  'old-password': [
    {required: true, message: '原密码不能为空', trigger: 'blur'},
    {pattern: /^[a-zA-Z0-9_-]{4,16}$/, message: '原密码格式有误', trigger: 'change'}
  ],
  'new-password': [
    {required: true, message: '新密码不能为空', trigger: 'blur'},
    {pattern: /^[a-zA-Z0-9_-]{4,16}$/, message: '新密码格式有误', trigger: 'change'}
  ],
  'confirm': [
    {required: true, message: '确认密码不能为空', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value === updatePasswordFormData['new-password']) callback();
        else callback('两次密码不一致');
      },
      trigger: 'blur'
    }
  ]
})

// method: 修改个人信息
let updatePasswordByUserId = () => {
  updatePasswordForm.value.validate(valid => {

    if (!valid) {
      ElMessage('校验失败，请检查表单参数');
      return false;
    }

    // 同步调用对应的API接口
    USER_UPDATE_PASSWORD_BY_USER_ID_API({
      'user-id': userId,
      'old-password': updatePasswordFormData['old-password'],
      'new-password': updatePasswordFormData['new-password'],
    }).then(resp => {
      if (resp["data"]["code"] > 0) {

        ElMessage("密码修改成功，请重新登录");

        // 修改登录状态
        vuex.dispatch('setLoginFlag', false);

        // 清除sessionStorage中的token值
        sessionStorage.removeItem("token");

        // 清除sessionStorage中的user-id值
        sessionStorage.removeItem("user-id");

        // 跳入到Login组件
        router.push("/login");

      } else {
        ElMessage("修改失败: " + resp["data"]["message"]);
      }
    }).catch(e => console.error(e));

  });
}

// mounted: 页面加载完毕后进行未登录保护
onMounted(() => {

  // 未登录保护
  if (!loginFlag) {

    ElMessage('请先登录！');

    // 两秒之后跳入登录组件
    setTimeout(() => router.push("/login"), 2000);
  }

})

</script>

<style lang="scss" scoped>

.user-update-password-body {

  padding: 50px; // 内边距

  /*确认修改按钮*/
  .update-btn {
    margin: 5px auto; // 上下外边距 左右自居中
    width: 100%; // 宽度
  }

}

</style>