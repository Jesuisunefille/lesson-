import { createStore } from 'vuex'

export default createStore({
  state: {
    loginFlag: false // 当前用户的登录状态，true表示用户已登录, false表示用户未登录
  },
  mutations: {
    setLoginFlag: (state, loginFlag) => state.loginFlag = loginFlag // 将参数赋予对应的state共享变量
  },
  actions: {
    setLoginFlag: (context, loginFlag) => context.commit('setLoginFlag', loginFlag) // 调用mutations中的 `setLoginFlag()` 方法
  },
  getters: {
  },
  modules: {
  }
})
