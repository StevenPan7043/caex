<template>
  <div class="register_container">
    <div class="title">忘记密码</div>
    <div class="register">
      <Form :model="formTop" label-position="top"  class="right_container">
        <FormItem label="手机号码/电子邮箱">
          <Input v-model="formTop.account"  placeholder="请输入手机号码或者电子邮箱" @on-blur="accountCheck"/>
        </FormItem>
        <div class="input-tip">
          <span v-show="tip.account">{{tipContent.account}}</span>
        </div>
        <FormItem label="图片验证码">
          <div class="flex_container">
            <Input v-model="formTop.imgCode" placeholder="请输入图片验证码" class="imgCodeInput"  @on-blur="imgCodeCheck"/>
            <img class="imgCodeBtn" :src="imgAuth.imgCode" @click="getKaptcha">
          </div>
        </FormItem>
        <div class="input-tip">
          <span v-show="tip.imgCode">{{tipContent.imgCode}}</span>
        </div>
        <FormItem label="短信/邮箱验证码" >
          <div class="flex_container">
            <Input class="code_input" v-model="formTop.msgCode" placeholder="请输入手机或邮箱验证码" @on-blur="msgCodeCheck"/>
            <get-code
              :account="formTop.account"
              :imgCode="formTop.imgCode"
              :tokencode="imgAuth.tokencode"
              type="realForget"
              :registerAccount="formTop.account"
              @getKaptcha="getKaptcha"
            />
          </div>
        </FormItem>
        <div class="input-tip">
          <span v-show="tip.msgCode">{{tipContent.msgCode}}</span>
        </div>
        <FormItem label="登陆密码" >
          <Input v-model="formTop.pwd" placeholder="请输入登录密码" @on-blur="pwdCheck" type="password"/>
        </FormItem>
        <div class="input-tip">
          <span v-show="tip.pwd">{{tipContent.pwd}}</span>
        </div>
        <FormItem label="确认密码">
          <Input v-model="formTop.confrimPwd" placeholder="请确认登录密码" @on-blur="confrimPwdCheck" type="password"/>
        </FormItem>
        <div class="input-tip">
          <span v-show="tip.confrimPwd">{{tipContent.confrimPwd}}</span>
        </div>
      </Form>
    </div>
    <div class="btn_container">
      <Button class="register" type="primary" @click="forgetClick">确定</Button>
    </div>
  </div>
</template>
<script>
import GetCode from '@/components/GetCode/index.vue';
import { getKaptcha, forgotV2 } from './api/index';

