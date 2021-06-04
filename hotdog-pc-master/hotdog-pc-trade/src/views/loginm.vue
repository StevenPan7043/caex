<template>
  <div id="MainView-Mobile-login" ref='loginMobile'>
    <div class="login-wrap page-login-Mobile">
      <div class="inner-form" v-if="!exchange">
        <div class="input-wrap" id="userAccount">
          <p>{{$t('login.MobilePhoneEmail')}}</p>
          <el-input
            type="text"
            :placeholder="$t('login.PleaseEnter')"
            v-model="account"
            @blur="useraccountIdentification"
          />
        </div>
        <div class="input-tip red">
          <p v-show="accountTip">{{accountTipContent}}</p>
        </div>
        <div class="input-wrap" id="userPwd">
          <p>{{$t('login.Password')}}</p>
          <el-input
            type="password"
            :placeholder="$t('login.PleaseEnterYourLoginPassword')"
            v-model="password"
            @blur="userpasswordIdentification"
          />
        </div>
        <div class="input-tip red">
          <p v-show="pwdTip">{{$t('login.PasswordCannotBeEmpty')}}</p>
        </div>
        <div id="captcha2"></div>
        <div class="login-forget" id="loginBtn">
          <el-button
            type="primary"
            class="login-btn f-fl"
            @click="loginClicknext"
            ref="loginBtn"
          >{{$t('login.logIn')}}</el-button>
        </div>
      </div>

      <div class="inner-form" v-if="exchange">
        <div class="input-tip mtext-exchange">
          <p>{{$t('login.PleaseEnters')}} {{account}} {{$t('login.VerificationCodeReceived')}}</p>
        </div>
        <div class="m-code-input code-input input-wrap f-cb">
          <p>{{$t('login.SMSVerificationCode')}}</p>
          <el-row>
            <el-col :span="16">
              <el-input
                class="f-fl code"
                type="text"
                :placeholder="$t('user.PleaseEnterTheverification')"
                v-model="mobileCode"
                @on-blur="codeCheck"
                v-on:input="codeInputChange"
              />
            </el-col>
            <el-col :span="7" :push="1">
              <div class="input-tip againsend">
                <el-button class="smsbtn" v-show="!sendBtnDisable" @click="sendCodeClick">{{sendCodeText}}</el-button>
                <el-button v-show="sendBtnDisable" :class="sendBtnDisable?'forbidBtn smsbtn':'smsbtn'">{{sendCodeText}}</el-button>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="input-tip red">
          <p v-show="codeTip">{{$t('register.VerificationEmpty')}}</p>
        </div>
        <div class="login-forget" id="loginBtn">
          <el-button
            type="primary"
            class="login-btn"
            @click="loginClick"
            ref="loginBtn"
          >{{$t('resetpassword.Affirm')}}</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getGtValidateCode,
  memberLogin,
  getMember,
  getKaptcha,
  getMail,
  verification,
  verifyLabAuth
} from "@/api/login.js";
import gtInit from "@/libs/gt.js";
import Cookies from "js-cookie";

