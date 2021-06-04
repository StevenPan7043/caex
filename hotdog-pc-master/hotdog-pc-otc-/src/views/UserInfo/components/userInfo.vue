<template>
  <div class="userinfo_container">
    <div class="left_container">
      <div class="left">
       <div class="my_avatar" :style="{backgroundColor:colorName}">{{nickName[0]}}</div>
      </div>
      <div class="right">
        <div class="name">{{nickName}}</div>
        <div class="real_name">
          <p>实名认证 </p>
          <i class="iconfont icon-duigou"></i>
        </div>
        <div class="register_timer">
          <p>注册时间：{{modifyTime.split(' ')[0]}}</p>
        </div>
      </div>
    </div>

    <div class="right_container">
      <trade-count :count='cashPledge' name='商家保证金'/>
      <trade-count :count='userTradeInfo.percent' name='30日完成率'/>
      <trade-count :count='userTradeInfo.totalDoneTrade' name='总成交单'/>
      <trade-count :count='userTradeInfo.done_30' name='30日成交单'/>
      <trade-count :count='userTradeInfo.consumingTime' name='平均放行'/>
    </div>
  </div>
</template>
<script>
import TradeCount from './tradeCount.vue';

export default {
  name: 'UserInfo',
  components: {
    TradeCount,
  },
  props: {
    userTradeInfo: Object,
    nickName: String,
    colorName: String,
    modifyTime: String,
    cashPledge: String,
  },
};
</script>

<style scoped>
.userinfo_container{
  width: 1200px;
  height: 96px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  justify-content: space-between;
  align-items: center
}
.left_container{
  display: flex;
  align-items: center;
  height: 96px;
}
.my_avatar{
  width: 66px;
  height: 66px;
  border-radius: 50%;
  font-size: 21px;
  line-height: 66px;
  color: #ffffff;
  text-align: center
}
.left_container .right{
  display: flex;
  flex-direction: column;
  height: 58px;
  justify-content: space-between;
  margin-left: 16px;
}
.left_container .right .name{
  font-size: 16px;
  line-height: 16px;
  color: #1e2629;
  font-weight: 500;
}
.left_container .right .real_name{
  display: flex;
  align-items: center;
}
.left_container .right .real_name p{
  font-size: 12px;
  color: #4f646a;
}
.left_container .right .real_name i{
  margin-left: 4px;
}
.register_timer{
  font-size: 12px;
  line-height: 12px;
  color: #4f646a;
}
.right_container{
  display: flex;
  height: 96px;
  align-items: center;
  width: 566px;
  justify-content: space-between
}
</style>
