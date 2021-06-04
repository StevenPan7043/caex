<template>
  <div class="eidtPwd_container">
    <div class="title_container">
      <p><router-link to="/user">个人中心</router-link></p>><span>修改登录密码</span>
    </div>
    <div class="edit_container">
      <Form :model="formTop" label-position="top" class="form_container">
        <FormItem label="原登录密码">
          <Input v-model="formTop.oldPwd" type="password"></Input>
        </FormItem>
        <FormItem label="新登录密码">
          <Input v-model="formTop.newPwd" type="password"></Input>
        </FormItem>
        <FormItem label="确认密码">
          <Input v-model="formTop.confrimPwd" type="password"></Input>
        </FormItem>
        <FormItem label="图片验证码">
          <div class="flex_container">
            <Input v-model="formTop.imgCode" placeholder="请输入图片验证码" class="imgCodeInput" @on-blur="imgCodeCheck"/>
            <img class="imgCodeBtn" :src="imgAuth.imgCode" @click="getKaptcha">
          </div>
        </FormItem>
        <FormItem label="短信/邮箱验证码" >
          <div class="flex_container">
            <Input class="code_input" v-model="formTop.msgCode" placeholder="请输入手机或邮箱验证码"   @on-blur="msgCodeCheck"/>
            <get-code
              :imgCode="formTop.imgCode"
              :tokencode="imgAuth.tokencode"
              type="forgot"
              @getKaptcha="getKaptcha"
            />
          </div>
        </FormItem>
        <Button type="primary" long class="sub_btn" @click="submitClick">修改</Button>
      </Form>
    </div>
  </div>
</template>
<script>
import GetCode from '@/components/GetCode/index.vue';
import { getKaptcha, resetPwd, getMemberLogout } from './api';
import { logoutApi } from '@/api/GetCode';

export default {
  components: {
    GetCode,
  },
  data() {
    return {
      formTop: {
        oldPwd: '',
        newPwd: '',
        confrimPwd: '',
        msgCode: '',
        imgCode: '',
      },
      imgAuth: {
        imgCode: '',
        tokencode: '',
      },
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
    submitClick() {
      if (this.formTop.newPwd !== this.formTop.confrimPwd) {
        this.$Message.error({
          content: '两次密码不一致',
          duration: 2,
          closable: true,
        });
        return;
      }
      const data = {
        m_security_pwd: this.formTop.oldPwd,
        m_pwd: this.formTop.newPwd,
        sms_code: this.formTop.msgCode,
      };
      resetPwd(data).then(({ state }) => {
        if (state === 1) {
          this.$Message.success({
            content: '修改密码成功',
            duration: 2,
            closable: true,
          });
          getMemberLogout().then((res) => {
            if (res.state === 1) {
              this.logout();
            }
          });
        } else if (state === -1) {
          this.getKaptcha();
        }
      });
    },
    logout() {
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
.eidtPwd_container{
  width: 1200px;
  height: 785px;
  background-color: #ffffff;
  box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.06);
  margin-top: 30px;
  margin-bottom:84px;
}
.title_container{
  width: 1200px;
  height: 60px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  align-items: center;
  padding-left: 30px;
  box-sizing: border-box;
  color: #999999;
  font-size: 16px;
  line-height: 16px;
}
.title_container p{
  color:#3b78c3;
  cursor: pointer;
}
.edit_container{
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 138px 0 89px 0;
}
.form_container{
  width: 480px;
}
.flex_container{
  display: flex;
}
.code_input{
  width: 416px;
  margin-right: 32px;
}
.imgCodeBtn{
  width: 120px;
  height: 48px;
  border-radius: 4px;
  border:  1px solid #dde5e5;
  margin-left: 32px;
}
.sub_btn{
  height: 48px;
}
</style>
<style>
.edit_container .ivu-input{
  height: 48px;
  padding:17px 20px
}
</style>
