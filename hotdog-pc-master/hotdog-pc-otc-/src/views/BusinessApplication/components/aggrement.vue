<template>
  <div class="agg_container">
    <!-- <Checkbox v-model="single">
      <span>我已阅读并同意 <a href="">《认证商家协议》</a></span>
    </Checkbox> -->
    <div class="sub_btn">
      <Button type="primary" size="large" @click="toAppl2">立即申请</Button>
    </div>
  </div>
</template>
<script>
// import { getMember } from '../api';
import { logoutApi } from '@/api/GetCode';

export default {
  data() {
    return {
      single: true,
    };
  },
  methods: {
    toAppl2() {
      this.$router.push('/businessApp2');
      // const regPhone = new RegExp('^[1][3,4,5,6,7,8,9][0-9]{9}$');
      // getMember().then(({ data, msg, state }) => {
      //   if (state === 1) {
      //     if (regPhone.test(data.m_name)) {
      //       this.$router.push('/businessApp2');
      //     } else {
      //       this.$Message.error({
      //         content: '抱歉，暂不支持邮箱用户申请商家',
      //         duration: 2,
      //         closable: true,
      //       });
      //     }
      //   } else if (state === -1) {
      //     if (msg === 'LANG_NO_LOGIN') {
      //       this.logout();
      //     }
      //   }
      // });
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
</style>
