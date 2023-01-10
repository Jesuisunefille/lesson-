<template>

  <header class="register-header">

    <!--引入通用头子组件-->
    <common-header title="用户注册"/>

  </header>

  <section class="register-body">

    <!--表单-->
    <!--
      ref="registerForm": 表单名
      :model="registerFormData": 绑定表单数据
      :rules="registerFormRule": 绑定校验规则
    -->
    <el-form ref="registerForm"
             :model="registerFormData"
             :rules="registerFormRule">

      <!--表单项: 登录账号-->
      <!--
        label="登录账号": 展示的文字
        prop="username": 对应registerFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item label="登录账号" prop="username">

        <!--控件: 单行文本框-->
        <!--
          v-model="registerFormData['username']": 双向绑定表单中的username数据
        -->
        <el-input type="text" v-model="registerFormData['username']" placeholder="请输入登录账号.."></el-input>
      </el-form-item>

      <!--表单项: 登录密码-->
      <el-form-item label="登录密码" prop="password">

        <!--控件: 单行密码框-->
        <el-input type="password" v-model="registerFormData['password']" placeholder="请输入登录密码.."></el-input>

      </el-form-item>

      <!--表单项: 确认密码-->
      <el-form-item label="确认密码" prop="confirm">

        <!--控件: 单行密码框-->
        <el-input type="password" v-model="registerFormData['confirm']" placeholder="请输入确认密码.."></el-input>

      </el-form-item>

      <!--表单项: 手机号码-->
      <el-form-item label="手机号码" prop="phone">

        <!--控件: 单行数字框-->
        <el-input type="number" v-model="registerFormData['phone']" placeholder="请输入手机号码.."></el-input>

      </el-form-item>

      <!--表单项: 真实姓名-->
      <el-form-item label="真实姓名" prop="realName">

        <!--控件: 单行文本框-->
        <el-input type="text" v-model="registerFormData['realName']" placeholder="请输入真实姓名.."></el-input>

      </el-form-item>

      <!--表单项: 身份证号-->
      <el-form-item label="身份证号" prop="idCard">

        <!--控件: 单行数字框-->
        <el-input type="number" v-model="registerFormData['idCard']" placeholder="请输入身份证号.."></el-input>

      </el-form-item>

      <!--按钮: 立刻注册-->
      <!--
        @click="register": 点击按钮，触发register方法
      -->
      <el-button class="register-btn" type="primary" @click="register">立刻注册</el-button>

      <!--按钮: 我要登录-->
      <!--
        @click="toLogin": 点击按钮，触发register方法
      -->
      <el-button class="login-btn" type="warning" @click="toLogin">我要登录</el-button>

    </el-form>

  </section>

</template>

<script setup>
import CommonHeader from "@/components/common-header";
import {USER_REGISTER_API} from '@/api';
import {reactive, ref, shallowReactive} from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";

// data: 注册表单对象
let registerForm = ref();

// data: 注册表单数据对象
let registerFormData = shallowReactive({
  username: '',
  password: '',
  confirm: '',
  phone: '',
  realName: '',
  idCard: ''
});

// data: 注册表单校验规则
let registerFormRule = reactive({
  username: [
    {required: true, message: '账号不能为空', trigger: 'blur'},
    {min: 4, max: 16, message: '账号长度在4-16个字符', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
    {min: 4, max: 16, message: '密码长度在4-16个字符', trigger: 'blur'}
  ],
  confirm: [
    {required: true, message: '确认密码不能为空', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value === registerFormData.password) callback();
        else callback('两次密码不一致');
      },
      trigger: 'blur'
    }
  ],
  phone: [
    {required: true, message: '手机号码不能为空', trigger: 'blur'},
    {pattern: /^[0-9]{11}$/, message: '手机号码格式不正确', trigger: 'blur'}
  ],
  realName: [
    {required: true, message: '真实姓名不能为空', trigger: 'blur'},
    {min: 2, max: 15, message: '真实姓名长度在2-5个字符', trigger: 'blur'}
  ],
  idCard: [
    {required: true, message: '身份证号码不能为空', trigger: 'blur'},
    {pattern: /^[0-9]{18}$/, message: '身份证号格式不正确', trigger: 'blur'}
  ],
});

// method: 用户注册，当点击注册按钮时触发
let register = () => {

  // 判断registerForm中所有控件是否都通过了规则验证
  registerForm.value.validate(valid => {

    if (!valid) {
      ElMessage('校验失败: 请检查表单参数');
      return false;
    }

    // 调用用户注册接口
    USER_REGISTER_API({
      "username": registerFormData.username,
      "password": registerFormData.password,
      "phone": registerFormData.phone,
      "real-name": registerFormData.realName,
      "id-card": registerFormData.idCard
    }).then(resp => {
          if (resp["data"]["code"] > 0) {
            ElMessage("注册成功");
            router.push("/login");
          } else {
            ElMessage("注册失败");
            console.error(resp['data']['message']);
          }
        }
    ).catch(e => console.log(e));

  });
}

// method: 用户登录，当点击登录按钮时触发
let toLogin = () => router.push('/login');

</script>

<style lang="scss" scoped>

.register-body {

  padding: 50px; // 内边距

  /*注册按钮和登录按钮*/
  .register-btn, .login-btn {
    margin: 5px auto; // 上下外边距 左右自居中
    width: 100%; // 宽度
  }
  
}

</style>