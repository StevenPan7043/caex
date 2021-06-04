<template>
  <div>
    <div>
      <my-header></my-header>
      <div id="MainView">
        <div class="page-resetpassword">
          <div class="resetpassword-wrap">
            <div class="inner-form">
              <div class="title">{{$t('resetpassword.ResetLoginPassword')}}</div>
              <div class="input-wrap user_account">
                <span>{{$t('login.MobilePhoneEmail')}}</span>
                <input type="text" :placeholder="$t('register.PleaseEnter')" v-model="user_account" @blur="accountCheck">
              </div>
              <div class="input-tip red"><span v-show="accountTip">{{user_account_Text}}</span></div>
              <div class="input-wrap img-code">
                <span>{{$t('user.PictureVerificationCode')}}</span>
                <input type="text" :placeholder="$t('register.PleaseVerificationCode')" v-model="img_code" @blur="imgCodeCheck">
                <img class="imgCodeBtn f-fr" :src="imgcode" alt="" id="imgCode" @click="getKaptcha">
              </div>
              <div class="input-tip red"><span v-show="imgCodeTip">{{$t('register.ImageVerificationEmpty')}}</span></div>
              <div class="input-wrap mail-code">
                <span>{{$t('user.verificationCode')}}</span>
                <input type="text" :placeholder="$t('user.PleaseEnterTheverification')" id="mailCode" v-model="mail_code" @blur="codeCheck">
                <button class="f-fr" @click="sendCodeClick" :class="sendBtnDisable?'forbidBtn':''">{{sendCodeText}}</button>
              </div>
              <div class="input-tip red"><span v-show="codeTip">{{$t('register.VerificationEmpty')}}</span></div>
              <div class="input-wrap user-password">
                <span>{{$t('user.LoginPassword')}}</span>
                <input type="password" :placeholder="$t('login.PleaseEnterYourLoginPassword')" v-model="user_password" @blur="loginPwdCheck">
              </div>
              <div class="input-tip red"><span v-show="loginPwdTip">{{$t('login.PasswordCannotBeEmpty')}}</span></div>
              <div class="input-wrap confirm-password">
                <span>{{$t('register.ConfirmPassword')}}</span>
                <input type="password" :placeholder="$t('header.EnterPwdAgain')" v-model="confirm_password" @blur="confirmPwdCheck">
              </div>
              <div class="input-tip red"><span v-show="confirmPwdTip">{{confirmPwdTipContent}}</span></div>
              <div class="login-forget" id="loginBtn">
                <button class="confirm-btn" id="confirmBtn" @click="confirmBtnClick">{{$t('resetpassword.Affirm')}}</button>
                <a class="registergo" href="#/login">{{$t('login.logingo')}}</a>
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
import { getKaptcha, getMail, forgotV2 } from '@/api/register'
export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      user_account: '',
      user_account_state: false,
      img_code: '',
      mail_code: '',
      user_password: '',
      confirm_password: '',
      user_account_Text: '',
      accountTip: false,
      imgcode: '',
      tokencode: '',
      imgCodeTip: false,
      codeTip: false,
      loginPwdTip: false,
      confirmPwdTipContent: '',
      confirmPwdTip: false,
      sendCodeText: '发送验证码',
      sendBtnDisable: false,
      second:60,
    }
  },
  mounted () {
    this.getKaptcha()
  },
  methods: {
    sendCodeClick () {
      if(!this.img_code){
        this.$message({
          type: 'error',
          message: this.$t('register.PleaseVerificationCode'),
          duration: 3000,
          showClose: true
        })
        return
      }
      if (this.user_account_state) {
        getMail(this.tokencode, this.img_code, this.user_account, 'forgot', '').then(res => {
          if (res.state === 1) {
            this.countDownBtn()
            this.$message({
              type: 'success',
              message: this.$t('header.SendScuucess'),
              duration: 3000,
              showClose: true
            })
            this.sendBtnDisable = true
          } else if (res.state === -1) {
            this.getKaptcha()
          }
        })
      } else {
        this.getKaptcha()
        this.$message({
          type: 'error',
          message: this.$t('header.PhoneORImgCodeError'),
          duration: 3000,
          showClose: true
        })
      }
    },
    confirmBtnClick () {
      if(this.confirmPwdTip){
        return
      }
      if (this.mail_code && this.confirm_password && this.user_account) {
        var data = {
          m_name: this.user_account,
          m_pwd: this.confirm_password,
          sms_code: this.mail_code
        }
        forgotV2(data).then(res => {
          if (res.state === 1) {
            this.$message({
              type: 'success',
              message: this.$t('user.ModifySuccessfully'),
              duration: 3000,
              showClose: true
            })
            this.$router.push('/login')
          } else if (res.state === -1) {
            this.getKaptcha()
          }
        })
      } else {
        this.$message({
          type: 'error',
          message: this.$t('register.PleaseCompleteTheFormInformation'),
          duration: 3000,
          showClose: true
        })
      }
    },
    countDownBtn () {
      if (this.sendBtnDisable) return
      this.sendBtnDisable = true
      this.sendCodeText = this.second + 's后重新发送'
      let clock = window.setInterval(() => {
        this.second--
        this.sendCodeText = this.second + 's后重新发送'
        if (this.second < 0) {
          window.clearInterval(clock)
          this.sendCodeText = '发送验证码'
          this.second = 60
          this.sendBtnDisable = false
        }
      }, 1000)
      this.clock = clock
    },
    accountCheck () {
      var regPhone = new RegExp('^[1][3,4,5,7,8,9][0-9]{9}$')
      var regEmail = new RegExp('^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$')
      if (this.user_account.trim() === '') {
        this.user_account_Text = this.$t('register.MobilePhoneEmpty')
        this.accountTip = true
        this.user_account_state = false
      } else if (this.user_account.length < 5) {
        this.user_account_Text = this.$t('register.TheAccountFormatIsIncorrect')
        this.accountTip = true
        this.user_account_state = false
      } else {
        this.accountTip = false
        this.user_account_state = true
      }
    },
    imgCodeCheck () {
      if (this.img_code.trim() === '') {
        this.imgCodeTip = true
      } else {
        this.imgCodeTip = false
      }
    },
    codeCheck () {
      if (this.mail_code.trim() === '') {
        this.codeTip = true
      } else {
        this.codeTip = false
      }
    },
    loginPwdCheck () {
      if (this.user_password.trim() === '') {
        this.loginPwdTip = true
      } else {
        this.loginPwdTip = false
      }
    },
    confirmPwdCheck () {
      if (this.confirm_password.trim() === '') {
        this.confirmPwdTipContent = this.$t('register.VerifyPasswordEmpty')
        this.confirmPwdTip = true
      } else if (this.user_password !== this.confirm_password) {
        this.confirmPwdTipContent = this.$t('register.TwoInputPasswords')
        this.confirmPwdTip = true
      } else {
        this.confirmPwdTip = false
      }
    },
    getKaptcha () {
      getKaptcha().then(res => {
        this.imgcode = res.check_code_img
        this.tokencode = res.check_code_token
      })
    }
  }
}
</script>
<style scoped>
.red {
  color: red;
  transition: 0.6s;
}
.registergo{
  color: #196bdf;
}
</style>