export default {
  components: {
    GetCode,
  },
  data() {
    return {
      formTop: {
        account: '',
        imgCode: '',
        msgCode: '',
        pwd: '',
        confrimPwd: '',
      },
      single: false,
      imgAuth: {
        imgCode: '',
        tokencode: '',
      },
      tip: {
        account: false,
        imgCode: false,
        msgCode: false,
        pwd: false,
        confrimPwd: false,
      },
      tipContent: {
        account: '',
        imgCode: '图片验证码不能为空！',
        msgCode: '验证码不能为空！',
        pwd: '',
        confrimPwd: '',
      },
      isPassed: true,
    };
  },
  created() {
    this.getKaptcha();
  },
  methods: {
    getKaptcha() {
      getKaptcha().then((res) => {
        this.imgAuth.imgCode = res.check_code_img;
        this.imgAuth.tokencode = res.check_code_token;
      });
    },
    accountCheck() {
      const regPhone = new RegExp('^[1][3,4,5,7,8][0-9]{9}$');
      const regEmail = new RegExp(
        '^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$',
      );
      if (this.formTop.account.trim() === '') {
        this.tipContent.account = '账号不能为空！';
        this.tip.account = true;
        this.isPassed = false;
      } else if (
        !regEmail.test(this.formTop.account.trim())
        && !regPhone.test(this.formTop.account.trim())
      ) {
        this.tipContent.account = '账号格式不正确！';
        this.tip.account = true;
        this.isPassed = false;
      } else {
        this.tip.account = false;
        this.isPassed = true;
      }
    },
    imgCodeCheck() {
      if (this.formTop.imgCode.trim() === '' || this.formTop.imgCode.trim().length !== 4) {
        this.tip.imgCode = true;
        this.isPassed = false;
      } else {
        this.tip.imgCode = false;
        this.isPassed = true;
      }
    },
    msgCodeCheck() {
      if (this.formTop.msgCode.trim() === '') {
        this.tip.msgCode = true;
        this.isPassed = false;
      } else {
        this.tip.msgCode = false;
        this.isPassed = true;
      }
    },
    pwdCheck() {
      const pattern = /^[\w_-]{6,16}$/;
      if(!pattern.test(this.formTop.pwd)){
        this.$Message.error({
          content: '密码长度不能少于6位，并不大于16位',
          duration: 2,
          closable: true,
        });
        this.formTop.pwd=''
      }
      if (this.formTop.pwd.trim() === '') {
        this.tipContent.pwd = '密码不能为空！';
        this.tip.pwd = true;
        this.isPassed = false;
      } else if (this.formTop.confrimPwd.trim() !== '' && this.formTop.pwd !== this.formTop.confrimPwd) {
        this.tipContent.pwd = '两次输入密码不一致！';
        this.tip.pwd = true;
        this.isPassed = false;
      } else {
        this.tip.pwd = false;
        this.isPassed = true;
      }
    },
    confrimPwdCheck() {
      if (this.formTop.confrimPwd.trim() === '') {
        this.tipContent.confrimPwd = '确认密码不能为空！';
        this.tip.confrimPwd = true;
        this.isPassed = false;
      } else if (this.formTop.pwd !== this.formTop.confrimPwd) {
        this.tipContent.confrimPwd = '两次输入密码不一致！';
        this.tip.confrimPwd = true;
        this.isPassed = false;
      } else {
        this.tip.confrimPwd = false;
        this.isPassed = true;
      }
    },
    forgetClick() {
      if (!this.isPassed) {
        this.$Message.error({
          content: '请先完成表单',
          duration: 2,
          closable: true,
        });
        return;
      }
      const data = {
        m_name: this.formTop.account,
        m_pwd: this.formTop.pwd,
        sms_code: this.formTop.msgCode,
      };
      forgotV2(data).then((res) => {
        if (res.state === 1) {
          this.$Message.success({
            content: '修改成功',
            duration: 2,
            closable: true,
          });
          this.$router.push('/login');
        } else if (res.state === -1) {
          this.getKaptcha();
        }
      });
    },
  },
};
</script>

<style scoped>
.register_container{
  width: 568px;
  height: 766px;
  margin: 115px 0 119px 0
}
.title{
  font-size: 30px;
  line-height: 30px;
  color: #233150;
  margin-bottom: 22px;
}
.agreement_container span{
  font-size: 14px;
  line-height: 22px;
  color: #8aa0a6;
  margin-left: 14px;
}
.agreement_container span a{
  font-size: 14px;
  line-height: 22px;
  color: #387ffa;
}
.agreement_container{
  display: flex;
  align-items: center
}
.btn_container{
  width: 568px;
  display: flex;
  justify-content: space-between;
  margin-top: 50px;
}
.btn_container .register{
  width: 250px;
  height: 48px;
  font-size: 16px;
  color: #ffffff;
}

.code_input{
  width: 416px;
  margin-right: 32px;
}
.flex_container{
  display: flex;
}
.imgCodeInput {
  width: 416px;
}
.imgCodeBtn{
  width: 120px;
  height: 48px;
  border-radius: 4px;
  border:  1px solid #dde5e5;
  margin-left: 32px;
}
.input-tip{
  margin-bottom: 7px;
  color: red;
  font-size: 14px;
  line-height: 14px;
  height: 14px;
}
</style>
<style>
.register .ivu-input{
  height: 48px;
  padding:17px 20px
}
.register .ivu-form .ivu-form-item-label{
  font-size: 14px;
  line-height: 14px;
  color: #8aa0a6;
}
.register .ivu-form-item{
  margin-bottom:5px;
}
</style>
