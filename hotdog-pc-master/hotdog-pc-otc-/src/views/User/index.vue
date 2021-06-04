<template>
  <div>
    <user-center :userInfo="userInfo" :userTradeInfo="userTradeInfo" :realnameText="realnameText"/>
    <account-safty :userInfo="userInfo" :phoneText="phoneText" :emailText="emailText" :contact="contact"/>
    <auth :realnameText="realnameText" :option="authOption"/>
    <billing-method :realName="realName" :userInfo="userInfo" :accountInfo="accountInfo" :payList="payList" @reload-account="getAccountInfo"/>
    <Modal v-model="firstLogin" width="460" @on-cancel="firstLoginCancel">
      <p slot="header" class="modalhead">
        <span>设置昵称与资金密码</span>
        <span>为保证交易安全，请完善一下信息</span>
      </p>
      <div >
        <Form :model="formTop" label-position="top">
          <FormItem label="昵称">
            <Input v-model="formTop.nickName"/>
          </FormItem>
          <FormItem label="资金密码" v-if="!hasSecPwd">
            <Input v-model="formTop.pwd" type="password" @on-blur="pwdCheck"/>
          </FormItem>
          <FormItem label="确认资金密码" v-if="!hasSecPwd">
            <Input v-model="formTop.confrimPwd" type="password"/>
          </FormItem>
          <FormItem label="图片验证码" v-if="!hasSecPwd">
            <div class="flex_container">
              <Input v-model="formTop.imgCode" placeholder="请输入图片验证码" class="imgCodeInput" />
              <img class="imgCodeBtn" :src="imgAuth.imgCode" @click="getKaptcha" style="height:32px;">
            </div>
          </FormItem>
          <FormItem label="短信/邮箱验证码" v-if="!hasSecPwd">
            <div class="flex_container">
              <Input v-model="formTop.msgCode" style="margin-right:20px"/>
              <get-code style="height:32px"
                :imgCode="formTop.imgCode"
                :tokencode="imgAuth.tokencode"
                type="forgot"
                @getKaptcha="getKaptcha"
              />
            </div>
          </FormItem>
          <div class="btn_tool">
            <span @click="firstLoginCancel">取消</span>
            <Button type="primary" @click="setNickPayPwd">完成设置</Button>
          </div>
        </Form>
      </div>
    </Modal>
  </div>
</template>
<script>
import UserCenter from './userCenter.vue';
import AccountSafty from './accountSafty.vue';
import Auth from './auth.vue';
import BillingMethod from './billingMethod.vue';
import GetCode from '@/components/GetCode/index.vue';

import {
  getAuthindentity, getMember, getTradeInfo, modifyNickName, setSecPwd, getKaptcha, getAccountId,
} from './api';
import { logoutApi } from '@/api/GetCode';
import { chooseBackgroundColor } from '@/libs/utils';

