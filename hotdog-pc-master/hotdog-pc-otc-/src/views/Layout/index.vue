<template>
  <div>
    <my-header></my-header>
    <div class="main-container">
      <router-view></router-view>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import MyHeader from './components/Header.vue';
import MyFooter from './components/Footer.vue';

export default {
  components: {
    MyHeader,
    MyFooter,
  },
};

</script>
<style scoped>
.main-container{
  width:100%;
  min-height: 800px;
  display: flex;
  flex-direction: column;
  align-items: center
}
</style>
