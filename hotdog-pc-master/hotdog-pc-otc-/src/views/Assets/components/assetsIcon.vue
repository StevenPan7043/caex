<template>
  <div class="assets_icon-container">
    <img
      src="../../../assets/images/USDTCoin.png"
      v-if="coin.currency==='USDT'"
    >
    <img
      src="../../../assets/images/BTCCoin.png"
      v-if="coin.currency==='BTC'"
    >
    <img
      src="../../../assets/images/ETHCoin.png"
      v-if="coin.currency==='ETH'"
    >
    <img
      src="../../../assets/images/ZCCoin.png"
      v-if="coin.currency==='ZC'"
    >
    <p>{{coin.currency}}</p>
  </div>
</template>
<script>
export default {
  props: {
    coin: Object,
  },
};
</script>

<style scoped>
.assets_icon_container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.assets_icon-container p {
  font-size: 16px;
  color: #333333;
  text-align: center;
  margin-top: 17px;
  line-height: 16px;
  font-weight: bolder;
}
</style>
