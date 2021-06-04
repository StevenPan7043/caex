<template>
  <div class="my-header">
    <div class="left">
      <a @click.prevent="toZZEX" >
        <img src="../../assets/images/logo.png" alt="全球领先区块链数字资产交易平台" class="logo" style="width: 200px;height: 32px" />
      </a>
      <ul class="l-nav">
        <li class="l-nav-item">
          <!-- <router-link tag="a" to="/legaltender">法币交易</router-link> -->
          <a @click.prevent="toOtc" >法币交易</a>
        </li>
        <li class="l-nav-item">
          <a @click.prevent="coin2Coin" >币币交易</a>
          <!-- <router-link to="/tradecenter">币币交易</router-link> -->
        </li>
        <li class="l-nav-item down-code">
          <a href="https://hotdogvip.com/#/downloadPage">APP下载</a>
        </li>
        <li class="l-nav-item">
          <router-link to="/dataLab">数据实验室</router-link>
        </li>
      </ul>
    </div>
    <div class="right">
      <ul class="r-nav">
        <li class="r-nav-item" v-if="dataLab">
          <router-link :to="{name:'dataLab'}">持仓数据</router-link>
        </li>
        <li class="r-nav-item" v-if="dataTrade">
          <router-link :to="{name:'tradeData'}">交易数据</router-link>
        </li>
        <li class="r-nav-item" v-if="dataCollection">
          <router-link :to="{name:'inOut'}">充提数据</router-link>
        </li>
        <li class="r-nav-item" v-if="dataWallet">
          <router-link :to="{name:'wallet'}">我的钱包</router-link>
        </li>
        <li class="r-nav-item">
          <el-dropdown>
            <span class="el-dropdown-link" @mouseenter="handleUp" @mouseleave="handleDown">
              切换交易对
              <i :class="[down_icon ? 'el-icon-caret-bottom' : 'el-icon-caret-top']"></i>
            </span>
            <el-dropdown-menu slot="dropdown" style="overflow:hidden;margin-top:0;">
              <el-dropdown-item v-for="(item,index) in pairList" :key="item.id">
                <span
                  @click="choseCoin(item.currencyPair,index)"
                  :class="[curIndex == index ? 'active' :'']"
                >{{ item.currencyPair }}</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { getFeeCurrencyPairList } from "@/api/header/index.js";
import { mapMutations } from "vuex";
import { zzexUrl, otcUrl } from "../../../public/config.js";
export default {
  created() {
    this._getFeeCurrencyPairList();
    // this.choseCoint(this.pairList[0].)0
    // this.initPairList();
    let index = localStorage.getItem("curIndex")
      ? localStorage.getItem("curIndex")
      : 0;
    this.curIndex = parseInt(index);
  },
  data() {
    return {
      idx: "",
      authList: "",
      curIndex: 0,
      is_show_code: false,
      down_icon: true,
      pairList: "",
      baseAuth: {}
    };
  },

  computed: {
    dataLab() {
      return this.filterAuth("dataLab");
    },
    dataTrade() {
      return this.filterAuth("dataTrade");
    },
    dataCollection() {
      return this.filterAuth("dataCollection");
    },
    dataWallet() {
      return this.filterAuth("dataWallet");
    }
  },
  watch: {
    authList: function() {
      let baseAuth = {
        dataLab: this.dataLab,
        tradeData: this.dataTrade,
        inOut: this.dataCollection,
        wallet: this.dataWallet
      };
      let toWithDrawandRec = {};
      if (baseAuth.wallet) {
        toWithDrawandRec.withDraw = true;
        toWithDrawandRec.withdrawRec = true;
        Object.assign(baseAuth, toWithDrawandRec);
      }
      this.changeAuths(baseAuth);
      for (var prop in baseAuth) {
        if (baseAuth[prop]) {
          if (
            this.$route.path.indexOf(prop) > 0 ||
            baseAuth[this.$route.name]
          ) {
            return;
          }
          this.$router.push({
            path: prop,
            params: { auth: baseAuth[prop] }
          });
          return;
        }
      }
    }
  },
  methods: {
    ...mapMutations("coinType", ["changeName","changeBaseWQ","changeValuationWQ"]),
    ...mapMutations("auth", ["changeAuths"]),
    filterAuth(show_item) {
      let temp = "";
      if (this.authList.length != 0) {
        this.authList.forEach(item => {
          if (item[show_item] !== undefined) {
            temp = item[show_item];
          }
        });
      }
      return temp;
    },
    choseCoin(val, index) {
      this.curIndex = index;
      localStorage.setItem("curIndex", this.curIndex);
      this.authList = this.pairList[index].authorityList;
      this.changeName({ name: val });
    },
    handlEnter() {
      this.is_show_code = true;
    },
    handlLeave() {
      this.is_show_code = false;
    },
    handleUp() {
      this.down_icon = false;
    },
    handleDown() {
      this.down_icon = true;
    },
    _getFeeCurrencyPairList() {
      return getFeeCurrencyPairList().then(res => {
        if (res.data.state == 1) {
          let val = res.data.data;
          let index = localStorage.getItem("curIndex")
            ? localStorage.getItem("curIndex")
            : 0;
          let _val = val[parseInt(index)].currencyPair;
          this.authList = val[parseInt(index)].authorityList;
          this.changeName({ name: _val });
          this.changeBaseWQ({base:val[parseInt(index)].baseWQuota})
          this.changeValuationWQ({Valuation:val[parseInt(index)].valuationWQuota})
          this.pairList = res.data.data;
        }else{
          this.$router.push('default')
        }
      });
    },
    toZZEX() {
       window.location.href = zzexUrl;
    },
    toOtc() {
       window.location.href = otcUrl;
    },
    coin2Coin() {
      window.location.href = zzexUrl + '/#/tradecenter';
    }
  }
};
</script>
<style lang="scss">
@import "~/css/baseLayout/head.scss";
</style>
