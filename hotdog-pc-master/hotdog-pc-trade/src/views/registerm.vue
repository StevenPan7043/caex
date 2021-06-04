<template>
  <div id="MainView-Mobile" ref="registerMobile">
    <div class="page-register-Mobile">
      <div class="title">
        <img src="../assets/images/registermlogo.png" />
      </div>
      <el-tabs type="border-card" class="tabs">
        <el-tab-pane label="注册">
          <div class="register-wrap">
            <div class="inner-form">
              <div class="change-reg-way-input">
                <div class="input-wrap account f-cb">
                  <p>{{$t('register.MobilePhoneEmail')}}</p>
                  <el-row>
                    <el-col :span="7">
                      <el-select v-model="curAreaCode" :placeholder="$t('register.chooseCountry')">
                        <el-option v-for="item in countries" :key="item.id" :value="item.area_code">
                          <span style="float: left">{{ item.country_name }}</span>
                          <span
                            style="float: right; color: #8492a6; font-size: 13px"
                          >{{ item.area_code }}</span>
                        </el-option>
                      </el-select>
                    </el-col>
                    <el-col :span="16" :push="1">
                      <el-input
                        type="text"
                        :placeholder="$t('register.PleaseEnter')"
                        class="user-account f-fr"
                        v-model="userRegAccount"
                        @blur="accountCheck"
                      />
                    </el-col>
                  </el-row>
                </div>
                <div class="input-tip red">
                  <p v-show="accountTip1">{{$t('register.MobilePhoneEmpty')}}</p>
                  <p v-show="accountTip2">{{$t('register.TheAccountFormatIsIncorrect')}}</p>
                </div>
                <div class="input-wrap img-validate f-cb" v-show="checkeds">
                  <p>{{$t('user.PictureVerificationCode')}}</p>
                  <el-row>
                    <el-col :span="16">
                      <el-input
                        class="f-fl"
                        type="text"
                        :placeholder="$t('register.PleaseVerificationCode')"
                        id="userRegImgCode"
                        v-model="userRegImgCode"
                        @blur="imgCodeCleck"
                      />
                    </el-col>
                    <el-col :span="7" :push="1">
                      <img
                        class="imgCodeBtn f-fl"
                        :src="imgcode"
                        alt
                        id="imgCode"
                        @click="getKaptcha"
                      />
                    </el-col>
                  </el-row>
                </div>
                <div class="input-tip red" v-show="checkeds">
                  <p v-show="imgCodeTip">{{$t('register.ImageVerificationEmpty')}}</p>
                </div>
                <div class="input-wrap mobile-validate f-cb" v-show="checkeds">
                  <p>{{$t('user.verificationCode')}}</p>
                  <el-row>
                    <el-col :span="16">
                      <el-input
                        class="f-fl"
                        type="text"
                        :placeholder="$t('register.PleaseEnterYourMobile')"
                        id="userRegMCode"
                        v-model="userRegMCode"
                        @blur="codeCheck"
                      />
                    </el-col>
                    <el-col :span="7" :push="1">
                      <el-button
                        type="button"
                        class="codeBtn f-fl"
                        id="codeBtn"
                        @click="sendCode"
                        :disabled="sendCodeDisable"
                        :class="sendCodeDisable?'forbidBtn':''"
                      >{{sendCodeText}}</el-button>
                    </el-col>
                  </el-row>
                </div>
                <div class="input-tip red" v-show="checkeds">
                  <p v-show="codeTip">{{$t('register.VerificationEmpty')}}</p>
                </div>
                <div class="input-wrap">
                  <p>{{$t('user.LoginPassword')}}</p>
                  <el-input
                    type="password"
                    :placeholder="$t('register.PleaseEnterYourLoginPassword')"
                    id="loginPwd"
                    v-model="loginPwd"
                    @blur="loginPwdCheck"
                  />
                </div>
                <div class="input-tip red">
                  <p v-show="loginPwdTip">{{$t('login.PasswordCannotBeEmpty')}}</p>
                </div>
                <div class="input-wrap">
                  <p>{{$t('register.ConfirmPassword')}}</p>
                  <el-input
                    type="password"
                    :placeholder="$t('register.PleaseConfirmTheLoginPassword')"
                    id="confirmPwd"
                    v-model="confirmPwd"
                    @blur="confirmPwdCheck"
                  />
                </div>
                <div class="input-tip red">
                  <p v-show="confirmPwdTip1">{{$t('register.VerifyPasswordEmpty')}}</p>
                  <p v-show="confirmPwdTip2">{{$t('register.TwoInputPasswords')}}</p>
                </div>
                <div class="input-wrap" v-show="checkeds">
                  <p>{{$t('register.InvitationCode')}}</p>
                  <el-input
                    type="text"
                    :placeholder="$t('register.PleaseEnterInviterUID')"
                    id="inviterId"
                    v-model="inviterId"
                    disabled
                  />
                </div>
                <div class="input-tip" v-show="checkeds"></div>
              </div>
              <div class="agreement">
                <input type="checkbox" id="Agreement" v-model="agreement" />
                {{$t('register.IAgreeToThe')}}
                <a
                  class="useragreement"
                  @click.prevent="toUserAgreement"
                >{{$t('register.ServiceAgreement')}}</a>
              </div>
              <div class="agreement">
                <input
                  type="checkbox"
                  id="Agreement"
                  v-model="simulation"
                  @change="simulationChange"
                />
                {{$t('register.demoAccount')}}
              </div>
              <div class="reg-back">
                <el-button type="primary" id="regBtn" @click="regBtnClick">{{$t('register.SignUp')}}</el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="登录">
        <Loginm></Loginm>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import Loginm from '@/views/loginm'
