import Vue from 'vue';
import App from './App.vue';
import router from './router'
import store from './store'
import './assets/css/base.css'
import './assets/css/common.css'
import './plugins/element.js'
import './assets/css/theme/index.less'
import './libs/directives.js'
import "./assets/css/animate.min.css";

import VueAwesomeSwiper from 'vue-awesome-swiper'
import VueClipboard from 'vue-clipboard2'
import VueI18n from 'vue-i18n'
import Meta from 'vue-meta'
import {getLang} from './libs/utils'
import './assets/iconfont/iconfont.css'
// require styles
import 'swiper/dist/css/swiper.css'
import VueQrcode from '@xkeshi/vue-qrcode'
import ElementUI from 'element-ui'
import {post,get} from '@/libs/http.js'
import {contractWsUr}from '../public/config'
import VueResource from 'vue-resource'

Vue.use(VueResource)
Vue.use(ElementUI)
Vue.use(VueClipboard)
Vue.use(Meta)
Vue.use(VueI18n)
Vue.use(VueAwesomeSwiper)
Vue.component(VueQrcode.name,VueQrcode)

Vue.prototype.$get=get
Vue.prototype.$ajax=post
Vue.prototype.$socket = function(){
	return new WebSocket( contractWsUr+this.$userID())
}


const back = window.localStorage.getItem('back')
// window.localStorage.setItem("User-Lang", "zh");
if (!back){
  window.localStorage.setItem('back','c')
}
const i18n = new VueI18n({
  // locale: LangStorage.getLang('zh'),  // 语言标识，后面会用做切换和将用户习惯存储到本地浏览器
  locale: getLang() || 'en', // 语言标识
  messages: {
    'zh': require('./locale/zh'),
    'en': require('./locale/en'),
    'ko': require('./locale/ko'),
    'ja': require('./locale/ja')
  }
})
Vue.config.productionTip = false
Vue.prototype.$userID = function(){
	return localStorage.getItem('contactToken') || '';
}

console.group('Hotdog | 全球领先数字资产交易平台')
console.log(
  '\n %c Hotdog v3.0.0 Pro %c Hotdog | 全球领先数字资产交易平台| https://www.hotdogvip.com',
  'color:#444;background:#eee;padding:5px 0;',
  'color:#eee;background:#444;padding:5px 0;'
)
console.groupEnd()

new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
