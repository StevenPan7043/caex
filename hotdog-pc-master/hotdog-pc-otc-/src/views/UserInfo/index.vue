<template>
  <div>
    <user-info
      class="user_info"
      :nickName="nickName"
      :colorName="colorName"
      :userTradeInfo="userTradeInfo"
      :modifyTime="modifyTime"
      :cashPledge="cashPledge"
    />
    <trade-panel
      tradeTitle='在线购买'
      class="trade_panel"
      :orders="sellOrders"
    />
    <trade-panel
      tradeTitle='在线出售'
      class="trade_panel"
      :orders="buyOrders"
    />
  </div>
</template>
<script>
import UserInfo from './components/userInfo.vue';
import TradePanel from './components/tradePanel.vue';
import { getTradeInfo, getOTCOrder, getMerchantInfo } from './api';
import { logoutApi } from '@/api/GetCode';
import { chooseBackgroundColor } from '@/libs/utils';

export default {
  components: {
    UserInfo,
    TradePanel,
  },
  data() {
    return {
      memberId: null,
      nickName: null,
      colorName: null,
      userInfo: {},
      userTradeInfo: {},
      realnameText: '',
      buyOrders: '',
      sellOrders: '',
      // 商家注册时间
      modifyTime: null,
      // 押金信息
      cashPledge: null,
    };
  },
  created() {
    this.memberId = localStorage.getItem('check-merchant-id');
    this.colorName = chooseBackgroundColor(this.memberId);
    this.nickName = localStorage.getItem('check-merchant-nickName');
    this.pageInit();
  },
  methods: {
    pageInit() {
      // 获取商家购买的ad
      const buyParams = {
        memberId: this.memberId,
        type: 'BUY',
        status: 'WATTING',
      };
      getOTCOrder(buyParams).then(({ data, state, msg }) => {
        if (state === 1) {
          const buyOrders = data.rows;
          buyOrders.forEach((ele) => {
            const accountArray = [];
            for (const i in ele.account) {
              accountArray.push(ele.account[i]);
            }

            ele.accountArray = accountArray;
            ele.color = chooseBackgroundColor(ele.memberId);
          });
          this.buyOrders = buyOrders;
        }
      });
      // 获取商家出售的ad
      const sellParams = {
        memberId: this.memberId,
        type: 'SELL',
        status: 'WATTING',
      };
      getOTCOrder(sellParams).then(({ data, state, msg }) => {
        if (state === 1) {
          const sellOrders = data.rows;
          sellOrders.forEach((ele) => {
            const accountArray = [];
            console.log(ele.totalDoneTrade);

            if (ele.totalDoneTrade === 0) {
              ele.completePerecent = '0%';
            } else {
              const cashPercent = ((1 - ele.totalComplainTrade / ele.totalDoneTrade) * 100).toFixed(2);
              const percent = `${cashPercent}%`;
              ele.completePerecent = percent;
            }
            console.log(ele.completePerecent);

            for (const i in ele.account) {
              accountArray.push(ele.account[i]);
            }

            ele.accountArray = accountArray;

            ele.color = chooseBackgroundColor(ele.memberId);
          });
          this.sellOrders = sellOrders;
        }
      });
      // 获取用户交易情况
      getTradeInfo(this.memberId).then(({ state, data, msg }) => {
        if (state === 1) {
          const userTradeInfo = data;
          userTradeInfo.consumingTime += '分钟';
          if (userTradeInfo.totalDoneTrade === 0) {
            userTradeInfo.percent = '0%';
          } else {
            const cashPercent = ((1 - userTradeInfo.totalComplainTrade / userTradeInfo.totalDoneTrade) * 100).toFixed(2);
            const percent = `${cashPercent}%`;
            userTradeInfo.percent = percent;
          }
          userTradeInfo.totalDoneTrade += '次';
          userTradeInfo.done_30 += '次';
          this.userTradeInfo = userTradeInfo;
        }
      });
      // 获取押金信息、注册时间
      const params = {
        memberId: this.memberId,
      };
      getMerchantInfo(params).then(({ data, msg, state }) => {
        if (msg === 'LANG_NO_LOGIN') {
          this.logout();
        }
        if (state === 1) {
          this.modifyTime = data.modifyTime;
          const { depositVolume } = data;
          const { depositCurrency } = data;
          this.cashPledge = `${depositVolume}${depositCurrency}`;
        }
      });
    },
    logout() {
      this.$Message.error({
        content: '请先登录',
        duration: 2,
        closable: true,
      });
      const loginState = false;
      localStorage.removeItem('userInfo');
      localStorage.removeItem('useruid');
      localStorage.removeItem('nickName');
      localStorage.removeItem('mNameHidden');
      localStorage.setItem('loginState', loginState);
      this.$store.commit('CLEAR_USERINFO');
      this.$router.push('/login');
      logoutApi();
    },
  },
};
</script>

<style scoped>
.user_info {
  margin-top: 40px;
}
.trade_panel {
  margin-top: 65px;
}
</style>
