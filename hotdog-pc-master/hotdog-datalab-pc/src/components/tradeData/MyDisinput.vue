<template>
  <div class="disable-input">
    <span :style="{marginRight:mr+'px'}">{{ desc }}</span>
    <input type="text" disabled :value="val" />
  </div>
</template>
<script>
export default {
    props:['val','desc','mr']
};
</script>
<style lang="scss" scoped>
@import "~/css/tradeData/myDisinput.scss";
</style>