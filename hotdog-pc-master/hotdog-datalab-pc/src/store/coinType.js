export default {
    namespaced:true,
    state: {
        coinName:'',
        baseWQuota: '',
        valuationWQuota: ''
    },
    getters: {
        _coinName(_state) {
            return _state.coinName;
        }
    },
    mutations: {
        changeName(_state,payload) {
            _state.coinName = payload.name;
        },
        changeBaseWQ(_state,payload) {
            _state.baseWQuota = payload.base;
        },
        changeValuationWQ(_state,payload) {
            _state.valuationWQuota = payload.Valuation;
        }
    }
}