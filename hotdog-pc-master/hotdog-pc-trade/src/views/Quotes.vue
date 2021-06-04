<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="usercenter">
        <div class="main-panel f-cb quotes">
          <div class="content" id="view">
            <div class="inner-account-info" id="innerAccountInfo">
              <div class="header"><i ></i>{{$t('home.IndustryNews')}}</div>
              <div class="user-recommend" v-loading="loading">
                <div class="title">{{newslist.date}}</div>
                <div class="lists">
                  <div class="l-invitecode list f-cb">
                    <el-timeline>
                      <el-timeline-item :timestamp="activity.created_at|times" placement="top"
                      v-for="(activity, index) in newslist.lives"
                      :key="index"
                      color="#f0bf41"
                      >
                        <el-card>
                          <h4>{{activity.content|title}}</h4>
                          <p>{{activity.content|content}}</p>
                        </el-card>
                      </el-timeline-item>
                    </el-timeline>
                  </div>
                </div>
              </div>
              <div class="fonter" @click="more"><i></i>{{$t('home.ClickMore')}}</div>
            </div>
          </div>
          <my-quotes
            :allTicker1="allTicker1"
            :allTicker2="allTicker2"
            :allTicker3="allTicker3"
            :userFavoriteArr="userFavoriteArr"
            @to-user-ticker="toUserTicker"
            @star-click="starClick"
            v-loading="loadingmake"
          ></my-quotes>
        </div>
      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import {  favorite, favoriteDel } from '@/api/home.js'
