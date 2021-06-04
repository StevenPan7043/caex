<template>
  <div class="eidtPwd_container">
    <div class="title_container">
      <p><router-link to="/user">个人中心</router-link></p>><span>设置联系方式</span>
    </div>
    <div class="edit_container">
      <Form :model="formTop" label-position="top" class="form_container">
        <FormItem label="手机号码">
          <Input v-model="formTop.phone" ></Input>
        </FormItem>
        <FormItem label="图片验证码">
          <div class="flex_container">
            <Input v-model="formTop.imgCode" placeholder="请输入图片验证码" class="imgCodeInput" />
            <img class="imgCodeBtn" :src="imgAuth.imgCode" @click="getKaptcha">
          </div>
        </FormItem>
        <FormItem label="手机短信验证码" >
          <div class="flex_container">
            <Input class="code_input" v-model="formTop.msgCode" placeholder="请输入手机短信验证码"  />
            <get-code
              :imgCode="formTop.imgCode"
              :tokencode="imgAuth.tokencode"
              type="check"
              :registerAccount="formTop.phone"
              @getKaptcha="getKaptcha"
            />
          </div>
        </FormItem>
        <Button type="primary" long class="sub_btn" @click="submitClick">确认</Button>
      </Form>
    </div>
  </div>
</template>
<script>
import GetCode from '@/components/GetCode/index.vue';
import { getKaptcha, setContact } from './api';

export default {
  components: {
    GetCode,
  },
  data() {
    return {
      isPassed: false,
      formTop: {
        phone: '',
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
      const phone = this.formTop.phone.trim();
      const params = {
        phone,
        sms_code: this.formTop.msgCode,
      };
      setContact(params).then(({ state, data, msg }) => {
        if (state === 1) {
          this.$Message.success({
            content: '修改联系成功',
            duration: 3,
            closable: true,
          });
          this.$router.push('/user');
        }
      });
    },
    accountCheck() {
      const regPhone = new RegExp('^[1][3,4,5,7,8][0-9]{9}$');
      if (this.formTop.account.trim() === '') {
        this.$Message.error({
          content: '手机号码不能为空！',
          duration: 3,
          closable: true,
        });
        this.isPassed = false;
      } else if (!regPhone.test(this.formTop.account.trim())
      ) {
        this.$Message.error({
          content: '手机号码格式不正确！',
          duration: 3,
          closable: true,
        });
        this.isPassed = false;
      } else {
        this.isPassed = true;
      }
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
