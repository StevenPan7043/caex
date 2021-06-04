<template>
  <div>
    <div class="auth_container">
      <my-title titleName='身份验证' subtitle="请如实填写您的身份信息，一经认证不可修改"/>
      <div class="item-container">
        <div class="left-container">
          <i class="iconfont icon-shimingrenzheng"></i>
          <p class="item-name">实名认证</p>
          <p>{{realnameText||'未绑定'}}</p>
        </div>
        <div class="right-container">
          <p @click="optionClick" :class="option==='已认证'?'has_auth':''">{{option}}</p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import MyTitle from './components/title.vue';
import { baseUrl } from '../../../public/config';

export default {
  components: {
    MyTitle,
  },
  props: {
    realnameText: String,
    option: String,
  },
  data() {
    return {

    };
  },
  methods: {
    optionClick() {
      if (this.option === '认证') {
        // this.$router.push('/auth');
        const token = localStorage.getItem('token');
        window.open(`${baseUrl}/#/identityVerify?token=${token}`);
      }
    },
  },
};
</script>

<style scoped>
.auth_container{
  width: 1200px;
  height: 140px;
  background-color: #ffffff;
  box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.06);
  border-radius: 6px;
  margin-top: 30px;
  padding-top:20px;
  box-sizing: border-box;
}
.item-container{
  width: 1135px;
  height: 59px;
  border-bottom: 1px dotted #e5e5e5;
  margin-left: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.left-container{
  display: flex;
  justify-items: center;
}
.left-container p{
  line-height: 24px;
}
.left-container i{
  margin-right: 20px;
}
.item-name{
  margin-right: 173px;
  width: 200px;
}

.right-container{
  display: flex;
  align-content: center;
  justify-content: flex-end;
}
.right-container p{
  font-size: 14px;
  color: #3b78c3;
  cursor: pointer;
}
.has_auth{
  color:#39404f !important;
  cursor:default !important
}
</style>
