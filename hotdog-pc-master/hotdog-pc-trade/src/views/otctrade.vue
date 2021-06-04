<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="otctrade-panel">
        <div class="otctrade-main f-cb">
          <div class="otctrade-left f-fl">

            <div class="order f-cb">
              <div class="order-client f-fl" id="client">
                <span>{{$t('otc.Merchants')}}</span>
                <span>{{currentOrder.owner_name}}</span>
              </div>
            </div>

            <div class="price" id="price">
              <div class="price-large">{{type == 1 ? $t('tradecenter.Buys') : $t('tradecenter.Sells')}}ZC{{$t('otc.OrderTotalAmount')}}
                <em>{{currentOrder.amount}}</em>
                CNY
              </div>
              <div class="price-unit">{{$t('otc.Pricess')}}<em>{{currentOrder.price.toFixed(2)}}</em> CNY</div>
              <div class="price-number">{{$t('otc.Amountss')}}<em>{{currentOrder.volume.toFixed(2)}}</em> ZC</div>
            </div>

            <div class="method">
              <span>{{$t('otc.MyPaymentMethod')}}</span>
              <div class="payment" id="payment">
                <img class="qrcode" :src="qrcode" alt="" v-show="qrcodeShow">
                <div class="method-pay method-visa f-cb" id="bank" v-if="bankShow">
                  <i class="icon f-fl"></i>
                  <span class="way f-fl">{{$t('otc.BankCard')}}</span>
                  <span class="name f-fl">{{bankInfo[0]}}</span>
                  <input class="account f-fl" :value="bankInfo[1]" readonly>
                  <span class="f-fl">{{bankInfo[2]}} {{bankInfo[3]}}</span>
                  <i class="iconfont icon-fuzhi"  v-clipboard:copy="bankInfo[1]" v-clipboard:success="onCopy" v-clipboard:error="onError"></i>
                </div>

                <div class="method-pay method-weixin f-cb" id="wxpay" v-if="wxpayShow">
                  <i class="icon f-fl"></i>
                  <span class="way f-fl">{{$t('otc.Wechat')}}</span>
                  <span class="name f-fl">{{wxpayInfo[0]}}</span>
                  <input class="account f-fl" :value="wxpayInfo[1]" readonly>
                  <i class="iconfont icon-weixin-copy" @mousemove="mousemove" @mouseout="mouseout"></i>
                </div>

                <div class="method-pay method-zhifubao f-cb" id="alipay" v-if="alipayShow">
                  <i class="icon f-fl"></i>
                  <span class="way f-fl">{{$t('otc.Alipay')}}</span>
                  <span class="name f-fl">{{alipayInfo[0]}}</span>
                  <input class="account f-fl" :value="alipayInfo[1]" readonly>
                  <i class="iconfont icon-zhifubao" @mousemove="mousemove" @mouseout="mouseout"></i>
                </div>
              </div>
            </div>

            <div class="state" id="state">
              <span>{{$t('otc.PleaseIntrovertWithin24Hours')}}{{currentOrder.owner_name}}{{$t('otc.Pay')}}<em>{{currentOrder.amount}}</em> CNY</span>
              <button type="button" @click="payBtnClick">{{$t('otc.IHaveThePayment')}}</button>
            </div>

          </div>
          <div class="otctrade-right f-fr">
            <div class="tips">
              <div class="title">{{$t('otc.Attention')}}</div>
              <div class="lists">
                <ul>
                  <li>{{$t('otc.IfMoreThan')}}</li>
                  <li>{{$t('otc.PleaseConfirmThe')}}/li>
                  <li>{{$t('otc.DoNotUseBank')}}</li>
                  <li>{{$t('otc.IfTheTransaction')}}</li>
                  <li>{{$t('otc.PleaseDoNotNote')}}</li>
                  <li>{{$t('otc.WhenThereIsOne')}}</li>
                </ul>
              </div>
              <div class="qrcode f-cb">
                <img class="f-fl tran" src="../assets/images/qrcode2.png" >
                <span class="f-fr">{{$t('otc.IfYouEncounter')}}</span>
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
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import { getOtcorders, getOtcAds, confirmorder } from '@/api/otctrade'
export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      id: '',
      type: '',
      click: false,
      currentOrder: '',
      bankInfo: [],
      bankShow: false,
      wxpayInfo: [],
      wxpayShow: false,
      alipayInfo:[],
      alipayShow:false,
      qrcode: require('../assets/images/qrcode2.png'),
      qrcodeShow: false,
    }
  },
  mounted () {
    this.id = this.$route.params.id
    this.type = this.$route.params.ad_type
    getOtcorders('ZC', 'CNY', '1', '50', this.type, 0).then(res => {
      this.isclick = true
      let orders = res.orders
      let currentOrderTemp = orders.filter(i => i.id === this.id)
      let currentOrder = currentOrderTemp[0]
      this.currentOrder = currentOrder
      getOtcAds(this.currentOrder.ad_id).then(res => {
        if (res.state === 1) {
          let ad = res.ad
          if (ad.bank_info) {
            this.bankShow = true
            let bankInfo = ad.bank_info.split('▲')
            this.bankInfo = bankInfo
          }
          if (ad.wxpay_info) {
            this.wxpayShow = true
            let wxpayInfo = ad.wxpay_info.split('▲')
            this.qrcode = wxpayInfo[2]
            this.wxpayInfo = wxpayInfo
          }
          if (ad.alipay_info) {
            this.alipayShow = true
            let alipayInfo = ad.alipay_info.split('▲')
            this.qrcode = alipayInfo[2]
            this.alipayInfo = alipayInfo
          }
        }
      })
    })
  },
  methods: {
    mousemove () {
      this.qrcodeShow = true
    },
    mouseout () {
      this.qrcodeShow = false
    },
    payBtnClick () {
      if (this.isclick) {
        this.isclick = false
        var data = {
          id: this.id
        }
        confirmorder(data).then(response => {
          if (response.state === 1) {
            this.$message({
              type: 'success',
              message: this.$t('otc.PaymentSuccessful'),
              duration: 3000,
              showClose: true
            })
            this.$router.push('/otcmyorder')
          }
        })
        setTimeout(() => {
          this.isclick = true
        }, 3000)
      }
    },
    onCopy () {
      this.$message({
        type: 'success',
        message: this.$t('header.CopySuccess'),
        duration: 3000,
        showClose: true
      })
    },
    onError () {
      this.$message({
        type: 'error',
        message: this.$t('header.CopyFail'),
        duration: 3000,
        showClose: true
      })
    }
  }
}
</script>
<style scoped>
.tran{
  translate: .6s
}
</style>
