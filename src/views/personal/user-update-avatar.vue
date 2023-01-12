<template>

  <header class="user-update-avatar-header">

    <!--引入通用头子组件-->
    <common-header title="修改用户头像"/>

  </header>

  <section class="user-update-avatar-body">

    <!--上传头像-->
    <!--
      ref="avatarUpload": 控件名称
      drag: 允许拖拽上传
      :http-request="updateAvatarByUserId": 当上传头像时执行自定方法
      :limit="1": 只允许上传一个文件
      :on-exceed="handleExceed": 当违反了limit规则时执行的方法
      :before-upload="beforeUpload": 上传头像之前执行自定方法
    -->
    <el-upload ref="avatarUpload" drag
               :http-request="updateAvatarByUserId"
               :limit="1"
               :on-exceed="handleExceed"
               :before-upload="beforeUpload"
    >
      <el-icon class="el-icon--upload">
        <upload-filled/>
      </el-icon>
      <div class="el-upload__text">
        将文件拖到此处或 <em>单击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          仅支持JPEG/JPG/PNG格式，且小于5M的头像文件
        </div>
      </template>

    </el-upload>

  </section>

</template>

<script setup>
import CommonHeader from "@/components/common-header";
import {UploadFilled} from '@element-plus/icons-vue';
import {USER_UPDATE_AVATAR_BY_USER_ID_API} from "@/api";
import {useStore} from "vuex";
import router from "@/router";
import {onMounted, shallowRef} from "vue";
import {ElMessage} from 'element-plus';

// data: vuex实例
const vuex = useStore();

// data: 用户登录状态
const loginFlag = vuex.state['loginFlag'];

// data: 用户主键
const userId = sessionStorage.getItem('user-id');

// data: 上传头像的EL控件
let avatarUpload = shallowRef();

// method: 当超出如格式和大小等限制时触发
let handleExceed = (files, fileList) => {

  // 只允许上传一个图片
  if (fileList.length >= 1) {
    ElMessage.warning('只能上传一个头像图片');
  }

}

// method: 在上传头像之前执行的函数
let beforeUpload = (file) => {

  // 定义允许的头像格式
  const allowMime = ['image/jpeg', 'image/jpg', 'image/png'];

  // 判断用户上传的文件是否是允许的格式
  if (allowMime.indexOf(file.type) < 0) {
    ElMessage.error('格式必须是JPEG/JPG/PNG中的一个');
    return false;
  }

  // 判断用户上传的文件大小是否合理
  if (file.size > 1024 * 1024 * 5) {
    ElMessage.error('图片大小不能超过5M');
    return false;
  }

  return true;
}

// method: 上传图片时触发
let updateAvatarByUserId = (upload) => {

  // 准备表单数据
  let formData = new FormData();
  formData.append('user-id', userId);
  formData.append('avatar-file', upload.file);

  // 同步调用对应的API接口
  USER_UPDATE_AVATAR_BY_USER_ID_API(formData).then(resp => {
    if (resp['data']['code'] > 0) {
      ElMessage.success('头像上传成功');
      setTimeout(() => router.push('/personal'), 1000);
    } else ElMessage.error(resp['data']['message']);

    // 清除文件列表
    avatarUpload.value.clearFiles();

  }).catch(e => console.error(e))
}

// mounted: 页面加载完毕后，进行未登录保护
onMounted(() => {

  // 未登录保护
  if (!loginFlag) {

    ElMessage.warning('请先登录！');

    // 两秒之后跳入登录组件
    setTimeout(() => router.push("/login"), 2000);
  }

})

</script>

<style lang="scss" scoped>

.user-update-avatar-body {

  padding: 50px; // 内边距

}

</style>