export default {
  data() {
    return {
      isclick: true,
      account: "",
      password: "",
      captchaObj: {},
      pwdTip: false,
      accountTip: false,
      accountTipContent: "",
      exchange: false,
      mobileCode: "",
      imgcode: "",
      tokencode: "",
      sendCodeText: "再次发送",
      sendBtnDisable: false,
      second: 60,
      token: "",
      hasLoadRobotIdentification: false,
      codeTip: false
    };
  },
  mounted() {
    this.getKaptcha();
    this.getGtValidateCode();
  },
  methods: {
    isLabAuth() {
      verifyLabAuth().then(res => {
        console.log(res);
        if (res.state == 1) {
          sessionStorage.setItem("auth", "true");
        }
      });
    },

    codeInputChange() {
      if (this.mobileCode.trim().length === 6) {
        this.loginClick();
      }
    },
    getGtValidateCode() {
      getGtValidateCode().then(res => {
        let response = res;
        if (response.success === 1) {
          this.loadRobotIdentification(this.isclick, response);
        } else {
          this.$message({
            type: "error",
            message: this.$t("login.ButtonValidationError"),
            duration: 3000,
            showClose: true
          });
        }
      });
    },
    loadRobotIdentification(isclick, response) {
      if (!this.hasLoadRobotIdentification) {
        this.hasLoadRobotIdentification = true;
        let _this = this;
        window.initGeetest(
          {
            gt: response.gt,
            challenge: response.challenge,
            new_captcha: response.new_captcha,
            offline: !response.success,
            product: "popup",
            width: "100%"
          },
          function(captchaObj) {
            captchaObj.appendTo("#captcha2");
            _this.captchaObj = captchaObj;
          }
        );
      }
    },
    useraccountIdentification() {
      let regPhone = new RegExp("^[1][3,4,5,6,7,8,9][0-9]{9}$");
      let regEmail = new RegExp(
        "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$"
      );
      if (this.account === "") {
        this.accountTip = true;
        this.accountTipContent = this.$t("login.TheAccountCannotBeEmpty");
      } else {
        this.accountTip = false;
      }
    },

    userpasswordIdentification() {
      if (this.password === "") {
        this.pwdTip = true;
      } else {
        this.pwdTip = false;
      }
    },
    upsetArr(arr) {
      return arr.sort(function() {
        return Math.random() - 0.5;
      });
    },
    sendCodeClick() {
      try {
        document.getElementsByClassName("geetest_radar_success")[0].remove();
      } catch (error) {}
      var check_code = this.upsetArr(
        (this.account + new Date().getTime()).split("")
      ).join("");
      getMail(this.tokencode, check_code, this.account, "login").then(res => {
        if (res.state === 1) {
          this.$message({
            type: "success",
            message: this.$t("header.SendScuucess"),
            duration: 3000,
            showClose: true
          });
          this.countDownBtn();
          this.sendBtnDisable = true;
        } else if (res.state === -1) {
          this.getKaptcha();
        }
      });
    },
    getKaptcha() {
      getKaptcha().then(res => {
        this.imgcode = res.check_code_img;
        this.tokencode = res.check_code_token;
      });
    },
    countDownBtn() {
      if (this.sendBtnDisable) return;
      this.sendBtnDisable = true;
      this.sendCodeText = this.second + "秒后重新发送";
      let clock = window.setInterval(() => {
        this.second--;
        this.sendCodeText = this.second + "秒后重新发送";
        if (this.second < 0) {
          window.clearInterval(clock);
          this.sendCodeText = "再次发送";
          this.second = 60;
          this.sendBtnDisable = false;
        }
      }, 1000);
      this.clock = clock;
    },
    codeCheck() {
      if (this.mobileCode.trim() === "") {
        this.codeTip = true;
      } else {
        this.codeTip = false;
      }
    },
    //账号密码下一步
    loginClicknext() {
      if (this.accountTip) return;
      if (this.pwdTip) return;
      let result = this.captchaObj.getValidate();
      if (!result) {
        this.$message({
          type: "error",
          message: "请完成按钮验证",
          duration: 3000,
          showClose: true
        });
      } else {
        if (this.isclick) {
          let result = this.captchaObj.getValidate();
          let data = {
            m_name: this.account,
            m_pwd: this.password,
            geetest_challenge: result.geetest_challenge,
            geetest_validate: result.geetest_validate,
            geetest_seccode: result.geetest_seccode,
            last_login_device: 3
          };
          memberLogin(data).then(res => {
            if (res.state === 1) {
              this.token = res.data;

              if (typeof this.token === "string") {
                this.isclick = false;
                this.exchange = true;
                setTimeout(() => {
                  this.isclick = true;
                }, 1000);
                this.sendCodeClick();
              } else if (typeof this.token === "object") {
                let loginState = true;
                localStorage.setItem("loginState", loginState);
                localStorage.setItem("contactToken", res.data.contactToken);
                this.saveToken(res.data.token);
                this.isUserLogin();
                this.$router.push("/");
                this.isLabAuth();
              }
            } else if (res.state === -1) {
              this.exchange = false;
              this.mobileCode = "";
              this.getGtValidateCode();
              this.captchaObj.reset();
            }
          });
          // document.getElementsByClassName('geetest_radar_success')[0].remove()
        }
      }
    },

    //登陆
    loginClick() {
      this.isclick = false;
      if (this.mobileCode.trim() !== "") {
        verification(this.account, this.mobileCode, this.token).then(res => {
          if (res.state == 1) {
            let loginState = true;
            localStorage.setItem("loginState", loginState);
            this.saveToken(res.data.token);
            this.isUserLogin();
            this.$router.push("/");
            localStorage.setItem("contactToken", res.data.contactToken);

            // localStorage.setItem('ZZEX_TOKEN',res.data.token)
            Cookies.set("ZZEX_TOKEN", res.data.token);
            this.isLabAuth();
          } else if (res.state === -1) {
            this.captchaObj.reset();
            if (res.msg === "LANG_LOGIN_TOKEN_EXPIRE") {
              this.nextStep = false;
            }
          }
        });
      } else {
        this.codeTip = true;
      }
    },
    isUserLogin() {
      getMember().then(res => {
        if (res.state === 1) {
          // 用户处于登录状态，将状态保存到session
          let loginState = true;
          let userInfo = res.data;
          localStorage.setItem("loginState", loginState);
          localStorage.setItem("useruid", userInfo.uid);
          localStorage.setItem("m_name_hidden", userInfo.m_name_hidden);

          // this.topbarUserOptionList()
        } else {
          let loginState = false;
          localStorage.setItem("loginState", loginState);
        }
      });
    },

    // 保存token到localStorage
    saveToken(token) {
      localStorage.setItem("token", token);
    }
  }
};
</script>

<style scoped>
.red {
  color: red;
  -webkit-transition: 0.6s;
  transition: 0.6s;
}

#captcha2 {
  width: 100%;
}
#captcha2 .geetest_holder.geetest_wind .geetest_radar_btn {
  width: 100%;
}
#captcha2 .geetest_holder.geetest_wind .geetest_radar_btn,
#captcha2 .geetest_holder.geetest_wind .geetest_radar_tip,
#captcha2
  .geetest_holder.geetest_wind
  .geetest_success_radar_tip,
#captcha2 {
  width: 100%;
}

.input-tip {
  margin-top: 0;
}

.forgetPass {
  margin-top: 30px;
  display: block;
  font-size: 14px;
  color: #196bdf;
}

.registergo {
  color: #6c859a;
}

.registergo em {
  color: #196bdf;
}
.smsbtn{
    margin-left: 0;
    width: 100%;
    overflow: hidden;
}

#loginBtn {
  margin-top: 30px;
}

#loginBtn button {
  width: 100%;
}

body,
html {
  min-width: 0;
}

#MainView-Mobile {
  padding: 20px 0;
  color: #1c242c;
}

.page-register-Mobile {
  width: 90%;
  margin: auto;
}

#MainView-Mobile .page-register-Mobile .title {
  font-size: 30px;
  height: 50px;
  line-height: 50px;
  text-align: center;
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

.againsend{
    margin-top: 0;
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
.login-btn {
  width: 100%;
  margin-top: 20px;
}
</style>
