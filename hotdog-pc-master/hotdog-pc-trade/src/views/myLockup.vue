<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
          <asset-side-nav></asset-side-nav>
          <div class="content" id="view">
            <div class="inner-my-lockup" id="innerMyLockup">
              <div class="header">{{$t('user.LockPosition')}}</div>
              <div class="estimate-allassets f-cb" @click="benn"></div>
              <div class="lockcoin-detail">
                <div class="detail-header f-cb">
                  <div class="f-fl">{{$t('user.CurrencyDetailsOfLockPosition')}}</div>
                </div>
                <div class="detail-bar" id="detailBar">
                  <div class="bar-head">
                    <span>{{$t('user.Coin')}}</span>
                    <span>{{$t('user.AvailableBalance')}}</span>
                    <span>{{$t('user.LockUpFrozen')}}</span>
                    <span>{{$t('user.TotalS')}}</span>
                    <span>{{$t('user.Action')}}</span>
                  </div>
                  <div class="bar-number">
                    <table class="option-list" cellspacing="0">
                      <tr>
                        <td>EBANK</td>
                        <td>{{moneyzc}}</td>
                        <td>{{frozen_amount}}</td>
                        <td>{{zc_balance}}</td>
                        <td>
                          <span>
                            <a @click="Join">{{$t('user.JoinTheLock')}}</a>
                          </span>
                        </td>
                      </tr>
                    </table>
                  </div>
                </div>
              </div>
              <div class="hold-lock" id="holdLock">
                <div class="hold-header">
                  <span>{{$t('user.HoldLockUp')}}</span>
                </div>
                <table class="hold-list" cellspacing="0">
                  <thead>
                    <tr>
                      <th>{{$t('user.LockUpTime')}}</th>
                      <th>{{$t('user.LockUpCurrency')}}</th>
                      <th>{{$t('user.LockUpDeadline')}}</th>
                      <th>{{$t('user.LockUpTheNumber')}}</th>
                      <th>{{$t('user.RateOfReturn')}}</th>
                      <th>{{$t('user.RemainingTime')}}</th>
                      <th>{{$t('user.Status')}}</th>
                    </tr>
                  </thead>
                  <tbody v-show="optionShow">
                    <tr v-for="(item, index) in list" :key="index">
                      <td>
                        <span>{{item.locktime|locktime(1)}}</span>
                        <span>{{item.locktime|locktime(2)}}</span>
                      </td>
                      <td>{{item.currency}}</td>
                      <td>{{item.l_type}}{{$t('user.Months')}}</td>
                      <td>{{item.amount}}</td>
                      <td>{{item.l_type|amount}}</td>
                      <td>{{item.undata|locktime(3)}}</td>
                      <td>{{item.l_status|status()}}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div class="pages">
                <div class="inner-pages f-cb">
                  <div class="flip f-fl">
                    <span class="prev" @click="prevClick2">{{$t('user.PageUp')}}</span>
                    <span class="next" @click="nextClick2">{{$t('user.PageDown')}}</span>
                  </div>
                  <div class="total f-fl">
                    {{$t('user.First')}}
                    <span class="first">{{page2}}</span>
                    {{$t('user.PpTotal')}}
                    <span class="sum">{{total}}</span>
                    {{$t('user.Page')}}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="lock-dialog" id="lockDialog" v-show="lockDialogShow">
        <div class="lock-dialog-inner">
          <div class="wrap">
            <div class="title-wrap">
              <div class="title">{{$t('user.JoinTheLock')}}</div>
              <span @click="closeAsDialog">x</span>
            </div>
            <div class="input-wrap">
              <div class="input-wrap-bottom-title">
                <span>{{$t('user.AvailableBalance')}}</span>
              </div>
              <div class="input-wrap-bottom">
                <input type="text" id="transferNum" v-model="moneyzc" disabled>
                <span class="all-in" id="allIn">EBANK</span>
              </div>
            </div>
            <div class="input-wrap">
              <div class="input-wrap-bottom-title">
                <span>{{$t('user.LockUpDeadline')}}</span>
              </div>
              <select v-model="form.val" @change="getCouponSelected">
                <option value="0">{{$t('user.ChoosingLockingPeriod')}}</option>
                <option value="1">1个月</option>
                <option value="3">3个月</option>
                <option value="6">6个月</option>
              </select>
            </div>
            <div class="input-wrap">
              <div class="input-wrap-bottom-title">
                <span>{{$t('user.LockUpTheNumber')}}</span>
              </div>
              <div class="input-wrap-bottom">
                <input
                  type="text"
                  id="transferNum"
                  :placeholder="$t('user.PleaseEnterTheTransferAmount')"
                  v-model="form.num"
                >
                <span class="all-in" id="allIn" @click="huazhuanAll">{{$t('user.All')}}</span>
              </div>
            </div>
            <div class="input-wrap">
              <div class="input-wrap-bottom-title">
                <span>{{$t('user.MoneyPassword')}}</span>
              </div>
              <div class="input-wrap-bottom">
                <input
                  type="password"
                  id="transferNum"
                  :placeholder="$t('user.PleaseEnterFundPassword')"
                  v-model="secPwd"
                >
              </div>
            </div>
            <div class="button-wrap f-cb">
              <button class="transfer f-fr" @click="huazhuanSubmit">{{$t('resetpassword.Affirm')}}</button>
              <p class="transfer f-fr" @click="closeAsDialog">{{$t('user.Cancel')}}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import MyHeader from "@/components/Header";
