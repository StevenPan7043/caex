<template>
  <div class="quotes white f-fl">
    <div class="mini-ticker" id="miniTicker">
      <div class="mt-header f-cb">
        <span class="uppercase f-fl" :class="selectTab===3?'cur':''" @click="tabClick(3)">
            <i class="iconfont icon-androidstar"></i>
            {{$t('home.Favofites')}}
          </span>
        <span class="uppercase f-fl" :class="selectTab===0?'cur':''" @click="tabClick(0)">zc</span>
        <span class="uppercase f-fl" :class="selectTab===1?'cur':''" @click="tabClick(1)">eth</span>
        <span class="uppercase f-fl" :class="selectTab===2?'cur':''" @click="tabClick(2)">usdt</span>
      </div>
      <div class="mt-data-wrap">
        <div class="mt-data-main" id="miniTickerList">
          <div class="cur" id="miniZCTicker">
            <dl class="f-cb" @click="toUserTicker(item)" v-for="(item,index) in areaTicker" :key="index">
              <dt></dt>
              <dd>
                <div class="coin-unit media-coin-unit">
                  <span>
                      <i
                        class="iconfont icon-androidstaroutline"
                        v-show="!item.position"
                        @click.stop="starClick(item,index)"
                      ></i>
                      <i
                        class="iconfont icon-androidstar"
                        v-show="item.position"
                        @click.stop="starClick(item,index)"
                      ></i>
                      <em class="base-currency">{{' ' + item.baseCurrencyName}}</em>
                    </span>
                  <span class="price">{{item.close.toFixed(item.pricePrecision)}}</span>
                  <span class="rate rise" v-if="item.tickerRiseFallPercent>0">{{item.tickerRiseFallPercent|isNaNFilter}}%</span>
                  <span class="rate fall" v-else>{{item.tickerRiseFallPercent|isNaNFilter}}%</span>
                </div>
              </dd>
            </dl>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {
    setTrade
  } from "@/libs/utils";
  export default {
    props: {
      allTicker1: Array,
      allTicker2: Array,
      allTicker3: Array,
      userFavoriteArr: Array
    },
    watch: {
      allTicker1() {
        if (this.selectTab === 0) {
          this.areaTicker = this.allTicker1.filter(
            item =>
            item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !==
            -1
          );
        }
      },
      allTicker2() {
        if (this.selectTab === 1) {
          this.areaTicker = this.allTicker2.filter(
            item =>
            item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !==
            -1
          );
        }
      },
      allTicker3() {
        if (this.selectTab === 2) {
          this.areaTicker = this.allTicker3.filter(
            item =>
            item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !==
            -1
          );
        }
      },
      userFavoriteArr() {
        if (this.selectTab === 3) {
          this.ownerUserFavoriteArr = this.userFavoriteArr.filter(
            item =>
            item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !==
            -1
          );
        }
      }
    },
    data() {
      return {
        selectTab: 0,
        areaTicker: [],
        searchText: "",
      };
    },
    filters: {
      isNaNFilter(val) {
        if (isNaN(val)) {
          return 0;
        } else {
          return val;
        }
      }
    },
    mounted() {
      this.areaTicker = this.allTicker1;
      
    },
    methods: {
      searchChange() {
        this.tabClick(this.selectTab);
      },
      tabClick(index) {
        this.selectTab = index;
        let selectTabTicker = [];
        switch (index) {
          case 0:
            selectTabTicker = this.allTicker1;
            break;
          case 1:
            selectTabTicker = this.allTicker2;
            break;
          case 2:
            selectTabTicker = this.allTicker3;
            break;
          default:
            selectTabTicker = this.userFavoriteArr;
            break;
        }
        this.areaTicker = selectTabTicker.filter(
          item =>
          item.symbol.toUpperCase().indexOf(this.searchText.toUpperCase()) !==
          -1
        );
      },
      toUserTicker(item) {
        this.$emit("to-user-ticker", item);
        setTrade(item.symbol.toUpperCase());
      },
      starClick(item) {
        this.$emit("star-click", item);
        setTimeout(() => {
          this.tabClick(1);
        }, 2000);
      },
    }
  };
</script>

<style scoped>
  
</style>
