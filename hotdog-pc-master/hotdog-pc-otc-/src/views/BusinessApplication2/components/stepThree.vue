<template>
  <div class="three_container">
    <div class="title">
      步骤三： 保证金冻结
    </div>
    <ul>
      <li>申请成为商家需冻结相应的保证金（USDT），账户保证金币不足将无法提交申请。</li>
      <li>保证金至认证商户所属的保证金账户，认证商户无法对该账户内保证金币进行交易或划转。</li>
      <li>保证金退还：认证商家向本网站提交申请解除认证，如认证商家账户中无仍处纠纷的不良记录等影响交易安全的行为，本网站将于3个工作日内审核通过，将保证金账户内资金划转至需解除认证商家的可操作账户中，解除认证后将不再享有认证商家的权限和服务。</li>
    </ul>
    <div class="agree_container">
      <RadioGroup
        v-model="agree"
        @on-change="agreeFreeze"
      >
        <Radio :label="agreeMsg">
        </Radio>
        <!-- <Radio label="初级社区">
        </Radio>
        <Radio label="中级社区">
        </Radio >
        <Radio label="高级社区">
        </Radio> -->
      </RadioGroup>
    </div>
    <div class="rest_money">{{currency}}余额：{{zzexNumber.toFixed(6)}}</div>
  </div>
</template>
<script>
export default {
  props: {
    zzexNumber: Number,
    agreeMsg: String,
    currency: String,
  },
  data() {
    return {
      agree: null,
    };
  },
  methods: {
    agreeFreeze() {
      this.$emit('agree-freeze', this.agree);
    },
  },
};
</script>
<style scoped>
.three_container {
  width: 1140px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  flex-direction: column;
}
.three_container .title {
  font-size: 14px;
  line-height: 14px;
  color: #1e2629;
  margin: 30px 0 10px 0;
}
li {
  margin-left: 20px;
  width: 900px;
  font-size: 14px;
  line-height: 30px;
  color: #8aa0a6;
}
.agree_container {
  display: flex;
  align-items: center;
  margin-top: 15px;
}
.agree_container span {
  font-size: 14px;
  line-height: 22px;
  color: #4f646a;
  margin-left: 10px;
}
.rest_money {
  font-size: 14px;
  line-height: 22px;
  color: #387ffa;
  margin-left: 27px;
  margin-top: 10px;
  margin-bottom: 27px;
}
</style>
