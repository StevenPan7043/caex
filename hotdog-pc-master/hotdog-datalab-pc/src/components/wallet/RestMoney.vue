<template>
    <div class="rest-money">
        <span class="rest">{{ coin }}账户余额:&nbsp;{{ rest }}</span>
        <span class="cny">&nbsp;≈&nbsp;{{ cny }}&nbsp;CNY</span>
        <router-link :to="{path:'/withDraw', query: { coinName: coin,restCoin:rest,limit:limit}}" class="to-withdraw" >提现</router-link>
    </div>
</template>
<script>
export default {
    props:['coin','rest','cny','limit']
}
</script>
<style lang="scss" scoped>
@import '~/css/wallet/restMoney.scss';
</style>