<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="usercenter">
        <div class="main-panel f-cb">
<!--          <side-nav></side-nav>-->
          <div class="content" id="view">
            <div class="inner-account-info" id="innerAccountInfo">
              <div class="header">{{$t('user.AccountInfos')}}</div>
              <div class="user-detail">
                <div class="header">{{$t('user.AccountInfo')}}</div>
<!--                <img class="f-fl" src="../assets/images/default-header.png" alt="">-->
                <div class="user-info">
                  <div class="row-1 userrow f-cb">
                    <span class="f-fl">账号：</span>
                    <span class="f-fl">{{userInfo.m_name_hidden}}</span>
                  </div>
                  <div class="row-2 userrow f-cb">
                    <span class="f-fl">UID: </span>
                    <input class="f-fl" type="text" v-model="userInfo.uid" readonly>
                  </div>
                  <div class="row-3 userrow f-cb">
                    <span class="f-fl">{{$t('user.LastLoginTime')}}</span>
                    <span class="f-fl">{{userInfo.last_login_time}}</span>
<!--                    <span>-->
<!--                      {{$t('user.LastLoginLocation')}} {{userInfo.last_login_ip}} -->
<!--                    </span>-->
                  </div>
                </div>
              </div>
              <div class="user-detail">
                <div class="header">{{$t('user.SecuritySettings')}}</div>
                <div class="user-info">
                  <div class="row-1 userrow f-cb">
                    <span class="f-fl">{{$t('user.LoginPassword')}}：</span>
                    <span class="f-fl">******</span>
                    <span class="f-fr" @click="editLoginPwd">{{$t('user.Modify')}}</span>
                  </div>
                  <div class="row-2 userrow f-cb">
                    <span class="ias f-fl">{{$t('user.MoneyPassword')}}</span>
                    <span class="f-fl" v-if="!hasSetAssetPwd">{{$t('user.NotSet')}}</span>
                    <span class="f-fr" v-if="!hasSetAssetPwd" @click="setAssetPwd">{{$t('user.Settings')}}</span>
                    <span class="f-fl" v-if="hasSetAssetPwd">{{$t('user.HaveSet')}}</span>
                    <span class="f-fr" v-if="hasSetAssetPwd" @click="editAssetPwd">{{$t('user.Modify')}}</span>
                  </div>
                  <div class="row-3 userrow f-cb">
                    <span class="ias f-fl" v-if="phoneOrEmail==='phone'">{{$t('user.PhoneNumber')}}</span>
                    <span class="ias f-fl" v-else>{{$t('user.EMAIL')}}</span>
                    <span class="f-fl" v-if="phoneOrEmail==='phone'">{{data.m_name_hidden}}</span>
                    <span class="f-fl" v-if="phoneOrEmail!=='phone'">{{data.m_name_hidden}}</span>
<!--                    <span class="f-fl" v-if="phoneOrEmail!=='phone'">{{$t('user.NotLogin')}}</span>-->
                  </div>
                </div>
              </div>
<!--              <div class="user-account-state">-->
<!--                <div class="title">{{$t('user.AccountStatus')}}</div>-->
<!--                <div class="lists">-->
<!--                  <div class="l-phone list f-cb">-->
<!--                    <i></i>-->
<!--                    <div class="f-fl phone">-->
<!--                      <span>{{$t('user.phoneNumber')}}</span>-->
<!--                      <span>{{phoneText}}</span>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                  <div class="l-email list f-cb">-->
<!--                    <i></i>-->
<!--                    <div class="f-fl email">-->
<!--                      <span>{{$t('user.Email')}}</span>-->
<!--                      <span>{{emailText}}</span>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                  <div class="l-realname list f-cb">-->
<!--                    <i></i>-->
<!--                    <div class="f-fl realname">-->
<!--                      <span>{{$t('user.Certification')}}</span>-->
<!--                      <span>{{realnameText}}</span>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                </div>-->

