<template>
  <div>
    <Select
      v-model="country"
      style="width:160px"
      @on-change="countryChange"
      placeholder="全部国家"
    >
      <Option
        v-for="item in countryList"
        :value="item.value"
        :key="item.value"
      >
        {{ item.label }}
      </Option>
    </Select>
    <Select
      v-model="coin"
      style="width:160px"
      placeholder="全部法币"
      @on-change="coinChange"
    >
      <Option
        v-for="item in coinList"
        :value="item.value"
        :key="item.value"
      >
        {{ item.label }}
      </Option>
    </Select>
    <Select
      v-model="pay"
      style="width:160px"
      placeholder="支付方式"
      @on-change="payChange"
    >
      <Option
        v-for="item in payList"
        :value="item.value"
        :key="item.value"
      >
        {{ item.label }}
      </Option>
    </Select>
  </div>
</template>

<script>
export default {
  data() {
    return {
      country: 'all',
      coin: 'all',
      pay: 'all',
      countryList: [
        {
          value: 'all',
          label: '全部国家',
        },
        {
          value: 'ZH',
          label: '中国',
        },
      ],
      coinList: [
        {
          value: 'all',
          label: '全部法币',
        },
        {
          value: 'CNY',
          label: '人民币',
        },
      ],
      payList: [
        {
          value: 'all',
          label: '全部',
        },
        {
          value: 'BANK',
          label: '银行卡',
        },
        {
          value: 'ALIPAY',
          label: '支付宝',
        },
        {
          value: 'WXPAY',
          label: '微信',
        },
      ],
    };
  },
  methods: {
    countryChange() {
      this.$emit('country-change', this.country);
    },
    coinChange() {
      this.$emit('coin-change', this.coin);
    },
    payChange() {
      this.$emit('pay-change', this.pay);
    },
  },
};
</script>

<style scoped>
.ivu-select {
  margin:0 5px 0 5px;
}
</style>
