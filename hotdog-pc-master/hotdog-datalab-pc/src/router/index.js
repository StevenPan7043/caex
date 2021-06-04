import Vue from 'vue'
import VueRouter from 'vue-router'
import Base from '../views/BaseLayout.vue'
// import Home from '../views/base/Home'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'base',
    component: Base,
    redirect: {name:'default'},
    children: [
      {
        path: '/dataLab',
        name: 'dataLab',
        component: () => import('../views/base/Home.vue'),
      },
      {
        path: '/tradeData',
        name: 'tradeData',
        component: () => import('../views/base/TradeData.vue'),
      },
      {
        path: '/inOut',
        name: 'inOut',
        component: () => import('../views/base/InOut.vue'),
      },
      {
        path: '/wallet',
        name: 'wallet',
        component: () => import('../views/base/Wallet.vue'),
      },
      {
        path: '/withDraw',
        name: 'withDraw',
        component: () => import('../views/base/WithDraw.vue'),
      },
      {
        path: '/withdrawRec',
        name: 'withdrawRec',
        component: () => import('../views/base/WithdrawRec.vue'),
      },
      {
        path: '/default',
        name: 'default',
        component: () => import('../views/base/default.vue'),
      }
    ]
  },
]

const router = new VueRouter({
  mode: 'hash',
  linkActiveClass: "my-active",
  base: process.env.BASE_URL,
  routes
})

export default router
