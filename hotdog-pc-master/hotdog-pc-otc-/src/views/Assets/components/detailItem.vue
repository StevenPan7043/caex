<template>
  <div class="item_container">
    <div class="first_child">{{time}}</div>
    <div class="middle_child">{{type}}</div>
    <div class="middle_child">{{coin}}</div>
    <div class="last_child">{{number}}&nbsp;&nbsp;{{coin}}</div>
  </div>
</template>
<script>
export default {
  props: ['time', 'type', 'coin', 'number'],
};
</script>

<style scoped>
.item_container{
  display: flex;
  height: 50px;
  font-size: 14px !important;
}
.first_child{
  width: 25%;
  display: flex;
  align-items: center;
  font-size: 14px !important;
}
.middle_child{
  width: 25%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px !important;
}
.last_child{
  width: 25%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 14px !important;
}
</style>
