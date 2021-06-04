<template>
  <div class="appl_container">
    <div v-show="formShow">
      <div class="title_container">
        商家申请
      </div>
      <step-one/>
      <step-two/>
      <step-three :zzexNumber="zzexNumber" :currency="currencydes" :agreeMsg="agreeMsg" @agree-freeze="agreeFreeze"/>
      <step-four/>
      <div class="agg_container">
        <Checkbox v-model="agreement">
          <span>我已阅读并同意 <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050744553-%E6%B3%95%E5%B8%81%E5%95%86%E5%AE%B6%E7%94%B3%E8%AF%B7">《认证商家协议》</a></span>
        </Checkbox>
        <div class="sub_btn">
          <Button type="primary" size="large" @click="applBtnClick">立即申请</Button>
        </div>
      </div>
    </div>
    <appl-result v-show="resultShow" :applInfo='applInfo' :userInfo="userInfo" :idNumber="idNumber" :realName="realName" @re-app="reApplication" @quit-merchant="quitMerchantClick"/>
    <Modal
      v-model="quitModalShow"
      title="退出商家"
      @on-cancel="cancelQuit">
      <Form :model="formTop" label-position="top">
        <FormItem label="资金密码">
            <Input v-model="formTop.pwd" type="password"/>
        </FormItem>
        <FormItem label="退出原因">
            <Input v-model="formTop.quitReason"/>
        </FormItem>
      </Form>
      <div class="btn_tool">
        <span @click="cancelQuit">取消</span>
        <Button
          type="primary"
          @click="submitClick"
        >确认退出</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import StepOne from './components/stepOne.vue';
import StepTwo from './components/stepTwo.vue';
import StepThree from './components/stepThree.vue';
import StepFour from './components/stepFour.vue';
import ApplResult from './components/applResult.vue';

import {
  getMember, createMerchant, getMerchantInfo, getMoneyInfo, getAuthindentity, getDepositInfo, quirMerchant,
} from './api';
import { logoutApi } from '@/api/GetCode';

