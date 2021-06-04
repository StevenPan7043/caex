import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    wsMsg: [],
    websock: null,
    userInfo: {},
    loginState: false,
    hash: null,
  },
  mutations: {
    INIT_SOCKET(state, websock) {
      state.websock = websock;
    },
    SET_SOCKET_ONMESSAGE(state, onmessage) {
      state.websock.onmessage = onmessage;
    },
    SET_SOCKET_ONOPEN(state, onopen) {
      state.websock.onopen = onopen;
    },
    SET_SOCKET_ONERROR(state, onerror) {
      state.websock.onerror = onerror;
    },
    SET_SOCKET_ONCLOSE(state, onclose) {
      state.websock.onclose = onclose;
    },
    CLOSE_SOCKET(state) {
      state.websock.close();
    },
    SET_USERINFO(state, userInfo) {
      state.userInfo = userInfo;
      state.loginState = true;
    },
    CLEAR_USERINFO(state) {
      state.userInfo = {};
      state.loginState = false;
    },
    SET_HASH(state, hash) {
      state.hash = hash;
    },
  },
  actions: {

  },
});
