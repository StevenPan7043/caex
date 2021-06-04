<template>
  <div>
    <my-assets
      :lastList="combineList"
      :coinList="coinList"
      :userInfo="userInfo"
      :cashDeposit="cashDeposit"
      :totalBTC="totalBTC"
      :totalZC='totalZC'
      @reload-page="pageInit"
    />
    <assets-detail
      class="assets_detail"
      :accountDetail="accountDetail"
      :total="total"
      :pageSize="pageSize"
      @page-change="pageChange"
      @coin-change="coinChange"
      @type-change="typeChange"
    />
  </div>
</template>
<script>
import MyAssets from './myAssets.vue';
import AssetsDetail from './assetsDetail.vue';
import { logoutApi } from '@/api/GetCode';
import { customToFixed, chooseBackgroundColor } from '@/libs/utils';
import {
  getMember,
  getAssetsLst,
  getCurrencyList,
  getAccountDetail,
  getMerchantInfo,
  getMoneyByCoinType,
  getAllTicker,
  getAssets,
  getUsdtPrice,
} from './api';

export default {
  components: {
    MyAssets,
    AssetsDetail,
  },
  data() {
    return {
      userInfo: {},
      // 支持的法币列表
      supportCurList: [],
      coinList: [],
      // 过滤条件
      filter: {
        coin: 'all',
        type: 'all',
      },
      // 资产明细
      accountDetail: [],
      // 资产明细总数
      total: 0,
      // 保证金
      cashDeposit: '',
      bibiZZEX: null,
      fabiZZEX: null,
      page: 1,
      pageSize: 10,
      fabiList: [],
      bibiList: [],
      combineList: null,
      assetRatio: {},
      totalZC: '0',
      totalBTC: '0',
      BTCPrice: '0',
      hasGetBiBI: false,
      UsdtPrice: 0,
    };
  },
  mounted() {
    this.pageInit();
    this.tokenLogin();
  },
  methods: {
    async getUsdt() {
      const res = await getUsdtPrice();
      [this.UsdtPrice] = res.USDT_CNY;
      const BTC_USDT = res.BTC_USDT[0]
      this.BTCPrice = BTC_USDT*this.UsdtPrice
    },
    tokenLogin() {
      const { token } = this.$router.history.current.query;
      if (token) {
        localStorage.setItem('token', token);
        this.isUserLogin();
        this.$router.push('/assets');
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
    async pageInit() {
      this.hasGetBiBI = false;
      this.combineList = null;
      this.fabiList = [];
      this.bibiList = [];
      await this.getUsdt();
      this.getAsset();
      await getMember().then(({ data, msg, state }) => {
        if (state === 1) {
          this.userInfo = data;
          this.getAccountDetail();
          this.getMerchantInfo();
        }
        if (msg === 'LANG_NO_LOGIN') {
          this.logout();
          throw new Error();
        }
      });
      await getCurrencyList().then(({ data }) => {
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
    },

    async getAsset() {
      const { data } = await getAssets();

      const combineList = [];
      data.fb.forEach((fb) => {
        data.bb.forEach((bb) => {
          if (fb.name === bb.name) {
            combineList.push({
              bibi: {
                currency: bb.name,
                enable: customToFixed(bb.enable, bb.name),
                frozen_balance: customToFixed(bb.frozen, bb.name),
                total_balance: customToFixed(bb.equal, bb.name),
              },
              fabi: {
                currency: fb.name,
                enable: customToFixed(fb.enable, fb.name),
                frozen_balance: customToFixed(fb.frozen, fb.name),
                total_balance: customToFixed(fb.equal, fb.name),
              },
            });
          }
        });
      });
      this.combineList = combineList;


      let totalZc = 0.0;
      data.fb.forEach((fb) => {
        totalZc += fb.equal;
      });
      this.totalZC = (totalZc ).toString();
      this.totalBTC = customToFixed(totalZc / this.BTCPrice, 'BTC');
    },

    getZZEXMoney() {
      const params = {
        memberId: this.userInfo.uid,
        currency: 'ZZEX',
      };
      getMoneyByCoinType(params).then(({ data }) => {
        this.getAllMoneyList();
        this.fabiList = [];
        if (data) {
          this.fabiList.push({
            currency: 'ZZEX',
            data,
          });
        } else {
          this.fabiList.push({
            currency: 'ZZEX',
            data: {
              currency: 'ZZEX',
              frozenBalance: 0,
              totalBalance: 0,
            },
          });
        }
      });
    },

    getAllMoneyList() {
      this.supportCurList.forEach((ele) => {
        const params = {
          memberId: this.userInfo.uid,
          currency: ele,
        };
        getMoneyByCoinType(params).then(({ data }) => {
          this.getBiBIMoney();
          if (data) {
            this.fabiList.push({
              currency: ele,
              data,
            });
          } else {
            this.fabiList.push({
              currency: ele,
              data: {
                currency: ele,
                frozenBalance: 0,
                totalBalance: 0,
              },
            });
          }
        });
      });
    },

    getBiBIMoney() {
      if (this.hasGetBiBI) {
        return;
      }
      this.hasGetBiBI = true;
      getAssetsLst().then(({ data }) => {
        const recdata = data.accountsLst;
        if (recdata) {
          let zzexFlag = false;
          recdata.forEach((ele) => {
            if (ele.currency === 'ZZEX') {
              zzexFlag = true;
              this.bibiList.push({
                currency: 'ZZEX',
                data: ele,
              });
            }
          });
          if (!zzexFlag) {
            this.bibiList.push(
              {
                currency: 'ZZEX',
                data: {
                  balance: 0,
                  currency: 'ZZEX',
                  frozen_balance: 0,
                  total_balance: 0,
                  zc_balance: 0,
                },
              },
            );
          }
        } else {
          this.bibiList.push(
            {
              currency: 'ZZEX',
              data: {
                balance: 0,
                currency: 'ZZEX',
                frozen_balance: 0,
                total_balance: 0,
                zc_balance: 0,
              },
            },
          );
        }
        this.supportCurList.forEach((el) => {
          if (recdata) {
            let flag = false;
            recdata.forEach((ele) => {
              if (ele.currency === el) {
                flag = true;
                this.bibiList.push({
                  currency: el,
                  data: ele,
                });
              }
            });
            if (!flag) {
              this.bibiList.push({
                currency: el,
                data: {
                  balance: 0,
                  currency: el,
                  frozen_balance: 0,
                  total_balance: 0,
                  zc_balance: 0,
                },
              });
            }
          } else {
            this.bibiList.push({
              currency: el,
              data: {
                balance: 0,
                currency: el,
                frozen_balance: 0,
                total_balance: 0,
                zc_balance: 0,
              },
            });
          }
        });
        this.combineListFunc();
      });
    },
    combineListFunc() {
      const combineList = [];
      this.fabiList.forEach((ele) => {
        this.bibiList.forEach((el) => {
          if (ele.currency === el.currency) {
            combineList.push({
              fabi: ele.data,
              bibi: el.data,
            });
          }
        });
      });

      combineList.forEach((ele) => {
        ele.bibi.total_balance = customToFixed(ele.bibi.total_balance, ele.bibi.currency);
        ele.bibi.frozen_balance = customToFixed(ele.bibi.frozen_balance, ele.bibi.currency);
        ele.bibi.enable = customToFixed(ele.bibi.total_balance - ele.bibi.frozen_balance, ele.bibi.currency);

        ele.fabi.totalBalance = customToFixed(ele.fabi.totalBalance, ele.fabi.currency);
        ele.fabi.frozenBalance = customToFixed(ele.fabi.frozenBalance, ele.fabi.currency);
        ele.fabi.enable = customToFixed(ele.fabi.totalBalance - ele.fabi.frozenBalance, ele.fabi.currency);
      });
      let totalZC = 0;

      combineList.forEach((ele) => {
        const curr = ele.fabi.currency;
        let cashTotal = 0;

        this.assetRatio.forEach((el) => {
          if (Object.keys(el)[0] === curr) {
            cashTotal = el[curr] * (ele.fabi.totalBalance);
          }
        });
        totalZC += cashTotal;
      });

      this.totalZC = customToFixed(totalZC, 'CNY');

      this.combineList = combineList;
    },
    coinChange(coin) {
      this.filter.coin = coin;
      this.getAccountDetail();
    },
    typeChange(type) {
      this.filter.type = type;
      this.getAccountDetail();
    },
    getAccountDetail() {
      const params = {
        memberId: this.userInfo.uid,
        page: this.page,
        pagesize: this.pageSize,
      };
      if (this.filter.coin !== 'all') {
        params.currency = this.filter.coin;
      }
      if (this.filter.type !== 'all') {
        params.procType = this.filter.type;
      }
      getAccountDetail(params).then(({ data }) => {
        const recData = data.rows;
        recData.forEach((ele) => {
          ele.num = customToFixed(ele.num, ele.currency);
        });
        this.accountDetail = recData;
        this.total = data.total;
      });
    },
    getMerchantInfo() {
      getMerchantInfo().then(({ data }) => {
        if (data) {
          this.cashDeposit = `${data.depositVolume} ${data.depositCurrency}`;
        } else {
          this.cashDeposit = '0';
        }
      });
    },
    // 获取各币种与zc的兑换比率
    getAllTickerPrice() {
      const assetRatio = [];
      assetRatio.push({ ZC: 1 });
      getAllTicker().then(({ data }) => {
        this.supportCurList.forEach((ele) => {
          if (ele !== 'ZC') {
            const smsg = data[`${ele.toLowerCase()}zc_ticker`].close;
            const cash = {};
            cash[ele] = smsg;
            assetRatio.push(cash);
          }
        });
        const BTCRatio = data.btczc_ticker.close;
        this.BTCPrice = BTCRatio;
        const ZZEXRatio = data.zzexzc_ticker.close;
        assetRatio.push({ ZZEX: ZZEXRatio });
        this.assetRatio = assetRatio;
        this.getZZEXMoney();
      });
    },
    pageChange(page) {
      this.page = page;
      this.getAccountDetail();
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
.assets_detail {
  margin-top: 80px;
}
</style>