export default {
  components: {
    StepOne,
    StepTwo,
    StepThree,
    StepFour,
    ApplResult,
  },
  data() {
    return {
      userInfo: {},
      zzexNumber: 0,
      freezeAgree: false,
      agreement: false,
      resultShow: false,
      formShow: false,
      idNumber: '',
      realName: '',
      // 资金冻结label
      agreeMsg: '',
      // 时候需要扣押金
      isDeposit: 'YES',
      quitModalShow: false,
      applInfo: {},
      formTop: {
        pwd: '',
        quitReason: '申请退出商家',
      },
      currencydes: '',
      otcDepositVolume: 0,
    };
  },
  created() {
    this.getAppliProgress();
    this.getAuthInfo();
    this.getDepositInfo();
  },
  methods: {
    async pageInit() {
      await getMember().then(({ data, msg }) => {
        this.userInfo = data;
        if (msg === 'LANG_NO_LOGIN') {
          this.logout();
        }
      });
      await getMoneyInfo(this.userInfo.uid).then(({ data }) => {
        let zzexNumber = 0;
        if (data != null) {
          data.rows.forEach((ele) => {
            if (ele.currency === this.currencydes) {
              zzexNumber = ele.totalBalance - ele.frozenBalance;
            }
          });
        }
        this.zzexNumber = zzexNumber;
      });
    },
    // 获取申请情况
    getAppliProgress() {
      getMerchantInfo().then(({ data, state, msg }) => {
        this.applInfo = data;
        if (this.applInfo) {
          this.resultShow = true;
        } else {
          this.formShow = true;
        }
      });
    },
    // 获取押金信息
    getDepositInfo() {
      getDepositInfo().then(({ data, msg, state }) => {
        this.currencydes = data.otcDepositCurrency;
        this.otcDepositVolume = data.otcDepositVolume;
        this.pageInit();
        this.agreeMsg = `同意冻结 ${data.otcDepositVolume} ${data.otcDepositCurrency} 币作为商家保证金`;
      });
    },
    // 获取身份信息
    getAuthInfo() {
      getAuthindentity().then(({ state, data }) => {
        if (state === 1) {
          if (data.authIdentity.id_status === 1) {
            this.idNumber = `${data.authIdentity.id_number.substring(0, 2)}*********${data.authIdentity.id_number.substring(data.authIdentity.id_number.length - 2)}`;
            this.realName = `${data.authIdentity.family_name}${data.authIdentity.given_name}`;
          }
        }
      });
    },
    agreeFreeze(agree) {
      if (agree[0] === '同') {
        if (parseFloat(this.zzexNumber) < this.otcDepositVolume) {
          this.$Message.error({
            content: '资金不足',
            duration: 2,
            closable: true,
          });
        } else {
          this.freezeAgree = true;
        }
      } else {
        this.isDeposit = 'NO';
        this.freezeAgree = true;
      }
    },

    applBtnClick() {
      getAuthindentity().then(({ state, data }) => {
        if (state === 1) {
          if (!data.authIdentity) {
            this.$Message.error({
              content: '请先完成实名认证',
              duration: 2,
              closable: true,
            });
            setTimeout(() => {
              this.$router.push('/user');
            }, 2000);
          }
          if (data.authIdentity.id_status === 1) {
            this.applFun();
          } else {
            this.$Message.error({
              content: '请先完成实名认证',
              duration: 2,
              closable: true,
            });
            setTimeout(() => {
              this.$router.push('/user');
            }, 2000);
          }
        }
      });
    },
    applFun() {
      if (!this.freezeAgree) {
        this.$Message.error({
          content: '请先同意冻结资金并确保有足够的资金',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (!this.agreement) {
        this.$Message.error({
          content: '请先阅读并同意《认证商家协议》',
          duration: 2,
          closable: true,
        });
        return;
      }
      const params = {
        memberId: this.userInfo.uid,
        isDeposit: this.isDeposit,
      };
      createMerchant(params).then(({ state }) => {
        if (state === 1) {
          this.$Message.success({
            content: '申请成功！',
            duration: 2,
            closable: true,
          });
          this.resultShow = true;
          this.formShow = false;
          this.getAppliProgress();
        }
      });
    },
    // 重新申请
    reApplication() {
      this.resultShow = false;
      this.formShow = true;
    },
    // 退出商家
    quitMerchantClick() {
      this.quitModalShow = true;
    },
    cancelQuit() {
      this.quitModalShow = false;
      this.quitReason = '申请退出商家';
    },
    submitClick() {
      const params = {
        securityPwd: this.formTop.pwd,
        memo: this.formTop.quitReason,
      };
      quirMerchant(params).then(({ data, msg, state }) => {
        if (state === 1) {
          this.$Message.success({
            content: '退出申请成功，请等待客服审核通过',
            duration: 2,
            closable: true,
          });
          this.quitModalShow = false;
        }
      });
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
.appl_container{
  width: 1200px;
  height: 1136px;
  box-shadow: 0px 0px 12px 0px rgba(4, 7, 6, 0.06);
  border-radius: 6px;
  margin-top: 30px;
  margin-bottom: 34px;
  display: flex;
  align-items: center;
  flex-direction: column;
}
.appl_container .title_container{
  width: 1140px;
  height: 60px;
  border-bottom: 1px solid #e1e1e1;
  font-size: 18px;
  color: #1e2629;
  display: flex;
  align-items: center
}
.agg_container{
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
span{
  font-size: 14px;
  line-height: 22px;
  color: #8aa0a6;
  margin-left: 14px;
}
a{
  font-size: 14px;
  line-height: 22px;
  color: #387ffa;
}
.sub_btn{
  margin-top: 30px
}
.sub_btn .ivu-btn{
  width: 100px;
}
.btn_tool {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.btn_tool span {
  margin-right: 35px;
  font-size: 14px;
  line-height: 14px;
  color: #3d414d;
  cursor: pointer;
}
</style>