import { getTicker, getSummary, getFavorite,getOrdersI } from '@/api/tradecenter'
import {getTrade,setMoneyShow,getMoneyShow} from '@/libs/utils'
import { getjinsenew } from '@/api/getjsnews.js'
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import MyQuotes from '@/components/Quotes'
export default {
  components: {
    MyHeader,
    MyFooter,
    MyQuotes,
  },
  data () {
    return {
      loading: true,
      loadingmake: true,
      userInfo: '',
      phoneText: '',
      emailText: '',
      realnameText: '',
      href: '',

      selectHeaderTab:1,
      hashsymbol: 'ETHZC',
      quoteCurrencyName: '',
      baseCurrencyName: '',

      userFavoriteArr: [],//自选列表
      allTicker1: [],//areId=1的coinsummary
      allTicker2: [],//areId=2的coinsummary
      allTicker3: [],//areId=3的coinsummary
        newslist:[],
        pageSize:10,
        page: 1,
    }
  },
  filters:{
    times(v){
      var date = new Date(v);
      // var h = date.getHours() + ':'
      var m = date.getMinutes() + ':'
      var s = date.getSeconds()
      return m+s;
    },
    title(v){
      var str = v.split('】');
      return str[0].substring(0,50)+'】';
    },
    content(v){
      var str = v.split('】');
      return str[1];
    }
  },
  mounted () {
    this.tradeSummary()
    this.loop = setInterval(() => {
      this.tradeSummary()
    }, 2000)
    this.getnews(this.page,this.pageSize)
  },
  beforeDestroy () {
    window.clearInterval(this.loop)
  },
  methods: {

    tradeSummary () {
      getSummary(this.hashsymbol, '1min', '0', '1', 'market').then((response) => {
        //hashsymbol类型币种的具体的信息
        let ticker = response.data.ticker
        this.ticker = ticker
        //所有币种的具体信息
        let tickersMap = response.data.tickersMap
        //ethzc的收盘价
        let ethzcTickerClose = tickersMap['ethzc_ticker'].close
        this.ethzcTickerClose = ethzcTickerClose
        //usdtzc的收盘价
        let usdtzcTickerClose = ''
        if (tickersMap['usdtzc_ticker']) {
          usdtzcTickerClose = tickersMap['usdtzc_ticker'].close
        } else {
          usdtzcTickerClose = 0
        }
        this.usdtzcTickerClose = usdtzcTickerClose
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
        this.coinSummaryReady=true


        //用户登录之后后台才返回的字段 
        let currencyLst = response.data.currencyLst
        this.currencyLst = currencyLst

        //hashsymbol的深度信息
        let depth = response.data.depth
        this.depth = depth
        this.depthReady=true
        //hashsymbol的交易信息
        let trade = response.data.trade
        this.trade = trade

        //所有币种的对象集合 （貌似和tickersMap无差）
        let allTickerObj = {}
        //所有币种的symbol集合
        let allTickerArr = []
        let btcclose= 0
        for (let i in tickersMap) {
          if(tickersMap[i].symbol=='btczc'){
              btcclose = tickersMap[i].close
              break
          }
        }
        this.btcclose=btcclose
        for (let i in tickersMap) {
          let tickerMapSymbol = tickersMap[i].symbol
          allTickerArr.push(tickerMapSymbol)
          allTickerObj[tickerMapSymbol] = tickersMap[i]
        }
        this.allTickerArr = allTickerArr
        this.allTickerObj = allTickerObj
        this.allTickerFilter()
        //获取用户喜欢的交易对
        getFavorite().then((response) => {
          //获取用户喜欢交易对 是否成功
          this.getFavoriteState = response.state
          //用户喜欢的交易对 只含名称
          let userFavorite = response.data
          this.userFavorite= userFavorite
          let userFavoriteSymbol = []
          if (this.getFavoriteState === 1) {
            userFavorite.forEach(ele => {
              userFavoriteSymbol.push(ele.pair_dsp_name)
            })
            this.userFavoriteSymbol = userFavoriteSymbol
            this.userFavoriteFilter()
          }else{
            this.splitAllTicker()
          }
        })
        // this.soldOutCanUseHeader()
        // this.buyInCanUseHeader()

      })
    },
    allTickerFilter(){
      let allTickerArr = []
      for (let j in this.allTickerObj) {
        allTickerArr.push(this.allTickerObj[j])
        allTickerArr.sort(this.sortNumber)
      }
      allTickerArr.forEach(ele => {
        ele.tickerRiseFallPercent = this.riseFallPercent(ele, ele.pricePrecision)
      })
      this.allTickerArr = allTickerArr
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
      return sign + (((close - open) * 100) / open).toFixed(2)
    },
    splitAllTicker(){
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
      this.loadingmake = false
    },
    userFavoriteFilter () {
      let userFavoriteObj = {}
      let userFavoriteArr = []
      this.allTickerArr.forEach( ele =>{
        ele.position=this.userFavoriteSymbol.indexOf(ele.symbol.toUpperCase())!==-1
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
        ele.tickerRiseFallPercent = this.riseFallPercent(ele, ele.pricePrecision)
      })
      this.userFavoriteArr = userFavoriteArr
    },

    toUserTicker (item) {
      this.hashsymbol = item.symbol.toUpperCase()
    },

    starClick(item){
      if (!item.position) {
        favorite(item.symbol.toUpperCase()).then(res => {
          if (res.state === 1) {
            // this.$set(this.areaTicker[index], 'position', 0)
            this.$message({
              type: 'success',
              message: this.$t('header.Collection')+' '+`${ item.symbol }`+' '+this.$t('header.success'),
              duration: 3000,
              showClose: true
            })
            this.tradeSummary()
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
                this.userLogout()
            }
          }
        })
      } else {
        favoriteDel(item.symbol.toUpperCase()).then(res => {
          if (res.state === 1) {
            // this.$set(this.areaTicker[index], 'position', -1)
            this.$message({
              type: 'success',
              message:this.$t('header.CancelCollection')+' '+`${ item.symbol }`+' '+this.$t('header.success'),
              duration: 3000,
              showClose: true
            })
            this.tradeSummary()
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
                this.userLogout()
            }
          }
        })
      }
      // this.tickerInit(false)
    },
    more(){
      this.getnews(this.page,this.pageSize);
    },
      getnews(page,pageSize){
        getjinsenew(page,pageSize).then(res=>{
          if (res.state == 1){
            if (res.data.lives.length<this.pageSize){
              this.$message({
                type: 'error',
                message: '没有更多快讯了',
                duration: 3000,
                showClose: true
              })
            }else{
              this.newslist = res.data
              this.pageSize = this.pageSize+10
              this.loading = false
            }
          }
        })
      }
  }
}
</script>
<style scoped>
.copyIcon{
  width: 20px;
  height: 20px
}
#usercenter .content, #assetsmanager .content, #helpCenter .content, #customerService .content{
  width: 860px;
  float: left; 
  margin-right: 10px;

  margin-left: 0px;
}
.el-card h4{
  color: #25425a;
  font-size: 16px;
}
.el-card p{
  color: #25425a;
  font-size: 14px;
}
</style>
