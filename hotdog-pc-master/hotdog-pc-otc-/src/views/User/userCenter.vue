<template>
  <div class="usercenter-container">
    <my-title titleName='个人中心'/>
    <div class="second-child">
      <div class="user-info">
        <div class="left-container">
          <div class="head-icon" :style="{backgroundColor:userInfo.color}">
            {{userInfo.m_nick_name!==undefined&&userInfo.m_nick_name.substr(0,1)}}
          </div>
          <div class="username-container">
            <p>{{userInfo.m_name_hidden}}</p>
            <div class="nick-container">
              <p>昵称：<span>{{userInfo.m_nick_name||'未设置'}}</span></p>
            </div>
            <div>ID: <span>{{userInfo.uid}}</span></div>
          </div>

          <div class="auth-container">
            <i class="iconfont icon-gantanhao yellow" v-if="realnameText ==='未认证'"></i>
            <i class="iconfont icon-duigou green" v-else></i>
            <p
              :class="realnameText==='未认证'?'yellow':'green'"
            >{{realnameText==='未认证'?'未认证':'已认证'}}</p>
          </div>
        </div>

        <div class="right-container">
          <trade-count name="总成交单" :count="userTradeInfo.totalDoneTrade"/>
          <trade-count name="30日成交单" :count="userTradeInfo.done_30"/>
          <trade-count name="30日完成率" :count="userTradeInfo.percent"/>
          <trade-count name="平均放行" :count="userTradeInfo.consumingTime"/>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import TradeCount from './components/tradeCount.vue';
import MyTitle from './components/title.vue';

export default {
  components: {
    TradeCount,
    MyTitle,
  },
  props: {
    realnameText: {
      type: String,
    },
    userInfo: {
      type: Object,
    },
    userTradeInfo: {
      type: Object,
    },
  },
};
</script>

<style scoped>
.usercenter-container {
  width: 1200px;
  height: 190px;
  background-color: #ffffff;
  box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.06);
  border-radius: 6px;
  margin-top: 24px;
  display: flex;
  flex-direction: column;
}

.usercenter-container .first-child p {
  font-size: 18px;
  color: #39404f;
  line-height: 59px;
}
.usercenter-container .second-child {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 130px;
}
.user-info {
  width: 1140px;
  height: 60px;
  display: flex;
  justify-content: space-between;
}
.left-container {
  display: flex;
  width: 273px;
  justify-content: space-between;
}
.head-icon {
  width: 66px;
  height: 66px;
  background-color: #63cdba;
  border-radius: 50%;
  text-align: center;
  line-height: 66px;
  font-size: 33px;
  color: #ffffff;
}
.username-container {
  display: flex;
  flex-direction: column;
  width: 100px;
  justify-content: space-between;
}
.username-container p {
  font-size: 16px;
  color: #000000;
}
.username-container div {
  font-size: 12px;
  color: #999999;
}
.nick-container {
  display: flex;
  justify-content: space-between;
}
.nick-container p {
  font-size: 12px;
  color: #999999;
}
.auth-container {
  display: flex;
}
.auth-container p, .auth-container i{
  font-size: 14px;
  margin-right: 7px;
}
.green{
  color: #088a68;
}
.yellow{
  color: #f17e06
}
.right-container{
  display: flex;
  width: 500px;
  justify-content: space-between;
  align-items: center
}
</style>
