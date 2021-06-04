<template>
  <div class="payment_container">
    <p class="title">卖方付款方式</p>
    <div
      v-for="(item,index) in oppositeAccount"
      :key="item.id"
      v-show="payStep==='NP'||payStep==='UNCONFIRMED'||payStep==='DONE'||payStep==='COMPLAINING'"
      class="pay_item"
    >
      <div class="payment_item">
        <i class="iconfont icon-yinhangqia" v-if="item.type==='BANK'" style="color: #768db0;font-size: 22px;"></i>
        <i class="iconfont icon-zhifubao" v-if="item.type==='ALIPAY'" style="color: #39a9ed;font-size: 22px;"></i>
        <i class="iconfont icon-weixindenglu" v-if="item.type==='WXPAY'"  style="color: #3baf34;font-size: 22px;"></i>
        <p style="margin-right:28px">{{item.type|typeFilter}}</p>
        <p>
          {{item.account}}&nbsp;&nbsp;{{item.name}}&nbsp;&nbsp;
          <span v-show="item.type==='BANK'">{{item.bankOrImg}}</span>
        </p>
        <i v-if="item.type!=='BANK'" class="iconfont icon-erweima" @mouseenter="opqrcodeMouseEnter()" @mouseleave="opqrcodeMouseLeave()"></i>
        <img :src="item.bankOrImg" alt="" v-show="opqrcodeShow" class="codeimg">
      </div>
    </div>
    <p class="title">卖方收款方式</p>

    <div
      v-for="(item,index) in payInfo"
      :key="item.id"
      v-show="payStep==='NP'||payStep==='UNCONFIRMED'||payStep==='DONE'||payStep==='COMPLAINING'"
      class="pay_item"
    >
      <div class="payment_item">
        <i class="iconfont icon-yinhangqia" v-if="item.type==='BANK'" style="color: #768db0;font-size: 22px;"></i>
        <i class="iconfont icon-zhifubao" v-if="item.type==='ALIPAY'" style="color: #39a9ed;font-size: 22px;"></i>
        <i class="iconfont icon-weixindenglu" v-if="item.type==='WXPAY'"  style="color: #3baf34;font-size: 22px;"></i>
        <p style="margin-right:28px">{{item.type|typeFilter}}</p>
        <p>
          {{item.account}}&nbsp;&nbsp;{{item.name}}&nbsp;&nbsp;
          <span v-show="item.type==='BANK'">{{item.bankOrImg}}</span>
        </p>
        <i v-if="item.type!=='BANK'" class="iconfont icon-erweima" @mouseenter="qrcodeMouseEnter(index)" @mouseleave="qrcodeMouseLeave(index)"></i>
        <img :src="item.bankOrImg" alt="" v-show="qrcodeShow[index]" class="codeimg">
      </div>
    </div>
    <div
      class="cancel_container"
      v-show="payStep==='CANCELED'"
    >
      订单已取消，无法查看支付方式
    </div>
  </div>
</template>
<script>
export default {
  props: ['payInfo', 'payStep', 'oppositeAccount'],
  data() {
    return {
      qrcodeShow: [false, false, false],
      opqrcodeShow: false,
    };
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
    qrcodeMouseEnter(index) {
      const cash = [];
      for (let i = 0; i < 3; i++) {
        if (i === index) {
          cash.push(true);
        } else {
          cash.push(false);
        }
      }
      this.qrcodeShow = cash;
    },
    qrcodeMouseLeave() {
      this.qrcodeShow = [false, false, false];
    },
    opqrcodeMouseEnter() {
      this.opqrcodeShow = true;
    },
    opqrcodeMouseLeave() {
      this.opqrcodeShow = false;
    },
  },
};
</script>

<style scoped>
.payment_container {
  width: 750px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  flex-direction: column;
}
.payment_container .title {
  font-size: 14px;
  line-height: 14px;
  color: #999999;
  margin-top: 30px;
}
.pay_item {
  margin-top: 30px;
  height: 40px;
  display: flex;
  justify-items: center;
}
.payment_item {
  display: flex;
  align-items: center;
}
.payment_item i {
  margin-left: 15px;
  margin-right: 17px;
}
.cancel_container {
  margin-top: 45px;
  width: 245px;
  height: 36px;
  background-color: #c8c8c8;
  font-size: 14px;
  line-height: 36px;
  text-align: center;
  color: #ffffff;
}
.codeimg{
  width: 200px;
  height: 200px;
  object-fit: contain;
  float: left;
}
</style>
