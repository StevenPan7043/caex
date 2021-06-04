import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [{
    path: '/',
    name: 'Home',
    component: resolve => require(['@/views/home'], resolve)
  }, {
    path: '/login',
    name: 'Login',
    component: resolve => require(['@/views/login'], resolve)
  },{
    path: '/loginm',
    name: 'Loginm',
    component: resolve => require(['@/views/loginm'], resolve)
  },{
    path: '/sharem',
    name: 'Sharem',
    component: resolve => require(['@/views/sharem'], resolve)
  }, {
    path: '/register',
    name: 'Register',
    component: resolve => require(['@/views/register'], resolve)
  },{
    path: '/registerm',
    name: 'Registerm',
    component: resolve => require(['@/views/registerm'], resolve)
  }, {
    path: '/resetpassword',
    name: 'Resetpassword',
    component: resolve => require(['@/views/resetpassword'], resolve)
  }, {
    path: '/accountInfo',
    name: 'AccountInfo',
    component: resolve => require(['@/views/accountInfo'], resolve)
  }, {
    path: '/support',
    name: 'Support',
    component: resolve => require(['@/views/support'], resolve)
  }, {
    path: '/newsdeta',
    name: 'Newsdeta',
    component: resolve => require(['@/views/newsdeta'], resolve)
  }, {
    path: '/detaillist',
    name: '/Detaillist',
    component: resolve => require(['@/views/detaillist'], resolve)
  }, {
    path: '/details',
    name: '/Details',
    component: resolve => require(['@/views/details'], resolve)
  }, {
    path: '/search',
    name: '/Search',
    component: resolve => require(['@/views/search'], resolve)
  }, {
    path: '/accountSecurity',
    name: 'AccountSecurity',
    component: resolve => require(['@/views/accountSecurity'], resolve)
  }, {
    path: '/loginRecord',
    name: 'LoginRecord',
    component: resolve => require(['@/views/loginRecord'], resolve)

  }, {
    path: '/activity',
    name: 'Activity',
    component: resolve => require(['@/views/activity'], resolve)
  }, {
    path: '/Activitydetails',
    name: 'Activitydetails',
    component: resolve => require(['@/views/Activitydetails'], resolve)

  }, {
    path: '/bgcc',
    name: 'Bgcc',
    component: resolve => require(['@/views/bgcc'], resolve)

  }, {
    path: '/apiManager',
    name: 'ApiManager',
    component: resolve => require(['@/views/apiManager'], resolve)

  }, {
    path: '/identityVerify',
    name: 'IdentityVerify',
    component: resolve => require(['@/views/identityVerify'], resolve)

  }, {
    path: '/myAssets',
    name: 'MyAssets',
    component: resolve => require(['@/views/myAssets'], resolve)

  }, {
    path: '/addressManage',
    name: 'AddressManage',
    component: resolve => require(['@/views/addressManage'], resolve)

  }, {
    path: '/assetsRecord',
    name: 'AssetsRecord',
    component: resolve => require(['@/views/assetsRecord'], resolve)

  },
    {
      path: '/myLockup',
      name: 'MyLockup',
      component: resolve => require(['@/views/myLockup'], resolve)
    },
    {
      path: '/myLockdest',
      name: 'MyLockdest',
      component: resolve => require(['@/views/myLockdest'], resolve)
    },
    {
      path: '/tradeBill',
      name: 'TradeBill',
      component: resolve => require(['@/views/tradeBill'], resolve)

    }, {
      path: '/detailLock',
      name: 'DetailLock',
      component: resolve => require(['@/views/detailLock'], resolve)

    }, {
      path: '/feeList',
      name: 'FeeList',
      component: resolve => require(['@/views/feeList'], resolve)

    }, {
      path: '/newsList',
      name: 'NewsList',
      component: resolve => require(['@/views/newsList'], resolve)

    }, {
      path: '/newsDetail',
      name: 'NewsDetail',
      component: resolve => require(['@/views/newsDetail'], resolve)
    }, {
      path: '/recharge',
      name: 'Recharge',
      component: resolve => require(['@/views/recharge'], resolve)

    }, {
      path: '/rechargeLock',
      name: 'RechargeLock',
      component: resolve => require(['@/views/rechargeLock'], resolve)

    }, {
      path: '/userAgreement',
      name: 'UserAgreement',
      component: resolve => require(['@/views/userAgreement'], resolve)
    }, {
      path: '/withdraw',
      name: 'Withdraw',
      component: resolve => require(['@/views/withdraw'], resolve)


    }, {
      path: '/tradecenter',
      name: 'Tradecenter',
      component: resolve => require(['@/views/tradecenter'], resolve)


    }, {
      path: '/Quotes',
      name: 'Quotes',
      component: resolve => require(['@/views/Quotes'], resolve)
    },
    {
      path: '/invite',
      name: 'invite',
      component: resolve => require(['@/views/invitePage'], resolve)
    },
    {
      path: '/APPliction',
      name: 'APPliction',
      component: resolve => require(['@/views/APPliction'], resolve)
    },
    {
      path: '/help',
      name: 'help',
      component: resolve => require(['@/views/help'], resolve)
    },
    {
      path: '/loginPass',
      name: 'loginPass',
      component: resolve => require(['@/views/loginPass'], resolve)
    },
    {
      path: '/capitalPass',
      name: 'capitalPass',
      component: resolve => require(['@/views/capitalPass'], resolve)
    },
    {
      path: '/order',
      name: 'Order',
      component: resolve => require(['@/views/contractOrder/order'], resolve)
    },
    {
      path: '/order_entrust',
      name: 'OrderEntrust',
      component: resolve => require(['@/views/contractDelegateOrder/index'], resolve)
    },
    {
      path: '/trade',
      name: 'Trade',
      component: resolve => require(['@/views/contract_trade/index'], resolve)
    },
    {
      path: '/trade2',
      name: 'Trade2',
      component: resolve => require(['@/views/trade'], resolve)
    },
    {
      path: '/contractAssets',
      name: 'ContractAssets',
      component: resolve => require(['@/views/contractAssets'], resolve)
    },
    {
      path: '/downloadPage',
      name: 'DownloadPage',
      component: resolve => require(['@/views/download_page/downloadPage'], resolve)
    },
    {
      path: '/maintain',
      name: 'maintain',
      component: resolve => require(['@/views/404'], resolve)
    }
  ],
  scrollBehavior () {
    return {x: 0, y: 0}
  }
})
