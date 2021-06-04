<template>
  <div class="detail-container">
    <div class="left-container">
      <assets-icon :coin="coinItem.fabi"/>
      <div class="number-container assets-number">
        <p class="assets-title">法币账户</p>
        <div class="number-detail-container">
          <p class="title">可用</p>
          <p class="content">{{coinItem.fabi.enable}}</p>
          <p class="content">{{coinItem.fabi.currency}}</p>
        </div>
        <div class="number-detail-container">
          <p class="title">冻结</p>
          <p class="content">{{coinItem.fabi.frozen_balance}}</p>
          <p class="content">{{coinItem.fabi.currency}}</p>
        </div>
      </div>
      <div class="number-container assets-number2">
        <p class="assets-title">币币账户</p>
        <div class="number-detail-container">
          <p class="title">可用</p>
          <p class="content">{{coinItem.bibi.enable}}</p>
          <p class="content">{{coinItem.bibi.currency}}</p>
        </div>
        <div class="number-detail-container">
          <p class="title">冻结</p>
          <p class="content">{{coinItem.bibi.frozen_balance}}</p>
          <p class="content">{{coinItem.bibi.currency}}</p>
        </div>
      </div>
    </div>
    <div class="right-container">
      <div class="huazhuan_button" @click="rollClick">
        划转
      </div>
    </div>
  </div>
</template>
<script>
import AssetsIcon from './assetsIcon.vue';

export default {
  components: {
    AssetsIcon,
  },
  props: {
    coinItem: Object,
    coinList: Array,
  },
  data() {
    return {
    };
  },
  methods: {
    rollClick() {
      this.$emit('roll-click', this.coinItem);
    },
    closeForm() {

    },
    setPayment() {

    },
  },
};
</script>

<style scoped>
.detail-container{
  margin-top: 36px;
  width: 1076px;
  height: 113px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #e1e1e1
}
.assets-number{
  width: 50%;
  margin-left: 65px;

}
.assets-number2{
  width: 50%
}
.huazhuan_button{
  width: 90px;
  height: 32px;
  background-color: #3b78c3;
  border-radius: 4px;
  font-size: 14px;
  color: #ffffff;
  text-align: center;
  line-height: 32px;
  margin-top: 24px;
  margin-right: 25px;
  cursor: pointer;
}
.left-container{
  display: flex;
  width: 80%
}
.modal_header{
  display: flex;
  flex-direction: column;
  height: 50px;
}
.modal_header .first{
  font-size: 18px;
  line-height: 18px;
  color: #000000;
  margin-bottom: 11px;
}
.modal_header .second{
  line-height: 14px;
  font-size: 14px;
  color: #999999;
}
.flex_container{
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.submit_btn{
  margin-left: 35px;
  background-color: #3b78c3;
  color: #fff;
  width: 104px;
  height: 35px;
  text-align: center
}
.number-container{
  display: flex;
  flex-direction: column;
}
.assets-title{
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  margin-bottom: 26px;
}
.number-detail-container{
  display: flex;
  margin-bottom: 11px;
  line-height: 11px;
}
.number-detail-container p{
  margin-right: 10px;
}
.title{
  font-size: 14px;
  color: #999999;
}
.content{
  font-size: 14px;
  color: #333333;
}
</style>
<style>
.ivu-modal-header{
  border-bottom:none !important
}
.ivu-modal-footer{
  border-top:none !important
}
</style>
