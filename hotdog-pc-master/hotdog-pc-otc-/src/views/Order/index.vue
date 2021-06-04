<template>
  <div>
    <my-tab class="my_tab"
    @tab-click="tabClick"
    @coin-change="coinChange"
    @type-change="typeChange"
    :typeList="typeList"
    :coinList="coinList"/>
    <form-header/>
    <div class="table_container">
      <div class="tab_body_container" v-for="item in rows" :key="item.id">
        <div class="flex-start order_number" style="width:15.5%">
          <p><a @click.prevent="toOrderDetail(item)">{{item.tNumber}}</a></p>
        </div>
        <div class="flex-start" style="width:17%">
          <p class="buy" :class="item.tType==='BUY'?'green':'red'">{{item.tType|tTypeFilter}}</p><p>{{item.volume}} {{item.baseCurrency}}</p>
        </div>
        <div class="flex-start" style="width:11.5%">
          <p>{{(item.price*item.volume).toFixed(2)}}CNY</p>
        </div>
        <div class="flex-start" style="width:11.5%">
          <p>{{item.price}} CNY</p>
        </div>
        <div class="flex-start" style="width:11.5%">
          <p>0.000000 {{item.baseCurrency}}</p>
        </div>
        <div class="flex-start" style="width:12.5%">
          <p>{{item.createTime}}</p>
        </div>
        <div class="flex-start" style="width:9.5%">
          <p >
            <a @click.prevent="" v-if="item.oppositeStatus==='complaining'||(item.status).toLowerCase()==='complaining'">申诉中</a>
            <a @click.prevent="" v-else>{{item.status|statusFilter}}</a>

          </p>
        </div>
        <div class="flex-start" style="width:9.5%">
          <Poptip  trigger='hover' :content="item.memoStr"  v-if="item.oppositeStatus==='complaining'||(item.status).toLowerCase()==='complaining'">
              <p ><a> {{item.memoStr!=null?item.memoStr.substring(0,6):item.memoStr}}...</a></p>
          </Poptip>
          <p  v-else>无</p>
        </div>
        <div class="flex-end" style="cursor:pointer" @click="toUserInfo(item)">
          <p class="business">{{item.opposite_nick_name}}</p>
        </div>
      </div>
    </div>
    <div class="page_container">
      <Page :total="total" :pagesize="pagesize" @on-change="pageChange" show-elevator/>
    </div>
  </div>
</template>
<script>
import MyTab from './components/tab.vue';
import FormHeader from './components/formHeader.vue';
import {
  getTradePage, getCurrencyList, getTradingPage, getComplainingPage, getMember,
} from './api';
import { logoutApi } from '@/api/GetCode';
import { chooseBackgroundColor } from '@/libs/utils';

