<template>
  <div class="first-child">
    <p>{{titleName}}</p>
    <p v-if="subtitle" class="subtitle">{{subtitle}}</p>
  </div>
</template>
<script>
export default {
  props: ['titleName', 'subtitle'],
};
</script>

<style scoped>
.first-child{
  width: 1170px;
  height: 59px;
  border-bottom: 1px solid #e1e1e1;
  margin-left: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.subtitle{
  font-size: 14px;
  color: #999999;
  margin-top: 9px;
}
</style>
