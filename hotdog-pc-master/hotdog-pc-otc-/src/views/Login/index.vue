<template>
  <div class="login_container">
    <!-- 账号密码 -->
    <div v-show="!nextStep">
      <div class="title">欢迎登录</div>
      <div class="login">
        <Form :model="form" label-position="top" class="right_container">
          <FormItem label="手机号码/电子邮箱">
            <Input v-model="form.account" placeholder="账号为邮箱或手机号码" @on-blur="accountCheck"/>
          </FormItem>
          <div class="input-tip">
            <span v-show="tips.account">{{tipContent.account}}</span>
          </div>
          <FormItem label="登录密码">
            <Input v-model="form.pwd" :type="pwdType" placeholder="请输入密码" @on-blur="pwdCheck"></Input>
          </FormItem>
          <div class="input-tip">
            <span v-show="tips.pwd">密码不能为空！</span>
          </div>
          <FormItem label="登录验证">
            <div v-if="authLoading">验证模块加载中</div>
            <div class="auth_container" v-else>
              <div id="captcha"></div>
            </div>
          </FormItem>
        </Form>
      </div>
      <div class="forget_pwd">
        <router-link to="/forgetPwd">忘记密码</router-link>
      </div>
      <div class="bottom_container">
        <Button class="btn" @click="loginClick" :loading="loading" type="primary">确认</Button>
        <div class="to_register">还没有账号？
          <span @click="linkToRegister">点击注册</span>
        </div>
      </div>
    </div>
    <!-- 验证码 -->
    <div v-show="nextStep">
      <div class="title">输入验证码</div>
      <div class="login">
        <Form :model="form" label-position="top" class="right_container">
          <FormItem label="短信验证码">
            <Input v-model="form.msgCode" placeholder="请输入验证码" @on-blur="msgCodeCheck" @on-change ='codeInputChange'/>
          </FormItem>
          <div class="input-tip">
            <span v-show="tips.msgCode">验证码不能为空</span>
          </div>
        </Form>
      </div>
      <div class="forget_pwd">
        <span v-show="time>0" class="grey_color">{{time}}s后再次发送</span>
        <span v-show="time===0" class="reSend_btn" @click="reSend">再次发送</span>
      </div>
      <div class="bottom_container">
        <Button class="btn" @click="netxtLogin" :loading="loading" type="primary">确认</Button>
      </div>
    </div>

  </div>
</template>
<script>
import gtInit from '@/libs/gt';
import {
  getGtValidateCode, memberLogin, getMember, getMail, nextLogin,
} from './api/Login';
import Cookies from 'js-cookie';
import { chooseBackgroundColor } from '@/libs/utils';

