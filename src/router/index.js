import {createRouter, createWebHashHistory} from 'vue-router'
import Welcome from '@/views/welcome'
import VideoDetail from '@/views/video-detail'
import Register from '@/views/personal/register'
import Login from '@/views/personal/login'

// 所有组件的路由都在这个数组中配置
const routes = [
  {path: '/', name: 'Welcome', component: Welcome},
  {path: '/video-detail', name: 'VideoDetail', component: VideoDetail},
  {path: '/register', name: 'Register', component: Register},
  {path: '/login', name: 'Login', component: Login},
  {path: '/banner', name: 'Banner', component: import('@/views/welcome/banner')},
  {path: '/video-list', name: 'VideoList', component: import('@/views/welcome/video-list')},
  {path: '/common-header', name: 'CommonHeader', component: import('@/components/common-header')},
  {path: '/common-footer', name: 'CommonFooter', component: import('@/components/common-footer')},
  {path: '/free-video', name: 'FreeVideo', component: import('@/views/video-detail/free-video')},
  {path: '/tab-summary', name: 'TabSummary', component: import('@/views/video-detail/tab-summary')},
  {path: '/tab-catalog', name: 'TabCatalog', component: import('@/views/video-detail/tab-catalog')},
  {path: '/VuexTest',name: 'VuexTest',component: import('@/views/VuexTest')},
  {path: '/VuexTest1',name: 'VuexTest1',component: import('@/views/VuexTest1')},
  {path: '/VuexTest2',name: 'VuexTest2',component: import('@/views/VuexTest2')}
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
