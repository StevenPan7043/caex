<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="otcmerchant-panel container">

        <div class="inner-otcmerchant-panel">
          <div class="header">
            <p class="title">{{$t('otc.BusinessApplication')}}</p>
            <p class="title-desc">{{$t('otc.BecomeAnAcceptor')}}</p>
          </div>
          <div class="main">
            <div class="box advertise">
              <i></i>
              <div class="theme">{{$t('otc.Advertising')}}</div>
              <p>{{$t('otc.BusinessesAreFree')}}</p>
            </div>
            <div class="box free">
              <i></i>
              <div class="theme">{{$t('otc.FreeOfCharge')}}</div>
              <p>{{$t('otc.AddZZEXAcceptor')}}</p>
            </div>
            <div class="box one-to-one">
              <i></i>
              <div class="theme">{{$t('otc.One_to_oneService')}}</div>
              <p>{{$t('otc.WeWillProvide')}}</p>
            </div>
          </div>
          <div class="apply-now">
            <input type="checkbox" v-model="check"><span>{{$t('otc.IHaveReadAndAgreed')}} <a href="#/" @click.prevent="toAgreement">{{$t('otc.AcceptorAgreement')}}</a> </span>
            <button @click="applyBtnClick">{{$t('otc.ApplyImmediately')}}</button>
          </div>
        </div>

      </div>

      <div class="otcmerchant-dialog" id="otcMerDialog" v-show="otcDialogShow">
        <div class="dialog-content">
          <img class="close_icon f-fr fa fa-times"  @click="closeAsDialog" src="../assets/icons/close.png" alt="">
          <div class="inner" id="inner">
            <div class="title">{{$t('otc.Application')}}</div>
            <div class="sub-title">{{$t('otc.PleaseAddExclusive')}}</div>
            <img src="../assets/images/qrcode2.png" alt="">
            <div class="desc">{{$t('otc.MerchantCertification')}}</div>
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

export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      check: false,
      otcDialogShow: false
    }
  },
  mounted () {},
  methods: {
    applyBtnClick () {
      // if (!this.check) {
      //   this.$message({
      //     type: 'error',
      //     message: '请先阅读并同意协议',
      //     duration: 3000,
      //     showClose: true
      //   })
      //   return
      // }
      this.otcDialogShow = true
    },
    toAgreement () {},
    closeAsDialog () {
      this.otcDialogShow = false
    }
  }
}
</script>
<style scoped>
.close_icon{
  width:30px !important;
  height: 30px !important;
}
</style>
