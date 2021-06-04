<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
          <!--          <asset-side-nav></asset-side-nav>-->
          <div class="content" id="view">
            <div class="inner-get-coin" id="innerGetCoin">
              <div class="header">
                <a href="#/myAssets" style="color: #196bdf">{{
                  $t('user.Assets')
                }}</a>
                <i class="iconfont icon-angleright"></i>
                <span>{{ $t('user.Withdraw') }}</span>
                <button class="btn btn-getcoin">
                  <a
                    ><router-link to="/addressManage">{{
                      $t('user.Address1')
                    }}</router-link></a
                  >
                </button>
              </div>
              <div class="getcoin-main">
                <div class="gm-inner">
                  <div class="coin-type input-wrap">
                    <div>
                      {{ currency }}{{ $t('user.Withdraw')
                      }}{{ $t('user.AddressS') }}
                    </div>
                    <el-autocomplete
                      popper-class="my-autocomplete"
                      class="input"
                      v-model="form.selectAddress"
                      :fetch-suggestions="querySearch"
                      :placeholder="$t('user.PleaseSelectTheWithdrawalAddress')"
                      @select="handleSelect"
                      style="display: block"
                    >
                      <template slot-scope="{ item }">
                        <div class="name">地址：{{ item.addr }}</div>
                        <span class="addr">标签：{{ item.addr_label }}</span>
                      </template>
                    </el-autocomplete>
                  </div>
                  <div class="chain_container" v-if="currency === 'USDT'">
                    <div class="LinkName">{{ $t('user.ChainName') }}</div>
                    <div class="LinkBot">
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
                  <!--                  <div class="coin-addr input-wrap">-->
                  <!--                    <label>{{$t('user.WithdrawAddress')}}</label>-->
                  <!-- <select class="type" v-model="form.selectAddress">
                      <option value>{{$t('user.PleaseSelectTheWithdrawalAddress')}}</option>
                      <option
                        :value="item.addr"
                        v-for="(item,index) in data"
                        :key="index"
                      >{{item.addr}}</option>
                    </select>-->
                  <!--                    <el-autocomplete-->
                  <!--                      popper-class="my-autocomplete"-->
                  <!--                      class="inline-input"-->
                  <!--                      v-model="form.selectAddress"-->
                  <!--                      :fetch-suggestions="querySearch"-->
                  <!--                      :placeholder="$t('user.PleaseSelectTheWithdrawalAddress')"-->
                  <!--                      @select="handleSelect"-->
                  <!--                    >-->
                  <!--                      <template slot-scope="{ item }">-->
                  <!--                        <div class="name">地址：{{ item.addr }}</div>-->
                  <!--                        <span class="addr">标签：{{ item.addr_label }}</span>-->
                  <!--                      </template>-->
                  <!--                    </el-autocomplete>-->
                  <!--                  </div>-->
                  <div class="coin-tips input-wrap" v-if="isInETH === 2">
                    <div
                      style="
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                      "
                    >
                      <span>{{ $t('header.AddressLabel') }}</span>
                      <span>注意：填写错误可能导致资产损失，请仔细核对</span>
                    </div>
                    <input
                      type="text"
                      :placeholder="$t('user.PleaseFillInTheAddressLabel')"
                      v-model="form.coinTips"
                      class="coin_tips"
                      @input="tipsChange"
                    />
                    <!--                    <div class="flex_container">-->
                    <!--                      <p>填写错误可能导致资产损失，请仔细核对</p>-->
                    <!--                      <el-tooltip-->
                    <!--                        effect="dark"-->
                    <!--                        :content="$t('header.WithdrawNode')"-->
                    <!--                        placement="top"-->
                    <!--                      >-->
                    <!--                        <div class="iconfont_container">-->
                    <!--                          <i class="iconfont icon-info"></i>-->
                    <!--                        </div>-->
                    <!--                      </el-tooltip>-->
                    <!--                    </div>-->
                    <!-- <span class="tip" >{{$t('header.WithdrawNode')}}</span> -->
                  </div>
                  <div class="coin-num input-wrap">
                    <div
                      style="
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                      "
                    >
                      <span>{{ $t('user.MentionACurrency') }}</span>
                      <span>
                        <span>
                          {{ $t('header.AvailableAssets') }}
                          <em style="color: #196bdf">{{ canUseAssets }}</em>
                        </span>
                        <span>
                          {{ $t('header.RemainWithdraw')
                          }}{{ withdraw.amount }}/{{ withdraw.max_withdraw }}
                          <span v-show="withdraw.limit_withdraw !== 0"
                            >{{ $t('header.RemainWithdrawTimes')
                            }}{{ withdraw.times }}/{{
                              withdraw.limit_withdraw
                            }}</span
                          >
                        </span></span
                      >
                    </div>
                    <input
                      type="text"
                      :placeholder="$t('header.EnterNumCoins')"
                      v-model="form.num"
                    />
                    <button class="allwith f-fl" @click="selectAll">
                      {{ $t('user.All') }}
                    </button>
                    <!--                    <div class="tip rest cloumn_flex_container">-->
                    <!--                      <p>-->
                    <!--                        {{$t('header.AvailableAssets')}}-->
                    <!--                        <em>{{canUseAssets}}</em>-->
                    <!--                      </p>-->
                    <!--                      <p>-->
                    <!--                        {{$t('header.RemainWithdraw')}}{{withdraw.amount}}/{{withdraw.max_withdraw}}-->
                    <!--                        <span-->
                    <!--                          v-show="withdraw.limit_withdraw!==0"-->
                    <!--                        >{{$t('header.RemainWithdrawTimes')}}{{withdraw.times}}/{{withdraw.limit_withdraw}}</span>-->
                    <!--                      </p>-->
                    <!--                    </div>-->
                  </div>
                  <div class="code input-wrap">
                    <div>{{ $t('user.ImageVerification') }}</div>
                    <input
                      type="text"
                      :placeholder="$t('user.PleaseVerificationCode')"
                      v-model="form.imgCode"
                    />
                    <img
                      :src="backImgCode"
                      alt
                      class="btn-img f-fl withdrawImg"
                      @click="getKaptcha"
                    />
                  </div>
                  <div class="code input-wrap">
                    <div>{{ $t('user.verificationCode') }}</div>
                    <input
                      type="text"
                      class="mobile"
                      :placeholder="$t('user.PleaseEnterTheverification')"
                      v-model="form.mobileCode"
                    />
                    <get-code
                      @get-kaptcha="getKaptcha"
                      :tokencode="tokencode"
                      :imgCode="form.imgCode"
                      type="forgot"
                    ></get-code>
                  </div>
                  <div class="tradepwd input-wrap">
                    <div>{{ $t('user.TradersPassword') }}</div>
                    <input
                      type="password"
                      :placeholder="$t('header.EnterTransPwd')"
                      v-model="form.password"
                    />
                    <!--                    <button @click="toAccountSecurity">{{$t('user.ForgetPassword')}}</button>-->
                  </div>
                  <!-- <button type="button" class="btn-confirm" @click="confirmBtnClick" :disabled="confirmBtnDisable">
                    <span class="text">{{$t('user.ConfirmToMentionMoney')}}</span>
                    <span class="loading" v-show="confirmBtnDisable"></span>
                  </button>-->
                  <div class="coin-num input-wrap" style="padding-top: 30px">
                    <el-button
                      class="btn-confirm"
                      @click="confirmBtnClick"
                      :disabled="confirmBtnDisable"
                      :loading="confirmBtnDisable"
                    >
                      <span class="text">{{
                        $t('user.ConfirmToMentionMoney')
                      }}</span>
                    </el-button>
                  </div>
                  <ul class="tips-list" id="tipsList">
                    <li v-for="(item, index) in currencyLst" :key="index">
                      {{ $t('user.MinimumWithdrawalAmount') }}
                      {{ item.c_min_withdraw }} {{ currency
                      }}{{ $t('user.ASingleChargeIsRequired') }}
                      {{ item.withdraw_fee }} {{ currency }}
                    </li>
                    <li v-show="currency === 'GBT'">
                      {{ $t('user.GBTTips') }}
                    </li>
                    <li v-show="isInETH === 2">{{ $t('user.GBTTipsTR') }}</li>
                  </ul>
                </div>
              </div>
              <div class="record-main BG">
                <div class="tr-title">
                  <span>{{ $t('user.WithdrawalHistory') }}</span>
                </div>
                <div class="rm-list">
                  <div class="rm-recharge cur" id="recharge">
                    <div class="rm-list-header">
                      <span class="date">{{ $t('user.Dates') }}</span>
                      <span class="type">{{ $t('user.Coin') }}</span>
                      <span class="category">{{ $t('user.Type') }}</span>
                      <span class="number">{{ $t('user.Num') }}</span>
                      <span class="truly">{{ $t('user.Fee') }}</span>
                      <span class="state">{{ $t('user.Withdrawss') }}</span>
                      <span class="state">{{ $t('user.Status') }}</span>
                      <span class="state">{{
                        $t('header.CurrencyAddress')
                      }}</span>
                    </div>
                    <div class="rm-list-main">
                      <dl v-for="(item, index) in clist" :key="index">
                        <dd>
                          <div class="rl-row">
                            <span>{{ item.w_create_time }}</span>
                            <span>{{ item.currency }}</span>
                            <span>{{ $t('user.Withdraw') }}</span>
                            <span>{{ item.w_amount }}</span>
                            <span v-if="item.currency !== 'CNY'">{{
                              item.w_fee
                            }}</span>
                            <span v-else>{{
                              (item.otc_volume * 5) / 1000
                            }}</span>
                            <el-tooltip
                              effect="dark"
                              :content="item.member_coin_addr"
                              placement="top"
                              v-if="item.member_coin_addr.length > 20"
                            >
                              <span class="roy_add"
                                >{{
                                  item.member_coin_addr.slice(0, 10)
                                }}......{{
                                  item.member_coin_addr.slice(
                                    item.member_coin_addr.length - 10
                                  )
                                }}</span
                              >
                            </el-tooltip>
                            <span v-else>{{ item.member_coin_addr }}</span>
                            <!-- <span v-if="withDrawReason&&item.w_status===2">{{$t('header.ReasonCancellation')}}{{item.reject_reason}}</span> -->
                            <el-tooltip
                              effect="dark"
                              :content="
                                $t('header.ReasonCancellation') +
                                item.reject_reason
                              "
                              placement="top-end"
                              v-if="withDrawReason && item.w_status === 2"
                            >
                              <span>{{ item.state }}</span>
                            </el-tooltip>
                            <span v-else>{{ item.state }}</span>
                            <el-tooltip
                              effect="dark"
                              :content="item.w_txid"
                              placement="top"
                            >
                              <span class="roy_add">{{
                                item.w_txid == null ? '--' : item.w_txid
                              }}</span>
                            </el-tooltip>
                          </div>
                        </dd>
                      </dl>
                    </div>

                    <!--                    <div class="pages">-->
                    <!--                      <div class="inner-pages f-cb">-->
                    <!--                        <div class="flip f-fl">-->
                    <!--                          <span class="prev" @click="prevClick">{{$t('user.PageUp')}}</span>-->
                    <!--                          <span class="next" @click="nextClick">{{$t('user.PageDown')}}</span>-->
                    <!--                        </div>-->
                    <!--                        <div class="total f-fl">-->
                    <!--                          {{$t('user.First')}}<span class="first">{{pageRecharge}}</span>{{$t('user.PpTotal')}}<span class="sum">{{rechareSum}}</span>{{$t('user.Page')}}-->
                    <!--                        </div>-->
                    <!--                      </div>-->
                    <!--                    </div>-->
                  </div>
                </div>
              </div>
              <!--              <div class="pages">-->
              <!--                <div class="inner-pages f-cb">-->
              <!--                  <div class="flip f-fl">-->
              <!--                    <span class="prev" @click="prevClick">{{$t('user.PageUp')}}</span>-->
              <!--                    <span class="next pageNext" @click="nextClick">{{$t('user.PageDown')}}</span>-->
              <!--                  </div>-->
              <!--                  <div class="total f-fl">-->
              <!--                    {{$t('user.First')}}<span class="first">{{pageRecharge}}</span>{{$t('user.PpTotal')}}<span class="sum pageNext">{{rechareSum}}</span>{{$t('user.Page')}}-->
              <!--                  </div>-->
              <!--                </div>-->
              <!--              </div>-->
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
import GetCode from '@/components/GetCode'
import AssetSideNav from '@/components/AssetSideNav'
import CustomRadio from '@/components/customRadio'
import { getLang } from '../libs/utils.js'
import {
  getWithdrawAddr,
  getKaptcha,
  withdrawCreateI,
  getAssetsLst,
  getwithdrawInfo,
  getAssetsWithDraw,
} from '@/api/withdraw'
export default {
  components: {
    MyHeader,
    MyFooter,
    AssetSideNav,
    GetCode,
    CustomRadio,
  },
  data() {
    return {
      // pageRecharge: 1,
      // rechareSum: '',
      currency: '',
      data: '',
      form: {
        selectAddress: '',
        coinTips: '',
        num: '',
        imgCode: '',
        password: '',
        mobileCode: '',
      },
      backImgCode: '',
      tokencode: '',
      confirmBtnDisable: false,
      currencyLst: '',
      canUseAssets: '',
      canUseAssetsNum: '',
      isInETH: '',
      withdraw: {
        times: 0,
        amount: 0,
        max_withdraw: 0,
        limit_withdraw: 0,
        index: 0,
      },
      select: 'ERC20',
      useruid: '',
      tipShow: false,
      address: '',
      clist: '',
      qrcodeShow: true,
      lock: false,
      lockInfo: null,
      index: 0,
      memberId: null,
      recordList: [],
      is_in_eth: 0,
      Lilist: ['OMNI', 'ERC20', 'TRC20'],
    }
  },
  created() {
    this.currey()
  },
  mounted() {
    this.getAddress()
    this.getAssetsLst()
    this.getKaptcha()
    this.getInfo()
    this.loadAssetsRecharge()
  },
  methods: {
    currey() {
      this.currency = this.$route.params.currency
      if (this.currency) {
        localStorage.setItem('withdrawCoin', this.currency)
      } else {
        this.currency = localStorage.getItem('withdrawCoin')
      }
    },
    handleSelect(item) {
      this.form.selectAddress = item.addr
    },
    querySearch(queryString, cb) {
      var address = this.data
      var results = queryString
        ? address.filter(this.createFilter(queryString))
        : address
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (address) => {
        return (
          address.addr.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        )
      }
    },
    handleTabClick(index) {
      this.index = index
    },
    getInfo() {
      getwithdrawInfo(this.currency).then((res) => {
        if (res.state === 1) {
          if (res.data) {
            this.withdraw.times = res.data.times
            this.withdraw.amount = res.data.amount
            this.withdraw.max_withdraw = res.data.max_withdraw
            this.withdraw.limit_withdraw = res.data.limit_withdraw
          }
        }
      })
    },
    getAddress() {
      getWithdrawAddr(this.currency, this.select).then((response) => {
        if (response.state === 1) {
          let data = response.data
          this.isInETH = response.is_in_eth
          this.data = data
        }
      })
    },
    getAssetsLst() {
      getAssetsLst().then((response) => {
        let accountsLst = response.data.accountsLst
        let accountsLstArr = []
        let currencyLst = response.data.currencyLst
        let newCurrencyLst = []
        currencyLst.forEach((ele) => {
          if (ele.currency === this.currency) {
            newCurrencyLst.push(ele)
          }
        })
        this.currencyLst = newCurrencyLst

        for (let j = 0; j < accountsLst.length; j++) {
          accountsLstArr.push(accountsLst[j].currency)
          if (accountsLst[j].currency === this.currency) {
            this.dataBalance = accountsLst[j].balance
            this.canUseAssets =
              ' ' + accountsLst[j].balance + ' ' + this.currency
            this.canUseAssetsNum = accountsLst[j].balance
          }
        }
        let position = accountsLstArr.indexOf(this.currency)
        if (position === -1) {
          this.dataBalance = 0
          this.canUseAssets = ' ' + 0 + ' ' + this.currency
          this.canUseAssetsNum = 0
        }
      })
    },
    changeSelect(index) {
      this.select = this.Lilist[index]
      this.getAddress()
    },
    tipsChange() {
      let reg = /[^A-Za-z0-9]/g
      this.form.coinTips = this.form.coinTips.replace(reg, '')
    },
    selectAll() {
      const remainAmount =
        parseFloat(this.withdraw.max_withdraw) - this.withdraw.amount
      const all = Math.min(this.canUseAssetsNum, remainAmount)
      if (
        this.withdraw.times === this.withdraw.limit_withdraw &&
        this.withdraw.limit_withdraw !== 0
      ) {
        this.form.num = 0
      } else {
        this.form.num = all
      }
    },
    getKaptcha() {
      getKaptcha().then((res) => {
        let imgcode = res.check_code_img
        let tokencode = res.check_code_token
        this.backImgCode = imgcode
        this.tokencode = tokencode
      })
    },
    userLogout() {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    },
    toAccountSecurity() {
      this.$router.push('/accountSecurity')
    },
    confirmBtnClick() {
      if (this.form.num <= 0) {
        this.$message({
          type: 'error',
          message: '请输入正确的数量',
          duration: 3000,
          showClose: true,
        })
        return
      }
      if (this.confirmBtnDisable) {
        return
      }
      if (this.is_in_eth == 2 && this.form.coinTips == '') {
        this.$message({
          type: 'error',
          message: '请输入地址标签',
          duration: 3000,
          showClose: true,
        })
        return
      }
      if (this.form.selectAddress == '') {
        this.$message({
          type: 'error',
          message: '请选择提币地址',
          duration: 3000,
          showClose: true,
        })
        return
      }

      this.confirmBtnDisable = true
      let data = {
        currency: this.currency,
        addr:
          this.isInETH === 2
            ? this.form.selectAddress + '▲' + this.form.coinTips
            : this.form.selectAddress,
        amount: this.form.num,
        sms_code: this.form.mobileCode,
        m_security_pwd: this.form.password,
      }

      withdrawCreateI(data).then((response) => {
        this.confirmBtnDisable = false
        if (response.state === 1) {
          this.$message({
            type: 'success',
            message: this.$t('header.SubmitSuccess'),
            duration: 3000,
            showClose: true,
          })
          this.form.selectAddress = ''
          this.form.coinTips = ''
          this.form.num = ''
          this.form.mobileCode = ''
          this.form.imgCode = ''
          this.form.password = ''
          this.getAssetsLst()
          this.loadAssetsRecharge()
        } else if (response.state === -1) {
          this.getKaptcha()
        }
      })
    },
    loadAssetsRecharge() {
      const uid = localStorage.getItem('useruid')
      getAssetsWithDraw(1, uid).then((response) => {
        if (response.state === 1) {
          let rechargeLst = response.data.withdrawLst
          let list = []
          rechargeLst.forEach((ele) => {
            if (ele.currency === this.currency) {
              list.push(ele)
            }
          })
          list.forEach((ele) => {
            switch (ele.w_status) {
              case -1:
                ele.state = this.$t('user.Obligation')
                break
              case 0:
                ele.state = this.$t('user.Unconfirmed')
                break
              case 1:
                ele.state = this.$t('user.Confirmed')
                break
              case 2:
                ele.state = this.$t('user.Canceled')
                break
            }
          })
          this.clist = list
          // let totalRecharge = this.clist.length
          // this.rechareSum = Math.ceil(totalRecharge / 20)
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
    // prevClick () {
    //   this.pageRecharge--
    //   if (this.pageRecharge < 1) {
    //     this.pageRecharge++
    //     this.$message({
    //       type: 'error',
    //       message: this.$t('tradecenter.ThisIsPageOne'),
    //       duration: 3000,
    //       showClose: true
    //     })
    //   } else {
    //     // 加载上一页
    //     // this.getAssetsRecharge(this.selectTab)
    //   }
    // },
    // nextClick () {
    //   this.pageRecharge++
    //   if (this.pageRecharge > this.rechareSum) {
    //     this.pageRecharge--
    //     this.$message({
    //       type: 'error',
    //       message: this.$t('tradecenter.ThisIsTheLastPage'),
    //       duration: 3000,
    //       showClose: true
    //     })
    //   } else {
    //     // 加载下一页
    //     // this.getAssetsRecharge(this.selectTab)
    //   }
    // },
  },
}
</script>
<style lang="less" >
.my-autocomplete {
  li {
    line-height: normal;
    padding: 7px;

    .name {
      text-overflow: ellipsis;
      overflow: hidden;
    }
    .addr {
      font-size: 12px;
      color: #b4b4b4;
    }

    .highlighted .addr {
      color: #ddd;
    }
  }
}
.el-input__inner {
  &::placeholder {
    color: #4e5b85;
  }

  &::-webkit-input-placeholder {
    /* WebKit browsers 适配谷歌 */
    color: #4e5b85;
  }

  &:-moz-placeholder {
    /* Mozilla Firefox 4 to 18 适配火狐 */
    color: #4e5b85;
  }

  &::-moz-placeholder {
    /* Mozilla Firefox 19+ 适配火狐 */
    color: #4e5b85;
  }

  &:-ms-input-placeholder {
    /* Internet Explorer 10+  适配ie*/
    color: #4e5b85;
  }
}
</style>
<style scoped>
.iconfont_container {
  display: flex;
  align-items: center;
  height: 40px;
  width: 16px;
}
.coin_tips {
  /*width: 120px !important;*/
}
.flex_container {
  display: flex;
  align-items: center;
}
.flex_container p {
  color: #ba4f4f;
  margin-right: 5px;
}
.cloumn_flex_container {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  height: 40px;
  padding-left: 10px;
}
.LinkBot {
  display: flex;
  height: 52px;
  align-items: flex-start;
}
.chain_container label {
  height: 40px;
  display: flex;
  align-items: center;
}
</style>
<style>
.gm-inner .el-button {
  padding-top: 0px;
}
</style>
