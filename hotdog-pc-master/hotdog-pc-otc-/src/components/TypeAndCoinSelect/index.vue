<template>
  <div>
    <Select
      v-model="coin"
      style="width:160px"
      placeholder="全部币种"
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
      v-model="type"
      style="width:160px"
      placeholder="交易类型"
    >
      <Option
        v-for="item in typeList"
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
  props: {
    coinList: Array,
    typeList: Array,
  },
  data() {
    return {
      coin: 'all',
      type: 'all',
    };
  },
};
</script>
<style scoped>
.ivu-select {
  margin:0 5px 0 5px;
}
</style>
