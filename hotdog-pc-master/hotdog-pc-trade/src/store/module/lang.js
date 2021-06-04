import {getCurrencyInfo } from '@/api/tradecenter'

const app = {
  state: {
    lang: 'zh',
    currencyInfo:''
  },
  mutations: {
    SET_LANG: (state, data) => {
      state.lang = data
    },
    SET_CURRENCYINFO:(state,data)=>{
      state.currencyInfo=data
    }
  },
  actions: {
    getCoinInfo ({ commit },baseCurrencyName) {
      getCurrencyInfo(baseCurrencyName).then((response) => {
        let currencyInfo = response.data
        commit('SET_CURRENCYINFO',currencyInfo)
      })
    },
  }
}

export default app
