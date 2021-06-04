import Vue from 'vue'
import Vuex from 'vuex'
import user from './module/user'
import lang from './module/lang'

import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    lang
  },
  state:{
    themecolor: 'c'
  },
  mutations:{
    setThemeColor(state,curcolor){
      this.state.themecolor = curcolor
    }
  },
  getters
})

export default store
