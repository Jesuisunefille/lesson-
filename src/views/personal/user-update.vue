<template>

  <header class="user-update-header">

    <!--引入通用头子组件-->
    <common-header title="修改用户信息"/>

  </header>

  <section class="user-update-body">

    <!--修改表单-->
    <!--
      ref="updateForm"：表单名称，校验的时候使用
      :model="updateFormData"：绑定表单数据对象
      :rules="updateFormRule"：绑定表单验证规则
    -->
    <el-form ref="updateForm"
             :model="updateFormData"
             :rules="updateFormRule">

      <!--表单项: 用户昵称-->
      <!--
        label="用户昵称": 展示的文字
         prop="nick-name": 对应updateFormRule中的属性，若想要开启校验，则必须设置此项目
      -->
      <el-form-item label="用户昵称" prop="nick-name">
        <el-input type="text" v-model="updateFormData['nick-name']" placeholder="请输入昵称.." clearable></el-input>
      </el-form-item>

      <!--表单项: 用户性别-->
      <el-form-item label="用户性别" prop="gender">
        <el-radio-group v-model="updateFormData['gender']">
          <el-radio-button label="0" border>男</el-radio-button>
          <el-radio-button label="1" border>女</el-radio-button>
          <el-radio-button label="2" border>保密</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <!--表单项: 用户年龄-->
      <el-form-item label="用户年龄" prop="age">
        <el-input-number v-model="updateFormData['age']" :min="16" :max="60"/>
      </el-form-item>

      <!--表单项: 手机号码-->
      <el-form-item label="手机号码" prop="phone">
        <el-input type="text" v-model="updateFormData['phone']" placeholder="请输入手机号.."></el-input>
      </el-form-item>

      <!--表单项: 用户介绍-->
      <el-form-item label="用户介绍" prop="info">
        <el-input type="textarea" :rows="10" v-model="updateFormData['info']" placeholder="请输入信息.."></el-input>
      </el-form-item>

      <!--确认修改按钮-->
      <el-button class="update-btn" type="primary" @click="updateByUserId">确认修改</el-button>

    </el-form>

  </section>

</template>

<script setup>
import CommonHeader from "@/components/common-header";
import {USER_SELECT_BY_USER_ID_API, USER_UPDATE_BY_USER_ID_API} from '@/api'
import {useStore} from "vuex";
import router from "@/router";
import {onMounted, reactive, shallowReactive, shallowRef} from "vue";
import {ElMessage} from 'element-plus'

// data: vuex实例
const vuex = useStore();

// data: 用户登录状态
const loginFlag = vuex.state['loginFlag'];

// data: 用户主键
const userId = router.currentRoute.value.query['user-id'];

// data: 修改表单
let updateForm = shallowRef();

// data: 修改表单数据对象
let updateFormData = shallowReactive({
  'nick-name': '',
  'gender': '',
  'age': '',
  'info': '',
  'phone': ''
});

// data: 修改表单校验规则
let updateFormRule = reactive({
  'nick-name': [
    {required: true, message: '昵称不能为空', trigger: 'blur'},
    {min: 2, max: 15, message: '昵称长度在2-15个字符', trigger: 'blur'}
  ],
  'gender': [
    {required: true, message: '性别不能为空', trigger: 'blur'},
    {pattern: /^[012]$/, message: '性别可选值：0男，1女，2保密', trigger: 'change'}
  ],
  'age': [
    {required: true, message: '年龄不能为空', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value >= 16 && value <= 60) callback();
        else callback('年龄必须在16-60之间');
      }, trigger: 'blur'
    }
  ],
  'info': [
    {required: true, message: '描述不能为空', trigger: 'blur'},
    {min: 0, max: 1024, message: '描述长度在0-1024个字符', trigger: 'blur'}
  ],
  'phone': [
    {required: true, message: '手机号码不能为空', trigger: 'blur'},
    {
      pattern: /^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/,
      message: '手机号码格式不正确',
      trigger: 'blur'
    }
  ],
})

// method: 查询个人信息
let selectUserByUserId = async (userId) => {
  try {
    const resp = await USER_SELECT_BY_USER_ID_API(userId);
    if (resp["data"]["code"] > 0) {
      let data = resp["data"]["data"];
      updateFormData['nick-name'] = data['nick-name'];
      updateFormData['gender'] = data['gender'];
      updateFormData['age'] = data['age'];
      updateFormData['info'] = data['info'];
      updateFormData['phone'] = data['phone'];
    } else {
      console.error(resp["data"]["message"]);
    }
  } catch (e) {
    console.error(e);
  }
}

// method: 修改个人信息
let updateByUserId = () => {
  updateForm.value.validate(valid => {

    if (!valid) {
      ElMessage('校验失败，请检查表单参数');
      return false;
    }

    USER_UPDATE_BY_USER_ID_API({
      'user-id': userId,
      'nick-name': updateFormData['nick-name'],
      'gender': updateFormData['gender'],
      'age': updateFormData['age'],
      'info': updateFormData['info'],
      'phone': updateFormData['phone']
    }).then(resp => {
      if (resp["data"]["code"] > 0) {
        ElMessage("修改成功");
        router.push("/personal");
      } else {
        ElMessage("修改失败");
        console.error(resp["data"]["message"]);
      }
    }).catch(e => console.log(e));

  });
}

// mounted: 页面加载完毕后，立刻调用 `selectUserByUserId()` 方法
onMounted(() => {
  // 未登录保护
  if (!loginFlag) {

    ElMessage('请先登录！');

    // 两秒之后跳入登录组件
    setTimeout(() => router.push("/login"), 2000);

    return;
  }
  selectUserByUserId(userId);
})

</script>

<style lang="scss" scoped>

.user-update-body {

  padding: 50px; // 内边距

  /*确认修改按钮*/
  .update-btn {
    margin: 5px auto; // 上下外边距 左右自居中
    width: 100%; // 宽度
  }

}

</style>