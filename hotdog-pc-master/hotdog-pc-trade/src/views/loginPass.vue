<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
          <div class="content" id="view">
            <div class="inner-get-coin" id="innerGetCoin">
              <div class="header">
                <a href="#/accountInfo" style="color: #196bdf">{{$t('user.AccountInfos')}}</a>
                <i class="iconfont icon-angleright"></i>
                <span>{{$t('user.ChangeLoginPassword')}}</span>
              </div>
              <div class="getcoin-main">
                <div class="gm-inner gm-inners">
                  <div class="tradepwd input-wrap password01">
                    <div>{{$t('user.Oldpassword')}}</div>
                    <input type="password" v-model="loginOldPwd">
                  </div>
                  <div class="tradepwd input-wrap password01">
                    <div>{{$t('user.NewPassword')}}</div>
                    <input type="password" v-model="loginNewPwd">
                  </div>
                  <div class="input-wrap password01 tradepwd ">
                    <div>{{$t('user.ConfirmPasswords')}}</div>
                    <input type="password" v-model="loginConfirmPwd">
                  </div>
                  <div class="code input-wrap password01">
                    <div>{{$t('user.ImageVerification')}}</div>
                    <input type="text" class="code-input f-fl" v-model="imgCode">
                    <img :src="backImgCode" alt="" class="f-fl" @click="imgCodeClick" style="bottom: -11px">
                  </div>
                  <div class="code input-wrap password01 lastPass">
                    <div>{{$t('user.verificationCode')}}</div>
                    <input type="text" class="code-input f-fl" v-model="asCurrencyDialog.code">
                    <!-- <button class="f-fl" @click="sendCodeClick" :disabled="sendCodeDisable" :class="sendCodeDisable?'forbidBtn':''">{{sendCodeText}}</button> -->
                    <get-code @get-kaptcha="getKaptcha" :tokencode="tokencode" :imgCode="imgCode" type="forgot" style="bottom: -12px"></get-code>
                  </div>
                  <button class="submit btn-confirm" @click="editLoinSubmit">{{$t('user.Submit')}}</button>
                </div>
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
  import SideNav from '@/components/SideNav'
  import GetCode from '@/components/GetCode'
  import { getMembers, getKaptcha, resetPwd, getMemberLogout, isSetCoinPwd, setSecPwd } from '@/api/accountSecurity'
  export default {
    components: {
      MyHeader,
      SideNav,
      MyFooter,
      GetCode
    },
    data () {
      return {
        data: '',
        asDialogShow: false,
        asCurrencyDialogShow: false,
        loginOldPwd: '',
        loginNewPwd: '',
        loginConfirmPwd: '',
        imgCode: '',
        mobileCode: '',
        backImgCode: '',
        tokencode: '',
        sendCodeDisable: false,
        second: 60,
        sendCodeText: '发送验证码',
        msgInterval: '',
        phoneOrEmail: '',
        hasSetAssetPwd: false,
        asCurrencyDialog: {
          pwd: '',
          confirmPwd: '',
          code: ''
        }
      }
    },
    mounted () {
      this.imgCodeClick(),
      getMembers().then(res => {
        if (res.state === 1) {
          let data = res.data
          this.data = data
          var regPhone = new RegExp('^[1][3,4,5,7,8,9][0-9]{9}$')
          var regEmail = new RegExp('^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$')
          if (regEmail.test(this.data.m_name)) {
            this.phoneOrEmail = 'email'
          } else {
            this.phoneOrEmail = 'phone'
          }
          isSetCoinPwd().then(res => {
            let data = res.data
            if (res.state === 1) {
              // var $dialog = $('#asCurrencyDialog')
              // var $money_pwd = $wrapper.find('#MoneyPwd')
              if (data == '0') {
                this.hasSetAssetPwd = false
              } else {
                this.hasSetAssetPwd = true
              }
            }
          })
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
    beforeDestroy () {
      window.clearInterval(this.msgInterval)
    },
    methods: {
      editLoginPwd () {
        this.asDialogShow = true
        this.getKaptcha()
      },
      editLoinSubmit () {
        if(this.loginConfirmPwd!==this.loginNewPwd){
          this.$message({
            type: 'error',
            message: this.$t('register.TwoInputPasswords'),
            duration: 3000,
            showClose: true
          })
          return
        }
        var data = {
          m_security_pwd: this.loginOldPwd,
          m_pwd: this.loginConfirmPwd,
          sms_code: this.asCurrencyDialog.code
        }
        resetPwd(data).then(res => {
          if (res.state === 1) {
            this.$message({
              type: 'success',
              message: this.$t('user.ModifySuccessfully'),
              duration: 3000,
              showClose: true
            })
            getMemberLogout().then(res => {
              if (res.state === 1) {
                this.userLogout()
              }
            })
          } else if (res.state === -1) {
            this.getKaptcha()
          }
        })
      },
      userLogout () {
        this.loginState = false
        localStorage.setItem('loginState', this.loginState)
        this.$router.push('/login')
      },
      imgCodeClick () {
        this.getKaptcha()
      },
      closeAsDialog () {
        clearInterval(this.msgInterval)
        this.asDialogShow = false
        this.loginOldPwd = ''
        this.loginNewPwd = ''
        this.loginConfirmPwd = ''
        this.imgCode = ''
        this.mobileCode = ''
      },
      getKaptcha () {
        getKaptcha().then(res => {
          let imgcode = res.check_code_img
          let tokencode = res.check_code_token
          this.backImgCode = imgcode
          this.tokencode = tokencode
        })
      },
      // countDown () {
      //   if (this.sendCodeDisable) return
      //   this.sendCodeDisable = true
      //   this.sendCodeText = this.second + 's后重新发送'
      //   let clock = window.setInterval(() => {
      //     this.second--
      //     this.sendCodeText = this.second + 's后重新发送'
      //     if (this.second < 0) {
      //       window.clearInterval(clock)
      //       this.sendCodeText = '发送验证码'
      //       this.second = 60
      //       this.sendCodeDisable = false
      //     }
      //   }, 1000)
      // },
      setAssetPwd () {
        this.changeCurrencyPwd()
      },
      editAssetPwd () {
        this.changeCurrencyPwd()
      },
      changeCurrencyPwd () {
        this.asCurrencyDialogShow = true
        this.getKaptcha()
      },
      closeAsCurrencyDialog () {
        window.clearInterval(this.msgInterval)
        this.asCurrencyDialogShow = false
        this.imgCode = ''
        this.asCurrencyDialog = {
          pwd: '',
          confirmPwd: '',
          code: ''
        }
      },
      asCurrencyDialogSubmit () {
        if(this.asCurrencyDialog.pwd!==this.asCurrencyDialog.confirmPwd){
          this.$message({
            type: 'error',
            message: this.$t('register.TwoInputPasswords'),
            duration: 3000,
            showClose: true
          })
          return
        }
        var data = {
          m_security_pwd: this.asCurrencyDialog.confirmPwd,
          sms_code: this.asCurrencyDialog.code
        }
        setSecPwd(data).then(res => {
          if (res.state === 1) {
            this.$message({
              type: 'success',
              message: '设置成功',
              duration: 3000,
              showClose: true
            })
            this.closeAsCurrencyDialog()
          } else if (res.state === -1) {
            this.getKaptcha()
          }
        })
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
