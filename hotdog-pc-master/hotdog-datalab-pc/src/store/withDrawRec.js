import { assetsRechargeRec } from "@/api/withDraw/index.js";
export default {
    namespaced:true,
    state: {
        totalPages:null,
        withDrawData:'',
    },
    mutations: {
        changeWithdraw(_state,payload) {
            _state.totalPages = payload.total;
            _state.withDrawData = payload.data;
        }
    },
    actions: {
        changeWithdraw({ commit },params) {
            assetsRechargeRec(params.page,params).then(res => {
                if (res.data.state == 1 && params.currency) {
                commit('changeWithdraw',{total:res.data.data.total,data:res.data.data.rechargeLst})
                }
              });
            },
           
        }
    }