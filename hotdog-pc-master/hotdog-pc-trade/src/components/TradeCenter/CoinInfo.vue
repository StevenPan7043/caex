<template>
  <div class="tp-row-3">
    <div
      class="tr3-inner"
      id="tr3Inner"
    >
      <div class="head"><span>{{$t('tradecenter.CurrencyData')}}</span></div>
      <div class="main">
        <!--<div class="coin-intro">-->
          <!--&lt;!&ndash;<div class="title">{{currencyInfo.name}}</div>&ndash;&gt;-->
          <!--&lt;!&ndash;<p class="desc" v-html="currencyInfo.introduce"></p>&ndash;&gt;-->
        <!--</div>-->
            <div class="table">
              <p> <span>{{$t('header.CurrencyName')}}</span> <span class="release-time">{{currencyInfo.name}}</span> </p>
              <p> <span>{{$t('header.PublishTime')}}</span> <span class="release-time">{{currencyInfo.release_time}}</span> </p>
              <p> <span>{{$t('tradecenter.TotalIssue')}}</span> <span class="release-amount">{{currencyInfo.release_amount}}</span> </p>
              <p> <span>{{$t('header.PublishPrice')}}</span> <span class="release-price">{{currencyInfo.release_price}}</span> </p>
              <p> <span>{{$t('tradecenter.Website')}}</span> <span class="official-website"><a :href="currencyInfo.site" style="color: #196bdf">{{$t('home.ClickView')}}</a></span> </p>
              <p> <span>{{$t('tradecenter.WhiteBook')}}</span> <span class="white-paper"><a :href="currencyInfo.white_paper" style="color: #196bdf">{{$t('home.ClickView')}}</a></span> </p>
              <p> <span>{{$t('tradecenter.BlockTheQuery')}}</span> <span class="block-search"><a :href="currencyInfo.block_search" style="color: #196bdf">{{$t('home.ClickView')}}</a></span> </p>
              <p> <span>{{$t('tradecenter.IntroductionCurrency')}}</span> </p>
            </div>
        <p class="desc" v-html="currencyInfo.introduce"></p>
      </div>
    </div>
  </div>
</template>
<script>
import {getCurrencyInfo } from '@/api/tradecenter'
import {getLang} from '../../libs/utils'
export default {
  props:["baseCurrencyName"],
  data(){
    return{
      currencyInfo:'',
      lang: 'zh'
    }
  },
  created () {
    this.lang = getLang();
    this.getCoinInfo(this.hashsymbol)
  },
  watch:{
    baseCurrencyName(){
      this.getCoinInfo()
    },
    lang() {
      this.getCoinInfo()
    }
  },
  methods:{
    getCoinInfo () {
      getCurrencyInfo(this.baseCurrencyName||'ETH').then((response) => {
        let currencyInfo = response.data
        this.currencyInfo = currencyInfo
        this.currencyInfo.introduce = ''
        if(this.lang == 'zh'){
          this.currencyInfo.introduce = currencyInfo.introduce_cn
        }else if(this.lang == 'en'){
          this.currencyInfo.introduce = currencyInfo.introduce_en
        }else if(this.lang == 'ko'){
          this.currencyInfo.introduce = currencyInfo.introduce_ko
        }else if(this.lang == 'ja'){
          this.currencyInfo.introduce = currencyInfo.introduce_jp
        }
      })
    },
  }
}
</script>
