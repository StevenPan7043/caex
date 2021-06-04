<template>
  <div class="panel buy-in f-fl">
    <div class="inner-panel">
      <div class="can-use f-cb" id="buyInCanUse" v-if="buyOrSell==='buy'">
        <span class="f-fl">
          {{$t('tradecenter.Available')}}
          <em class="uppercase">{{balance}}</em>
          {{quoteCurrencyName}}
        </span>
        <a
                @click.prevent="toFabi"
                class="f-fr"
                v-if="quoteCurrencyName === 'ZC' && showOtc"
                style="color:#196bdf"
        >{{$t('tradecenter.Deposit')}}</a>
        <span v-else>
          <a
                  class="f-fr"
                  @click.prevent="toRecharge(1)"
                  v-if="state && tickerCanRecharge && showOtc"
                  style="color:#196bdf"
          >{{$t('tradecenter.Deposit')}}</a>
          <span class="f-fr" v-else>{{$t('tradecenter.Deposit')}}</span>
        </span>
      </div>

      <div class="can-use f-cb" id="soldOutCanUse" v-if="buyOrSell!=='buy'">
        <span class="f-fl">
          {{$t('tradecenter.Available')}}
          <em class="uppercase">{{balance}}</em>
          {{baseCurrencyName}}
        </span>
        <a
                @click.prevent="toFabi"
                class="f-fr"
                v-if="baseCurrencyName === 'ZC' && showOtc"
                style="color:#196bdf"
        >{{$t('tradecenter.Deposit')}}</a>
        <span v-else>
          <a
                  class="f-fr"
                  @click.prevent="toRecharge(0)"
                  v-if="state && tickerCanRecharge && showOtc"
                  style="color:#196bdf"
          >{{$t('tradecenter.Deposit')}}</a>
          <span class="f-fr" v-else>{{$t('tradecenter.Deposit')}}</span>
        </span>
      </div>

      <div class="buy-in-price price">
        <span>{{buyOrSell==='buy'?$t('tradecenter.BidPrice'):$t('tradecenter.SellingRate')}}</span>
        <div class="input-wrap">
          <input
                  type="text"
                  maxlength="14"
                  v-model="buyinPrice"
                  @input="buyinPriceChange"
                  :readonly="fixed_buy_price!==0"
          />
          <i>{{quoteCurrencyName}}</i>
          <div class="transfer-cny">
            <span v-if="convertedval>=0">≈ {{convertedval.toFixed(2)}} CNY</span>
          </div>
        </div>
      </div>
      <div class="buy-in-sum">
        <span>{{buyOrSell==='buy'?$t('tradecenter.Purchases'):$t('tradecenter.SellQuantity')}}</span>
        <div class="input-wrap">
          <input type="text" maxlength="14" v-model="buyinNum" @input="buyinNumChange"/>
          <i>{{baseCurrencyName}}</i>
        </div>
      </div>
      <div class="input-range">
        <el-slider
                :step="25"
                show-stops
                v-model="sliderRange"
                :format-tooltip="formatTooltip"
                @change="sliderRangeChange"
        ></el-slider>
        <!-- <div class="point"></div> -->
      </div>
      <!--<div class="amount_range uppercase f-cb">-->
      <!--<span class="min f-fl">-->
      <!--<span class="min_num">0</span>-->
      <!--<em>{{baseCurrencyName}}</em>-->
      <!--</span>-->
      <!--<span class="max f-fr">-->
      <!--<span class="max_num">-->
      <!--{{buyMaxNum}}-->
      <!--</span>-->
      <!--<em>{{baseCurrencyName}}</em>-->
      <!--</span>-->
      <!--</div>-->
      <div class="trade-total">
        <span>
          <span style="color:#5a7896">{{$t('tradecenter.TradingVolume')}}</span>
          <em>{{buyTradeTotal}}</em>
          <i class="uppercase">{{quoteCurrencyName}}</i>
        </span>
      </div>
      <button
              class="btn"
              :class="buyOrSell==='buy'?'buy-in-button':' sold-out-button'"
              id="buyInBtn"
              @click="buyinBtnClick"
              :disabled="buyinBtnDisable"
      >
        <span class="text">
          {{buyOrSell==='buy'? $t('tradecenter.Buys') : $t('tradecenter.Sells') }}
          <i>{{baseCurrencyName}}</i>
        </span>
        <span class="loading" v-show="buyinBtnDisable"></span>
      </button>
      <div class="innerkun"></div>
    </div>
  </div>
</template>
<script>
import {orderCreate, getLimit} from '@/api/tradecenter.js'
import {getSSOToken} from '@/api/login.js'
import {jumpUrl} from '../../../public/config.js'

