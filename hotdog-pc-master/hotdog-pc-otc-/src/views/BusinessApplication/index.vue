<template>
  <div>
    <my-title class="my_title"></my-title>
    <div class="middle_container">
      <div class="card_container">
        <img src="../../assets/images/application3.png" alt="">
        <div>广告发布</div>
        <p>商家自由发布广告，买卖更灵活、更便捷。</p>
      </div>

      <div class="card_container">
        <img src="../../assets/images/application2.png" alt="">
        <div>做市场赚差价</div>
        <p>交易所让利给商家，获得高额差价报酬。</p>
      </div>

      <div class="card_container">
        <img src="../../assets/images/application1.png" alt="">
        <div>一对一服务</div>
        <p>我们将向商家提供一对一专属服务。</p>
      </div>
    </div>
    <aggrement class="agg"/>
  </div>
</template>
<script>
import MyTitle from './components/title.vue';
import Aggrement from './components/aggrement.vue';

export default {
  components: {
    MyTitle,
    Aggrement,
  },
};
</script>
<style scoped>
.my_title{
  margin-top: 88px;
}
.middle_container{
  width: 1075px;
  display: flex;
  justify-content: space-between;
  margin-top: 60px;
}
.agg{
  margin-top: 67px;
}
.card_container{
  width: 275px;
  height: 344px;
  box-shadow: 0px 0px 12px 0px rgba(4, 7, 6, 0.06);
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.icon_img{
  width: 64px;
  height: 64px;
  background-color: #50c8ff;
  border-radius: 50%;
}
.card_container div{
  font-size: 18px;
  line-height: 18px;
  color: #1e2629;
  margin-top: 33px
}
.card_container p{
  font-size: 14px;
  line-height: 24px;
  color: #8aa0a6;
  margin-top: 14px;
  width: 182px;
  text-align: center;
}
</style>
