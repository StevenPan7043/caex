<template>
  <div class="main_container">
    <div class="title">{{type}}</div>
    <div class="type_container">
      <div :class="select===index+1 ?'cur':''"
        @click="tabClick(index+1)"
        v-for="(item,index) in currencyList"
        :key="index"
      >{{item}}</div>
    </div>
  </div>
</template>
<script>

export default {
  props: {
    type: {
      type: String,
    },
    select: {
      type: Number,
      default: 0,
    },
    currencyList: {
      type: Array,
    },
  },
  data() {
    return {

    };
  },

  methods: {
    tabClick(e) {
      const params = {
        tabType: this.type,
        tabSelect: e,
      };
      this.$emit('tab-click', params);
    },

  },
};
</script>

<style scoped>
.main_container{
  width: 155px;
  height: 60px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.title{
  margin-top: 5px;
  font-size: 14px;
  color: #333333;
}
.type_container{
  display: flex;
  font-size: 16px;
  letter-spacing: 1px;
  color: #3b78c3;
  justify-content: space-between;
  align-items: flex-start;
  height: 22px;
}
.type_container div{
  padding-bottom: 4px;
  cursor: pointer;
}
.cur{
  border-bottom: 2px solid #333333;
  color: #333333
}
</style>
