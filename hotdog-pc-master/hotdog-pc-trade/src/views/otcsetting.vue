<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="otcsetting-panel container">
        <div class="inner-otcsetting-panel" id="innerOtcSetting">
          <div class="otcsetting-header"><span>{{$t('otc.ReceivingSet')}}</span></div>
          <div class="otcsetting-main">
            <div class="tip">{{$t('otc.ToSetThePayment')}}</div>
            <div class="zhifubao-wrap">
              <div class="wrap-header">
                <i class="zhifubao"></i>{{$t('otc.BindAlipay')}}
                <span class="state rise" v-if="isAliBind">{{$t('otc.IsBinding')}}</span>
                <span class="state fall" v-else>{{$t('otc.NotBound')}}</span>
              </div>
              <div class="zhifubao-input f-cb">
                <div class="input-wrap" id="zhifubaoName">
                  <label>{{$t('otc.RealName')}}</label>
                  <input type="text" :placeholder="$t('otc.PleaseInputTheReal')" v-model="aliForm.name">
                </div>
                <div class="input-wrap" id="zhifubaoAcc">
                  <label>{{$t('otc.AlipayAccount')}}</label>
                  <input type="text" :placeholder="$t('otc.PleaseEnterAlipayAccount')" v-model="aliForm.account">
                </div>
                <div class="input-wrap file-upload">
                  <label>{{$t('otc.ReceiptQrCode')}}</label>
                  <img :src="aliForm.QRPreview" alt="" id="aliQRPreview">
                  <input type="file" id="aliQR" @change="QRChange('ali',$event)">
                </div>
                <div class="input-wrap" id="zhifubaoPas">
                  <label>{{$t('otc.MoneyPassword')}}</label>
                  <input type="password" :placeholder="$t('otc.PleaseEnterFundPassword')" v-model="aliForm.password">
                </div>
                <button type="button" class="confirmBtn" id="zhifubao" @click="aliSubmitClick">{{$t('otc.ConfirmTheBinding')}}</button>
              </div>
            </div>
            <div class="weixin-wrap">
              <div class="wrap-header">
                <i class="weixin"></i>{{$t('otc.WechatAccount')}}
                  <span class="state rise" v-if="isWechatBind">{{$t('otc.IsBinding')}}</span>
                  <span class="state fall" v-else>{{$t('otc.NotBound')}}</span>
              </div>
              <div class="weixin-input f-cb">
                <div class="input-wrap" id="wxName">
                  <label>{{$t('otc.RealName')}}</label>
                  <input type="text" :placeholder="$t('otc.PleaseEnterTheReal')" v-model="wechatForm.name">
                </div>
                <div class="input-wrap" id="wxAcc">
                  <label>{{$t('otc.WechatAccount')}}</label>
                  <input type="text" :placeholder="$t('otc.PleaseEnterYourWeChatAccount')" v-model="wechatForm.account">
                </div>
                <div class="input-wrap file-upload">
                  <label>{{$t('otc.ReceiptQrCode')}}</label>
                  <img :src="wechatForm.QRPreview" alt="" id="wxQRPreview">
                  <input type="file" id="wxQR" @change="QRChange('wechat',$event)">
                </div>
                <div class="input-wrap" id="wxPas">
                  <label>{{$t('otc.MoneyPassword')}}</label>
                  <input type="password" :placeholder="$t('otc.PleaseEnterFundPassword')" v-model="wechatForm.password">
                </div>
                <button type="button" class="confirmBtn" id="weixin" @click="wechatSubmitClick">{{$t('otc.ConfirmTheBinding')}}</button>
              </div>
            </div>
            <div class="visa-wrap">
              <div class="wrap-header">
                <i class="visa"></i>{{$t('otc.BindBankCard')}}
                  <span class="state rise" v-if="isBankBind">{{$t('otc.IsBinding')}}</span>
                  <span class="state fall" v-else>{{$t('otc.NotBound')}}</span>
              </div>
              <div class="visa-input f-cb">
                <div class="input-wrap" id="proName">
                  <label>{{$t('otc.RealName')}}</label>
                  <input type="text" :placeholder="$t('otc.PleaseEnterA')" v-model="bankForm.name">
                </div>
                <div class="input-wrap" id="proAcc">
                  <label>{{$t('otc.BankCardAccount')}}</label>
                  <input type="text" :placeholder="$t('otc.PleaseEnterYourBankCardAccount')" v-model="bankForm.account">
                </div>
                <div class="input-wrap" id="proFaName">
                  <label>{{$t('otc.BankName')}}</label>
                  <input type="text" :placeholder="$t('otc.PleaseEnterDetailedBankInformation')" v-model="bankForm.bank">
                </div>
                <div class="input-wrap" id="proPas">
                  <label>{{$t('otc.MoneyPassword')}}</label>
                  <input type="password" :placeholder="$t('otc.PleaseEnterFundPassword')" v-model="bankForm.password">
                </div>
                <button type="button" class="confirmBtn" id="pro" @click="bankBtnClick">{{$t('otc.ConfirmTheBinding')}}</button>
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
import { getOtcAccountInfo, getAliOssPolicy, postImg, otcAccountInfo } from '@/api/otcsetting'
export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      isclick: true,
      aliForm: {
        name: '',
        account: '',
        password: '',
        QRPreview: require('../assets/images/otc-add5.png')
      },
      wechatForm: {
        name: '',
        account: '',
        password: '',
        QRPreview: require('../assets/images/otc-add5.png')
      },
      bankForm: {
        name: '',
        account: '',
        bank: '',
        password: ''
      },
      isAliBind: false,
      isWechatBind: false,
      isBankBind: false
    }
  },
  created () {
    getOtcAccountInfo().then(res => {
      if (res.state === 1) {
        let data = res.data
        let payArr = []
        let payObj = {}
        for (let i = 0; i < data.length; i++) {
          payArr.push(data[i].a_type)
          payObj[data[i].a_type] = data[i]
        }
        if (payArr.indexOf('bank') >= 0) {
          this.isBankBind = true
          this.bankForm = {
            name: payObj['bank'].a_name,
            account: payObj['bank'].a_account,
            password: '',
            bank: payObj['bank'].a_bank_or_img
          }
        } else {
          this.isBankBind = false
        }

        if (payArr.indexOf('alipay') >= 0) {
          this.isAliBind = true
          this.aliForm = {
            name: payObj['alipay'].a_name,
            account: payObj['alipay'].a_account,
            password: '',
            QRPreview: payObj['alipay'].a_bank_or_img
          }
        } else {
          this.isAliBind = false
        }

        if (payArr.indexOf('wxpay') >= 0) {
          this.isWechatBind = true
          this.wechatForm = {
            name: payObj['wxpay'].a_name,
            account: payObj['wxpay'].a_account,
            password: '',
            QRPreview: payObj['wxpay'].a_bank_or_img
          }
        } else {
          this.isWechatBind = false
        }
      } else if (res.state === -1) {
        if (res.msg === 'LANG_NO_LOGIN') {
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
    })
  },
  mounted () {},
  methods: {
    aliSubmitClick () {
      if (this.isclick) {
        this.isclick = false
        var data = {
          sec_pwd: this.aliForm.password,
          a_type: 'alipay',
          a_name: this.aliForm.name,
          a_account: this.aliForm.account,
          a_bank_or_img: this.aliForm.QRPreview
        }
        otcAccountInfo(data).then(res => {
          if (res.state === 1) {
            this.$message({
              type: 'success',
              message: this.$t('otc.BindingSuccess'),
              duration: 3000,
              showClose: true
            })
            this.isAliBind = true
          }
        })
        this.aliForm.password = ''
        setTimeout(() => {
          this.isclick = true
        }, 3000)
      }
    },
    wechatSubmitClick () {
      if (this.isclick) {
        this.isclick = false
        var data = {
          sec_pwd: this.wechatForm.password,
          a_type: 'wxpay',
          a_name: this.wechatForm.name,
          a_account: this.wechatForm.account,
          a_bank_or_img: this.wechatForm.QRPreview
        }
        otcAccountInfo(data).then(res => {
          if (res.state === 1) {
            this.$message({
              type: 'success',
              message: this.$t('otc.BindingSuccess'),
              duration: 3000,
              showClose: true
            })
            this.isWechatBind = true
          }
        })
        this.wechatForm.password = ''
        setTimeout(() => {
          this.isclick = true
        }, 3000)
      }
    },
    bankBtnClick () {
      if (this.isclick) {
        this.isclick = false
        var data = {
          sec_pwd: this.bankForm.password,
          a_type: 'bank',
          a_name: this.bankForm.name,
          a_account: this.bankForm.account,
          a_bank_or_img: this.bankForm.bank
        }
        otcAccountInfo(data).then(res => {
          if (res.state === 1) {
            this.$message({
              type: 'success',
              message: this.$t('otc.BindingSuccess'),
              duration: 3000,
              showClose: true
            })
            this.isBankBind = true
          }
        })
        this.bankForm.password = ''
        setTimeout(() => {
          this.isclick = true
        }, 3000)
      }
    },
    userLogout () {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    },
    QRChange (payType, e) {
      let date = Date.parse(new Date())
      getAliOssPolicy().then(res => {
        if (res.state === 1) {
          let data = res.data
          let file = e.target.files[0]
          let imagesType = ['image/jpeg', 'image/png']
          let isexists = imagesType.includes(file.type)
          if (!isexists) {
            this.$message({
              type: 'error',
              message: this.$t('otc.FileType'),
              duration: 3000,
              showClose: true
            })
            file = ''
            return
          }
          if (file.size > 5 * 1024 * 1024) {
            this.$message({
              type: 'error',
              message: this.$t('otc.FileSize'),
              duration: 3000,
              showClose: true
            })
            file = ''
            return
          }
          const timestamp = Date.parse(new Date())/1000;
          let useruid = JSON.parse(sessionStorage.getItem("useruid"));
          file.name='web'+timestamp+useruid+'01'
          // 封装成 formData
          let formData = new FormData()
          formData.append('OSSAccessKeyId', data.accessid)
          formData.append('policy', data.policy)
          formData.append('Signature', data.signature)
          formData.append('key', data.dir + useruid + file.name + date)
          formData.append('success_action_status', '200')
          formData.append('file', file)
          // 上传
          postImg(data.host, formData).then(res => {
            let url = data.host + '/' + data.dir + useruid + file.name + date
            if (payType === 'ali') {
              this.aliForm.QRPreview = url
            } else {
              this.wechatForm.QRPreview = url
            }
          })
        }
      })
    }
  }
}
</script>
