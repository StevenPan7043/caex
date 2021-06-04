import Vue from 'vue';
import Router from 'vue-router';
import Layout from './views/Layout/index.vue';
import Home from '@/views/Home/index.vue';
import User from '@/views/User/index.vue';
import Assets from '@/views/Assets/index.vue';
import Order from '@/views/Order/index.vue';
import MyAds from '@/views/MyAds/index.vue';
import PublishAds from '@/views/PublishAds/index.vue';
import BusinessApplication from '@/views/BusinessApplication/index.vue';
import BusinessApplication2 from '@/views/BusinessApplication2/index.vue';
import Login from '@/views/Login/index.vue';
import Register from '@/views/Register/index.vue';
import ForgetPwd from '@/views/ForgetPwd/index.vue';
import UserInfo from '@/views/UserInfo/index.vue';
import OrderDetail from '@/views/OrderDetail/index.vue';
import Auth from '@/views/auth/index.vue';
import EditLoginPwd from '@/views/editLoginPwd/index.vue';
import ResetAssetPwd from '@/views/resetAssetPwd/index.vue';
import ResetContact from '@/views/resetContact/index.vue';

import store from './store';

Vue.use(Router);

const router = new Router({
  scrollBehavior() {
    return { x: 0, y: 0 };
  },
  routes: [
    {
      path: '/',
      redirect: 'home',
      component: Layout,
      children: [
        {
          path: 'home',
          name: 'home',
          component: Home,
        },
        {
          path: 'user',
          name: 'user',
          component: User,
        },
        {
          path: 'assets',
          name: 'assets',
          component: Assets,
        },
        {
          path: 'order',
          name: 'order',
          component: Order,
        },
        {
          path: 'myAds',
          name: 'myAds',
          component: MyAds,
        },
        {
          path: 'publishAds',
          name: 'publishAds',
          component: PublishAds,
        },
        {
          path: 'businessApp',
          name: 'businessApplication',
          component: BusinessApplication,
        },
        {
          path: 'businessApp2',
          name: 'businessApplication2',
          component: BusinessApplication2,
        },
        {
          path: 'login',
          name: 'login',
          component: Login,
        },
        {
          path: 'register',
          name: 'register',
          component: Register,
        },
        {
          path: 'forgetPwd',
          name: 'forgetPwd',
          component: ForgetPwd,
        },
        {
          path: 'userInfo',
          name: 'userInfo',
          component: UserInfo,
        },
        {
          path: 'orderDetail/:id',
          name: 'orderDetail',
          component: OrderDetail,
        },
        {
          path: 'auth',
          name: 'auth',
          component: Auth,
        },
        {
          path: 'editLoginPwd',
          name: 'editLoginPwd',
          component: EditLoginPwd,
        },
        {
          path: 'resetAssetPwd',
          name: 'resetAssetPwd',
          component: ResetAssetPwd,
        },
        {
          path: 'resetContact',
          name: 'resetContact',
          component: ResetContact,
        },
      ],
    },
    { path: '*', redirect: '/home' },
  ],
});
router.beforeEach((to, form, next) => {
  if (to.name === 'home') {
    store.commit('SET_HASH', 'home');
    next();
  } else if (to.name === 'publishAds') {
    store.commit('SET_HASH', 'publishAds');
    next();
  } else if (to.name === 'order') {
    store.commit('SET_HASH', 'order');
    next();
  } else if (to.name === 'assets') {
    store.commit('SET_HASH', 'assets');
    next();
  } else {
    store.commit('SET_HASH', 'null');
    next();
  }
});
export default router;
