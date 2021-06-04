import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '~/css/reset.css'
import '~/css/style.css'
import { Message } from 'element-ui';
Vue.config.productionTip = false
router.beforeEach((to, from, next) => {
  if (!store.state.auth.auths) {
    next()
  } else {
    if (store.state.auth.auths[to.name] == true) {
      next();
    } else {
      Message({
        message: '你没有权限哦',
        type: 'error',
        duration: 3000,
        showClose: true
      })
      next(from.path)
    }

  }

})
Vue.use(ElementUI)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