import MyFooter from "@/components/Footer";
import AssetSideNav from "@/components/AssetSideNav";
import { lockTransfer, getrecord, getamount } from "@/api/myLockup.js";
import { getSummary } from "@/api/myAssets.js";
export default {
  components: {
    MyHeader,
    MyFooter,
    AssetSideNav
  },
  filters: {
    locktime: function(value, sta) {
      if (!value) return "";
      var date = new Date(value * 1000); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
      var Y = date.getFullYear() + "-";
      var M =
        (date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1) + "-";
      var D =
        date.getDate() < 10 ? "0" + date.getDate() + " " : date.getDate() + " ";
      var h =
        date.getHours() < 10
          ? "0" + date.getHours() + ":"
          : date.getHours() + ":";
      var m =
        date.getMinutes() < 10
          ? "0" + date.getMinutes() + ":"
          : date.getMinutes() + ":";
      var s =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      if (sta == 3) {
        const end = value;
        // 当前时间戳
        const now = Date.parse(new Date()) / 1000;
        // 相差的毫秒数
        const second_time = end - now;
        var time = "0 分";
        if (parseInt(second_time) > 60) {
          var min = parseInt(second_time / 60);
          time = min + "分";

          if (min > 60) {
            min = parseInt(second_time / 60) % 60;
            var hour = parseInt(parseInt(second_time / 60) / 60);
            time = hour + "时" + min + "分";

            if (hour > 24) {
              hour = parseInt(parseInt(second_time / 60) / 60) % 24;
              var day = parseInt(
                parseInt(parseInt(second_time / 60) / 60) / 24
              );
              time = day + "天" + hour + "时" + min + "分";
            }
          }
        }
      }
        // 一秒后递归
        switch (sta) {
          case 1:
            return Y + M + D;
          case 2:
            return h + m + s;
          case 3:
            return time;
          default:
            return Y + M + D;
        }
    },
    status(value) {
      if (
        window.localStorage.getItem("User-Lang") == "undefined" ||
        window.localStorage.getItem("User-Lang") == null
      ) {
        window.localStorage.setItem("User-Lang", "zh");
      }
      if (value == 1) {
        return window.localStorage.getItem("User-Lang") == "zh"
          ? "已释放"
          : "HasBeenReleased";
      } else {
        return window.localStorage.getItem("User-Lang") == "zh"
          ? "未释放"
          : "Undelivered";
      }
    },
    amount(v) {
      switch (v) {
        case 1:
          return "10%";
        case 3:
          return "45%";
        case 6:
          return "108%";
        default:
          return "0";
      }
    }
  },
  data() {
    return {
      rReleaseNum: "",
      rUnnum: "",
      rAmount: "",
      page2: 1,
      curSum2: 5,
      day: 0,
      hr: 0,
      min: 0,
      sec: 0,
      total: 1,
      lockDialogShow: false,
      moneyzc: 0,
      frozen_balance: 0,
      total_balance: 0,
      frozen_amount: 0,
      zc_balance: 0,
      secPwd: "",
      a: "00",
      form: {
        num: "",
        val: 0
      },
      list: [],
      optionShow: false
    };
  },
  created() {
    this.loadDetailOrder(this.page2, this.curSum2);
    this.getSummaryss();
  },
  mounted() {
  },
  methods: {
    // countdown () {
    //   const end = Date.parse(new Date('2017-12-01'))
    //   const now = Date.parse(new Date())
    //   const msec = end - now
    //   let day = parseInt(msec / 1000 / 60 / 60 / 24)
    //   let hr = parseInt(msec / 1000 / 60 / 60 % 24)
    //   let min = parseInt(msec / 1000 / 60 % 60)
    //   let sec = parseInt(msec / 1000 % 60)
    //   this.day = day
    //   this.hr = hr > 9 ? hr : '0' + hr
    //   this.min = min > 9 ? min : '0' + min
    //   this.sec = sec > 9 ? sec : '0' + sec
    //   const that = this
    //   setTimeout(function () {
    //     that.countdown()
    //   }, 1000)
    // },
    getSummaryss() {
      //获取EBANK信息
      getSummary("EBANK", "1min", "0", "1", "market").then(response => {
        if (response.state == 1) {
          let moneyzc = 0;
          let frozen_balance = 0;
          let total_balance = 0;
          let zc_balance = 0;
          for (let i in response.data.currencyLst) {
            if (response.data.currencyLst[i].currency == "EBANK") {
              moneyzc = response.data.currencyLst[i].balance;
              frozen_balance = response.data.currencyLst[i].frozen_balance;
              total_balance = response.data.currencyLst[i].total_balance;
              zc_balance = response.data.currencyLst[i].zc_balance;
              break;
            }
          }
          this.moneyzc = moneyzc;
          this.zc_balance = zc_balance;
          this.frozen_balance = frozen_balance;
          this.total_balance = total_balance;
        }
      });
      getamount("EBANK").then(res => {
        if (res.state == 1) {
          if (res.data) {
            this.frozen_amount = res.data.frozen_amount;
          }
        }
      });
    },
    getCouponSelected() {
      this.form;
    },
    loadDetailOrder(p, s) {
      //获取锁仓记录
      getrecord(p, s).then(res => {
        // debugger
        if (res.state === 1) {
          this.total = Math.ceil(res.total / this.curSum2);
          if (res.datas) {
            this.list = res.datas;
            this.optionShow = true;
            setInterval(() => {
              for(var o in this.list){
                this.list[o].undata = this.list[o].undata-1;
              }
            }, 1000);
          } else {
            this.$message({
              type: "error",
              message: this.$t("tradecenter.ThisIsTheLastPage"),
              duration: 3000,
              showClose: true
            });
          }
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
    },
    Join() {
      this.lockDialogShow = true;
    },
    benn() {
      this.$router.push("/mylockdest");
    },
    closeAsDialog() {
      this.lockDialogShow = false;
      this.form.num = "";
    },
    huazhuanAll() {
      this.form.num = this.moneyzc;
    },
    huazhuanSubmit() {
      let params = {
        currency: "ebank",
        l_type: this.form.val,
        amount: this.form.num,
        secPwd: this.secPwd
      };
      lockTransfer(params).then(res => {
        if (res.state === 1) {
          this.$message({
            type: "success",
            message: this.$t("user.TransferSuccess"),
            duration: 3000,
            showClose: true
          });
          this.getSummaryss();
          this.loadDetailOrder(this.page2, this.curSum2);
          this.closeAsDialog();
        }
      });
    },
    userLogout() {
      this.loginState = false;
      localStorage.setItem("loginState", this.loginState);
      this.$router.push("/login");
    },
    toRechargeLock() {
      this.lockDialogShow = false;
      this.form.num = "";
    },
    prevClick2() {
      this.page2--;
      if (this.page2 < 1) {
        this.page2++;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsPageOne"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.loadDetailOrder(this.page2, this.curSum2);
      }
    },
    nextClick2() {
      this.page2++;
      if (this.page2 > this.total) {
        this.page2--;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsTheLastPage"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.loadDetailOrder(this.page2, this.curSum2);
      }
    }
  }
};
</script>
<style scoped>
.close_icon {
  width: 30px;
  height: 30px;
}
</style>
