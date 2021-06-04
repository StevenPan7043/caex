export default {
    namespaced:true,
    state: {
        auths:''
    },
    getters: {
        _auths(_state) {
            return _state.auths;
        }
    },
    mutations: {
        changeAuths(_state,payload) {
            // console.log(payload)
            _state.auths = payload;
        }
    }
}