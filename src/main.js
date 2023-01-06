import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/display.css'

createApp(App)
    .use(store)
    .use(router)
    .use(ElementPlus)
    .mount('#app')

//全局变量
export const ip = 'http://192.168.242.77';
export const backgroundPort = '5277';

export const nginxBanner = 'http://192.168.242.77:8023/banner/';