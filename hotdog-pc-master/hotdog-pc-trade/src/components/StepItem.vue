<template>
  <div class="item">
    <div class="number" :class="select?'select':'unselect'">
      <p>{{number}}</p>
    </div>
    <div class="text">{{text}}</div>
  </div>
</template>
<script>
export default {
  props: {
    number: String,
    text: String,
    select: Boolean
  }
};
</script>
<style lang="less" scoped>
.select {
  background-color: #196bdf;
}
.unselect {
  background-color: #768d9a;
}
.item {
  display: flex;
  flex-direction: row;
  align-items: center;
  .number {
    width: 24px;
    height: 24px;
    border-radius: 12px;
    display: flex;
    justify-content: center;
    align-items: center;
    p {
      font-size: 14px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #ffffff;
    }
  }
  .text {
    margin-left: 10px;
    font-size: 14px;
    font-weight: normal;
    font-stretch: normal;
    letter-spacing: 0px;
    color: #6c859a;
  }
}
</style>