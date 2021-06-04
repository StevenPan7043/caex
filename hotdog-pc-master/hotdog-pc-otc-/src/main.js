import Vue from 'vue';
import VueClipboard from 'vue-clipboard2';

import App from './App.vue';
import router from './router';
import store from './store';
import './assets/css/reset.css';
import './assets/iconfont/iconfont.css';
import './plugins/iview';

Vue.use(VueClipboard);

Vue.config.productionTip = false;
if (process.env.NODE_ENV !== 'production') {
  Vue.config.performance = true;
}

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
