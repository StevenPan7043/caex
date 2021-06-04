<template>
  <div class="tr2-c2">
    <div class="tr2-c2-inner" v-loading='loading'
    element-loading-background="rgba(138, 134, 134, 0.2)">
      <div class="head">
        <span>{{$t('tradecenter.MarketTrades')}}</span>
      </div>
      <div class="roy-market-header">
        <div class="head-list">
          <dl class="time" ><dt>{{$t('tradecenter.Date')}}</dt></dl>
          <!--<dl class="type"><dt>{{$t('tradecenter.Direction')}}</dt></dl>-->
          <dl class="price" ><dt>{{$t('tradecenter.Price')}}<span class="uppercase">(<i>{{quoteCurrencyName}}</i>)</span></dt></dl>
          <dl class="amount"><dt>{{$t('tradecenter.Num')}}<span class="uppercase">(<i>{{baseCurrencyName}}</i>)</span></dt></dl>
        </div>
      </div>
      <div class="main">
        <div class="market-list">
          <dl class="market-trades-time" id="mtTime">
            <dd v-for="(item,index) in tradeList" :key="index">
              {{item.formatdate}}
            </dd>
          </dl>
          <!--<dl class="market-trades-type" id="mtType">-->
            <!--<div v-for="(item,index) in tradeList" :key="index">-->
              <!--<dd class="rise" v-if="item.taker==='buy'">{{$t('tradecenter.Buys')}}</dd>-->
              <!--<dd class="fall" v-else >{{$t('tradecenter.Sells')}}</dd>-->
            <!--</div>-->
          <!--</dl>-->
          <dl class="market-trades-price" id="mtPrice">
            <div v-for="(item,index) in tradeList" :key="index">
              <dd class="rise" v-if="item.taker==='buy'">{{item.price.toFixed(currentPricePrecision)}}</dd>
              <dd class="fall" v-else >{{item.price.toFixed(currentPricePrecision)}}</dd>
              <!--{{item.price.toFixed(currentPricePrecision)}}-->
            </div>
          </dl>
          <dl class="market-trades-amount" id="mtAmount">
            <dd v-for="(item,index) in tradeList" :key="index">
              {{item.volume.toFixed(currentVolumePrecision)}}
            </dd>
          </dl>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props:['quoteCurrencyName','baseCurrencyName','currentPricePrecision','currentVolumePrecision','trade'],
  data(){
    return{
      hashsymbol: 'ETHZC',
      loading: true
    }
  },
  computed:{
    tradeList(){
      let trade = this.trade
      if(trade){
        for (let i = 0; i < trade.length; i++) {
          let date = new Date(trade[i].timestamp)
          let formatdate = this.formatDate(date)
          trade[i].formatdate = formatdate
        }
      this.loading = false
      }
      return trade
    }
  },
  methods:{
    formatDate (ts) {
      let hour = ts.getHours()
      let minute = ts.getMinutes()
      let second = ts.getSeconds()
      hour = hour < 10 ? '0' + hour : hour
      minute = minute < 10 ? '0' + minute : minute
      second = second < 10 ? '0' + second : second
      return hour + ':' + minute + ':' + second
    },
  }
}
</script>
<style scoped>
.roy-market-header{
  padding: 10px 15px;
  /*margin: 10px 0;*/
  font-size:12px;
  color: #718bab;
}
.themea .roy-market-header{
  color: #91a4b7;
}
.roy-market-header .head-list{
  display:flex;
  justify-content: space-between;
}
.roy-market-header dl{
  width:25%;
  white-space: nowrap;
}
.roy-market-header dl.price{
  text-align: right;
}
.roy-market-header dl:last-child{
  text-align:right;
  width: 28%;
}
@media only screen and (max-width: 1400px){
  .roy-market-header{
    /*padding: 0 15px;*/
    font-size:12px;
  }
  .roy-market-header dl:nth-child(2){
    text-align:right;
  }
}
</style>
