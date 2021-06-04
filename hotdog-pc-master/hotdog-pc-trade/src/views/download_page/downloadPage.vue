<template>
  <div>
    <my-header></my-header>
    <div class="main_container">
      <img src="../../assets/images/dp1.png" class="dp1">
      <img src="../../assets/images/dp2.png" class="dp2">
      <img src="../../assets/images/dp3.png" class="dp3"  style="width: 466px;height: 550px">
      <div class="text_container">
        <p class="title">{{$t('contract.Client')}}</p>
        <p class="sub_title">{{$t('contract.Market')}}</p>
        <div class="button_container">
          <div class="left">
            <a :href="url" target="_blank">
              <div class="button"><i class="iconfont icon-apple"></i>
                iOS
              </div>
            </a>
            <a :href="url" target="_blank">
              <div class="button"><i class="iconfont icon-android"></i>
                Android
              </div>
            </a>

          </div>
          <div class="right">
            <img :src="img" style="width: 106px;height: 106px">
          </div>
        </div>
      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>

<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import {baseUrl} from "../../../public/config.js";
import codes_test from '../../assets/images/codes_test.png';
import codes from'../../assets/images/codes.png'

export default {
  name: 'downloadPage',
  components: {
    MyHeader,
    MyFooter,
  },
  data() {
    return {
      url:baseUrl+'App/',
      img:baseUrl=='http://www.cxex.pro/'?codes_test:codes
    }
  },
}
</script>

<style scoped>
  .main_container {
    height: 933px;
    width: 100%;
    min-width: 1200px;
    position: relative;
  }

  .dp1 {
    position: absolute;
    right: 0;
    top: 0;
    width: 66%;
  }

  .dp2 {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 18%;
  }

  .dp3 {
    position: absolute;
    right: 18%;
    top: 129px;
  }

  .text_container {
    height: 200px;
    position: absolute;
    left: 15%;
    top: 284px;
    z-index: 999;
  }

  .text_container .title {
    font-size: 36px;
    color: #1C242C;
  }

  .text_container .sub_title {
    font-size: 20px;
    color: rgba(28, 36, 44, 1);
    margin-top: 20px;
  }

  .button_container {
    margin-top: 64px;
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .button_container .left {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 112px;
  }

  .button_container .left .button {
    width: 160px;
    height: 40px;
    background: rgba(212, 229, 253, 1);
    border: 1px solid rgba(79, 140, 227, 1);
    border-radius: 19px 19px 19px 19px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #196BDF;
    cursor: pointer;
  }

  .button_container .right img {
    width: 106px;
    height: 106px;
    margin-left: 50px;
  }
</style>
