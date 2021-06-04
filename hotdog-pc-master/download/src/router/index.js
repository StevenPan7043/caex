import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
    routes: [{
        path: '/',
        name: 'Home',
        component: resolve => require(['@/views/home'], resolve)
    }, {
        path: '/download',
        name: 'Download',
        component: resolve => require(['@/views/download'], resolve)
    }, {
        path: '/testflight',
        name: 'TestFlight',
        component: resolve => require(['@/views/testflight'], resolve)
    }]
})