import { getKaptcha, getMail, member, getCountrylist } from "@/api/register";
import { getLang } from "../libs/utils";

export default {
  metaInfo: {
    meta: [
      {
        name: "viewport",
        content:
          "width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"
      }
    ]
  },
  components: {
      Loginm
  },
  data() {
    return {
      countries: [
        {
          id: 1,
          area_code: "86",
          country_name: this.$t("header.China")
        },
        {
          id: 2,
          area_code: "1",
          country_name: this.$t("header.USA")
        },
        {
          id: 3,
          area_code: "82",
          country_name: this.$t("header.Korea")
        },
        {
          id: 4,
          area_code: "44",
          country_name: this.$t("header.UK")
        },
        {
          id: 5,
          area_code: "81",
          country_name: this.$t("header.Japan")
        },
        {
          id: 6,
          area_code: "7",
          country_name: this.$t("header.Russia")
        },
        {
          id: 7,
          area_code: "852",
          country_name: this.$t("header.HK")
        },
        {
          id: 8,
          area_code: "886",
          country_name: this.$t("header.TW")
        }
      ],
      curAreaCode: "",
      userRegAccount: "",
      userRegAccount_state: false,
      userRegImgCode: "",
      userRegMCode: "",
      loginPwd: "",
      loginPwd_state: false,
      confirmPwd: "",
      inviterId: "",
      agreement: false,
      simulation:false,
      accountTip1: false,
      accountTip2: false,
      imgCodeTip: false,
      codeTip: false,
      loginPwdTip: false,
      confirmPwdTip1: false,
      confirmPwdTip2: false,
      imgcode: "",
      tokencode: "",
      sendCodeText: "发送验证码",
      sendCodeDisable: false,
      second: 60,
      lang: "zh",
      checkeds: true,
    };
  },
  created() {
    this.getParams();
  },
  mounted() {
    if (location.hash.indexOf("?") !== -1) {
      this.inviterId = location.hash
        .split("?")[1]
        .split("invite_code=")[1]
        .split("&")[0];
    }
    this.getKaptcha();
    this.lang = getLang();
    // getCountrylist().then(res => {
    //     console.log(res);
    // });

    this.$refs.registerMobile.style.minHeight = `${document.documentElement.clientHeight}px`
  },
  beforeDestroy() {
    window.clearInterval(this.clock);
    document.body.style.minWidth = '1200px'
    document.html.style.minWidth = '1200px'

  },
  methods: {
    simulationChange(value) {
      this.checkeds = !value.target.checked
      this.userRegMCode = ''
      this.inviterId = ''
    },
    toUserAgreement() {
      let lang = getLang();
      switch (lang) {
        case "zh":
          window.open(
            "https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050743293-%E7%94%A8%E6%88%B7%E5%8D%8F%E8%AE%AE"
          );
          break;

        default:
          window.open(
            "https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050743293-%E7%94%A8%E6%88%B7%E5%8D%8F%E8%AE%AE"
          );
          break;
      }
    },
    getParams() {
      const routerParams = this.$route.query.mallCode;
      // console.log(routerParams);
      this.userRegAccount = routerParams;
    },
    accountCheck() {
      let regPhone = new RegExp("^[1][3,4,5,7,8,9][0-9]{9}$");
      let regEmail = new RegExp(
        "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$"
      );
      if (this.userRegAccount.trim() === "") {
        this.accountTip1 = true;
        this.accountTip2 = false;
        this.userRegAccount_state = false;
      } else if (this.userRegAccount.length < 5) {
        this.accountTip1 = false;
        this.accountTip2 = true;
        this.userRegAccount_state = false;
      } else {
        this.accountTip1 = false;
        this.accountTip2 = false;
        this.userRegAccount_state = true;
      }
    },
    imgCodeCleck() {
      if (
        this.userRegImgCode.trim() === "" ||
        this.userRegImgCode.trim().length != 4
      ) {
        this.imgCodeTip = true;
      } else {
        this.imgCodeTip = false;
      }
    },
    codeCheck() {
      if (this.userRegMCode.trim() === "") {
        this.codeTip = true;
      } else {
        this.codeTip = false;
      }
    },
    loginPwdCheck() {
      if (this.loginPwd.trim() === "") {
        this.loginPwdTip = true;
      } else {
        this.loginPwdTip = false;
      }
    },
    confirmPwdCheck() {
      if (this.confirmPwd.trim() === "") {
        this.confirmPwdTip1 = true;
        this.confirmPwdTip2 = false;
        this.loginPwd_state = false;
      } else if (this.loginPwd !== this.confirmPwd) {
        this.confirmPwdTip1 = false;
        this.confirmPwdTip2 = true;
        this.loginPwd_state = false;
      } else {
        this.confirmPwdTip1 = false;
        this.confirmPwdTip2 = false;
        this.loginPwd_state = true;
      }
    },
    sendCode() {
      if (this.userRegAccount === "") {
        this.$message({
          type: "error",
          message: this.$t("header.EnterPhoneOrEmail"),
          duration: 3000,
          showClose: true
        });
        return;
      }
      if (this.userRegImgCode === "") {
        this.$message({
          type: "error",
          message: this.$t("header.EnterImgCode"),
          duration: 3000,
          showClose: true
        });
        return;
      }
      if (this.curAreaCode === "") {
        this.$message({
          type: "error",
          message: this.$t("header.areaCodeNotNull"),
          duration: 3000,
          showClose: true
        });
        return;
      }
      if (this.userRegAccount_state) {
        getMail(
          this.tokencode,
          this.userRegImgCode,
          this.userRegAccount,
          "reg",
          this.curAreaCode
        ).then(res => {
          if (res.state === 1) {
            this.countDown();
            this.$message({
              type: "success",
              message: this.$t("header.SendScuucess"),
              duration: 3000,
              showClose: true
            });
            this.sendBtnDisable = true;
          } else if (res.state === -1) {
            this.getKaptcha();
          }
        });
      } else {
        this.getKaptcha();
        this.$message({
          type: "error",
          message: this.$t("header.PhoneOrEmailError"),
          duration: 3000,
          showClose: true
        });
      }
    },
    regBtnClick() {
      if (this.userRegAccount_state && this.loginPwd_state) {
        if (this.curAreaCode === "") {
          this.$message({
            type: "error",
            message: this.$t("header.areaCodeNotNull"),
            duration: 3000,
            showClose: true
          });
          return;
        }
        if (!this.agreement) {
          this.$message({
            type: "error",
            message: this.$t("header.SelectServeAgreement"),
            duration: 3000,
            showClose: true
          });
        }
        let data = {
          m_name: this.userRegAccount,
          m_pwd: this.loginPwd,
          sms_code: this.userRegMCode,
          invite_code: this.inviterId,
          area_code: this.curAreaCode,
          checkeds: !this.checkeds==true?1:0,
        };
        member(data).then(res => {
          if (res.state === 1) {
            this.$router.push("/login");
            this.$message({
              type: "success",
              message: this.$t("header.RegisterSuccess"),
              duration: 3000,
              showClose: true
            });
          } else {
            this.getKaptcha();
          }
        });
      } else {
        this.$message({
          type: "error",
          message: this.$t("register.PleaseCompleteTheFormInformation"),
          duration: 3000,
          showClose: true
        });
      }
    },
    backLoginBtnClick() {
      this.$router.push("/login");
    },
    getKaptcha() {
      getKaptcha().then(res => {
        this.imgcode = res.check_code_img;
        this.tokencode = res.check_code_token;
      });
    },
    countDown() {
      if (this.sendCodeDisable) return;
      this.sendCodeDisable = true;
      this.sendCodeText = this.second + "s后重新发送";
      let clock = window.setInterval(() => {
        this.second--;
        this.sendCodeText = this.second + "s后重新发送";
        if (this.second < 0) {
          window.clearInterval(clock);
          this.sendCodeText = "发送验证码";
          this.second = 60;
          this.sendCodeDisable = false;
        }
      }, 1000);
      this.clock = clock;
    }
  }
};
</script>
<style>
body,
html {
  min-width: 0px;
}

.tabs{
    opacity: 0.9;
}
#MainView-Mobile {
  padding: 20px 0;
  color: #1c242c;
  background-image: url('../assets/images/registermbg.png');
  background-size: 100% 100%;
  background-repeat: no-repeat;
}

.page-register-Mobile {
  width: 90%;
  margin: auto;
}

#MainView-Mobile .page-register-Mobile .title {
  height:70px;
  line-height: 70px;
  text-align: center;
}
#MainView-Mobile .page-register-Mobile .title img{
    height: 70px;
}

.input-wrap {
  margin-top: 20px;
}

.input-wrap p {
  margin-bottom: 10px;
}

.input-tip {
  margin-top: 10px;
}
.codeBtn {
  width: 100%;
  overflow: hidden;
}
.imgCodeBtn {
  width: 100%;
}
.input-tip.red {
  color: red;
  -webkit-transition: 0.6s;
  transition: 0.6s;
}

.agreement {
  height: 30px;
  line-height: 30px;
}

.agreement .useragreement {
  color: #196bdf;
}

.reg-back {
  margin-top: 20px;
}

.reg-back button {
  width: 100%;
}
</style>
