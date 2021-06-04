<template>
  <div>
    <my-header :selectHeaderTab="selectHeaderTab" @lang-change="langChange"></my-header>
    <div id="MainView">
      <div class="trade-panel">
        <div class="tp-row-1">
          <div class="sidebar">
            <ticker-summary
                    :allTicker1="allTicker3"
                    :allTicker2="allTicker2"
                    :allTicker3="allTicker1"
                    :userFavoriteArr="userFavoriteArr"
                    @to-user-ticker="toUserTicker"
                    @star-click="starClick"
                    :ticker="ticker"
                    :trade="trade"
                    :selectTicker="hashsymbol"
            ></ticker-summary>
          </div>
          <div class="had-row-2" v-if="!compTab">
            <div
                    class="pc-row-2-c2"
                    v-loading="loadingdepth"
                    element-loading-background="rgba(138, 134, 134, 0.2)"
            >
              <div class="head">
                <div class="title f-fl">{{$t('home.Handicap')}}</div>
                <up-down @appenddata="appenddata"/>
              </div>
              <last-price
                      :hashsymbol="hashsymbol"
                      @sell-bar-click="sellBarClick"
                      @buy-bar-click="buyBarClick"
                      :convertCNY="convertCNY"
                      :ticker="ticker"
                      :quoteCurrencyName="quoteCurrencyName"
                      :baseCurrencyName="baseCurrencyName"
                      :currentPricePrecision="currentPricePrecision"
                      :currentVolumePrecision="currentVolumePrecision"
                      :depth="depth"
                      v-if="depthReady"
                      :lastprice="lastprice"
                      :depthBuyStyle="depthBuyStyle"
                      :depthSellStyle="depthSellStyle"
                      :trade="trade"
                      :UsdtPrice="UsdtPrice"
                      :BtcPrice="BTCPrice"
                      :EthPrice="ETHPrice"

              />
            </div>
            <real-time-trade
                    :quoteCurrencyName="quoteCurrencyName"
                    :baseCurrencyName="baseCurrencyName"
                    :currentPricePrecision="currentPricePrecision"
                    :currentVolumePrecision="currentVolumePrecision"
                    :trade="trade"
            />
          </div>
          <div class="had-row-2" v-if="compTab">
            <div class="head f-cb">
              <div class="f-fl entrust flex_container">
                <span class="tab_container">
                  <span
                          class="f-fl"
                          :class="entrustTab===0?'cur':''"
                          @click="entrustTabClick(0)"
                  >{{$t('home.Handicap')}}</span>
                  <span
                          class="f-fr"
                          :class="entrustTab===1?'cur':''"
                          @click="entrustTabClick(1)"
                  >{{$t('home.RealTime')}}</span>
                </span>
                <up-down @appenddata="appenddata"/>
              </div>
            </div>
            <div class="main">
              <div class="main-list" id="currentList" :class="entrustTab===0?'cur':''">
                <div
                        class="pc-row-2-c2"
                        id="depth"
                        v-loading="loadingdepth"
                        element-loading-background="rgba(138, 134, 134, 0.2)"
                >
                  <last-price
                          :hashsymbol="hashsymbol"
                          @sell-bar-click="sellBarClick"
                          @buy-bar-click="buyBarClick"
                          :convertCNY="convertCNY"
                          :ticker="ticker"
                          :quoteCurrencyName="quoteCurrencyName"
                          :baseCurrencyName="baseCurrencyName"
                          :currentPricePrecision="currentPricePrecision"
                          :currentVolumePrecision="currentVolumePrecision"
                          :depth="depth"
                          v-if="depthReady"
                          :lastprice="lastprice"
                          :depthBuyStyle="depthBuyStyle"
                          :depthSellStyle="depthSellStyle"
                          :trade="trade"
                          :UsdtPrice="UsdtPrice"
                          :BtcPrice="BTCPrice"
                          :EthPrice="ETHPrice"
                  />
                </div>
              </div>
              <div class="main-list" id="historyList" :class="entrustTab===1?'cur':''">
                <real-time-trade
                        :quoteCurrencyName="quoteCurrencyName"
                        :baseCurrencyName="baseCurrencyName"
                        :currentPricePrecision="currentPricePrecision"
                        :currentVolumePrecision="currentVolumePrecision"
                        :trade="trade"
                />
              </div>
            </div>
          </div>
          <div class="page-content">
            <div class="chart-wrap pc-row-1">
              <div class="chart-header" id="chartHeader">
                <coin-summary
                        :ticker="ticker"
                        :currentPricePrecision="currentPricePrecision"
                        :currentVolumePrecision="currentVolumePrecision"
                        :convertCNY="convertCNY"
                        @background="background"
                        v-if="coinSummaryReady"
                        :lastprice="lastprice"
                        :trade="trade"
                        :UsdtPrice="UsdtPrice"
                        :BtcPrice="BTCPrice"
                        :EthPrice="ETHPrice"
                        :allTicker="allTickerArr"
                        :quoteCurrencyName="quoteCurrencyName"
                />
              </div>

              <div class="chart">
                <span
                        class="f-fl"
                        :class="nathTab===0?'cur':''"
                        @click="charts('tv')"
                >{{$t('tradecenter.Chart')}}</span>
                <span
                        class="f-fl"
                        :class="nathTab===1?'cur':''"
                        @click="charts('hig')"
                >{{$t('tradecenter.Depth')}}</span>
              </div>

              <TVChartContainer
                      v-show="chart"
                      :symbol="hashsymbol.toLowerCase()"
                      :backgrounds="backgrounds"
                      :tradePrice="trade.length>0?trade[0].price:0"
                      style="position: absolute;top: 45px;right: 0;left: 0;z-index: 1;width: 100%;"
              />


              <highcharts
                      class="conta-chart"
                      :depth="depth"
                      :currentPricePrecision="currentPricePrecision"
                      :currentVolumePrecision="currentVolumePrecision"
                      :bg="backgrounds"
                      v-show="!chart"
                      style="position: absolute;top: 45px;right: 0;left: 0;z-index: 1;width: 100%"
              ></highcharts>
            </div>
            <!-- 买卖单 -->
            <div class="pc-row-2">
              <div class="pc-row-2-c1" id="pcRow2C1">
                <div class="inner">
                  <div class="buy-and-sold-header">
                    <span>{{$t('tradecenter.BuyOrSell')}}</span>
                  </div>
                  <div class="buy-and-sold-main f-cb">
                    <buy-and-sell
                            @reload="reload"
                            :quoteCurrencyName="quoteCurrencyName"
                            :baseCurrencyName="baseCurrencyName"
                            :tickerCanRecharge="ticker.canRecharge"
                            :saleoutBalance="saleoutBalance"
                            :balance="buyInBalance"
                            :hashsymbol="hashsymbol"
                            :currentPricePrecision="currentPricePrecision"
                            :ethzcTickerClose="ethzcTickerClose"
                            :usdtzcTickerClose="usdtzcTickerClose"
                            :currentVolumePrecision="currentVolumePrecision"
                            :symbol="hashsymbol"
                            :soldOutPosition="soldOutPosition"
                            buyOrSell="buy"
                            :buyinPriceProps="buyin.price"
                            :UsdtPrice="UsdtPrice"
                            :BTCPrice="BTCPrice"
                            :ETHPrice="ETHPrice"
                            @buy-or-sell-click="buyOrSellClick"
                            :fixed_buy_price="fixed_buy_price"
                    />
                    <sell-panel
                            @reload="reload"
                            :quoteCurrencyName="quoteCurrencyName"
                            :baseCurrencyName="baseCurrencyName"
                            :tickerCanRecharge="ticker.canRecharge"
                            :balance="saleoutBalance"
                            :hashsymbol="hashsymbol"
                            :currentPricePrecision="currentPricePrecision"
                            :ethzcTickerClose="ethzcTickerClose"
                            :usdtzcTickerClose="usdtzcTickerClose"
                            :currentVolumePrecision="currentVolumePrecision"
                            :symbol="hashsymbol"
                            :soldOutPosition="soldOutPosition"
                            buyOrSell="sell"
                            :buyinPriceProps="sell.price"
                            :UsdtPrice="UsdtPrice"
                            :BTCPrice="BTCPrice"
                            :ETHPrice="ETHPrice"
                            @buy-or-sell-click="buyOrSellClick"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="bz-row">
          <coin-info :lang="lang" :hashsymbol="hashsymbol" :baseCurrencyName="baseCurrencyName"></coin-info>
          <div class="tp-row-2">
            <div class="tr2-inner" id="tr2Inner">
              <trade-list
                      :orders="orders"
                      :historyOrders="historyOrders"
                      @trade-list-reload="tradeListReload"
                      :currentPricePrecision="currentPricePrecision"
                      :currentVolumePrecision="currentVolumePrecision"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div></div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import TVChartContainer from '../components/TVChartContainer.vue'
