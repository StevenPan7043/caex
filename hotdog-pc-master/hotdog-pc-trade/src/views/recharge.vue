<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
          <!--          <asset-side-nav></asset-side-nav>-->
          <div class="content" id="view">
            <div class="inner-top-up" id="innerRecharge">
              <div class="header">
                <a href="#/myAssets" style="color: #196bdf">{{
                  $t('user.Assets')
                }}</a>
                <i class="iconfont icon-angleright"></i>
                <span>{{ $t('user.Deposit') }}</span>
              </div>
              <div class="topup-main">
                <div class="tm-inner" id="tmInner">
                  <div class="coin-type input-wrap">
                    <div style="font-size: 12px; color: #495666">
                      {{ currency }}{{ $t('user.Deposit')
                      }}{{ $t('user.AddressS') }}
                    </div>
                    <input type="text" readonly v-model="address" />
                    <button
                      v-clipboard:copy="address"
                      v-clipboard:success="onCopy"
                      v-clipboard:error="onError"
                    >
                      {{ $t('header.CopyS') }}
                    </button>
                  </div>
                  <div class="chain_container" v-if="currency === 'USDT'">
                    <div
                      style="
                        font-size: 12px;
                        color: #495666;
                        height: 34px;
                        line-height: 34px;
                      "
                    >
                      {{ $t('user.ChainName') }}
                    </div>
                    <div class="chain_containerBot">
                      <div @click="changeSelect(0)">
                        <custom-radio :cur="select === 'OMNI'"
                          >OMNI</custom-radio
                        >
                      </div>
                      <div @click="changeSelect(1)">
                        <custom-radio :cur="select === 'ERC20'"
                          >ERC20</custom-radio
                        >
                      </div>
                      <div @click="changeSelect(2)">
                        <custom-radio :cur="select === 'TRC20'"
                          >TRC20</custom-radio
                        >
                      </div>
                    </div>
                  </div>
                  <!--                  <div class="coin-address input-wrap">-->
                  <!--                    <div>{{$t('user.AddressSlab')}}</div>-->
                  <!--                    <input-->
                  <!--                      type="text"-->
                  <!--                      readonly-->
                  <!--                      v-model="address"-->
                  <!--                    >-->
                  <!--                    <i-->
                  <!--                      class="iconfont icon-fuzhi"-->
                  <!--                      v-clipboard:copy="address"-->
                  <!--                      v-clipboard:success="onCopy"-->
                  <!--                      v-clipboard:error="onError"-->
                  <!--                    ></i>-->
                  <!--                    <i class="iconfont icon-erweima" -->
                  <!--                      @mouseenter="qrcodeMouseenter" -->
                  <!--                      @mouseleave="qrcodeMouseleave"-->
                  <!--                    ></i>-->
                  <!--                    <div class="qrcodeContainer">-->
                  <!--                      <qrcode :value="address" :options="{size:130}"></qrcode>-->
                  <!--                    </div>-->
                  <!--                  </div>-->
                  <div class="coin-tips input-wrap" v-show="tipShow">
                    <div style="font-size: 12px; color: #495666">
                      {{ $t('user.AddressSlab') }}
                    </div>
                    <input type="text" readonly v-model="useruid" />
                    <button
                      v-clipboard:copy="useruid"
                      v-clipboard:success="onCopy"
                      v-clipboard:error="onError"
                    >
                      {{ $t('header.CopyS') }}
                    </button>
                  </div>
                </div>
                <div class="qrcodeContainer">
                  <qrcode :value="address" :options="{ size: 100 }"></qrcode>
                </div>
                <ul class="tips-listtt" id="tipsList">
                  <div v-for="(item, index) in currencyLst" :key="index">
                    <li>
                      {{ $t('user.ItIsForbiddenTo') }}{{ currency
                      }}{{ $t('user.AddressRechargeSSS') }}{{ currency
                      }}{{ $t('user.OtherAssets') }}{{ currency
                      }}{{ $t('user.TheAddressOfThe') }}{{ currency
                      }}{{ $t('user.AssetsWillNotBeRecovered') }}
                    </li>
                    <li v-if="currency == 'AAC' || currency == 'EOS'">
                      {{ $t('user.PleaseBeSureToFillOut') }}
                    </li>
                    <li v-else>
                      {{ $t('user.Use') }}{{ currency
                      }}{{ $t('user.AddressRecharge') }}
                    </li>
                    <li>
                      {{ $t('user.TheMinimumAmount')
                      }}{{ ' ' + item.c_min_recharge + ' ' + currency
                      }}{{ $t('user.RechargeLessThanTheMinimumAmountWill') }}
                    </li>
                    <li v-if="currency === 'ETH'">{{ $t('user.ETHAdd') }}</li>
                  </div>
                </ul>
              </div>
              <div class="topup-record">
                <div class="tr-title">
                  <span
                    class="tab-bar"
                    :class="index === 0 ? 'select' : ''"
                    @click="handleTabClick(0)"
                    >{{ $t('user.DepositHistory') }}</span
                  >
                  <span
                    class="tab-bar"
                    :class="index === 1 ? 'select' : ''"
                    v-if="lock"
                    @click="handleTabClick(1)"
                    >锁仓记录</span
                  >
                </div>
                <div class="tr-list" v-show="index === 0">
                  <div class="tr-list-header">
                    <span class="tlh-time">{{ $t('user.UpdateTime') }}</span>
                    <span class="tlh-type">{{
                      $t('header.RechargeCurrency')
                    }}</span>
                    <span class="tlh-types">{{
                      $t('header.Rechargetype')
                    }}</span>
                    <span class="tlh-num">{{
                      $t('header.NumberOfCoins')
                    }}</span>
                    <span class="tlh-state">{{
                      $t('tradecenter.Status')
                    }}</span>
                    <span class="tlh-addr">{{
                      $t('header.CurrencyAddress')
                    }}</span>
                  </div>
                  <div class="tr-list-main" id="trListMain">
                    <dl v-for="(item, index) in list" :key="index">
                      <dd>
                        <div class="tl-row">
                          <span>{{ item.r_create_time }}</span>
                          <span>{{ item.currency }}</span>
                          <span>{{ $t('user.Deposit') }}</span>
                          <span>{{ item.r_amount }}</span>
                          <span>{{ item.mystate }}</span>
                          <el-tooltip
                            effect="dark"
                            :content="item.r_txid"
                            placement="top"
                          >
                            <span class="roy_add">{{
                              item.r_txid == null ? '--' : item.r_txid
                            }}</span>
                          </el-tooltip>
                        </div>
                      </dd>
                    </dl>
                  </div>
                </div>

                <div class="tr-list" v-show="index === 1">
                  <div class="tr-list-header">
                    <span class="tlh-time">锁仓时间</span>
                    <span class="tlh-type" style="width: 14%">锁仓币种</span>
                    <span class="tlh-num">锁仓数量</span>
                    <span class="tlh-time">已释放</span>
                    <span class="tlh-time">待释放</span>
                    <span class="tlh-addr">状态</span>
                  </div>
                  <div class="tr-list-main" id="trListMain">
                    <dl v-for="(item, index) in recordList" :key="index">
                      <dd>
                        <div class="tl-rowTR">
                          <span class="tlh-time">{{ item.createTime }}</span>
                          <span class="tlh-type">{{ item.currency }}</span>
                          <span class="tlh-num">{{
                            item.warehouseAmount
                          }}</span>
                          <span class="tlh-time">{{
                            item.warehouseReleaseAmount
                          }}</span>
                          <span class="tlh-time">{{
                            item.warehouseAmount - item.warehouseReleaseAmount
                          }}</span>
                          <span
                            class="tlh-addr"
                            :class="item.isRelease == 0 ? 'locking' : 'lockend'"
                            >{{ item.isRelease | releaseStateFilter }}</span
                          >
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
import AssetSideNav from '@/components/AssetSideNav'
import CustomRadio from '@/components/customRadio'
import {
  getAssetsRechargeAddr,
  genCoinAddress,
  getAssetsRecharge,
  getAssetsLst,
  getLockCoinList,
  getLockRecord,
  getMember,
} from '@/api/recharge'
export default {
  components: {
    MyHeader,
    MyFooter,
    CustomRadio,
    AssetSideNav,
  },
  data() {
    return {
      currency: '',
      useruid: '',
      tipShow: false,
      address: '',
      list: '',
      currencyLst: '',
      qrcodeShow: true,
      lock: false,
      lockInfo: null,
      index: 0,
      memberId: null,
      recordList: [],
      qrcodeShow: false,
      is_in_eth: 0,
      select: 'ERC20',
      Lilist: ['OMNI', 'ERC20', 'TRC20'],
    }
  },
  filters: {
    releaseStateFilter(state) {
      if (state === 0) {
        return '锁仓中'
      } else {
        return '已结束'
      }
    },
  },

  created() {
    this.currey()
    this.loadAssetsRechargeAddr()
    this.loadAssetsRecharge()
    this.loadAssetsRechargeTips()
    this.getLockCoinList()
    this.getLockRecord()
  },
  methods: {
    currey() {
      this.currency = this.$route.params.currency
      if (this.currency) {
        localStorage.setItem('rechargeCoin', this.currency)
      } else {
        this.currency = localStorage.getItem('rechargeCoin')
      }
    },
    changeSelect(index) {
      this.select = this.Lilist[index];
      this.loadAssetsRechargeAddr()
    },
    async getLockCoinList() {
      var res = await getLockCoinList()
      var lockCoin = res.Rows
      lockCoin.forEach((item) => {
        if (item.currency === this.currency) {
          this.lock = true
          this.lockInfo = item
          let ruleType = item.ruleType
          let date = ruleType.split('_')[0]
          let dateNum = ruleType.split('_')[1]
          let display = ''
          switch (date) {
            case 'HOUR':
              display = '小时'
              break
            case 'MONTH':
              display = '月'
              break
            case 'DATE':
              display = '天'
              break
          }
          this.$alert(
            `您所充值的${this.currency}币种，充值后将直接进入锁仓账户，将按照${dateNum}${display}${item.ruleDetail}%释放`,
            '温馨提示',
            {
              confirmButtonText: '确定',
            }
          )
        }
      })
    },
    handleTabClick(index) {
      this.index = index
    },
    async getLockRecord() {
      let userRes = await getMember()
      if (userRes.state === 1) {
        let userInfo = userRes.data
        this.memberId = userInfo.uid
      }
      let params = {
        memberId: this.memberId,
        currency: this.currency,
      }
      let res = await getLockRecord(params)
      this.recordList = res.Rows
    },
    loadAssetsRechargeAddr() {
      getAssetsRechargeAddr(this.currency, this.select).then((response) => {
        if (response.state === 1) {
          let rechargeAddr = response.data.rechargeAddr
          if (response.data.is_in_eth == 2) {
            this.useruid = JSON.parse(localStorage.getItem('useruid'))
            this.tipShow = true
          }
          if (rechargeAddr == null) {
            let data = {
              currency: this.currency,
            }
            if (this.currency === 'USDT') {
              data['currency_chain_type'] = this.select
            }
            genCoinAddress(data).then((response) => {
              this.address = response.msg
            })
          } else {
            this.address = rechargeAddr
          }
        } else if (response.state === -1) {
          if (response.msg === 'LANG_NO_LOGIN') {
            this.$message({
              type: 'error',
              message: this.$t('header.PlaseLogin'),
              duration: 3000,
              showClose: true,
            })
            setTimeout(() => {
              this.userLogout()
            }, 2000)
          }
        }
      })
    },
    loadAssetsRecharge() {
      const uid = localStorage.getItem('useruid')
      getAssetsRecharge(1, uid).then((response) => {
        if (response.state === 1) {
          let rechargeLst = response.data.rechargeLst
          let list = []
          rechargeLst.forEach((ele) => {
            if (ele.currency === this.currency) {
              list.push(ele)
            }
          })
          list.forEach((ele) => {
            switch (ele.r_status) {
              case -1:
                ele.mystate = this.$t('user.Obligation')
                break
              case 0:
                ele.mystate = this.$t('user.Unconfirmed')
                break
              case 1:
                ele.mystate = this.$t('user.Confirmed')
                break
              case 2:
                ele.mystate = this.$t('user.Canceled')
                break
            }
          })
          this.list = list
        } else if (response.state === -1) {
          if (response.msg === 'LANG_NO_LOGIN') {
            this.$message({
              type: 'error',
              message: this.$t('header.PlaseLogin'),
              duration: 3000,
              showClose: true,
            })
            setTimeout(() => {
              this.userLogout()
            }, 2000)
          }
        }
      })
    },
    loadAssetsRechargeTips() {
      getAssetsLst().then((response) => {
        let currencyLst = response.data.currencyLst
        let newCurrencyLst = []
        currencyLst.forEach((ele) => {
          if (ele.currency === this.currency) {
            newCurrencyLst.push(ele)
          }
        })
        this.currencyLst = newCurrencyLst
      })
    },
    onCopy() {
      this.$message({
        type: 'success',
        message: this.$t('header.CopySuccess'),
        duration: 3000,
        showClose: true,
      })
    },
    onError() {
      this.$message({
        type: 'error',
        message: this.$t('header.CopyFail'),
        duration: 3000,
        showClose: true,
      })
    },
    userLogout() {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    },
    qrcodeMouseenter() {
      this.qrcodeShow = true
    },
    qrcodeMouseleave() {
      this.qrcodeShow = false
    },
  },
}
</script>
<style scoped>
.icon-fuzhi {
  position: relative;
  left: -60px;
  margin-top: 2px;
}
.icon-erweima {
  position: relative;
  left: -60px;
  margin-top: 2px;
}
.qrcodeContainer {
  width: 120px;
  height: 120px;
  padding: 10px;
  box-sizing: border-box;
  box-shadow: 0 0 4px #999999;
  /*position: absolute;*/
  /*top: -40px;*/
  /*  right: -100px;*/
  margin-left: 20px;
  margin-top: 10px;
}
.coin-address {
  width: 688px;
}
.topup-main {
  /*height: 350px;*/
}
.tips-listtt {
  line-height: 24px;
}
.tab-bar {
  margin-right: 20px;
  cursor: pointer;
}
.select {
  color: #196bdf;
  font-size: 14px;
}
.locking {
  color: #196bdf;
}
.lockend {
  color: #c3cfda;
}
.chain_containerBot {
  display: flex;
  /*width: 900px;*/
  height: 70px;
  align-items: flex-start;
  /*margin-top: 10px;*/
}
.chain_container label {
  height: 40px;
  display: flex;
  align-items: center;
}
</style>

