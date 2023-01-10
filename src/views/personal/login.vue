<template>

  <header class="login-header">

    <!--引入通用头组件-->
    <CommonHeader title="用户登录"/>

  </header>

  <section class="login-body">

    <!--登录表单-->
    <!--
      ref="loginForm"：表单名称，校验的时候使用
      :model="loginFormData"：绑定表单数据对象
      :rules="loginFormRule"：绑定表单验证规则
    -->
    <el-form ref="loginForm" :model="loginFormData" :rules="loginFormRule">

      <!--表单项: 登录账号-->
      <!--
        label="登录账号": 展示的文字
        prop="username": 对应registerFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item label="账号" prop="username">
        <el-input type="text" v-model="loginFormData['username']" placeholder="请输入账号.."></el-input>
      </el-form-item>

      <!--表单项: 登录密码-->
      <!--
        label="登录密码": 展示的文字
        prop="username": 对应registerFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="loginFormData['password']" placeholder="请输入密码.."></el-input>
      </el-form-item>

      <!--按钮: 立刻登录-->
      <!--
        @click="login": 点击按钮，触发login方法
      -->
      <el-button class="login-btn" type="primary" @click="login">立刻登录</el-button>

      <!--按钮: 我要注册-->
      <!--
        @click="toRegister": 点击按钮，触发toRegister方法
      -->
      <el-button class="register-btn" type="warning" @click="toRegister">我要注册</el-button>

    </el-form>

  </section>

</template>

<script setup>
import CommonHeader from "@/components/common-header";
import {USER_LOGIN_API} from '@/api'
import {reactive, ref, shallowReactive} from "vue";
import {useStore} from 'vuex'
import {ElMessage} from 'element-plus'
import router from "@/router";

// data: 实例化vuex
const vuex = useStore();

// data: 登录表单
let loginForm = ref();

// data: 登录表单数据对象
let loginFormData = shallowReactive({
  username: '',
  password: ''
});

// data: 登录表单校验规则
let loginFormRule = reactive({
  username: [
    {required: true, message: '账号不能为空', trigger: 'blur'},
    {min: 4, max: 16, message: '账号长度在4-16个字符', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
    {min: 4, max: 16, message: '密码长度在4-16个字符', trigger: 'blur'}
  ]
})

// method: 当点击登录按钮时触发
let login = () => {

  // 判断loginForm中所有控件是否都通过了规则验证
  loginForm.value.validate(valid => {

    if (!valid) {
      ElMessage('校验失败！');
      return false;
    }

    // 调用用户登录接口
    USER_LOGIN_API({
      username: loginFormData.username,
      password: loginFormData.password
    }).then(resp => {
      if (resp["data"]["code"] > 0) {

        let data = resp['data']['data'];

        // 将用户的Token令牌存入sessionStorage中
        sessionStorage.setItem('token', data['token']);
        sessionStorage.setItem('user-id', data['user']['id']);

        // 将用户登录状态存入vuex中
        vuex.dispatch('setLoginFlag', true);

        // 提示登录成功
        ElMessage("登录成功");

        // 跳转到Personal组件
        router.push("/personal");

      } else {
        ElMessage("登录失败");
        console.error(resp["data"]["message"])
      }
    }).catch(e => console.log(e));
  });

}

// method: 用户注册，当点击注册按钮时触发
let toRegister = () => router.push('/register');

</script>

<style lang="scss" scoped>

.login-body {

  padding: 50px; // 内边距

  /*注册按钮和登录按钮*/
  .login-btn, .register-btn {
    margin: 5px auto; // 上下外边距 左右自居中
    width: 100%; // 宽度
  }

}

</style>