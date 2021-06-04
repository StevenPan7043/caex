<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
          <!--          <asset-side-nav></asset-side-nav>-->
          <div class="content" id="view">
            <div class="inner-my-assets" id="innerMyAssets">
              <div class="header">{{$t('user.MyAssets')}}</div>
              <div class="estimate-all-assets">
                <div class="eaa-inner f-cb" v-show="btnShow">
                  <div class="btns f-fl" v-if="showOtc">
                    <button class="btn btn-topup">
                      <a>
                        <router-link to="/assetsRecord">{{$t('user.Financial')}}</router-link>
                      </a>
                    </button>
                    <button class="btn btn-getcoin">
                      <a>
                        <router-link to="/addressManage">{{$t('user.Address1')}}</router-link>
                      </a>
                    </button>
                  </div>
                  <div class="money f-fl">
                    <span class="moneyyg">{{$t('tradecenter.NetAssetConversion')}}</span>
                    <span class="moneyzc">{{(moneyzc/BTCPrice).toFixed(6)}} BTC</span>
                    <span class="moneybtc">≈ {{moneyzc.toFixed(2)}} CNY</span>
                  </div>
                </div>
              </div>
              <div class="currency-type-detail">
                <div class="title-wrap">
                  <!--                  <div class="tw-title f-fl">{{$t('user.CurrencyAssetDetails')}}</div>-->
                  <div class="tw-check-box">
                    <input type="checkbox" v-model="hideZeroInput" @click="hideZeroClick" />
                    {{$t('user.HideTheCurrencyOf0')}}
                  </div>
                  <div class="tw-search-box">
                    <i class="iconfont icon-search"></i>
                    <input type="text" class="uppercase" @input="searchChange" v-model="searchText" />
                    <!--                      :placeholder="$t('user.SearchTheCurrency')"-->
                  </div>
                </div>
                <div class="table-wrap">
                  <div class="currency-header sort-icon-wrap">
                    <span class="type">{{$t('user.Coin')}}</span>
                    <span class="use">{{$t('user.Available')}}</span>
                    <span class="freeze">{{$t('user.OnOrders')}}</span>
                    <span class="freeze">{{$t('user.Lock')}}</span>
                    <span class="freeze">{{$t('user.WaitRelease')}}</span>
                    <span class="estimate">{{$t('user.TotalCny')}}</span>
                    <span class="operate">{{$t('user.Action')}}</span>
                  </div>
                  <div class="currency-list">
                    <dl v-for="(item,index) in currencyLst" :key="index">
                      <dd>
                        <div class="cl-row">
                          <span>{{item.currency}}</span>
                          <span>{{item.position>=0?accountsLstObj[item.currency].balance:0}}</span>
                          <span>{{item.position>=0?accountsLstObj[item.currency].frozen_balance:0}}</span>
                          <span>{{item.position>=0?accountsLstObj[item.currency].lock_num:0}}</span>
                          <span>{{item.position>=0?accountsLstObj[item.currency].wait_release_num:0}}</span>
                          <span>{{item.position>=0?accountsLstObj[item.currency].zc_balance:0}}</span>
                          <span>
                            <em
                              v-if="item.can_recharge===0"
                              style="color:#999"
                            >{{$t('user.Deposit')}}</em>
                            <em v-else @click="toCharge(item)">{{$t('user.Deposit')}}</em>
                            <em
                              v-if="item.can_withdraw===0"
                              style="color:#999"
                            >{{$t('user.Withdraw')}}</em>
                            <em v-else @click="toWithdraw(item)">{{$t('user.Withdraw')}}</em>
                            <em v-if="item.currency!=='USDT'" style="color:#999">资金划转</em>
                            <em v-else @click="openDialog">资金划转</em>
                          </span>
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

    <!-- <el-dialog title="温馨提示" :visible.sync="dialogVisible" width="40%">
      <ul class="dialog_ul">
        <li>1、您所充值的{{coinType}}币种，充值后将直接进入锁仓账户；</li>
        <li>2、当您在{{coinType}}币种对应的交易对购买一定量的{{coinType}}的同时，锁仓账户也将<span v-if="lockTime!=0">在{{lockTime/60}}小时后</span>释放{{lockReleasePercent*100}}%的{{coinType}}；</li>
        <li>3、活动解释权归Hotdog交易所所有。</li>
      </ul>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleConfirm">确 定</el-button>
      </span>
    </el-dialog>-->

    <DialogForm
      :dialogFormVisible="dialogFormVisible"
      :bbMoney="bbMoney"
      :fbMoney="fbMoney"
      :qcMoney="qcMoney"
      :zcMoney="zcMoney"
      @close-dialog="closeDialog"
      @reload-page="reloadPage"
    />
  </div>
