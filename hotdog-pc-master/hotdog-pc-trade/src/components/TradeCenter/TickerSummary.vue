<template>
  <div class="mini-ticker" id="miniTicker" v-loading="loading" element-loading-background="rgba(138, 134, 134, 0.2)">
    <div class="mt-header f-cb">
      <span class="uppercase f-fl" :class="selectTab===3?'cur':''" @click="tabClick(3)">
        <i class="iconfont icon-androidstar"></i>
        {{$t('home.Favofites')}}
      </span>
      <span class="uppercase f-fl" :class="selectTab===0?'cur':''" @click="tabClick(0)">usdt</span>
      <span class="uppercase f-fl" :class="selectTab===1?'cur':''" @click="tabClick(1)">eth</span>
      <span class="uppercase f-fl" :class="selectTab===2?'cur':''" @click="tabClick(2)">btc</span>
    </div>
    <div class="mt-search-wrap">
      <i class="iconfont icon-search"></i>
      <input class="uppercase" type="text" :placeholder="$t('tradecenter.SearchTransactionPair')" v-model="searchText"
             @input="searchChange">
    </div>
    <div class="mt-data-wrap">
      <div class="mt-sort-wrap sort-icon-wrap media-coin-unit">
        <span>{{$t('tradecenter.TradingOn')}}</span>
        <span>{{$t('home.LatestPrices')}}</span>
        <span>{{$t('home.TopGainers')}}</span>
      </div>
      <div class="mt-data-main" id="miniTickerList">
        <div class="cur" id="miniZCTicker">
          <dl class="f-cb" :class="item.dspName === IDindex?'showList':''" @click="toUserTicker(item,index)"
              v-for="(item,index) in areaTicker" :key="index">
            <dt></dt>
            <dd>
              <div class="coin-unit media-coin-unit">
                <span>
                  <i class="iconfont icon-androidstar light" v-show="!item.position"
                     @click.stop="starClick(item,index)"></i>
                  <i class="iconfont icon-androidstar dart" v-show="item.position"
                     @click.stop="starClick(item,index)"></i>
                  <em class="base-currency"> {{' ' + item.baseCurrencyName}}</em>
                </span>
                <span class="price" v-if="trade.length===0">{{item.close.toFixed(item.pricePrecision)}}</span>
                <span class="price" v-else>{{item.dspName.replace('/','').toUpperCase()===selectTicker.toUpperCase()?trade[0].price.toFixed(item.pricePrecision):item.close.toFixed(item.pricePrecision)}}</span>
                <span class="rate rise" v-if="item.tickerRiseFallPercent>0 && item.pStatus != 3">{{item.tickerRiseFallPercent|isNaNFilter}}%</span>
                <span class="rate fall" v-else-if="item.tickerRiseFallPercent<=0 && item.pStatus != 3">{{item.tickerRiseFallPercent|isNaNFilter}}%</span>
                <span class="rate rise" v-else-if="item.pStatus == 3">即将上线</span>
              </div>
            </dd>
          </dl>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {setTrade} from '@/libs/utils'

export default {
  props: {
    allTicker1: Array,
    allTicker2: Array,
    allTicker3: Array,
    userFavoriteArr: Array,
    trade: Array,
    selectTicker: String
  },
  watch: {
    allTicker1 () {
      if (this.selectTab === 0) {
        this.areaTicker = this.allTicker1.filter(item => item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !== -1)
        this.loadingshow()
      }
    },
    allTicker2 () {
      if (this.selectTab === 1) {
        this.areaTicker = this.allTicker2.filter(item => item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !== -1)
        this.loadingshow()
      }
    },
    allTicker3 () {
      if (this.selectTab === 2) {
        this.areaTicker = this.allTicker3.filter(item => item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !== -1)
        this.loadingshow()
      }
    },
    userFavoriteArr () {
      if (this.selectTab === 3) {
        this.ownerUserFavoriteArr = this.userFavoriteArr.filter(item => item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !== -1)
        this.loadingshow()
      }
    },
  },
  data () {
    return {
      selectTab: 0,
      searchText: '',
      areaTicker: [],
      loading: true,
      IDindex: ''
    }
  },
  filters: {
    isNaNFilter (val) {
      if (isNaN(val)) {
        return 0
      } else {
        return val
      }
    }
  },
  mounted () {
    this.areaTicker = this.allTicker1
    const selectSymbol = localStorage.getItem('selectSymbol')
    this.IDindex = selectSymbol ? selectSymbol : 'ETH/USDT'

    const selectTab = parseInt(localStorage.getItem("selectTab"))
    if (selectTab){
      this.tabClick(selectTab)
    }else{
      this.tabClick(0)
    }
  },
  methods: {
    loadingshow () {
      this.loading = false
    },
    searchChange () {
      this.tabClick(this.selectTab)
    },
    tabClick (index) {
      this.selectTab = index
      localStorage.setItem('selectTab', index)
      let selectTabTicker = []
      switch (index) {
        case 0:
          selectTabTicker = this.allTicker1
          break
        case 1:
          selectTabTicker = this.allTicker2
          break
        case 2:
          selectTabTicker = this.allTicker3
          break
        default:
          selectTabTicker = this.userFavoriteArr
          break
      }
      this.areaTicker = selectTabTicker.filter(item => item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !== -1)
      // this.loading = false
    },
    toUserTicker (item) {
      if (item.pStatus == 1) {
        this.IDindex = item.dspName
        this.$emit('to-user-ticker', item)
        setTrade(item.symbol.toUpperCase())
        localStorage.setItem('selectSymbol', item.dspName)
      }
    },
    starClick (item) {
      this.$emit('star-click', item)
      if (this.selectTab === 3) {
        setTimeout(() => {
          this.tabClick(3)
        }, 2000)
      }

    }
  }
}
</script>
