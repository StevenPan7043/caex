<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
          <!--          <asset-side-nav></asset-side-nav>-->

          <div class="content" id="view">
            <div class="inner-trade-bill" id="innerTradeBill">
              <div class="header">
                <span class>{{$t('user.Myorder')}}</span>
              </div>
              <div class="sort f-cb">
                <div class="tab-entrust f-fl">
                  <span
                    :class="status==='0'?'cur':''"
                    @click="tabClick('0')"
                  >{{$t('user.OpenOrders')}}</span>
                  <span
                    :class="status==='1'?'cur':''"
                    @click="tabClick('1')"
                  >{{$t('user.OrderHistory')}}</span>
                  <span
                    :class="status==='2'?'cur':''"
                    @click="tabClick('2')"
                  >{{$t('user.TransactionHistory')}}</span>
                </div>
                <div class="market input-wrap f-fr">
                  <select id="marketSelect" v-model="marketSelect">
                    <option value="All">{{$t('user.AllTheMarket')}}</option>
                    <option value="ZC">{{$t('user.ZCMarket')}}</option>
                    <option value="ETH">{{$t('user.ETHMarket')}}</option>
                    <option value="USDT">{{$t('user.USDTMarket')}}</option>
                  </select>
                </div>
                <div class="type input-wrap f-fr">
                  <select id="pairsSelect" v-model="pairsSelect">
                    <option value="ETHZC">{{$t('user.PleaseSelectTheTradingPair')}}</option>
                    <option
                      :value="item.symbol"
                      v-for="(item,index) in curSymbols"
                      :key="index"
                    >{{item.base_currency}}/{{item.quote_currency}}</option>
                  </select>
                </div>
              </div>
              <div class="list">
                <!-- 当前委托 -->
                <div class="current-entrust" :class="status==='0'?'cur':''">
                  <div class="list-header">
                    <span class="time">{{$t('tradecenter.Date')}}</span>
                    <span class="sn">{{$t('tradecenter.orderID')}}</span>
                    <span class="pairs">{{$t('home.TradingOn')}}</span>
                    <span class="category">{{$t('tradecenter.Type')}}</span>
                    <span class="price">{{$t('tradecenter.EntrustedPrice')}}</span>
                    <span class="number">{{$t('tradecenter.Amount')}}</span>
                    <span class="deal">{{$t('tradecenter.DelegateTotal')}}</span>
                    <span class="poundage">{{$t('tradecenter.Executed')}}</span>
                    <span class="state">{{$t('tradecenter.Unexecuted')}}</span>
                    <span class="option">{{$t('tradecenter.Action')}}</span>
                  </div>
                  <div class="list-main">
                    <dl v-for="(item,index) in orders" :key="index">
                      <dd>
                        <div class="lm-row">
                          <span>{{item.create_time}}</span>
                          <el-tooltip
                            class="item"
                            effect="dark"
                            :content="item.o_no"
                            placement="top"
                          >
                            <span>{{item.o_no}}</span>
                          </el-tooltip>
                          
                          <span>{{item.base_currency+'/'+item.quote_currency}}</span>
                          <span
                            class="buy rise"
                            v-if="item.o_type==='buy'"
                          >{{$t('tradecenter.Buys')}}</span>
                          <span class="buy fall" v-else>{{$t('tradecenter.Sells')}}</span>
                          <span>{{item.price}}</span>
                          <span>{{item.volume}}</span>
                          <span>{{(item.price*item.volume).toFixed(4)}}</span>
                          <span>{{item.done_volume}}</span>
                          <span>{{(item.volume-item.done_volume).toFixed(4)}}</span>
                          <span @click="cancelBtnClick(item)">{{$t('tradecenter.Revocation')}}</span>
                        </div>
                      </dd>
                    </dl>
                  </div>
                  <div class="pages">
                    <div class="inner-pages f-cb">
                      <div class="flip f-fl">
                        <span class="prev" @click="prevClick(0)">{{$t('user.PageUp')}}</span>
                        <span class="next" @click="nextClick(0)">{{$t('user.PageDown')}}</span>
                      </div>
                      <div class="total f-fl">
                        {{$t('user.First')}}
                        <span class="first">{{page}}</span>
                        {{$t('user.PpTotal')}}
                        <span class="sum">{{curSum}}</span>
                        {{$t('user.Page')}}
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 历史委托 -->
                <div class="history-entrust" :class="status==='1'?'cur':''">
                  <div class="list-header">
                    <span class="time">{{$t('tradecenter.Date')}}</span>
                    <span class="sn">{{$t('tradecenter.orderID')}}</span>
                    <span class="pairs">{{$t('tradecenter.TradingOn')}}</span>
                    <span class="category">{{$t('tradecenter.Type')}}</span>
                    <span class="price">{{$t('tradecenter.EntrustedPrice')}}</span>
                    <span class="number">{{$t('tradecenter.Amount')}}</span>
                    <span class="poundage">{{$t('tradecenter.Executed')}}</span>
                    <span class="state">{{$t('tradecenter.AveragePrice')}}</span>
                    <span class="option">{{$t('tradecenter.Status')}}</span>
                  </div>
                  <div class="list-main">
                    <dl v-for="(item,index) in historyOrders" :key="index">
                      <dd>
                        <div class="lm-row">
                          <span>{{item.create_time}}</span>
                           <el-tooltip
                            class="item"
                            effect="dark"
                            :content="item.o_no"
                            placement="top"
                          >
                            <span>{{item.o_no}}</span>
                          </el-tooltip>
                          <span>{{item.base_currency+'/'+item.quote_currency}}</span>
                          <span
                            class="buy rise"
                            v-if="item.o_type==='buy'"
                          >{{$t('tradecenter.Buys')}}</span>
                          <span class="buy fall" v-else>{{$t('tradecenter.Sells')}}</span>
                          <span>{{item.price}}</span>
                          <span>{{item.volume}}</span>
                          <span>{{item.done_volume}}</span>
                          <span>{{item.done_avg_price}}</span>
                          <span>{{item.status}}</span>
                        </div>
                      </dd>
                    </dl>
                  </div>
                  <div class="pages">
                    <div class="inner-pages f-cb">
                      <div class="flip f-fl">
                        <span class="prev" @click="prevClick1">{{$t('user.PageUp')}}</span>
                        <span class="next" @click="nextClick1">{{$t('user.PageDown')}}</span>
                      </div>
                      <div class="total f-fl">
                        {{$t('user.First')}}
                        <span class="first">{{page1}}</span>
                        {{$t('user.PpTotal')}}
                        <span class="sum">{{curSum1}}</span>
                        {{$t('user.Page')}}
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 交易明细 -->
                <div class="trade-entrust" :class="status==='2'?'cur':''">
                  <div class="list-header">
                    <span class="time">{{$t('tradecenter.Date')}}</span>
                    <span class="sn">{{$t('tradecenter.orderID')}}</span>
                    <span class="pairs">{{$t('tradecenter.TradingOn')}}</span>
                    <span class="category">{{$t('tradecenter.Type')}}</span>
                    <span class="price">{{$t('tradecenter.AveragePrice')}}</span>
                    <span class="number">{{$t('tradecenter.Amount')}}</span>
                    <span class="poundage">{{$t('user.Fee')}}</span>
                    <span class="state">{{$t('tradecenter.TradingVolumeS')}}</span>
                    <span class="option">{{$t('tradecenter.Status')}}</span>
                  </div>
                  <div class="list-main">
                    <dl v-for="(item,index) in detailOrders" :key="index">
                      <dd>
                        <div class="lm-row">
                       
                          <span>{{item.done_time}}</span>
                                       <el-tooltip
                            class="item"
                            effect="dark"
                            :content="item.o_no"
                            placement="top"
                          >
                            <span>{{item.o_no}}</span>
                          </el-tooltip>
                          <span>{{item.base_currency+'/'+item.quote_currency}}</span>
                          <span
                            class="buy rise"
                            v-if="item.t_type==='buy'"
                          >{{$t('tradecenter.Buys')}}</span>
                          <span class="buy fall" v-else>{{$t('tradecenter.Sells')}}</span>
                          <span>{{item.price}}</span>
                          <span>{{item.volume}}</span>
                          <span>{{item.fee}}</span>
                          <span>{{item.amount}}</span>
                          <span>{{$t('tradecenter.Transaction')}}</span>
                        </div>
                      </dd>
                    </dl>
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
                        <span class="sum">{{curSum2}}</span>
                        {{$t('user.Page')}}
                      </div>
                    </div>
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
import MyHeader from "@/components/Header";
import MyFooter from "@/components/Footer";
import AssetSideNav from "@/components/AssetSideNav";
import { getSymbol, getOrdersI, orderCancel, getTrades } from "@/api/tradeBill";
export default {
  components: {
    MyHeader,
    MyFooter,
    AssetSideNav
  },
  data() {
    return {
      marketSelect: "All",
      pairsSelect: "ETHUSDT",
      curSymbols: "",
      page: "1",
      page1: "1",
      page2: "1",
      curSum: "",
      curSum1: "",
      curSum2: "",
      total: "",
      total1: "",
      total2: "",
      status: "0",
      orders: [],
      historyOrders: [],
      detailOrders: []
    };
  },
  mounted() {
    this.loadTradepairs();
    this.loadCurrentOrder();
  },
  watch: {
    marketSelect() {
      this.loadTradepairs();
    },
    pairsSelect() {
      switch (this.status) {
        case "0":
          this.loadCurrentOrder();
          break;
        case "1":
          this.loadHistoryOrder();
          break;
        case "2":
          this.loadDetailOrder();
          break;
        default:
          break;
      }
    }
  },
  methods: {
    loadTradepairs() {
      getSymbol().then(res => {
        if (res.state === 1) {
          let symbols = res.symbols;
          let curSymbols = [];
          if (this.marketSelect === "All") {
            this.curSymbols = symbols;
          } else {
            symbols.forEach(ele => {
              if (ele.quote_currency == this.marketSelect) {
                curSymbols.push(ele);
              }
            });
            this.curSymbols = curSymbols;
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
    userLogout() {
      this.loginState = false;
      localStorage.setItem("loginState", this.loginState);
      this.$router.push("/login");
    },
    loadCurrentOrder() {
      getOrdersI(this.pairsSelect, this.page, this.status).then(res => {
        if (res.state === 1) {
          console.log("sss");
          console.log(res);
          console.log("sss");
          this.orders = res.orders;
          this.total = res.total;
          this.curSum = Math.ceil(this.total / 20);
        } else if (res.state === -1) {
          if (res.msg === "LANG_NO_LOGIN") {
            setTimeout(() => {
              this.$message({
                type: "error",
                message: this.$t("header.PlaseLogin"),
                duration: 3000,
                showClose: true
              });
              this.userLogout();
            }, 2000);
          }
        }
      });
    },
    loadHistoryOrder() {
      getOrdersI(this.pairsSelect, this.page1, this.status).then(res => {
        if (res.state === 1) {
          let historyOrders = res.orders;
          historyOrders.forEach(ele => {
            switch (ele.o_status) {
              case "done":
                ele.status = this.$t("tradecenter.Transaction");
                break;
              case "canceled":
                ele.status = this.$t("tradecenter.Cancellations");
                break;
              case "partial-canceled":
                ele.status = this.$t("tradecenter.PartOfIts");
                break;
            }
          });
          this.historyOrders = historyOrders;
          this.total1 = res.total;
          this.curSum1 = Math.ceil(this.total1 / 20);
          // 遍历 当前委托 数据到页面
        } else if (res.state === -1) {
          if (res.msg === "LANG_NO_LOGIN") {
            setTimeout(() => {
              this.$message({
                type: "error",
                message: this.$t("header.PlaseLogin"),
                duration: 3000,
                showClose: true
              });
              this.userLogout();
            }, 2000);
          }
        }
      });
    },
    loadDetailOrder() {
      getTrades(this.pairsSelect, this.page2).then(res => {
        if (res.state === 1) {
          this.detailOrders = res.trades;
          this.total2 = res.total;
          this.curSum2 = Math.ceil(this.total2 / 20);
          // 遍历 当前委托 数据到页面
        } else if (res.state === -1) {
          if (res.msg === "LANG_NO_LOGIN") {
            setTimeout(() => {
              this.$message({
                type: "error",
                message: this.$t("header.PlaseLogin"),
                duration: 3000,
                showClose: true
              });
              this.userLogout();
            }, 2000);
          }
        }
      });
      // getOrdersI(this.pairsSelect, this.page2, this.status).then(res => {
      //   if (res.state === 1) {
      //     this.detailOrders = res.orders
      //     this.total2 = res.total
      //     this.curSum2 = Math.ceil(this.total2 / 20)
      //     // 遍历 当前委托 数据到页面
      //   } else if (res.state === -1) {
      //     if (res.msg === 'LANG_NO_LOGIN') {
      //       setTimeout( () =>{
      //         this.$message({
      //           type: 'error',
      //           message: '请先登录',
      //           duration: 3000,
      //           showClose: true
      //         })
      //         this.userLogout()
      //       }, 2000)
      //     }
      //   }
      // })
    },
    cancelBtnClick(item) {
      var id = item.id;
      var no = item.o_no;
      var symbol = item.symbol;
      orderCancel(id, no, symbol).then(response => {
        if (response.state === 1) {
          this.$message({
            type: "success",
            message: this.$t("tradecenter.RevocationOfSuccess"),
            duration: 3000,
            showClose: true
          });
          this.loadCurrentOrder();
        }
      });
    },
    prevClick() {
      this.page--;
      if (this.page < 1) {
        this.page++;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsPageOne"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.loadCurrentOrder();
      }
    },
    prevClick1() {
      this.page1--;
      if (this.page1 < 1) {
        this.page1++;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsPageOne"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.loadHistoryOrder();
      }
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
        this.loadDetailOrder();
      }
    },
    nextClick() {
      this.page++;
      if (this.page > this.curSum) {
        this.page--;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsTheLastPage"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.loadCurrentOrder();
      }
    },
    nextClick1() {
      this.page1++;
      if (this.page1 > this.curSum1) {
        this.page1--;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsTheLastPage"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.loadHistoryOrder();
      }
    },
    nextClick2() {
      this.page2++;
      if (this.page2 > this.curSum2) {
        this.page2--;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsTheLastPage"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.loadDetailOrder();
      }
    },
    tabClick(index) {
      this.status = index;
      // this.marketSelect = 'All'
      // this.pairsSelect = 'ETHZC'
      switch (index) {
        case "0":
          this.loadCurrentOrder();
          break;
        case "1":
          this.loadHistoryOrder();
          break;
        case "2":
          this.loadDetailOrder();
          break;
        default:
          break;
      }
    }
  }
};
</script>
<style scoped>
.el-tooltip__popper {
  left: 100px;
  position: absolute;
}
</style>