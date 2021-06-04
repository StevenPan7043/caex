<template>
  <div>
    <my-header :selectHeaderTab="selectHeaderTab"></my-header>
    <div id="MainView">
      <div class="otc-panel container" id="otcPanel">

        <div class="otc-header">
          <div class="otc-header-inner f-cb">
            <div class="left-btns f-fl">
              <button type="button" class="buy" :class="selectTab===0?'cur':''" @click="tabClick(0)">
                {{$t('otc.BuyZC')}}
              </button>
              <button type="button" class="sell" :class="selectTab===1?'cur':''"  @click="tabClick(1)">
                {{$t('otc.SellZC')}}
              </button>
            </div>
            <div class="right-btns f-fr">
              <div class="list-header settings"  @click="rightBtnClick(0)">
                <span><i></i>
                  {{$t('otc.ReceivingSet')}}
                </span>
              </div>
              <div class="list-header merchants"  @click="rightBtnClick(1)">
                <span><i></i>
                  {{$t('otc.ToApplyForBusiness')}}
                </span>
              </div>
              <div class="list-header orders" @click="rightBtnClick(2)">
                <span><i></i>
                  {{$t('otc.MyOrders')}}
                </span>
              </div>
            </div>
          </div>
        </div>
        <div class="otc-main">
          <table class="list-main cur" cellspacing=0>
            <thead>
              <tr>
                <th>{{$t('otc.Acceptance')}}</th>
                <th>{{$t('otc.MonovalentCNY')}}</th>
                <th>{{$t('otc.NumCNY')}}</th>
                <th>{{$t('otc.DoneCNY')}}</th>
                <th>{{$t('otc.TradingLimits')}}</th>
                <th>{{$t('otc.PayMent')}}</th>
                <th>{{$t('otc.Operation')}}</th>
              </tr>
            </thead>
            <tbody id="buyin">
              <tr v-for="(item,index) in ads" :key="index">
                <td>{{item.o_name}}</td>
                <td>{{item.price}}</td>
                <td>{{item.max_quote}}</td>
                <td>{{item.total_volume}}</td>
                <td>{{item.min_quote + " ~ " + item.max_quote}}</td>
                <td>
                  <i class="zhifubao" data-pay="" v-if="item.alipay_info!==''"></i>
                  <i class="visa" data-pay="bank" v-else-if="item.bank_info!==''"></i>
                  <i class="weixin" data-pay="wxpay" v-else></i>
                </td>
                <td>
                  <button type="button" v-show="item.ad_type===1" @click="buyinClick(item)">{{$t('otc.BuyZC')}}</button>
                  <button type="button" v-show="item.ad_type===2" @click="buyinClick(item)">{{$t('otc.SellZC')}}</button>
                </td>
              </tr>
            </tbody>
          </table>
          <table class="list-main" cellspacing=0>
            <thead>
              <tr>
                <th>{{$t('otc.Acceptance')}}</th>
                <th>{{$t('otc.MonovalentCNY')}}</th>
                <th>{{$t('otc.NumCNY')}}</th>
                <th>{{$t('otc.DoneCNY')}}</th>
                <th>{{$t('otc.TradingLimits')}}</th>
                <th>{{$t('otc.PayMent')}}</th>
                <th>{{$t('otc.Operation')}}</th>
              </tr>
            </thead>
            <tbody id="soldout"></tbody>
          </table>
        </div>
        <div class="otc-tips">
          <div class="list-tips">
            <span>{{$t('otc.TradingRules')}}</span>
            <ul>
              <li v-html="$t('otc.OneOTC')"></li>
              <li v-html="$t('otc.TwoOTC')"></li>
              <li v-html="$t('otc.ThreeOTC')"></li>
              <li v-html="$t('otc.FourOTC')"></li>
              <li v-html="$t('otc.FiveOTC')"></li>
              <li v-html="$t('otc.SixOTC')"></li>
            </ul>
          </div>
        </div>

      </div>

      <div class="otc-dialog" id="otcDialog" v-show="isDialogShow">

        <div class="dialog-content">
          <div class="inner" id="inner">
            <div class="title f-cb">
              <span :class="dialogTitle===$t('otc.BuyZC')?'rise':'fall'">{{dialogTitle}}</span>
              <img class="close_icon f-fr fa fa-times"  @click="closeDialog" src="../assets/icons/close.png" alt="">
            </div>
            <div class="input-wrap price-wrap">
              <label>{{priceWrapText}}</label>
              <span>{{priceWrapPrice}}</span>
            </div>
            <div class="input-wrap number-wrap">
              <label>{{numberWrapText}}</label>
              <input type="text" :placeholder="$t('otc.PleaseEnterQuantity')" v-model="dialogNumber" >
            </div>
            <div class="input-wrap amount-wrap">
              <label>{{amountWrapText}}</label>
              <input type="text" readonly class="amount" v-model="dialogAmount">
            </div>
            <div class="input-wrap pwd-wrap">
              <label>{{$t('otc.Fundpasswords')}}</label>
              <input type="password" :placeholder="$t('otc.PleaseEnterFundPassword')" v-model="diaglogPassword">
            </div>
            <div class="input-wrap pay-limit">
              <label>{{$t('otc.PaymentLimit')}}</label>
              <span>{{payLimitText}}</span>
            </div>
            <div class="input-wrap pay-method">
              <label>{{$t('otc.paymentMethod')}}</label>
              <div class="radio-wrap">
                <span v-if="dialogPayType===0">
                  <input type="radio" class="bank" name="pay" value="bank" id="bank" v-model="dialogPayPicked">
                  {{$t('otc.BankCard')}}
                </span>
                <span v-if="dialogPayType===1">
                  <input type="radio" class="alipay" name="pay" value="alipay" id="alipay" v-model="dialogPayPicked">{{$t('otc.Alipay')}}
                </span>
                <span v-if="dialogPayType===2">
                  <input type="radio" class="wxpay" name="pay" value="wxpay" id="wxpay" v-model="dialogPayPicked">{{$t('otc.Wechat')}}
                </span>
              </div>
            </div>
            <div class="tips">{{$t('otc.HintPretend')}}</div>
            <button type="button" class="submit" @click="submit">{{$t('otc.Submit')}}</button>
            <button type="button" class="cancel" @click="closeDialog">{{$t('otc.Close')}}</button>
          </div>
        </div>

      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import { getMember, getOtcAdsList, getIsSetCoinPwd, getOtcAccountInfo, otcAddorder } from '@/api/otc.js'
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      selectHeaderTab:0,
      selectAds: '',
      ads: [],
      buyAds: [],
      saleAds: [],
      selectTab: 0,
      isclick: true,
      isSubmitClick: true,
      isDialogShow: false,
      dialogTitle: '',
      priceWrapText: '',
      priceWrapPrice: '',
      numberWrapText: '',
      amountWrapText: '',
      dialogNumber: '',
      diaglogPassword: '',
      payLimitText: '',
      dialogPayType: '',
      dialogPayPicked: 'bank'
    }
  },
  computed: {
    dialogAmount () {
      let amount = this.priceWrapPrice * parseFloat(this.dialogNumber)
      if (isNaN(amount)) {
        return 0
      } else {
        return amount
      }
    }
  },
  created () {
    this.getOtcAdsList(true)
  },
  methods: {
    rightBtnClick (index) {
      getMember().then(res => {
        if (res.state === 1) {
          switch (index) {
            case 0:
              this.$router.push('/otcsetting')
              break
            case 1:
              this.$router.push('/otcmerchant')
              break
            case 2:
              this.$router.push('/otcmyorder')
              break
            default:
              break
          }
        } else {
          this.$message({
            type: 'error',
            message: this.$t('header.PlaseLogin'),
            duration: 3000,
            showClose: true
          })
        }
      })
    },
    // 获得广告列表
    // 固定BC/CNY 0显示全部 1显示买入 2显示卖出
    // 购买列表
    getOtcAdsList (isInit) {
      getOtcAdsList('ZC', 'CNY', '0', '1', '15').then(res => {
        let ads = res.ads
        let buyAds = []
        let saleAds = []
        ads.forEach(ele => {
          if (ele.ad_type === 1) {
            buyAds.push(ele)
          } else {
            if(ele.alipay_info===''){
              saleAds.push(ele)              
            }

          }
        })
        if (isInit) {
          this.ads = buyAds
        }
        this.buyAds = buyAds
        this.saleAds = saleAds
      })
    },
    tabClick (index) {
      this.selectTab = index
      switch (index) {
        case 0:
          this.ads = this.buyAds
          break
        case 1:
          this.ads = this.saleAds
          break
        default:
          break
      }
    },
    buyinClick (item) {
      this.selectAds = item
      if (this.isclick) {
        this.isclick = false
        getMember().then(res => {
          if (res.state === 1) {
            getIsSetCoinPwd().then(res => {
              if (res.state === 1 && res.data === '1') {
                this.showDialog(item)
              } else {
                this.$message({
                  type: 'error',
                  message: this.$t('header.SetPwd'),
                  duration: 3000,
                  showClose: true
                })
              }
            })
          } else {
            this.$message({
              type: 'error',
              message: this.$t('header.PlaseLogin'),
              duration: 3000,
              showClose: true
            })
          }
        })
        setTimeout(() => {
          this.isclick = true
        }, 3000)
      }
    },
    saleoutClick () {},
    showDialog (item) {
      // 判断是否绑定
      this.isclick = true
      getOtcAccountInfo().then(res => {
        var data = res.data
        if (data.length === 0) {
          this.$message({
            type: 'error',
            message: this.$t('header.SetPaySetting'),
            duration: 3000,
            showClose: true
          })
        }
      })
      this.isDialogShow = true
      this.dialogTitle = item.ad_type === 1 ? this.$t('otc.BuyZC') : this.$t('otc.SellZC')
      this.priceWrapText = item.ad_type === 1 ? this.$t('otc.BuyPrice') :  this.$t('otc.SellPrice')
      this.priceWrapPrice = item.price
      this.numberWrapText = item.ad_type === 1 ?  this.$t('otc.Quantitys') :this.$t('otc.QuantitySold')
      this.amountWrapText = item.ad_type === 1 ? this.$t('otc.BuyTotal') : this.$t('otc.SellTotal')
      // 付款限额
      this.payLimitText = `${item.min_quote} ~ ${item.max_quote}`
      if (item.bank_info !== '') {
        this.dialogPayType = 0
        this.dialogPayPicked = 'bank'
      }
      if (item.alipay_info !== '') {
        this.dialogPayType = 1
        this.dialogPayPicked = 'alipay'
      }
      if (item.wxpay_info !== '') {
        this.dialogPayType = 2
        this.dialogPayPicked = 'wxpay'
      }
    },
    closeDialog () {
      this.isDialogShow = false
      this.dialogNumber = ''
      this.diaglogPassword = ''
    },
    submit () {
      if (this.isSubmitClick) {
        this.isSubmitClick = false
        if ((this.dialogNumber.trim() !== '') && (this.diaglogPassword.trim() !== '') && this.dialogPayPicked) {
          var data = {
            ad_type: this.selectAds.ad_type,
            volume: this.dialogNumber,
            ads_id: this.selectAds.id,
            sec_pwd: this.diaglogPassword,
            account_types: this.dialogPayPicked
          }
          otcAddorder(data).then(res => {
            if (res.state === 1) {
              this.$message({
                type: 'success',
                message: this.selectAds.ad_type === 1 ? this.$t('header.BuySuccess') : this.$t('header.SellSuccess'),
                duration: 3000,
                showClose: true
              })
              this.$router.push('/otcmyorder')
            } else if (res.state === -1) {
              this.closeDialog()
            }
          })
        } else {
          this.$message({
            type: 'error',
            message: this.$t('header.IncompletePurchaseInformation'),
            duration: 3000,
            showClose: true
          })
        }

        setTimeout(() => {
          this.isSubmitClick = true
        }, 5000)
      }
    }
  }
}
</script>
<style scoped>
.close_icon{
  width:30px;
  height: 30px;
}
</style>
