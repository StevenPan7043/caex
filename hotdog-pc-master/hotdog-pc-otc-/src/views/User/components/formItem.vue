<template>
  <div class="item-container">
    <div class="left-container">
      <i class="iconfont" :class="iconInfo"></i>
      <p class="item-name">{{title}}</p>
      <p>{{info||'未绑定'}}</p>
    </div>
    <div class="right-container">
      <p style="margin-right:26px">{{option2}}</p>
      <p @click="optionClick">{{option}}</p>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    title: String,
    info: String,
    option: String,
    option2: String,
    optionType: String,
    iconInfo: String,
  },
  methods: {
    optionClick() {
      this.$emit('option-click', this.optionType);
    },
  },
};
</script>

<style scoped>
.item-container{
  width: 1135px;
  height: 59px;
  border-bottom: 1px dotted #e5e5e5;
  margin-left: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.left-container{
  display: flex;
  justify-items: center
}
.left-container p{
  line-height: 24px;
}
.left-container i{
  margin-right: 20px;
}
.item-name{
  margin-right: 173px;
  width: 200px;
}

.right-container{
  display: flex;
  align-content: center;
  justify-content: flex-end;
}
.right-container p{
  font-size: 14px;
  color: #3b78c3;
  cursor: pointer;
}
</style>