export default {
  components: {
    MyTab,
    FormHeader,
  },
  data() {
    return {
      userInfo: {},
      status: 'WATTING',
      page: 1,
      pagesize: 10,
      rows: [],
      total: 0,
      coin: 'all',
      type: 'all',
      coinList: [],
      typeList: [
        {
          label: '全部交易类型',
          value: 'all',
        },
        {
          label: '购买',
          value: 'BUY',
        },
        {
          label: '出售',
          value: 'SELL',
        },
      ],
    };
  },
  filters: {
    tTypeFilter(val) {
      switch (val) {
        case 'SELL':
          return '出售';
        case 'BUY':
          return '购买';
        default:
          return '';
      }
    },
    statusFilter(val) {
      switch (val) {
        case 'NP':
          return '进行中';
        case 'UNCONFIRMED':
          return '进行中';
        case 'DONE':
          return '已完成';
        case 'CANCELED':
          return '已取消';
        case 'COMPLAINING':
          return '申诉中';
        default:
          return '';
      }
    },
  },
  created() {
    this.pageInit();
  },
  mounted() {
    this.tokenLogin();
  },
  methods: {
    tokenLogin() {
      const { token } = this.$router.history.current.query;
      if (token) {
        localStorage.setItem('token', token);
        this.isUserLogin();
        this.$router.push('/order');
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
    pageInit() {
      // 获取币种
      getCurrencyList().then(({ data }) => {
        this.supportCurList = data;
        const coinList = [{ label: '全部币种', value: 'all' }];
        data.forEach((ele) => {
          coinList.push({
            label: ele,
            value: ele,
          });
        });
        this.coinList = coinList;
      });

      this.getWaitingOrder();
    },
    // 获取除交易中订单
    getList() {
      const params = {
        page: this.page,
        pagesize: this.pagesize,
      };
      if (this.coin !== 'all') {
        params.baseCurrency = this.coin;
      }
      if (this.type !== 'all') {
        params.tType = this.type;
      }
      if (this.status) {
        params.status = this.status;
      }
      getTradePage(params).then(({ data, msg, state }) => {
        this.total = data.total;
        if (data.rows) {
          this.rows = data.rows;
        }
      });
    },
    // 获取交易中订单
    getWaitingOrder() {
      const params = {
        page: this.page,
        pagesize: this.pagesize,
      };
      if (this.coin !== 'all') {
        params.baseCurrency = this.coin;
      }
      if (this.type !== 'all') {
        params.tType = this.type;
      }
      getTradingPage(params).then(({ data, msg, state }) => {
        if (state === 1) {
          this.rows = data.rows;
          this.total = data.total;
        }
        if (msg === 'LANG_NO_LOGIN') {
          this.logout();
        }
      });
    },
    // tab切换
    tabClick(tab) {
      this.page = 1;
      this.status = tab;
      this.rows = [];
      if (this.status === 'WATTING') {
        this.getWaitingOrder();
      } else if (this.status === 'Complaining') {
        this.getComplainingList();
      } else {
        this.getList();
      }
    },
    // 币种切换
    coinChange(coin) {
      this.coin = coin;
      this.rows = [];
      if (this.status === 'WATTING') {
        this.getWaitingOrder();
      } else if (this.status === 'Complaining') {
        this.getComplainingList();
      } else {
        this.getList();
      }
    },
    // buy、sell切换
    typeChange(type) {
      this.type = type;
      this.rows = [];

      if (this.status === 'WATTING') {
        this.getWaitingOrder();
      } else if (this.status === 'Complaining') {
        this.getComplainingList();
      } else {
        this.getList();
      }
    },

    getComplainingList() {
      const params = {
        page: this.page,
        pagesize: this.pagesize,
      };
      if (this.coin !== 'all') {
        params.baseCurrency = this.coin;
      }
      if (this.type !== 'all') {
        params.tType = this.type;
      }
      getComplainingPage(params).then(({ data, msg, state }) => {
        if (state === 1) {
          this.rows = data.rows;
          this.total = data.total;
        }
        if (msg === 'LANG_NO_LOGIN') {
          this.logout();
        }
      });
    },
    // 分页切换
    pageChange(page) {
      this.page = page;
      this.rows = [];
      if (this.status === 'WATTING') {
        this.getWaitingOrder();
      } else if (this.status === 'Complaining') {
        this.getComplainingList();
      } else {
        this.getList();
      }
    },
    toOrderDetail(item) {
      localStorage.setItem('order-id', item.id);
      this.$router.push({ name: 'orderDetail', params: { id: item.id } });
    },
    toUserInfo(item) {
      localStorage.setItem('check-merchant-id', item.opposite_member_id);
      localStorage.setItem('check-merchant-nickName', item.opposite_nick_name);
      this.$router.push('/userInfo');
    },
    // 登出
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
  margin-top: 66px;
}

.tab_body_container{
  width: 1200px;
  height: 70px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #e1e1e1;
}
.tab_body_container div{
  font-size: 14px;
  color: #999999;
}
.tab_body_container .flex-start{
  display: flex;
  justify-content: flex-start;
  align-items: center;
  color: #333333
}
.tab_body_container .flex-end{
  width: 11%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.order_number{
  color:#3b78c3!important
}
.buy{
  margin-right: 5px;
}
.green{
  color: #149664 ;
}
.red{
  color: #ef4e48 ;
}
.sell{
  color: #f22e58 ;
  margin-right: 5px;
}
.business{
  color: #3b78c3;
}
.page_container{
  width:1200px;
  margin-top: 56px;
  display: flex;
  justify-content: center;
  height: 228px;
}
.table_container{
  height: 700px;
}
</style>
