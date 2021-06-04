<template>
  <div class="count-conatiner">
    <p class="count">
      {{count}}
    </p>
    <p class="name">
      {{name}}
    </p>
  </div>
</template>
<script>
export default {
  props: ['count', 'name'],
};
</script>

<style scoped>
.count-conatiner{
  width: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.count{
  font-size: 18px;
  color: #0ca495;
}
.name{
  font-size: 14px;
  color: #999999;
  margin-top: 11px;
}
</style>
