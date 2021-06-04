<template>
    <div >
        <div class="custom_radio " :class="cur?'cur':''">
        <slot></slot>
        </div>
        <i class="iconfont icon-xuanzhong " v-show="cur"></i>
    </div>
</template>

<script>
export default {
    props:{
        cur:{
            default:false,
            type:Boolean
        }
    }
}
</script>

<style scoped>

 .iconfont{
  position: relative;
  left: 124px;
  top: -17px;
  color: #196bdf;
}
.custom_radio{
  width: 138px;
  height: 38px;
  border: 1px solid #5a7896;
  border-radius: 5px;
  text-align: center;
  line-height: 38px;
  margin-right: 20px;
  cursor: pointer;
}
.cur{
  border: 1px solid  #196bdf;;
 color: #196bdf;
}
</style>