export default {
  components: {
    UserCenter,
    AccountSafty,
    Auth,
    BillingMethod,
    GetCode,
  },
  data() {
    return {
      realnameText: '',
      realName: '',
      authOption: '',
      userInfo: {},
      userTradeInfo: {},
      phoneText: '',
      emailText: '',
      contact: '',
      firstLogin: false,
      formTop: {
        nickName: '',
        pwd: '',
        confrimPwd: '',
        msgCode: '',
        imgCode: '',
      },
      imgAuth: {
        imgCode: '',
        tokencode: '',
      },
      accountInfo: [],
      payList: [

      ],
      hasSecPwd: false,
    };
  },
  mounted() {
    this.getUserInfo();
    this.getAccountInfo();
  },
  methods: {
    async getUserInfo() {
      await this.getMember();
      const res = await getAuthindentity();
      const { state, data } = res;
      if (state === 1) {
        if (data.authIdentity == null
            || data.authIdentity.id_status === 0
            || data.authIdentity.id_status === 2
            || data.authIdentity.id_status === 3) {
          this.realnameText = '未认证';
          this.authOption = '认证';
        } else if (data.authIdentity.id_status === 1) {
          const idNumber = `${data.authIdentity.id_number.substring(0, 2)}*********${data.authIdentity.id_number.substring(data.authIdentity.id_number.length - 2)}`;
          this.realnameText = `${data.authIdentity.family_name}${data.authIdentity.given_name},${idNumber}`;
          this.realName = `${data.authIdentity.family_name}${data.authIdentity.given_name}`;
          this.authOption = '已认证';
        }
      }
      await getTradeInfo(this.userInfo.uid).then(({ state, msg, data }) => {
        if (state === 1) {
          const userTradeInfo = data;
          userTradeInfo.consumingTime += '分钟';
          if (userTradeInfo.totalDoneTrade === 0) {
            userTradeInfo.percent = '0%';
          } else {
            const cashPercent = ((1 - userTradeInfo.totalComplainTrade / userTradeInfo.totalDoneTrade) * 100).toFixed(2);
            const percent = `${cashPercent}%`;
            userTradeInfo.percent = percent;
          }
          userTradeInfo.totalDoneTrade += '次';
          userTradeInfo.done_30 += '次';
          this.userTradeInfo = userTradeInfo;
        }
      });
    },
    getMember() {
      const res = getMember();
      res.then(({ data, msg, state }) => {
        if (state === 1) {
          const userInfo = data;
          userInfo.color = chooseBackgroundColor(userInfo.uid);
          if (!userInfo.m_nick_name) {
            this.firstLogin = true;
            this.getKaptcha();
          } else {
            this.firstLogin = false;
            this.getKaptcha();
          }
          if (userInfo.m_security_pwd === '1') {
            this.hasSecPwd = true;
          } else {
            this.hasSecPwd = false;
          }
          this.userInfo = userInfo;
          const regPhone = new RegExp('^[1][3,4,5,7,8][0-9]{9}$');
          const regEmail = new RegExp('^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$');
          if (regPhone.test(this.userInfo.m_name)) {
            this.phoneText = this.userInfo.m_name_hidden;
          } else {
            this.phoneText = '未设置';
          }
          if (regPhone.test(this.userInfo.phone.trim())) {
            this.contact = this.userInfo.phone.trim();
          } else {
            this.contact = '未设置';
          }
          if (regEmail.test(this.userInfo.m_name)) {
            this.emailText = this.userInfo.m_name_hidden;
          } else {
            this.emailText = '未设置';
          }
        }
        if (state === -1 && msg === 'LANG_NO_LOGIN') {
          this.logout();
          throw Error;
        }
      });
    },


    pwdCheck() {
      const pattern = /^[\w_-]{6,16}$/;
      if (!pattern.test(this.formTop.pwd)) {
        this.$Message.error({
          content: '密码长度不能少于6位，并不大于16位',
          duration: 2,
          closable: true,
        });
        this.formTop.pwd = '';
      }
    },
    setNickPayPwd() {
      const uPattern = /^[\u4e00-\u9fa5\w]{2,12}$/;
      if (!this.formTop.nickName) {
        this.$Message.error({
          content: '请输入昵称',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (!uPattern.test(this.formTop.nickName)) {
        this.$Message.error({
          content: '昵称长度应在2-12位，并且不能包含标点符号',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (!this.hasSecPwd) {
        if (!this.formTop.pwd || !this.formTop.confrimPwd) {
          this.$Message.error({
            content: '请输入资金密码',
            duration: 2,
            closable: true,
          });
          return;
        }
        if (this.formTop.pwd !== this.formTop.confrimPwd) {
          this.$Message.error({
            content: '两次密码不一致',
            duration: 2,
            closable: true,
          });
          return;
        }
        if (!this.formTop.msgCode) {
          this.$Message.error({
            content: '请输入短信/邮箱验证码',
            duration: 2,
            closable: true,
          });
          return;
        }
      }

      if (!this.hasSecPwd) {
        const params = {
          id: this.userInfo.id,
          nickName: this.formTop.nickName,
        };
        const p1 = modifyNickName(params);
        const pwdParams = {
          m_security_pwd: this.formTop.pwd,
          sms_code: this.formTop.msgCode,
        };
        const p2 = setSecPwd(pwdParams);
        Promise.all([p1, p2]).then((res) => {
          if (res[0].state === 1 && res[1].state === 1) {
            this.firstLogin = false;
            this.getUserInfo();
            this.isUserLogin();
            getMember().then(({ data }) => {
              this.$store.commit('SET_USERINFO', data);
            });
          }
        });
      } else {
        const params = {
          id: this.userInfo.id,
          nickName: this.formTop.nickName,
        };
        modifyNickName(params).then((res) => {
          if (res.state === 1) {
            this.firstLogin = false;
            this.getUserInfo();
            getMember().then(({ data }) => {
              this.$store.commit('SET_USERINFO', data);
            });
            this.isUserLogin();
          }
        });
      }
    },
    isUserLogin() {
      getMember().then((res) => {
        if (res.state === 1) {
        // 用户处于登录状态，将状态保存到session
          const loginState = true;
          const userInfo = res.data;
          userInfo.color = chooseBackgroundColor(parseInt(userInfo.uid, 0));
          localStorage.setItem('userInfo', JSON.stringify(userInfo));
          localStorage.setItem('loginState', loginState);
          localStorage.setItem('useruid', userInfo.uid);
          localStorage.setItem('nickName', userInfo.m_nick_name);
          localStorage.setItem('mNameHidden', userInfo.m_name_hidden);
          this.$store.commit('SET_USERINFO', userInfo);
        } else {
          const loginState = false;
          localStorage.setItem('loginState', loginState);
        }
      });
    },
    getKaptcha() {
      getKaptcha().then((res) => {
        this.imgAuth.imgCode = res.check_code_img;
        this.imgAuth.tokencode = res.check_code_token;
      });
    },
    // 获取收款方式
    getAccountInfo() {
      getAccountId().then(({ data }) => {
        const accountInfo = [];
        data.rows.forEach((ele) => {
          if (ele.isDelete === 'NO') {
            accountInfo.push(ele);
          }
        });
        let hasBank = false;
        let hasAli = false;
        let hasWx = false;
        accountInfo.forEach((ele) => {
          if (ele.type === 'BANK') {
            hasBank = true;
          }
          if (ele.type === 'ALIPAY') {
            hasAli = true;
          }
          if (ele.type === 'WXPAY') {
            hasWx = true;
          }
        });
        const payList = [];
        if (!hasBank) {
          payList.push({
            value: 'BANK',
            label: '银行卡',
          });
        }
        if (!hasAli) {
          payList.push({
            value: 'ALIPAY',
            label: '支付宝',
          });
        }
        if (!hasWx) {
          payList.push({
            value: 'WXPAY',
            label: '微信',
          });
        }
        this.payList = payList;

        this.accountInfo = accountInfo;
      });
    },
    firstLoginCancel() {
      this.$router.go(-1);
    },
    logout() {
      this.$Message.error({
        content: '请先登录',
        duration: 2,
        closable: true,
      });
      const loginState = false;
      localStorage.removeItem('userInfo');
      localStorage.removeItem('useruid');
      localStorage.removeItem('nickName');
      localStorage.removeItem('mNameHidden');
      localStorage.setItem('loginState', loginState);
      this.$store.commit('CLEAR_USERINFO');
      this.$router.push('/login');
      logoutApi();
    },
  },
};
</script>
<style scoped>
.modalhead{
  display: flex;
  flex-direction: column;
  height: 57px;
}
.modalhead p{
  font-size: 16px;
  line-height: 16px;
  margin-bottom:  26px;
  color: #393f4e;
}
.flex_container{
  display: flex;
}
.imgCodeBtn{
  width: 120px;
  height: 48px;
  border-radius: 4px;
  border:  1px solid #dde5e5;
  margin-left: 32px;
}
.btn_tool{
  display: flex;
  align-items: center;
  justify-content: flex-end
}
.btn_tool span{
  margin-right: 35px;
  font-size: 14px;
  line-height: 14px;
  color: #3d414d;
  cursor: pointer;
}
</style>