import CoinInfo from '@/components/TradeCenter/CoinInfo'
import RealTimeTrade from '@/components/TradeCenter/RealTimeTrade'
import LastPrice from '@/components/TradeCenter/LastPrice'
import CoinSummary from '@/components/TradeCenter/CoinSummary'
import TradeList from '@/components/TradeCenter/TradeList'
import TickerSummary from '@/components/TradeCenter/TickerSummary'
import BuyAndSell from '@/components/TradeCenter/buyAndSell'
import SellPanel from '@/components/TradeCenter/SellPanel'
import UpDown from '@/components/TradeCenter/UpDown'
import {favorite, favoriteDel} from '@/api/home.js'
import {getUsdtPrice} from '@/api/myAssets'

import {
  getAllTicker,
  getSummary,
  getFavorite,
  getOrdersI,
  getAssetsLst,
  getTicker,
  tradeRule
} from '@/api/tradecenter'

import {setTrade} from '@/libs/utils'

import highcharts from '@/components/highcharts.vue'
import {getTrade, setMoneyShow, getMoneyShow} from '@/libs/utils'
import {WSUrl} from '../../public/config'

export default {
  components: {
    MyHeader,
    MyFooter,
    TVChartContainer,
    CoinInfo,
    RealTimeTrade,
    LastPrice,
    CoinSummary,
    TradeList,
    TickerSummary,
    BuyAndSell,
    SellPanel,
    UpDown,
    highcharts
  },
  data () {
    return {
      nathTab: 0,
      entrustTab: 0,
      compTab: 1,
      loadingdepth: true,
      selectHeaderTab: 1,
      hashsymbol: 'ETHUSDT',
      quoteCurrencyName: 'USDT',
      baseCurrencyName: '',
      // search: '',
      allTickerArr: [],
      allTickerObj: '',
      state: false,
      userFavorite: '',
      userFavoriteObj: '',
      ticker: '',
      currentPricePrecision: '',
      currentVolumePrecision: '',
      ethzcTickerClose: 0,
      usdtzcTickerClose: 0,
      convertCNY: 0,
      coinSummaryReady: false,
      headerRiseFallPercent: '',
      currencyLst: [],
      saleoutBalance: 0,
      soldOutPosition: '',
      buyInBalance: 0,
      buyInPosition: '',
      depth: '',
      depthReady: false,
      depthBidsArr: '',
      depthAsksArr: '',
      buyAmountArr: [],
      sellAmountArr: [],
      backgroundWidth: [],
      SellBackgroundWidth: [],
      trade: [],
      symbol: '',
      upDownSelect: 2,
      depthSellStyle: '',
      depthBuyStyle: '',
      getFavoriteState: '',
      userFavoriteArr: [], //自选列表
      allTicker1: [], //areId=1的coinsummary
      allTicker2: [], //areId=2的coinsummary
      allTicker3: [], //areId=3的coinsummary
      buyinConvertedval: '',
      dragBarPosition: '',
      orders: '',
      historyOrders: '',
      buyin: {
        price: ''
      },
      sell: {
        price: ''
      },
      money: 0,
      btcclose: 0,
      moneyShow: false,
      lang: 'zh',
      chart: true,
      backgrounds: '',
      classn: '',
      lastprice: 0,
      websock: null,
      screenWidth: '',
      fixed_buy_price: 0,
      UsdtPrice: 1,
      BTCPrice: 1,
      ETHPrice: 1,
      loop:null,
      loop2:null,
    }
  },
  watch: {
    hashsymbol () {
      // this.getTicker()
      // this.tradeSummary()
      this.currentEntrustPanel()
      this.historyEntrustPanel()
      if (this.websock !== null) {
        this.websocketonopen()
        this.websocketonmessage()
      }
    }
  },
  created () {
    this.tokenLogin()
    // const bg = localStorage.getItem('back');
    // localStorage.setItem('background',bg);
    this.screenWidth = document.body.clientWidth
    if (this.screenWidth <= 1550) {
      this.compTab = 1
    } else {
      this.compTab = 0
    }
    window.onresize = () => {
      return (() => {
        this.screenWidth = document.body.clientWidth
        if (this.screenWidth <= 1550) {
          this.compTab = 1
        } else {
          this.compTab = 0
        }
      })()
    }
    this.state = JSON.parse(localStorage.getItem('loginState'))
    this.getAssetsLst()
    this.moneyShow = getMoneyShow()
    let hash = getTrade()
    if (hash) {
      this.hashsymbol = hash
      this.tradeRule(this.hashsymbol)
    }
    this.tradeRule(this.hashsymbol)
    this.getAllTicker()
    this.loop = setInterval(() => {
      this.getAllTicker()
    }, 2000)
    this.currentEntrustPanel()
    this.historyEntrustPanel()
    this.initWebSocket()
    this.getUsdt()


  },
  mounted () {
    var classn = window.localStorage.getItem('background')
    if (classn == null) {
      document.getElementById('app').className = 'theme' + 'c'
    } else {
      document.getElementById('app').className = 'theme' + classn
    }
    this.loop2=setInterval(()=> {
      this.getUsdt()
    },3000)
  },
  beforeDestroy () {
    window.clearInterval(this.loop)
    window.clearInterval(this.loop2)
    document.getElementById('app').className =
        'theme' + window.localStorage.setItem('back', 'a')
    this.websock.close()
  },
  methods: {
    async getUsdt () {
      const res = await getUsdtPrice();
      // [this.UsdtPrice] = res.USDT_CNY;
      // [this.BTCPrice] = res.BTC_CNY;
      // [this.ETHPrice] = res.ETH_CNY

          [this.UsdtPrice] = res.USDT_CNY
      const ETH_USDT = res.ETH_USDT[0]
      const BTC_USDT = res.BTC_USDT[0]
      this.BTCPrice = this.UsdtPrice * BTC_USDT
      this.ETHPrice = this.UsdtPrice * ETH_USDT
    },
    tokenLogin () {
      const {token} = this.$router.history.current.query
      if (token) {
        localStorage.setItem('token', token)
        this.$router.push('/tradecenter')
      }
    },
    tradeRule (hashsymbol) {
      tradeRule(hashsymbol.toLowerCase()).then(res => {
        if (res.state == 1) {
          let data = res.data
          this.fixed_buy_price = data.fixed_buy_price
          localStorage.setItem('fixed_buy_price', this.fixed_buy_price)
        }
      })
    },
    entrustTabClick (index) {
      this.entrustTab = index
    },
    appenddata (e) {
      this.entrustTab = 0
      this.depthBuyStyle = e[0]
      this.depthSellStyle = e[1]
    },
    initWebSocket () {
      //初始化weosocket
      const wsuri = WSUrl
      this.websock = new WebSocket(wsuri)
      this.websock.onopen = this.websocketonopen
      this.websock.onmessage = this.websocketonmessage
      this.websock.onerror = this.websocketonerror
      this.websock.onclose = this.websocketclose
    },
    websocketonopen () {
      //连接建立之后执行send方法发送数据
      if (this.websock.readyState == 1) {
        let depthaction = {
          channel: `depth.${this.hashsymbol.toLowerCase()}`
        }
        let tickeraction = {
          channel: `ticker.${this.hashsymbol.toLowerCase()}`
        }
        let tradeaction = {
          channel: `trade.${this.hashsymbol.toLowerCase()}`
        }
        if (this.websock.readyState === 1) {
          this.websocketsend(JSON.stringify(depthaction))
          this.websocketsend(JSON.stringify(tickeraction))
          this.websocketsend(JSON.stringify(tradeaction))
        }
      }
    },
    websocketonerror () {
      //连接建立失败重连
      this.websock.channel()
      this.initWebSocket()
    },
    websocketonmessage (e) {
      //数据接收
      if (e) {
        const redata = JSON.parse(e.data)
        if (redata.msgType === 'ticker') {
          let ticker = redata.data
          this.ticker = ticker
          this.baseCurrencyName = ticker.baseCurrencyName
          this.quoteCurrencyName = ticker.quoteCurrencyName
          this.symbol = ticker.symbol.toUpperCase()
          this.currentPricePrecision = ticker.pricePrecision
          this.currentVolumePrecision = ticker.volumePrecision
          this.convertCNY = ticker.cnyClose
          // if(ticker.quoteCurrencyName == 'ETH'){
          //   this.convertCNY = ticker.close * this.ethzcTickerClose;
          // } else if(ticker.quoteCurrencyName == 'USDT'){
          //   this.convertCNY = ticker.close * this.usdtzcTickerClose;
          // } else {
          //   this.convertCNY = ticker.close;
          // }
          this.coinSummaryReady = true
          this.soldOutCanUseHeader()
          this.buyInCanUseHeader()
        }
        if (redata.msgType === 'trade') {
          let trade = redata.data
          this.trade = trade.trades
          // if(redata.isFirst === 1){
          //   this.trade = trade.trades;
          // }else{
          //   this.trade.unshift(trade.trades[0]);
          //   if(this.trade.length > 50){
          //     this.trade.pop();
          //   }
          // }
          if (trade.trades.length) {
            this.lastprice = this.trade[0].price
          }
        }
        if (redata.msgType === 'depth') {
          let depth = redata.data
          this.depth = depth
          this.depthReady = true
          this.loadingdepth = false
        }
      }
    },
    websocketsend (Data) {
      //数据发送
      this.websock.send(Data)
    },
    websocketclose (e) {
      //关闭
      console.log('断开连接', e)
    },
    buyOrSellClick () {
      // this.getTicker()
      this.tradeSummary()
      this.currentEntrustPanel()
      this.historyEntrustPanel()
      this.getAssetsLst()
    },
    tradeListReload () {
      this.currentEntrustPanel()
      this.historyEntrustPanel()
      this.getAssetsLst()
    },
    reload () {
      this.currentEntrustPanel()
      this.historyEntrustPanel()
      // this.tradeSummary();
    },
    toDefaultSymbol () {
      this.hashsymbol = `ZZEX${this.quoteCurrencyName}`
      setTrade(this.hashsymbol.toUpperCase())
      this.fixed_buy_price = 0
      debugger;
      this.buyin.price = ''
    },

    currentEntrustPanel () {
      getOrdersI(this.hashsymbol, '1', '0').then(response => {
        if (response.state === 1) {
          let orders = response.orders
          this.orders = orders
        }
      })
    },
    historyEntrustPanel () {
      getOrdersI(this.hashsymbol, '1', '1').then(response => {
        if (response.state === 1) {
          let orders = response.orders
          orders.forEach(ele => {
            switch (ele.o_status) {
              case 'done':
                ele.status = this.$t('tradecenter.Transaction')
                break
              case 'canceled':
                ele.status = this.$t('tradecenter.Cancellations')
                break
              case 'partial-canceled':
                ele.status = this.$t('tradecenter.PartOfIts')
                break
            }
          })
          this.historyOrders = orders
        }
      })
    },
    getTicker () {
      getTicker(this.hashsymbol).then(response => {
        console.log(response)
        this.baseCurrencyName = response.baseCurrencyName
        this.quoteCurrencyName = response.quoteCurrencyName
        this.symbol = response.symbol.toUpperCase()
        this.currentPricePrecision = response.pricePrecision
        this.currentVolumePrecision = response.volumePrecision

        // this.userBuyInPanel()
        // this.userSoldOutPanel()
      })
    },


    getAllTicker () {
      getAllTicker().then(response => {
        if (response.state == 1) {
          let tickersMap = response.data

          // try {
          //   this.ethzcTickerClose = tickersMap["ethbtc_ticker"].close;
          // } catch (error) {}
          // try {
          //   this.usdtzcTickerClose = tickersMap["usdtbtc_ticker"].close;
          // } catch (error) {}

          let allTickerObj = {}
          let allTickerArr = []
          for (let i in tickersMap) {
            let tickerMapSymbol = tickersMap[i].symbol
            allTickerArr.push(tickerMapSymbol)
            allTickerObj[tickerMapSymbol] = tickersMap[i]
          }
          this.allTickerArr = allTickerArr
          this.allTickerObj = allTickerObj
          // 判断目前页面的交易对是否是是将上线的交易对，
          // 如果是则跳转到改交易区的第一个交易对
          var symbol = this.hashsymbol.toLowerCase()
          var pStatus = allTickerObj[symbol].pStatus
          if (pStatus == 3) {
            this.toDefaultSymbol()
          }
          localStorage.setItem('fixed_buy_price', this.fixed_buy_price)

          this.allTickerFilter()
          //获取用户喜欢的交易对
          getFavorite().then(response => {
            //获取用户喜欢交易对 是否成功
            this.getFavoriteState = response.state
            //用户喜欢的交易对 只含名称
            let userFavorite = response.data
            this.userFavorite = userFavorite
            let userFavoriteSymbol = []
            if (this.getFavoriteState === 1) {
              userFavorite.forEach(ele => {
                userFavoriteSymbol.push(ele.pair_dsp_name)
              })
              this.userFavoriteSymbol = userFavoriteSymbol
              this.userFavoriteFilter()
            } else {
              this.splitAllTicker()
            }
          })
        }
      })
    },
    getAssetsLst () {
      getAssetsLst().then(res => {
        if (res.state == 1) {
          this.currencyLst = res.data.accountsLst
          this.state = true
          this.soldOutCanUseHeader()
          this.buyInCanUseHeader()
        } else {
        }
      })
    },
    tradeSummary () {
      getSummary(this.hashsymbol, '1min', '0', '1', 'market').then(response => {
        //hashsymbol类型币种的具体的信息
        let ticker = response.data.ticker
        this.ticker = ticker
        //所有币种的具体信息
        let tickersMap = response.data.tickersMap
        //ethzc的收盘价
        let ethzcTickerClose = tickersMap['ethbtc_ticker'].close
        this.ethzcTickerClose = ethzcTickerClose
        //usdtzc的收盘价
        let usdtzcTickerClose = ''
        if (tickersMap['usdtbtc_ticker']) {
          usdtzcTickerClose = tickersMap['usdtbtc_ticker'].close
        } else {
          usdtzcTickerClose = 0
        }
        this.usdtzcTickerClose = usdtzcTickerClose

        //hashsymbol的深度信息
        let depth = response.data.depth
        this.depth = depth
        this.depthReady = true
        //hashsymbol的交易信息
        let trade = response.data.trade
        this.trade = trade
        if (trade.length !== 0) {
          this.lastprice = trade[0].price
        }

        this.loadingdepth = false

        //货币等于多少rmb
        let convertCNY = ''
        if (ticker.areaId === 1) {
          convertCNY = ticker.close
        } else if (ticker.areaId === 2) {
          convertCNY = ticker.close * ethzcTickerClose
        } else if (ticker.areaId === 3) {
          convertCNY = ticker.close * usdtzcTickerClose
        }
        this.convertCNY = convertCNY
        this.coinSummaryReady = true

        //用户登录之后后台才返回的字段
        let currencyLst = response.data.currencyLst
        if (currencyLst) {
          this.state = true
        }
        this.currencyLst = currencyLst

        //所有币种的对象集合 （貌似和tickersMap无差）
        let allTickerObj = {}
        //所有币种的symbol集合
        let allTickerArr = []
        let btcclose = 0
        for (let i in tickersMap) {
          if (tickersMap[i].symbol == 'btczc') {
            btcclose = tickersMap[i].close
            break
          }
        }
        this.btcclose = btcclose
        for (let i in tickersMap) {
          let tickerMapSymbol = tickersMap[i].symbol
          allTickerArr.push(tickerMapSymbol)
          allTickerObj[tickerMapSymbol] = tickersMap[i]
        }
        this.allTickerArr = allTickerArr
        this.allTickerObj = allTickerObj
        this.allTickerFilter()
        //获取用户喜欢的交易对
        getFavorite().then(response => {
          //获取用户喜欢交易对 是否成功
          this.getFavoriteState = response.state
          //用户喜欢的交易对 只含名称
          let userFavorite = response.data
          this.userFavorite = userFavorite
          let userFavoriteSymbol = []
          if (this.getFavoriteState === 1) {
            userFavorite.forEach(ele => {
              userFavoriteSymbol.push(ele.pair_dsp_name)
            })
            this.userFavoriteSymbol = userFavoriteSymbol
            this.userFavoriteFilter()
          } else {
            this.splitAllTicker()
          }
        })

        this.soldOutCanUseHeader()
        this.buyInCanUseHeader()
      })
    },
    allTickerFilter () {
      let allTickerArr = []
      for (let j in this.allTickerObj) {
        allTickerArr.push(this.allTickerObj[j])
        allTickerArr.sort(this.sortNumber)
      }
      allTickerArr.forEach(ele => {
        ele.tickerRiseFallPercent = this.riseFallPercent(
            ele,
            ele.pricePrecision
        )
      })
      this.allTickerArr = allTickerArr
    },
    splitAllTicker () {
      let allTicker1 = []
      let allTicker2 = []
      let allTicker3 = []
      let allTickerArr = this.allTickerArr
      for (let i = 0; i < allTickerArr.length; i++) {
        let areaId = allTickerArr[i].areaId
        if (areaId === 1) {
          allTicker1.push(allTickerArr[i])
        }
        if (areaId === 2) {
          allTicker2.push(allTickerArr[i])
        }
        if (areaId === 3) {
          allTicker3.push(allTickerArr[i])
        }
      }
      this.allTicker1 = allTicker1
      this.allTicker2 = allTicker2
      this.allTicker3 = allTicker3
    },
    userFavoriteFilter () {
      let userFavoriteObj = {}
      let userFavoriteArr = []
      this.allTickerArr.forEach(ele => {
        ele.position =
            this.userFavoriteSymbol.indexOf(ele.symbol.toUpperCase()) !== -1
      })
      this.splitAllTicker()
      //如果获取用户喜欢交易对成功
      for (let i = 0; i < this.userFavorite.length; i++) {
        //获取用户喜欢交易对的symbol
        let favorSymbol = this.userFavorite[i].pair_dsp_name.toLowerCase()
        //如果所有交易对包含这个用户喜欢的symbol
        if (this.allTickerObj[favorSymbol]) {
          //将这个交易对的对象传给userFavoriteObj
          userFavoriteObj[favorSymbol] = this.allTickerObj[favorSymbol]
        }
      }
      //将自选对象转化成数组
      for (let a in userFavoriteObj) {
        userFavoriteArr.push(userFavoriteObj[a])
      }
      userFavoriteArr.forEach(ele => {
        ele.tickerRiseFallPercent = this.riseFallPercent(
            ele,
            ele.pricePrecision
        )
      })
      this.userFavoriteArr = userFavoriteArr
    },

    sortNumber (a, b) {
      return a.order - b.order
    },
    riseFallPercent (ticker, precision) {
      let open = ticker.open.toFixed(precision)
      let close = ticker.close.toFixed(precision)
      let sign
      if (open - close < 0) {
        sign = '+'
      } else {
        sign = ''
      }
      if (open != 0) {
        return sign + (((close - open) * 100) / open).toFixed(2)
      } else {
        return '0.00'
      }
    },

    buyInCanUseHeader () {
      let quoteCurrencyNameArr = []
      if (this.state) {
        for (let i = 0; i < this.currencyLst.length; i++) {
          quoteCurrencyNameArr.push(this.currencyLst[i].currency)
        }
        let position = quoteCurrencyNameArr.indexOf(this.quoteCurrencyName)
        let buyInBalance = ''
        this.buyInPosition = position
        if (position >= 0) {
          buyInBalance = this.currencyLst[position].balance
        } else {
          buyInBalance = 0
        }
        this.buyInBalance = buyInBalance
      }
    },

    soldOutCanUseHeader () {
      let baseCurrencyNameArr = []
      let money = 0
      if (this.state) {
        for (let i = 0; i < this.currencyLst.length; i++) {
          money += this.currencyLst[i].zc_balance
          baseCurrencyNameArr.push(this.currencyLst[i].currency)
        }
        this.money = money
        let position = baseCurrencyNameArr.indexOf(this.baseCurrencyName)
        let balance = ''
        this.soldOutPosition = position
        if (position >= 0) {
          balance = this.currencyLst[position].balance
        } else {
          balance = 0
        }
        this.saleoutBalance = balance
      }
    },
    starClick (item) {
      if (!item.position) {
        favorite(item.symbol.toUpperCase()).then(res => {
          if (res.state === 1) {
            // this.$set(this.areaTicker[index], 'position', 0)
            this.$message({
              type: 'success',
              message:
                  this.$t('header.Collection') +
                  ' ' +
                  `${item.symbol}` +
                  ' ' +
                  this.$t('header.success'),
              duration: 3000,
              showClose: true
            })
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
              setTimeout(() => {
                this.userLogout()
              }, 2000)
            }
          }
        })
      } else {
        favoriteDel(item.symbol.toUpperCase()).then(res => {
          if (res.state === 1) {
            // this.$set(this.areaTicker[index], 'position', -1)
            this.$message({
              type: 'success',
              message:
                  this.$t('header.CancelCollection') +
                  ' ' +
                  `${item.symbol}` +
                  ' ' +
                  this.$t('header.success'),
              duration: 3000,
              showClose: true
            })
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
              setTimeout(() => {
                this.userLogout()
              }, 2000)
            }
          }
        })
      }
      this.tickerInit(false)
    },

    buyBarClick (item) {
      this.buyin.price = item[0].toFixed(this.currentPricePrecision)
      this.sell.price = item[0].toFixed(this.currentPricePrecision)
    },
    sellBarClick (item) {
      this.buyin.price = item[0].toFixed(this.currentPricePrecision)
      this.sell.price = item[0].toFixed(this.currentPricePrecision)
    },
    toUserTicker (item) {
     if (this.hashsymbol !== item.symbol.toUpperCase()){
       this.hashsymbol = item.symbol.toUpperCase()
       this.tradeRule(this.hashsymbol)
       this.trade=[]
       this.lastprice=0
     }
    },
    hideMoney () {
      this.moneyShow = false
      setMoneyShow(false)
    },
    showMoney () {
      this.moneyShow = true
      setMoneyShow(true)
    },
    langChange (lang) {
      this.lang = lang
    },
    toLogin () {
      this.$router.push('/login')
    },
    background (A) {
      this.backgrounds = A
      window.localStorage.setItem('background', A)
    },
    charts (v) {
      if (v == 'tv') {
        this.chart = true
        this.nathTab = 0
      } else {
        this.chart = false
        this.nathTab = 1
      }
    },

  }
}
</script>
<style scoped>
  .flex_container {
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: 45px;
    padding: 0 15px;
    box-sizing: border-box;
  }

  .tab_container {
    display: flex;
    flex-direction: row;
    justify-content: start;
    width: 150px !important;
  }
</style>
