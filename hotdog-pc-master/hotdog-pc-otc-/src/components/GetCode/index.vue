<template>
  <Button
    class="button"
    @click="sendCodeClick"
    :disabled="sendCodeDisable"
  >
    {{sendCodeText}}
  </Button>
</template>
<script>
import { getMail, getMember, logoutApi } from '@/api/GetCode';

export default {
  props: {
    tokencode: {
      type: String,
      required: true,
    },
    imgCode: {
      type: String,
      default: '',
      required: true,
    },
    type: {
      type: String,
      required: true,
    },
    account: {
      type: String,
    },
    registerAccount: {
      type: String,
    },
  },
  data() {
    return {
      sendCodeDisable: false,
      sendCodeText: '发送验证码',
      second: 60,
      clock: '',
      data: '',
    };
  },
  beforeDestroy() {
    window.clearInterval(this.clock);
  },
  methods: {
    sendCodeClick() {
      if (this.sendCodeDisable === true) {
        return;
      }
      if (!this.account && this.type === 'reg') {
        this.$Message.error({
          content: '请先输入账号',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (this.imgCode === '') {
        this.$Message.error({
          content: '请先输入图片验证码',
          duration: 2,
          closable: true,
        });
        return;
      }
      this.countDown();
      this.getMobileCode();
    },
    countDown() {
      if (this.sendCodeDisable) return;
      this.sendCodeDisable = true;
      this.sendCodeText = `${this.second}s后重新发送`;
      const clock = window.setInterval(() => {
        this.second--;
        this.sendCodeText = `${this.second}s后重新发送`;
        if (this.second < 0) {
          window.clearInterval(clock);
          this.sendCodeText = '发送验证码';
          this.second = 60;
          this.sendCodeDisable = false;
        }
      }, 1000);
      this.clock = clock;
    },
    getMobileCode() {
      if (this.type === 'reg') {
        this.sendCode();
      } else if (this.type === 'forgot') {
        getMember().then((res) => {
          if (res.state === 1) {
            this.data = res.data;
            this.sendCode();
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
              this.logout();
            }
          }
        });
      } else {
        this.sendCode();
      }
    },
    sendCode() {
      let thirdParams = '';
      if (this.type === 'reg') {
        thirdParams = this.account;
      } else {
        thirdParams = this.data.m_name;
      }
      if (this.type === 'realForget') {
        thirdParams = this.registerAccount;
      }
      if (this.type === 'check') {
        if (this.registerAccount.trim() === '') {
          this.$Message.error({
            content: '请先输入账号',
            duration: 2,
            closable: true,
          });
          return;
        }
        thirdParams = this.registerAccount;
      }
      const regPhone = new RegExp('^[1][3,4,5,7,8][0-9]{9}$');
      const regEmail = new RegExp(
        '^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*.[a-zA-Z0-9]{2,6}$',
      );
      debugger;
      if (!regEmail.test(thirdParams.trim())
        && !regPhone.test(thirdParams.trim())
      ) {
        this.$Message.error({
          content: '格式不正确！',
          duration: 3,
          closable: true,
        });
        return;
      }

      const params = {
        token: this.tokencode,
        code: this.imgCode,
        useraccount: thirdParams.trim(),
        type: this.type === 'realForget' ? 'forgot' : this.type,
      };
      getMail(params).then(
        (res) => {
          if (res.state === 1) {
            this.$Message.success({
              content: '发送成功',
              duration: 2,
              closable: true,
            });
          } else if (res.state === -1) {
            this.$emit('getKaptcha');
          }
        },
      );
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
.button{
  width: 120px;
  height: 48px;
  font-size: 14px;
  border: solid 1px #387ffa;
  color: #387ffa;
}
</style>