<!--              </div>-->
<!--              <div class="user-recommend">-->
<!--                <div class="title">{{$t('user.MyPromotion')}}</div>-->
<!--                <div class="lists">-->
<!--                  <div class="l-invitecode list f-cb">-->
<!--                    <i></i>-->
<!--                    <div class="f-fl invitecode">-->
<!--                      <span>{{$t('user.MyInvitationCode')}}</span>-->
<!--                      <input type="text" readonly v-model="userInfo.uid">-->
<!--                      <span class="copy fa fa-files-o" v-clipboard:copy="userInfo.uid" v-clipboard:success="onCopy" v-clipboard:error="onError"><img class="copyIcon" src="../assets/icons/copy.png" alt=""/></span>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                  <div class="l-invitelink list f-cb">-->
<!--                    <i></i>-->
<!--                    <div class="f-fl invitelink">-->
<!--                      <span>{{$t('user.MyInvitationLink')}}</span>-->
<!--                      <input type="text" value="" readonly v-model="href">-->
<!--                      <span class="copy fa fa-files-o" v-clipboard:copy="href" v-clipboard:success="onCopy" v-clipboard:error="onError"><img class="copyIcon" src="../assets/icons/copy.png" alt=""/></span>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                </div>-->
<!--              </div>-->
              <div class="loginrecord">
                <div class="header">{{$t('user.LoginRecord')}}</div>
                <div class="inner-login-record" id="ilr">
                  <div class="login-record-list">
                    <div class="inner-list">
                      <div class="ilr-head">
                        <span>{{$t('user.LogonTime')}}</span>
                        <span>{{$t('user.IPAddress')}}</span>
                        <span>{{$t('user.Status')}}</span>
                      </div>

                      <div class="ilr-content" id="dlMain"></div>
                      <dl v-for="(item,index) in log" :key="index">
                        <dd>
                          <div class="ilr-row">
<!--                            <span>{{item.m_name}}</span>-->
                            <span>{{item.oper_time}}</span>
                            <span>{{item.oper_ip}}</span>
                            <span>{{$t('header.success')}}</span>
                          </div>
                        </dd>
                      </dl>
                    </div>

                  </div>
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
import { getMember, getAuthindentity } from '@/api/accountInfo'
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import SideNav from '@/components/SideNav'
import GetCode from '@/components/GetCode'
import { getMembers, getKaptcha, resetPwd, getMemberLogout, isSetCoinPwd, setSecPwd } from '@/api/accountSecurity'
import { getLoginlog } from '@/api/loginRecord'
export default {
  components: {
    MyHeader,
    MyFooter,
    SideNav,
    GetCode
  },
  data () {
    return {
      userInfo: '',
      phoneText: '',
      emailText: '',
      realnameText: '',
      href: '',
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
      },
      log: '',
      user:[
        this.$t('LoginRecord'),
        this.$t('Username'),
        this.$t('IPAddress'),
        this.$t('LogonTime'),
      ]
    }
  },
  mounted () {
    getMember().then(res => {
      if (res.state === 1) {
        let userInfo = res.data
        this.userInfo = userInfo
        var regPhone = new RegExp('^[1][3,4,5,7,8,9][0-9]{9}$')
        var regEmail = new RegExp('^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$')
        if (regPhone.test(this.userInfo.m_name)) {
          this.phoneText = this.$t('user.HaveSet')
        } else {
          this.phoneText = this.$t('user.NotSet')
        }
        if (regEmail.test(this.userInfo.m_name)) {
          this.emailText = this.$t('user.HaveSet')
        } else {
          this.emailText = this.$t('user.NotSet')
        }
        getAuthindentity().then(res => {
          let data = res.data
          if (res.state === 1) {
            if (data == null || data.id_status === 0 || data.id_status === 2) {
              this.realnameText = this.$t('user.Unverified')
            } else if (data.id_status === 1) {
              this.realnameText = this.$t('user.Certified')
            }
          }
        })

        this.href = location.href.split('#')[0] + '#/register?uid=' + this.userInfo.uid
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
    }),
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
      }),
    getLoginlog().then(res => {
              if (res.state === 1) {
                this.log = res.data
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
      this.$router.push('/loginPass')
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
        sms_code: this.mobileCode
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
    countDown () {
      if (this.sendCodeDisable) return
      this.sendCodeDisable = true
      this.sendCodeText = this.second + 's后重新发送'
      let clock = window.setInterval(() => {
        this.second--
        this.sendCodeText = this.second + 's后重新发送'
        if (this.second < 0) {
          window.clearInterval(clock)
          this.sendCodeText = '发送验证码'
          this.second = 60
          this.sendCodeDisable = false
        }
      }, 1000)
    },
    setAssetPwd () {
      this.changeCurrencyPwd()
    },
    editAssetPwd () {
      this.changeCurrencyPwd()
    },
    changeCurrencyPwd () {
      this.$router.push('/capitalPass')
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
.copyIcon{
  width: 20px;
  height: 20px
}
</style>
