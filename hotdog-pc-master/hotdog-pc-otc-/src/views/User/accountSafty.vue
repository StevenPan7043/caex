<template>
  <div class="account_container">
    <my-title titleName='账户安全'/>
    <form-item title="登录密码" info="******" option="修改" iconInfo="icon-suodinglocked35" optionType="editLoginPwd" @option-click="editLoginPwd"/>
    <form-item title="资金密码" info="******" option="重置" iconInfo="icon-9" optionType="resetAssetPwd" @option-click="resetAssetPwd"/>
    <form-item title="手机验证" :info="phoneText" iconInfo="icon-tel" option="" option2=""/>
    <form-item title="邮箱验证" :info="emailText" iconInfo="icon-web__youxiangrenzheng" option=""/>
    <form-item title="联系电话" :info="contact" iconInfo="icon-tel" option="设置" optionType="resetContact"  @option-click="resetContact" v-if="phoneText==='未设置'"/>
    <!-- <form-item title="谷歌验证码" option=""/> -->
  </div>
</template>
<script>
import MyTitle from './components/title.vue';
import FormItem from './components/formItem.vue';

export default {
  components: {
    MyTitle,
    FormItem,
  },
  props: {
    userInfo: {
      type: Object,
    },
    phoneText: String,
    emailText: String,
    contact: String,
  },
  methods: {
    editLoginPwd(optionType) {
      if (optionType === 'editLoginPwd') {
        this.$router.push('/editLoginPwd');
      }
    },
    resetAssetPwd(optionType) {
      if (optionType === 'resetAssetPwd') {
        this.$router.push('/resetAssetPwd');
      }
    },
    resetContact(optionType) {
      if (optionType === 'resetContact') {
        this.$router.push('/resetContact');
      }
    },
  },
};
</script>

<style scoped>
.account_container{
  width: 1200px;
  height: 360px;
  background-color: #ffffff;
  box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.06);
  border-radius: 6px;
  margin-top: 30px;
}
</style>