export default {
  props: [
    'hashsymbol',
    'quoteCurrencyName',
    'baseCurrencyName',
    'tickerCanRecharge',
    'saleoutBalance',
    'balance',
    'currentPricePrecision',
    'ethzcTickerClose',
    'usdtzcTickerClose',
    'currentVolumePrecision',
    'symbol',
    'buyOrSell',
    'soldOutPosition',
    'buyinPriceProps',
    'fixed_buy_price',
    'UsdtPrice',
    'BTCPrice',
    'ETHPrice',
  ],
  data () {
    return {
      sliderRange: 0,
      state: '',
      buyinPrice: '',
      buyinNum: '',
      buyMaxNum: '0.0000',
      buyin: {
        price: ''
      },
      sell: {
        price: ''
      },
      convertedval: -1,
      drag_bar_position: '',
      buyInInputRange: '',
      left: 0,
      candrag: false,
      ox: '',
      buyinBtnDisable: false,
      showOtc: true,
      limitPrice: null,
    }
  },
  watch: {
    buyinPriceProps () {
      this.buyinPrice = this.buyinPriceProps
      this.buyinPriceChange()
    },
    hashsymbol () {
      this.buyinPrice = ''
      this.buyinNum = ''
      this.sliderRange = 0
      this.buyMaxNum = parseFloat(0).toFixed(this.currentVolumePrecision)
      this.convertedval = -1
      setTimeout(() => {
        let localprice = JSON.parse(localStorage.getItem('fixed_buy_price'))
        if (localprice) {
          // console.log(this.hashsymbol + '=' + localprice)
          this.buyinPrice = localprice
        } else {
          this.buyinPrice = ''
        }
      }, 1000)
    }
  },
  created () {
    this.showOtc = JSON.parse(localStorage.getItem('showOtc'))
    this.state = JSON.parse(localStorage.getItem('loginState'))
    this.buyMaxNum = parseFloat(0).toFixed(this.currentVolumePrecision)
    setTimeout(() => {
      let localprice = JSON.parse(localStorage.getItem('fixed_buy_price'))
      if (localprice) {
        // console.log(this.hashsymbol + '=' + localprice)
        this.buyinPrice = localprice
      }
    }, 1000)
  },
  mounted () {
  },
  computed: {
    buyTradeTotal () {
      let pow = Math.pow(10, this.currentPricePrecision)
      return (Math.floor(this.buyinNum * this.buyinPrice * pow) / pow).toFixed(
          this.currentPricePrecision
      )
    }
  },
  methods: {
    async getLimit () {
      var res = await getLimit(this.symbol)
      if (res.data[this.symbol] === null) {
        return true
      } else {
        const limitData = res.data[this.symbol].highPrice
        this.limitPrice = limitData
        if (this.buyinPrice > limitData) {
          return false
        } else {
          return true
        }
      }
    },
    toFabi () {
      getSSOToken().then(({data, state, msg}) => {
        if (state === 1) {
          window.location.href = `${jumpUrl}home?&test=test&api_key=${data.api_key}&sign=${data.sign}&timestamp=${data.timestamp}`
        } else {
          window.location.href = jumpUrl
        }
      })
    },
    formatTooltip (val) {
      return val + '%'
    },
    buyinPriceChange () {
      this.buyinPrice = this.inputNumberFilter(
          this.buyinPrice,
          this.currentPricePrecision,
          '',
          false
      )
      let close_ethzc = parseFloat(this.ETHPrice)
      let close_usdtzc = parseFloat(this.UsdtPrice)
      let close_btczc = parseFloat(this.BTCPrice)
      let balance = parseFloat(this.balance)
      let inputVal = parseFloat(this.buyinPrice)
      let volumeVal = this.buyinNum
      let convertedval
      if (this.quoteCurrencyName == 'BTC') {
        convertedval = parseFloat(inputVal * close_btczc)
      } else if (this.quoteCurrencyName == 'ETH') {
        convertedval = parseFloat(inputVal * close_ethzc)
      } else if (this.quoteCurrencyName == 'USDT') {
        console.log(close_usdtzc)
        convertedval = parseFloat(inputVal * close_usdtzc)
      }
      this.convertedval = convertedval
      if (inputVal && this.state) {
        let cutnum = this.cutNum(
            balance / inputVal,
            this.currentVolumePrecision
        )
        this.buyMaxNum = cutnum
        if (volumeVal) {
          let rightVal = parseFloat(this.buyMaxNum)
          let divideVal = volumeVal / rightVal
          if (divideVal > 1) {
            this.sliderRange = 1 * 100
          } else {
            this.sliderRange = divideVal * 100
          }
        }
      } else {
        this.buyMaxNum = parseFloat(0).toFixed(this.currentVolumePrecision)
      }
    },
    buyinNumChange () {
      this.buyinPriceChange()
      this.buyinNum = this.inputNumberFilter(
          this.buyinNum,
          '',
          this.currentVolumePrecision,
          true
      )
      let inputVal = this.buyinNum
      let priceVal = this.buyinPrice
      let rightVal = parseFloat(this.buyMaxNum)
      if (inputVal && priceVal && rightVal > 0) {
        let divideVal = inputVal / rightVal
        if (divideVal > 1) {
          this.sliderRange = 100
        } else {
          this.sliderRange = divideVal * 100
        }
      } else {
        this.sliderRange = 0
      }
    },
    sliderRangeChange (e) {
      let pow = Math.pow(10, this.currentVolumePrecision)
      this.buyinNum = (
          Math.floor(((this.buyMaxNum * e) / 100) * pow) / pow
      ).toFixed(this.currentVolumePrecision)
    },
    cutNum (num, len) {
      let numStr = num.toString()
      if (len == null || len == undefined) {
        len = numStr.length
      }
      let index = numStr.indexOf('.')
      if (index == -1) {
        index = numStr.length
        numStr += '.0000000000000'
      } else {
        numStr += '0000000000000'
      }
      let newNum = numStr.substring(0, index + len + 1)
      return newNum
    },
    toRecharge (index) {
      switch (index) {
        case 0:
          this.$router.push({
            name: 'Recharge',
            params: {currency: this.baseCurrencyName}
          })
          break
        case 1:
          this.$router.push({
            name: 'Recharge',
            params: {currency: this.quoteCurrencyName}
          })
          break
        default:
          break
      }
    },
    inputNumberFilter (obj, pricePrecision, volumePrecision, isNum) {
      let reg
      if (pricePrecision !== '') {
        reg = new RegExp('([0-9]+\\.[0-9]{' + pricePrecision + '})[0-9]*')
      }
      if (volumePrecision !== '') {
        reg = new RegExp('([0-9]+\\.[0-9]{' + volumePrecision + '})[0-9]*')
      }
      obj = obj.replace(/[^\d\.]/g, '') // 先把非数字的都替换掉，除了数字和.
      obj = obj.replace(/^\./g, '') // 必须保证第一个为数字而不是.
      obj = obj.replace(/\.{2,}/g, '.') // 保证只有出现一个.而没有多个.
      obj = obj
          .replace('.', '$#$')
          .replace(/\./g, '')
          .replace('$#$', '.') // 保证.只出现一次，而不能出现两次以上
      obj = obj.replace(reg, '$1') // 保证精度
      if (isNum) {
        if (parseFloat(obj) > parseFloat(this.buyMaxNum)) {
          return this.buyMaxNum
        }
      }
      return obj
    },
    async buyinBtnClick () {
      const isLimit = await this.getLimit()
      if (!isLimit) {
        this.$message({
          type: 'error',
          message: `超过当日最大涨幅限价：${this.limitPrice}`,
          duration: 3000,
          showClose: true
        })
        this.buyinPrice = this.limitPrice
        return
      }
      this.sliderRange = 0
      this.buyinBtnDisable = true
      let data = {
        o_price_type: 'limit',
        price: this.buyinPrice,
        source: 'web',
        symbol: this.symbol,
        volume: this.buyinNum
      }
      if (this.buyOrSell === 'buy') {
        data.o_type = 'buy'
      } else {
        data.o_type = 'sell'
      }
      orderCreate(data).then(response => {
        if (response.state == 1) {
          this.buyinBtnDisable = false
          this.$message({
            type: 'success',
            message:
                this.buyOrSell === 'buy'
                    ? this.$t('header.BuySuccess')
                    : this.$t('header.SellSuccess'),
            duration: 3000,
            showClose: true
          })
          this.$emit('buy-or-sell-click')
        } else if (response.state == -1) {
          this.buyinBtnDisable = false
          if (response.msg === 'LANG_NO_LOGIN') {
            this.$message({
              type: 'error',
              message: this.$t('header.PlaseLogin'),
              duration: 3000,
              showClose: true
            })
            setTimeout(() => {
              this.userLogout()
            }, 2000)
          }
        }

        this.sliderRange = 0
        // this.buyinPrice=''
        this.buyinNum = ''
        this.convertedval = -1
        this.buyMaxNum = parseFloat('0').toFixed(this.currentPricePrecision)
      })
    },
    userLogout () {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    }
  }
}
</script>
<style>
  .el-slider__bar {
    background: #29d190 !important;
  }

  .buy-in .el-slider__runway {
    background: rgb(77, 101, 117) !important;
    margin-top: 0px !important;
    margin-bottom: 0px !important;
  }

  /* .point{
    position: absolute;
    height: 6px;
    width: 6px;
    border-radius: 100%;
    background-color:#fff;
    margin-left: 450px;
    position: relative;
    top: -5px;
  } */
  .buy-in .inner-panel {
    position: relative;
  }

  .innerkun {
    position: absolute;
    right: 0;
    top: 50px;
    bottom: 10px;
    width: 0;
    /*height: 200px;*/
    border-right: 1px dashed #2d3e55;
  }
</style>