</template>
<script>
import MyHeader from "@/components/Header";
import MyFooter from "@/components/Footer";
import AssetSideNav from "@/components/AssetSideNav";
import DialogForm from "./contractAssets/components/DialogForm";

import { getAssets } from "../api/contract";
import {
  getAccountsI,
  getAssetsLst,
  getSummary,
  getLockTime,
  getUsdtPrice
} from "@/api/myAssets";
import { getSSOToken } from "@/api/login.js";
import { jumpUrl } from "../../public/config.js";
export default {
  components: {
    MyHeader,
    MyFooter,
    AssetSideNav,
    DialogForm
  },
  data() {
    return {
      btnShow: false,
      data: "",
      currencyLst: "",
      accountsLstObj: "",
      accountsLstArr: [],
      currencyLstArr: [],
      currencyLstObj: "",
      hideZeroInput: false,
      currencyLstAll: "",
      searchText: "",
      moneyzc: 0,
      moneybtc: 0,
      accountAuth: 0,
      // dialogVisible: false,
      coinType: "",
      item: null,
      lockTime: 0,
      lockReleasePercent: 0.0,
      showOtc: true,
      UsdtPrice: 0,
      BTCPrice: 0,
      dialogFormVisible: false,
      bbMoney: 0,
      fbMoney: 0,
      qcMoney: 0,
      zcMoney: 0
    };
  },
  created() {
    this.showOtc = JSON.parse(localStorage.getItem("showOtc"));
    getAccountsI().then(res => {
      if (res.state === 1) {
        this.btnShow = true;
      } else if (res.state === -1) {
        if (res.msg === "LANG_NO_LOGIN") {
          this.$message({
            type: "error",
            message: this.$t("header.PlaseLogin"),
            duration: 3000,
            showClose: true
          });
          setTimeout(() => {
            this.userLogout();
          }, 2000);
        }
      }
    });
    this.getList();
    this.getSummary();
  },
  methods: {
    // handleConfirm() {
    //   // this.dialogVisible = false;
    //   this.$router.push({
    //     name: "Recharge",
    //     params: { currency: this.item.currency }
    //   });
    // },
    openDialog() {
      this.dialogFormVisible = true;
      this.getBBFbMoney();
      this.getQCZCMoney();
    },

    closeDialog() {
      this.dialogFormVisible = false;
    },
    reloadPage() {
      this.getList();
      this.getSummary();
    },
    // 获取币币和法币的资金
    async getBBFbMoney() {
      let res = await getAssets();
      res.data.bb.forEach(item => {
        if (item.name === "USDT") {
          this.bbMoney = item.enable;
        }
      });
      res.data.fb.forEach(item => {
        if (item.name == "USDT") {
          this.fbMoney = item.enable;
        }
      });
    },
    // 获取全仓和逐仓的资金
    getQCZCMoney() {
      this.$ajax("/account/getMyInfos", {
        token: this.$userID()
      }).then(res => {
        if (res.data.status) {
          if (this.$route.name === "Login") {
            this.$router.push("/");
          }
          this.qcMoney = res.data.data.usdt;
          this.zcMoney = res.data.data.zcUsdt;
        } else {
          this.$router.push("/login");
        }
      });
    },
    toFabi() {
      getSSOToken().then(({ data, state, msg }) => {
        if (state === 1) {
          window.location.href = `${jumpUrl}home?&test=test&api_key=${data.api_key}&sign=${data.sign}&timestamp=${data.timestamp}`;
        } else {
          window.location.href = jumpUrl;
        }
      });
    },
    getList() {
      getAssetsLst().then(res => {
        if (res.state === 1) {
          let data = res.data;
          this.data = data;
          this.accountAuth = res.data.accountAuth;
          let currencyLst = data.currencyLst;
          let accountsLst = data.accountsLst;
          let accountsLstObj = {};
          let accountsLstArr = [];
          let currencyLstObj = {};
          let currencyLstArr = [];
          for (let i in accountsLst) {
            accountsLstObj[accountsLst[i].currency] = accountsLst[i];
            if (
              accountsLst[i].total_balance > 0 ||
              accountsLst[i].lock_num > 0
            ) {
              accountsLstArr.push(accountsLst[i].currency);
            }
          }
          for (let i in currencyLst) {
            currencyLstObj[currencyLst[i].currency] = currencyLst[i];
            currencyLstArr.push(currencyLst[i].currency);
          }
          currencyLst.forEach(ele => {
            ele.position = accountsLstArr.indexOf(ele.currency);
          });
          let newList = [];
          currencyLst.forEach(ele => {
            let exit =
              ele.currency_name.indexOf(this.searchText.toUpperCase()) !== -1;
            if (exit) {
              newList.push(ele);
            }
          });
          let newList2 = [];
          if (this.hideZeroInput === true) {
            newList.forEach(ele => {
              if (ele.position >= 0) {
                newList2.push(ele);
              }
            });
            this.currencyLst = newList2;
          } else {
            this.currencyLst = newList;
          }
          currencyLst.sort(this.sortNumber);
          this.currencyLstAll = currencyLst;
          this.accountsLstArr = accountsLstArr;
          this.accountsLstObj = accountsLstObj;
          this.currencyLstObj = currencyLstObj;
          this.currencyLstArr = currencyLstArr;
        }
      });
    },
    sortNumber(a, b) {
      return a.c_order - b.c_order;
    },
    userLogout() {
      this.loginState = false;
      localStorage.setItem("loginState", this.loginState);
      this.$router.push("/login");
    },
    async toCharge(item) {
      this.$router.push({
        name: "Recharge",
        params: { currency: item.currency }
      });
      // if (item.is_lock === 1) {
      //   this.coinType = item.currency;
      //   getLockTime().then(res => {
      //     res.datas.forEach(ele => {
      //       if (ele.currency === this.coinType) {
      //         this.lockTime = ele.lock_release_time;
      //         this.lockReleasePercent = ele.lock_release_percent
      //         this.dialogVisible = true;
      //         this.item = item;
      //       }
      //     });
      //   });
      // } else {
      //   this.$router.push({
      //     name: "Recharge",
      //     params: { currency: item.currency }
      //   });
      // }
      // if(this.accountAuth == 1){
      //   this.$router.push({ name: 'Recharge', params: { currency: item.currency } });
      // }else{
      //   this.$message({
      //     type: 'error',
      //     message: '请先完成实名认证',
      //     duration: 3000,
      //     showClose: true
      //   })
      // }
    },
    getLockTime() {},

    toWithdraw(item) {
      if (this.accountAuth == 1) {
        this.$router.push({
          name: "Withdraw",
          params: { currency: item.currency }
        });
      } else {
        this.$message({
          type: "error",
          message: "请先完成实名认证",
          duration: 3000,
          showClose: true
        });
      }
    },
    hideZeroClick() {
      this.getList();
    },
    searchChange() {
      this.getList();
    },
    async getUsdt() {
      const res = await getUsdtPrice();
      [this.UsdtPrice] = res.USDT_CNY;
      const BTC_USDT = res.BTC_USDT[0];
      this.BTCPrice = this.UsdtPrice * BTC_USDT;
    },
    getSummary() {
      this.getUsdt();
      getSummary("btc", "1min", "0", "1", "market").then(response => {
        if (response.state == 1) {
          let moneyzc = 0;
          let moneybtc = 0;
          for (let i in response.data.currencyLst) {
            if (
              response.data.currencyLst[i].zc_balance &&
              response.data.currencyLst[i].zc_balance !== null
            ) {
              moneyzc += response.data.currencyLst[i].zc_balance;
            }
          }
          for (let i in response.data.tickersMap) {
            if (response.data.tickersMap[i].symbol == "btczc") {
              moneybtc = response.data.tickersMap[i].close;
              break;
            }
          }
          this.moneyzc = moneyzc;
          this.moneybtc = moneybtc;
        }
      });
    }
  }
};
</script>
<style scoped>
.moneyzc {
  color: #25425a !important;
  /*font-size: 24px !important;*/
  /*font-weight: bold;*/
  /*margin-left: 20px;*/
}
.moneyyg {
  color: #25425a !important;
  font-size: 16px !important;
}
.moneybtc {
  color: #9199a4 !important;
  font-size: 12px !important;
}
.money {
  /*height: 120px !important;*/
  float: left !important;
}
.btns {
  float: right !important;
}
.dialog_ul li {
  margin: 10px 0;
}
</style>
