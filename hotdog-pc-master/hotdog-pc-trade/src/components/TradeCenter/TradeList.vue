<template>
  <div class="tr2-c1">
    <div class="tr2-c1-inner">
      <div class="head f-cb">
        <div class="f-fl entrust flex_container">
          <span class="tab_container">
            <span class="f-fl" :class="entrustTab===0?'cur':''" @click="entrustTabClick(0)">{{$t('tradecenter.OpenOrders')}}</span>
            <span class="f-fr" :class="entrustTab===1?'cur':''" @click="entrustTabClick(1)">{{$t('tradecenter.OrderHistory')}}</span>
          </span>
          <span @click="toTradeBill()" style="font-size: 12px">{{$t('header.ViewAll')}}</span>
        </div>
      </div>
      <div class="main">
        <div class="main-list " id="currentList" :class="entrustTab===0?'cur':''">
          <div class="head-list">
            <span>{{$t('tradecenter.Date')}}</span>
            <span>{{$t('tradecenter.TradingOn')}}</span>
            <span>{{$t('tradecenter.Type')}}</span>
            <span>{{$t('tradecenter.EntrustedPrice')}}</span>
            <span>{{$t('tradecenter.Amount')}}</span>
            <span>{{$t('tradecenter.DelegateTotal')}}</span>
            <span>{{$t('tradecenter.Executed')}}</span>
            <span>{{$t('tradecenter.Unexecuted')}}</span>
            <span>{{$t('tradecenter.Action')}}</span>
          </div>
          <div class="current-list list">
            <dl v-for="(item,index) in orders" :key="index">
              <dd>
                <span>{{item.create_time}}</span>
                <span>{{item.base_currency}}/{{item.quote_currency}}</span>
                <span class="buy rise" v-if="item.o_type==='buy'">{{$t('tradecenter.Buys')}}</span>
                <span class="buy fall" v-else>{{$t('tradecenter.Sells')}}</span>
                <span>{{item.price}}</span>
                <span>{{item.volume}}</span>
                <span>{{(item.price*item.volume).toFixed(currentVolumePrecision)}}</span>
                <span>{{item.done_volume}}</span>
                <span>{{(item.volume - item.done_volume).toFixed(currentVolumePrecision)}}</span>
                <span @click.stop="cancelBtnClick(item)">{{$t('tradecenter.Revocation')}}</span>
              </dd>
            </dl>
          </div>
        </div>
        <div class="main-list" id="historyList" :class="entrustTab===1?'cur':''">
          <div class="head-list">
            <span>{{$t('tradecenter.Date')}}</span>
            <span>{{$t('tradecenter.TradingOn')}}</span>
            <span>{{$t('tradecenter.Type')}}</span>
            <span>{{$t('tradecenter.EntrustedPrice')}}</span>
            <span>{{$t('tradecenter.Amount')}}</span>
            <span>{{$t('tradecenter.Executed')}}</span>
            <span>{{$t('tradecenter.AveragePrice')}}</span>
            <span>{{$t('tradecenter.Status')}}</span>
          </div>
          <div class="history-list list" >
            <dl v-for="(item,index) in historyOrders" :key="index" >
              <dd>
                <span>{{item.create_time}}</span>
                <span>{{item.base_currency}}/{{item.quote_currency}}</span>
                <span class="buy rise" v-if="item.o_type==='buy'">{{$t('tradecenter.Buys')}}</span>
                <span class="buy fall" v-else>{{$t('tradecenter.Sells')}}</span>
                <span>{{item.price}}</span>
                <span>{{item.volume}}</span>
                <span>{{item.done_volume}}</span>
                <span>{{item.done_avg_price}}</span>
                <span>{{item.status}}</span>
              </dd>
            </dl>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {orderCancel} from '@/api/tradecenter'

export default {
  props:['orders','historyOrders','currentPricePrecision','currentVolumePrecision'],
  data(){
    return{
      hashsymbol: 'ETHZC',
      entrustTab:0
    }
  },
  methods:{
    entrustTabClick (index) {
      this.entrustTab = index
    },
    cancelBtnClick (item) {
      let id = item.id
      let no = item.o_no
      let symbol = item.symbol
      orderCancel(id, no, symbol).then((response) => {
        if (response.state === 1) {
          this.$message({
            type: 'success',
            message: this.$t('tradecenter.RevocationOfSuccess'),
            duration: 3000,
            showClose: true
          })
          this.$emit('trade-list-reload')
        }
      })
    },
    toTradeBill(){
      this.$router.push({name:'TradeBill'})
    }
  }
}
</script>
<style scoped>
.flex_container{
  display: flex;
  justify-content: space-between;
  width: 100%;
}
.tab_container{
  display: flex;
  flex-direction: row;
  justify-content: start;
  width: 200px !important;
}
.tr2-c1 .tr2-c1-inner .main .current-list dl dd span:last-child{
  color: #196bdf;
}
</style>
