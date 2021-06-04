<template>
  <div>
    <div class="top-container">
      <div class="tab-container">
        <buy-or-sell-tab type="购买" :select="buySelect" @tab-click="tabClick" :currencyList="currencyList"/>
        <div class="split_line"/>
        <buy-or-sell-tab type="出售" :select="sellSelect" @tab-click="tabClick" :currencyList="currencyList"/>
      </div>
      <div class="filter-container">
        <type-coin-pay-select
          @coin-change="coinChange"
          @type-change="typeChange"
          @pay-change="payChange"
        />
      </div>
    </div>
    <my-table
      :tradeList="tradeList"
      :total="total"
      :pageSize="pagesize"
      :tradeType="tradeType"
       @page-change="pageChange"/>
    <!-- <online-service /> -->
  </div>
</template>
<script>


import MyTable from './table.vue';
import BuyOrSellTab from './components/BuyOrSellTab.vue';
import TypeCoinPaySelect from '@/components/TypeCoinPaySelect/index.vue';

import {
  getTradeOtcOrder, getCurrencyList, tokenLogin, getMember,
} from './api';
import { customToFixed, chooseBackgroundColor } from '@/libs/utils';


export default {
  components: {
    MyTable,
    BuyOrSellTab,
    TypeCoinPaySelect,
  },
  data() {
    return {
      buySelect: 1,
      sellSelect: 0,
      coinType: '',
      tradeType: 'SELL',
      page: 1,
      pagesize: 10,
      tradeList: [],
      total: 0,
      filter: {
        coin: 'all',
        type: 'all',
        payment: 'all',
      },
      currencyList: [],
      loop: null,
    };
  },
  mounted() {
    this.tokenLogin();
    this.pageInit(true);
    this.loop = setInterval(() => {
      this.pageInit(false);
    }, 6000);
  },
  beforeDestroy() {
    window.clearInterval(this.loop);
  },
  methods: {
    pageChange(page) {
      this.page = page;
      this.pageInit(false);
    },
    tokenLogin() {
      const { token } = this.$router.history.current.query;
      if (token) {
        localStorage.setItem('token', token);
        this.isUserLogin();
        this.$router.push('/home');
      }
    },
    isUserLogin() {
      getMember().then((res) => {
        if (res.state === 1) {
        // 用户处于登录状态，将状态保存到session
          const loginState = true;
          const userInfo = res.data;
          userInfo.color = chooseBackgroundColor(userInfo.uid);
          localStorage.setItem('userInfo', JSON.stringify(userInfo));
          localStorage.setItem('loginState', loginState);
          localStorage.setItem('useruid', userInfo.uid);
          localStorage.setItem('nickName', userInfo.m_nick_name);
          localStorage.setItem('mNameHidden', userInfo.m_name_hidden);
          this.$store.commit('SET_USERINFO', userInfo);
        } else {
          const loginState = false;
          localStorage.setItem('loginState', loginState);
        }
      });
    },
    tabClick(e) {
      const params = e;
      if (params.tabType === '购买') {
        this.buySelect = params.tabSelect;
        this.sellSelect = 0;
        this.tradeType = 'SELL';
        this.coinType = this.currencyList[this.buySelect - 1];
      } else {
        this.sellSelect = params.tabSelect;
        this.buySelect = 0;
        this.tradeType = 'BUY';
        this.coinType = this.currencyList[this.sellSelect - 1];
      }
      this.tradeList = [];
      this.pageInit(false);
    },
    async pageInit(firstLoad) {
      if (firstLoad) {
        await getCurrencyList().then(({ data }) => {
          const [coinType] = data;
          this.coinType = coinType;
          this.currencyList = data;
        });
      }
      const params = {
        type: this.tradeType,
        page: this.page,
        pagesize: this.pagesize,
        baseCurrency: this.coinType,
        sortname: 'price',
      };
      if (this.tradeType === 'SELL') {
        params.sortorder = 'asc';
      } else {
        params.sortorder = 'desc';
      }
      await getTradeOtcOrder(params).then((res) => {
        const tradeList = res.Rows.filter(ele => ele !== null && ele.baseCurrency === this.coinType);
        tradeList.forEach((ele) => {
          if (ele.totalDoneTrade === 0) {
            ele.completePerecent = '0%';
          } else {
            const cashPercent = ((1 - ele.totalComplainTrade / ele.totalDoneTrade) * 100).toFixed(2);
            const percent = `${cashPercent}%`;
            ele.completePerecent = percent;
          }
          const accountArray = [];
          for (const i in ele.account) {
            accountArray.push(ele.account[i]);
          }
          ele.accountArray = accountArray;

          ele.color = chooseBackgroundColor(ele.memberId);
        });
        tradeList.forEach((ele) => {
          ele.remainVolume = customToFixed(ele.remainVolume, ele.baseCurrency);
        });

        if (this.filter.payment !== 'all') {
          const afterFilterList = [];
          tradeList.forEach((ele) => {
            if (ele.accountArray.indexOf(this.filter.payment) > -1) {
              afterFilterList.push(ele);
            }
          });
          this.tradeList = afterFilterList;
        } else {
          this.tradeList = tradeList;
        }
        this.total = res.Total;
      });
    },

    coinChange(coin) {
      this.filter.coin = coin;
      this.pageInit();
    },
    typeChange(type) {
      this.filter.type = type;
      this.pageInit();
    },
    payChange(payment) {
      this.filter.payment = payment;
      this.pageInit();
    },

  },
};
</script>
<style scoped>
.top-container{
  width: 1200px;
  display: flex;
  justify-content: space-between;
  padding-top: 54px;
  height: 173px;
  box-sizing: border-box

}
.tab-container{
  display: flex;
  flex-direction: row;
  height: 60px;
}

.split_line{
  margin:0 100px;
  width: 2px;
  height: 60px;
  background-color: #ececec;
}
.el-input{
  display: flex;
  align-items: center;
  width: 160px;
}
.filter-container{
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 510px;
  height: 60px;
}

</style>
<style>
.el-input__inner{
  height: 36px;
}
</style>
