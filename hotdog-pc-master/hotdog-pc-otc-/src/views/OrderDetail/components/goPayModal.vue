<template>
  <div >
    <div class="flex_container">
      <div>
        <p class="title">支付金额：</p>
        <p class="totalMoney">{{(totalMoney).toFixed(2)}} {{quoteCurrency}}</p>
        <p class="title">对方 {{selectPayInfo.type|typeFilter}}</p>
        <p class="bank_info">{{selectPayInfo.name}}
          <span v-if="selectPayInfo.type==='BANK'"> {{selectPayInfo.bankOrImg}}</span>
        </p>
        <p class="bank_number">{{selectPayInfo.account}}</p>
        <p class="tip"><span class="red">*请本人实名付款</span>，否则卖家可拒收</p>
      </div>
      <div class="account_code">
        <img :src="selectPayInfo.bankOrImg" alt="" v-show="selectPayInfo.type!=='BANK'"  v-viewer>
      </div>
    </div>
    <viewer :images="selectPayInfo.bankOrImg">
      <img :src="src" >
    </viewer>
  <div class="btn_tool">
    <span @click="payCancel">还未付款</span>
    <Button type="primary" @click="hasPayClick">已付款</Button>
    </div>
  </div>
</template>
<script>
import 'viewerjs/dist/viewer.css';
import Viewer from 'v-viewer';
import Vue from 'vue';

Vue.use(Viewer);

export default {
  props: {
    selectPayInfo: Object,
    totalMoney: Number,
    quoteCurrency: String,
  },
  filters: {
    typeFilter(val) {
      switch (val) {
        case 'BANK':
          return '银行卡';
        case 'ALIPAY':
          return '支付宝';
        case 'WXPAY':
          return '微信';
        default:
          return '';
      }
    },
  },
  methods: {
    payCancel() {
      this.$emit('pay-cancel');
    },
    hasPayClick() {
      this.$emit('has-pay');
    },
  },
};
</script>

<style scoped>
.flex_container{
  display: flex;
  justify-content: space-between
}
.title{
  font-size: 12px;
  line-height: 12px;
  color: #666666;
}
.totalMoney{
  margin-top: 15px;
  font-size: 18px;
  line-height: 18px;
  color: #0ca495;
  margin-bottom: 29px;
}
.bank_info{
  font-size: 14px;
  line-height: 14px;
  color: #333333;
  margin-top: 15px;

}
.bank_number{
  font-size: 14px;
  line-height: 14px;
  color: #333333;
  margin-top: 17px;
}
.tip{
  font-size: 12px;
  line-height: 12px;
  color: #666666;
  margin-top: 40px;
  margin-bottom: 37px;
}
.red{
  color: #e92f2f
}
.btn_tool{
  display: flex;
  align-items: center;
  justify-content: flex-end
}

.btn_tool  span{
  margin-right: 35px;
  font-size: 14px;
  line-height: 14px;
  color: #3d414d;
  cursor: pointer;
}
.account_code img{
  width: 220px;
  height: 220px;
   object-fit: contain;
}
</style>
