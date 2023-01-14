<template>

  <header class="login-header">

    <!--引入通用头组件-->
    <CommonHeader title="用户手机号码登录"/>

  </header>

  <section class="login-body">

    <!--登录表单-->
    <!--
      ref="loginByPhoneForm"：表单名称，校验的时候使用
      :model="loginByPhoneFormData"：绑定表单数据对象
      :rules="loginByPhoneFormRule"：绑定表单验证规则
    -->
    <el-form ref="loginByPhoneForm" :model="loginByPhoneFormData" :rules="loginByPhoneFormRule">

      <!--表单项: 登录账号-->
      <!--
        label="登录账号": 展示的文字
        prop="username": 对应registerFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item prop="phone">
        <el-input
            v-model="loginByPhoneFormData['phone']"
            size="large"
            placeholder="请输入手机号码"
            :prefix-icon="Iphone"
        />
      </el-form-item>

      <!--表单项: 登录密码-->
      <!--
        label="登录密码": 展示的文字
        prop="username": 对应registerFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item prop="verification-code">
        <el-input
            v-model="loginByPhoneFormData['verification-code']"
            size="large"
            placeholder="请输入4位验证码"
            :prefix-icon="Key"
        >

          <template #append>
            <el-button @click="getVerificationCode">获取验证码</el-button>
          </template>

        </el-input>
      </el-form-item>

      <!--按钮: 立刻登录-->
      <!--
        @click="login": 点击按钮，触发login方法
      -->
      <el-button class="login-btn" type="primary" @click="loginByPhone">立刻登录</el-button>

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
import {Iphone, Key} from '@element-plus/icons-vue';

import {USER_GET_VERIFICATION_CODE, USER_LOGIN_BY_PHONE_API, USER_REGISTER_API} from '@/api'
import {reactive, ref, shallowReactive} from "vue";
import {useStore} from 'vuex'
import {ElMessage} from 'element-plus'
import router from "@/router";

// data: 实例化vuex
const vuex = useStore();

// data: 登录表单
let loginByPhoneForm = ref();

// data: 登录表单数据对象
let loginByPhoneFormData = shallowReactive({
  'phone': '',
  'verification-code': ''
});

// data: 登录表单校验规则
let loginByPhoneFormRule = reactive({
  'phone': [
    {required: true, message: '手机号码不能为空', trigger: 'blur'},
    {pattern: /^[0-9]{11}$/, message: '手机号码格式不正确', trigger: 'blur'}
  ],
  'verification-code': [
    {required: true, message: '验证码不能为空', trigger: 'blur'},
    {min: 4, max: 4, message: '密码长度是4个字符', trigger: 'blur'}
  ]
})

// method: 当点击登录按钮时触发
let loginByPhone = () => {

  // 判断loginForm中所有控件是否都通过了规则验证
  loginByPhoneForm.value.validate(valid => {

    if (!valid) {
      ElMessage.error('校验失败！');
      return false;
    }

    // 调用用户登录接口
    USER_LOGIN_BY_PHONE_API({
      'phone': loginByPhoneFormData['phone'],
      'verification-code': loginByPhoneFormData['verification-code']
    }).then(resp => {
      if (resp["data"]["code"] > 0) {

        let data = resp['data']['data'];

        // 将用户的Token令牌存入sessionStorage中
        sessionStorage.setItem('token', data['token']);
        sessionStorage.setItem('user-id', data['user']['id']);

        // 将用户登录状态存入vuex中
        vuex.dispatch('setLoginFlag', true);

        // 提示登录成功
        ElMessage.success("登录成功");

        // 跳转到Personal组件
        router.push("/personal");

      } else ElMessage.error(resp["data"]["message"]);
    }).catch(e => console.log(e));
  });

}

let getVerificationCode = async () => {

  let phone = loginByPhoneFormData['phone'];

  if('' === phone){
    ElMessage.error('手机号码不能为空');
    return false;
  }

  USER_GET_VERIFICATION_CODE(phone).then(resp => {
    if (resp['data']['code'] > 0) {
      ElMessage.info('验证码发送成功，请在手机端查看');
    } else ElMessage.error(resp['data']['message'])
  }).catch(e => console.error(e))


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