<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="usercenter">
        <div class="main-panel f-cb">
<!--          <side-nav></side-nav>-->
          <div class="content" id="view">

            <div class="inner-login-record" id="ilr">
              <div class="header">{{$t('user.LoginRecord')}}</div>
              <div class="login-record-list">
                <div class="inner-list">

                  <div class="ilr-head">
                    <span>{{$t('user.Username')}}</span>
                    <span>{{$t('user.IPAddress')}}</span>
                    <span>{{$t('user.LogonTime')}}</span>
                  </div>

                  <div class="ilr-content" id="dlMain"></div>
                  <dl v-for="(item,index) in log" :key="index">
                    <dd>
                      <div class="ilr-row">
                        <span>{{item.m_name}}</span>
                        <span>{{item.oper_ip}}</span>
                        <span>{{item.oper_time}}</span>
                      </div>
                    </dd>
                  </dl>
                </div>

              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import SideNav from '@/components/SideNav'
import { getLoginlog } from '@/api/loginRecord'
export default {
  components: {
    MyHeader,
    MyFooter,
    SideNav
  },
  data () {
    return {
      log: '',
      user:[
        this.$t('LoginRecord'),
        this.$t('Username'),
        this.$t('IPAddress'),
        this.$t('LogonTime'),
      ]
    }
  },
  mounted () {
    getLoginlog().then(res => {
      if (res.state === 1) {
        this.log = res.data
      } else if (res.state === -1) {
        if (res.msg === 'LANG_NO_LOGIN') {
          this.$message({
            type: 'error',
            message: this.$t('header.PlaseLogin'),
            duration: 3000,
            showClose: true
          })
          setTimeout(() => {
            this.userLogout()
          }, 2000)
        }
      }
    })
  },
  methods: {
    userLogout () {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    }
  }
}
</script>
