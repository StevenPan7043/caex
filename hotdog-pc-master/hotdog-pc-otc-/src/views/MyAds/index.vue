<template>
  <div>
    <my-tab class="my_tab"
      :coinList="coinList"
      @coin-change="coinChange"
      @type-change='typeChange'/>
    <table-header/>
    <table-item
      :adsList="adsList"
      :total="total"
      @page-change="pageChange"
      @cancel-order="cancelOrder"/>
  </div>
</template>
<script>

import MyTab from './components/tab.vue';
import TableHeader from './components/tableHeader.vue';
import TableItem from './components/tableItem.vue';
import {
  getOTCAds, getMember, getCurrencyList, cancelOrderApi, getMerchantInfo,
} from './api';
import { logoutApi } from '@/api/GetCode';

export default {
  components: {
    MyTab,
    TableHeader,
    TableItem,
  },
  data() {
    return {
      userInfo: {},
      page: 1,
      pageSize: 10,
      adsList: [],
      total: 0,
      coinList: [],
      coin: '',
      type: '',
    };
  },
  created() {
    this.pageInit();
  },
  methods: {
    async pageInit() {
      await getMerchantInfo().then(({ msg, data, state }) => {
        if (msg === 'LANG_NO_LOGIN') {
          this.logout();
          return;
        }
        if (data.status !== 'APPLY_PASSED') {
          throw new Error();
        }
      });
      await getCurrencyList().then(({ data }) => {
        const coinList = [{ label: '全部币种', value: '' }];
        data.forEach((ele) => {
          coinList.push({
            label: ele,
            value: ele,
          });
        });
        this.coinList = coinList;
      });
      await getMember().then(({ data, state, msg }) => {
        this.userInfo = data;
      });

      await this.getOtcList();
    },
    getOtcList() {
      const params = {
        memberId: this.userInfo.uid,
        page: this.page,
        pagesize: this.pageSize,
        // status: 'WATTING',
      };
      if (this.type) {
        params.type = this.type;
      }
      if (this.coin) {
        params.baseCurrency = this.coin;
      }
      getOTCAds(params).then(({ data }) => {
        this.adsList = data.rows;
        this.total = data.total;
      });
    },
    pageChange(page) {
      this.page = page;
      this.getOtcList();
    },
    coinChange(coin) {
      this.coin = coin;
      this.page = 1;
      this.getOtcList();
    },
    typeChange(type) {
      this.type = type;
      this.page = 1;
      this.getOtcList();
    },
    cancelOrder(id) {
      cancelOrderApi(id).then(({ data, state, msg }) => {
        if (state === 1) {
          this.$Message.success({
            content: '下架成功',
            duration: 2,
            closable: true,
          });
          this.pageInit();
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
.my_tab{
  margin-top: 63px;
}
</style>