export default {
  data() {
    return {
      form: {
        account: '',
        pwd: '',
        msgCode: '',
      },
      captchaObj: {},
      loading: false,
      tips: {
        account: false,
        pwd: false,
        msgCode: false,
      },
      tipContent: {
        account: '账号不能为空！',
      },
      pwdType: 'password',
      authLoading: true,
      jsSession: '',
      nextStep: false,
      token: '',
      msgCode: '',
      time: 60,
      loop: null,
    };
  },
  mounted() {
    getGtValidateCode().then((res) => {
      const response = res;
      if (response.success === 1) {
        this.loadRobotIdentification(response);
      } else {
        this.$Message.error({
          content: '按钮验证错误',
          duration: 2,
          closable: true,
        });
      }
    });
  },
  beforeDestroy() {
    this.time = 60;
    clearInterval(this.loop);
  },
  methods: {
    codeInputChange() {
      if (this.form.msgCode.length === 6) {
        this.netxtLogin();
      }
    },
    linkToRegister() {
      this.$router.push('/register');
    },
    accountCheck() {
      const regPhone = new RegExp('^[1][3,4,5,6,7,8,9][0-9]{9}$');
      const regEmail = new RegExp(
        '^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$',
      );
      if (this.form.account === '') {
        this.tips.account = true;
        this.tipContent.account = '账号不能为空';
      } else if (
        !regEmail.test(this.form.account)
        && !regPhone.test(this.form.account)
      ) {
        this.tips.account = true;
        this.tipContent.account = '账号格式不正确！';
      } else {
        this.tips.account = false;
      }
    },
    pwdCheck() {
      if (this.form.pwd === '') {
        this.tips.pwd = true;
      } else {
        this.tips.pwd = false;
      }
    },
    msgCodeCheck() {
      if (this.form.msgCode === '') {
        this.tips.msgCode = true;
      } else {
        this.tips.msgCode = false;
      }
    },
    loadRobotIdentification(response) {
      const _ = this;
      window.initGeetest(
        {
          gt: response.gt,
          challenge: response.challenge,
          new_captcha: response.new_captcha,
          offline: !response.success,
          product: 'popup',
          width: '608px',
        },
        (captchaObj) => {
          captchaObj.appendTo('#captcha');
          _.captchaObj = captchaObj;
        },
      );
      this.authLoading = false;
    },
    loginClick() {
      if (this.loading) {
        return;
      }
      const result = this.captchaObj.getValidate();
      if (!result) {
        this.$Message.error({
          content: '请完成按钮验证',
          duration: 2,
          closable: true,
        });
      } else {
        this.loading = true;
        const data = {
          m_name: this.form.account,
          m_pwd: this.form.pwd,
          geetest_challenge: result.geetest_challenge,
          geetest_validate: result.geetest_validate,
          geetest_seccode: result.geetest_seccode,
          last_login_device: 3
        };
        memberLogin(data).then((res) => {
          this.loading = false;
          if (res.state === 1) {
            this.token = res.data;
            if (typeof (this.token) === 'string') {
              this.getMsgCode();
            } else {
              // 用户处于登录状态，将状态保存到session
              const loginState = true;
              const userInfo = res.data;
              userInfo.color = chooseBackgroundColor(userInfo.uid);
              localStorage.setItem('userInfo', JSON.stringify(userInfo));
              localStorage.setItem('loginState', loginState);
              localStorage.setItem('useruid', userInfo.uid);
              localStorage.setItem('nickName', userInfo.m_nick_name);
              localStorage.setItem('mNameHidden', userInfo.m_name_hidden);
              localStorage.setItem('token', res.data.token);
              localStorage.setItem('contactToken', res.data.contactToken);

              this.$store.commit('SET_USERINFO', userInfo);
              this.$Message.success({
                content: '登录成功',
                duration: 3,
                closable: true,
              });
              this.$router.push('/');
            }
          } else {
            this.captchaObj.reset();
          }
        });
      }
    },
    netxtLogin() {
      const params = {
        mName: this.form.account,
        code: this.form.msgCode,
        token: this.token,
      };
      nextLogin(params).then((res) => {
        if (res.state === 1) {
          const loginState = true;
          localStorage.setItem('loginState', loginState);
          this.isUserLogin();
          Cookies.set('ZZEX_TOKEN', res.data.token);
          this.$router.push('/');
        } else if (res.state === -1) {
          this.captchaObj.reset();
          if (res.msg === 'LANG_LOGIN_TOKEN_EXPIRE') {
            this.nextStep = false;
          }
        }
      });
    },
    reSend() {
      this.getMsgCode();
      this.time = 60;
    },
    getMsgCode() {
      const params = {
        token: '123456789',
        code: '123456789',
        useraccount: this.form.account,
        type: 'login',
      };
      getMail(params).then(
        (res) => {
          if (res.state === 1) {
            this.loop = setInterval(() => {
              if (this.time > 0) {
                this.time--;
              } else {
                clearInterval(this.loop);
              }
            }, 1000);
            this.nextStep = true;
          }
        },
      );
    },

    isUserLogin() {
      getMember().then((res) => {
        if (res.state === 1) {
          // 用户处于登录状态，将状态保存到session
          const loginState = true;
          const userInfo = res.data;
          userInfo.color = chooseBackgroundColor(userInfo.uid);
          localStorage.setItem('userInfo', JSON.stringify(userInfo));
          localStorage.setItem('loginState', loginState);
          localStorage.setItem('useruid', userInfo.uid);
          localStorage.setItem('nickName', userInfo.m_nick_name);
          localStorage.setItem('mNameHidden', userInfo.m_name_hidden);
          this.$store.commit('SET_USERINFO', userInfo);
          localStorage.setItem('token', res.data.token);

          this.$Message.success({
            content: '登录成功',
            duration: 3,
            closable: true,
          });
          this.$router.push('/');
        } else {
          const loginState = false;
          localStorage.setItem('loginState', loginState);
        }
      });
    },
  },
};
</script>

<style scoped>
.reSend_btn{
  cursor: pointer;
}
.grey_color{
  color:#999
}
.login_container {
  width: 568px;
  display: flex;
  flex-direction: column;
  margin-top: 166px;
}
.login {
  margin-top: 37px;
}
.title {
  font-size: 30px;
  line-height: 30px;
  color: #233150;
}
.forget_pwd {
  font-size: 14px;
  line-height: 14px;
  color: #387ffa;
  margin-top: 30px;
}
.bottom_container {
  display: flex;
  align-items: center;
  margin-top: 25px;
  margin-bottom: 160px;
}
.bottom_container .btn {
  width: 250px;
  height: 48px;
  font-size: 16px;
}
.to_register {
  font-size: 14px;
  line-height: 14px;
  color: #8aa0a6;
  margin-left: 47px;
}
.to_register span {
  color: #387ffa;
  cursor: pointer;
}
.input-tip {
  margin-bottom: 7px;
  color: red;
  font-size: 14px;
  line-height: 14px;
  height: 14px;
}
.auth_container {
  width: 568px;
  height: 48px;
}
</style>
<style>
.login .ivu-input {
  height: 48px;
  padding: 17px 20px;
}
.login .ivu-form .ivu-form-item-label {
  font-size: 14px;
  line-height: 14px;
  color: #8aa0a6;
}
.login .ivu-form-item {
  margin-bottom: 5px;
}
#captcha .geetest_holder {
  width: 568px !important;
  height: 48px !important;
}
.geetest_logo,
.geetest_success_logo {
  display: none;
}

</style>
