<template>
  <div>
    <my-header></my-header>
    <div class="bgm404"></div>
    <my-footer></my-footer>
  </div>
</template>

<script>
import MyHeader from '@/components/Header/index.vue'
import MyFooter from '@/components/Footer'
export default {
  // beforeCreate: function () {
  //   document.getElementsByTagName('body')[0].className = 'bgm404'
  // },
  // beforeDestroy: function () {
  //   document.body.removeAttribute('class', 'bgm404')
  // },
  components: {
    MyHeader,
    MyFooter,
  },
}
</script>

<style>
.bgm404 {
  width: 100%;
  height: 750px;
  background-image: url('../assets/images/404.png');
  background-size: 100%;
}
</style>