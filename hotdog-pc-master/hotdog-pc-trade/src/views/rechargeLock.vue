<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div
        class="container"
        id="assetsmanager"
      >
        <div class="main-panel f-cb">
          <side-nav></side-nav>
          <div class="content" id="view">
            <div class="inner-recharge-lock" id="innerRechargeLock">

              <div class="header">
                <a href="#/myLockup">{{$t('user.LockPosition')}}</a>
                <i class="iconfont icon-angleright"></i>
                <span>{{$t('user.ImitationCoinLockUp')}}</span>
              </div>

              <div class="rechargelock-main">
                <div class="rm-inner">
                  <div class="coin-type input-wrap">
                    <label>{{$t('user.Coin')}}</label>
                    <input
                      type="text"
                      value=""
                      readonly
                      v-model="currencyType"
                    >
                  </div>
                  <div class="coin-address input-wrap">
                    <label>{{$t('user.AddressS')}}</label>
                    <input
                      type="text"
                      value=""
                      readonly
                      v-model="coinAddress"
                    >
                    <i class="iconfont icon-fuzhi"
                      v-clipboard:copy="coinAddress"
                      v-clipboard:success="onCopy"
                      v-clipboard:error="onError"></i>
                  </div>
                  <ul
                    class="tips-list"
                    id="tipsList"
                  >
                    <li>{{$t('user.ItIsProhibited')}}</li>
                    <li>{{$t('user.PleaseRechargeAtLeast100')}}</li>
                    <li>{{$t('user.TheHRCCharged')}}</li>
                    <li>{{$t('user.HRCLockBin')}}</li>
                  </ul>
                </div>
              </div>

              <div class="rechargelock-record">
                <div class="rr-title">{{$t('user.DepositHistory')}}</div>
                <div class="rr-list">
                  <div class="rr-list-header">
                    <span class="rlh-addr">{{$t('header.CurrencyAddress')}}</span>
                    <span class="rlh-type">{{$t('header.RechargeCurrency')}}</span>
                    <span class="rlh-num">{{$t('header.NumberOfCoins')}}</span>
                    <span class="rlh-time">{{$t('user.UpdateTime')}}</span>
                    <span class="rlh-state">{{$t('tradecenter.Status')}}</span>
                  </div>
                  <div
                    class="rr-list-main"
                    id="rrListMain"
                  >
                    <dl v-for="(item,index) in list" :key="index">
                      <dd>
                        <div class="rl-row">
                          <span>{{item.r_address}}</span>
                          <span>{{item.currency}}</span>
                          <span>{{item.r_amount}}</span>
                          <span>{{item.r_create_time}}</span>
                          <span>{{item.state}}</span>
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
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import SideNav from '@/components/SideNav'
import { getAssetsRechargeAddr, genCoinAddress, getMember, getLockList } from '@/api/rechargeLock'
export default {
  components: {
    MyHeader,
    MyFooter,
    SideNav
  },
  data () {
    return {
      currency: '',
      coinAddress: '',
      list: ''
    }
  },
  created () {
    this.currency = this.$route.params.currency
    getAssetsRechargeAddr(this.currency).then(response => {
      let rechargeAddr = response.data.rechargeAddr
      if (response.state === 1) {
        // 如果没有获取到 请求获取
        if (rechargeAddr == null) {
          let data = {
            currency: this.currency
          }
          genCoinAddress(data).then((response) => {
            this.coinAddress = response.msg
          })
        } else {
          this.coinAddress = rechargeAddr
        }
      } else if (response.state === -1) {
        if (response.msg === 'LANG_NO_LOGIN') {
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
    getMember().then(res => {
      let memberId = res.data.id
      getLockList(memberId).then((response) => {
        if (response.status === 1) {
          let list = response.list
          list.forEach(ele=>{
            switch (ele.r_status) {
              case -1:
                ele.state=this.$t('user.Obligation')
                break;
              case 0:
                ele.state=this.$t('user.Unconfirmed')
                break;
              case 1:
                ele.state=this.$t('user.Confirmed')
                break;
              case 2:
                ele.state=this.$t('user.Canceled')
                break;
            }
          })
          this.list = list
        }
      })
    })
  },
  methods: {
    onCopy () {
      this.$message({
        type: 'success',
        message: this.$t('header.CopySuccess'),
        duration: 3000,
        showClose: true
      })
    },
    onError () {
      this.$message({
        type: 'error',
        message: this.$t('header.CopyFail'),
        duration: 3000,
        showClose: true
      })
    },
    userLogout () {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    }
  }
}
</script>
