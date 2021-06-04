import Vue from 'vue'
import Vuex from 'vuex'
import coinType from './coinType'
import withDrawRec from './withDrawRec'
import auth from './auth'
Vue.use(Vuex)

export default new Vuex.Store({
  modules:{
    coinType,
    withDrawRec,
    auth
  }
})
