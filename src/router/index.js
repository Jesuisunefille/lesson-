import {createRouter, createWebHashHistory} from 'vue-router'

// 所有组件的路由都在这个数组中配置
const routes = [
  {path: '/banner', name: 'banner', component: import('@/views/welcome/banner')}
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
