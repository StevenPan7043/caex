<template>
  <div class="main_container">
    <div class="title">
      {{ $t('contract.USDTContractTransaction') }}
    </div>
    <div class="body">
      <div class="search">
        <i class="iconfont icon-search"></i>
        <input type="text" class="serach_input" placeholder="搜索" />
      </div>
      <div class="table_header">
        <div class="left">
          <p>{{ $t('contract.Contract') }}</p>
          <p>{{ $t('contract.LatestPrice') }}</p>
        </div>
        <div class="right">{{ $t('contract.Applies') }}</div>
      </div>
      <div class="table_body">
        <div
          class="table_item"
          :class="symbol == item.name ? 'select' : ''"
          v-for="(item, index) in symbolList"
          @click="changeSymbol(item)"
          :key="index"
        >
          <div class="left">
            <p>{{ item.name }}{{ $t('contract.Sustainable') }}</p>
            <p>{{ item.usdtPrice }}</p>
          </div>
          <div
            class="right"
            :class="item.isout === 0 ? 'red' : 'green'"
            v-if="item.symbol == 'fil_usdt'"
          >
            {{ item | riseFallPercent(item) }}%
          </div>
          <div class="right" :class="item.isout === 0 ? 'red' : 'green'" v-else>
            {{ item.tickerRiseFallPercent | isNaNFilter }}%
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'symbol_container',
  props: ['symbolList', 'symbol', 'coinPrice'],
  filters: {
    isNaNFilter(val) {
      if (isNaN(val)) {
        return 0
      } else {
        return val
      }
    },
    riseFallPercent(ticker) {
      let open = ticker.openVal.toFixed(ticker.pricePrecision)
      let close = ticker.usdtPrice.toFixed(ticker.pricePrecision)
      let sign
      if (open - close < 0) {
        sign = '+'
      } else {
        sign = ''
      }
      if (open != 0) {
        return sign + (((close - open) * 100) / open).toFixed(2)
      } else {
        return '0.00'
      }
    },
  },
  methods: {
    changeSymbol(item) {
      this.$emit('changeSymbol', item.name)
    },
  },
}
</script>

<style scoped>
.main_container {
  width: 100%;
  height: 465px;
  margin-bottom: 6px;
}

.title {
  width: 100%;
  height: 45px;
  background-color: #f2f6fa;
  font-size: 14px;
  color: #25425a;
  padding-left: 20px;
  box-sizing: border-box;
  line-height: 45px;
}

.body {
  height: 420px;
  width: 100%;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.search {
  width: 314px;
  height: 30px;
  border: solid 1px #e8eef5;
  margin-top: 15px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  box-sizing: border-box;
}
.search i {
  margin-right: 10px;
}
.serach_input {
  border-width: 0;
  outline: none;
  width: 100%;
  height: 100%;
  font-size: 12px;
  color: #718bab;
}
.serach_input::-webkit-input-placeholder {
  font-size: 12px;
  color: #718bab;
}
.serach_input:-moz-placeholder {
  font-size: 12px;
  color: #718bab;
}
.serach_input:-ms-input-placeholder {
  font-size: 12px;
  color: #718bab;
}

.table_header {
  height: 37px;
  width: 100%;
  padding: 16px 30px 0 20px;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  color: #91a4b7;
  font-size: 12px;
}

.table_header .left {
  width: 211px;
  display: flex;
  justify-content: space-between;
}

.table_header .right {
  display: flex;
  justify-content: flex-end;
}
.table_body {
  height: 338px;
  width: 100%;
  overflow: scroll;
}

.table_item {
  height: 26px;
  width: 100%;
  padding: 0 20px 0 20px;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  color: #25425a;
  font-size: 12px;
  align-items: center;
  cursor: pointer;
}
.table_item:hover {
  background-color: #f2f6fa;
}
.select {
  background-color: #f2f6fa;
}

.table_item .left {
  width: 211px;
  display: flex;
  justify-content: space-between;
}

.table_item .right {
  display: flex;
  justify-content: flex-end;
}
.red {
  color: #da3f4d;
}
.green {
  color: #5bbe8e;
}
</style>
