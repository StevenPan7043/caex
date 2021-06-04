<template>
  <div>
    <my-header></my-header>
    <div class="rank">
      <div class="rank_topbars" ondragstart="return false;" oncontextmenu="return false;">
        <swiper :options="swiperOption" ref="mySwiper">
          <swiper-slide v-for="(item,index) in poster" :key="index">
            <img :src="item">
          </swiper-slide>
          <div class="swiper-pagination" slot="pagination"></div>
          <!-- <div class="swiper-button-prev" slot="button-prev"></div>
          <div class="swiper-button-next" slot="button-next"></div>-->
        </swiper>
      </div>
      <div class="nav">
        <div class="ct">
          <span>{{$t('activity.EventItems')}}</span>
          <div class="selects">
            <select name id onfocus="this.blur()" v-model="val">
              <option value="3">{{$t('activity.All')}}</option>
              <option value="1">{{$t('activity.HaveInHand')}}</option>
              <option value="2">{{$t('activity.HasEnded')}}</option>
              <option value="0">{{$t('activity.NotOpened')}}</option>
            </select>
          </div>
        </div>
      </div>
      <div id="MainView">
        <div class="container" id="mylockdest">
          <div class="main-panel f-cb">
            <div class="content" id="view">
              <div class="container">
                <div class="rank_list">
                  <ul v-for="(item,index) in list" :key="index" @click="activitydeta(item.id)" v-if="status==3?status!=item.activity_status:status==item.activity_status">
                    <li>
                      <img :src="item.logo" alt>
                    </li>
                    <li>
                      {{lang=='zh'?item.name:item.name_en}}
                      <span :class="item.activity_status==1?'curs':' '">{{item.activity_status|sta}}</span>
                    </li>
                    <li>
                      {{lang=='zh'?item.activity_abstract:item.activity_abstract_en}}
                    </li>
                    <li>{{$t('activity.ActivityTime')}}：{{item.startdate}} / {{item.enddate}}</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import MyHeader from "@/components/Header";
import MyFooter from "@/components/Footer";
import bg1 from "../assets/images/WechatIMG62.png";
import {getactivitylist} from '@/api/activity'
// import bg2 from "../assets/images/WechatIMG79.png";
// import bg3 from "../assets/images/WechatIMG78.png";
export default {
  components: {
    MyHeader,
    MyFooter
  },
  data() {
    return {
      rank: [],
      swiperOption: {
        // slidesPerView: 1,
        // spaceBetween: 30,
        // slidesPerGroup: 1,
        // autoplay: true,
        // loop: true,
        loopFillGroupWithBlank: true,
        pagination: {
          el: ".swiper-pagination",
          clickable: true
        }
        // navigation: {
        //   nextEl: ".swiper-button-next",
        //   prevEl: ".swiper-button-prev"
        // }
      },
      banners: [bg1],
      status: 3,
      val: '',
      list:[],
      lang: 'zh',
      poster: [],
    };
  },
  
  filters: {
    sta(i){
      switch (i) {
        case 0:
            return '未开始';
          break;
        case 1:
            return '进行中';
          break;
        case 2:
            return '已结束';
          break;
        default:
          break;
      }
    }

  },
  watch: {
    val(val) {
      switch (val) {
        case "0":
          this.status = 0;
          break;
        case "1":
          this.status = 1;
          break;
        case "2":
          this.status = 2;
          break;
        case "3":
          this.status = 3;
          break;
        default:
          this.status = 3;
          break;
      }
    },
  },
  computed: {
    swiper() {
      return this.$refs.mySwiper.swiper;
    },
    myLang() {
      return this.lang;
    }
  },

  created() {
    this.val = "3";
    getactivitylist(3,1,4).then(res => {
      if (res.state == 1){
        this.list = res.datas
        for (var i =0;i<res.datas.length;i++){
          this.poster.push(res.datas[i].poster);
        }
      }
    })
  },
  mounted() {},
  methods: {
    activitydeta(i) {
      this.$router.push({path:'/Activitydetails',query:{id:i}})
    }
  }
};
</script>
<style scoped>
.swiper-container {
  height: 100%;
  width: 100%;
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}
.swiper-slide img,
.swiper-slide {
  border-radius: 0px;
}
.swiper-slide,
.swiper-slide a {
  height: 440px !important;
}
.container {
  position: relative;
  min-width: 1200px;
}
.rank {
  background-color: #f2f3f8;
}
.rank_list {
  padding-top: 0;
}
.rank_list ul {
  background-color: #ffffff;
  height: 133px;
  padding: 30px;
  margin: 15px 0;
  cursor: pointer;
  -webkit-transition: 0.6s;
}
.rank_list ul:hover {
  box-shadow: 0px 0px 5px 5px #eeeeee;
}
.rank_list ul li:nth-child(1) {
  float: left;
  height: 150px;
  width: 120px;
  text-align: center;
}
.rank_list ul li:nth-child(2) {
  color: #333333;
  font-size: 20px;
}
select {
  /*Chrome和Firefox里面的边框是不一样的，所以复写了一下*/
  border: solid 1px #d7d7d7;
  background-color: #ffffff;
  width: 150px;
  height: 30px;
  /*很关键：将默认的select选择框样式清除*/
  appearance: none;
  -moz-appearance: none;
  -webkit-appearance: none;

  padding: 0 10px;
  /*在选择框的最右侧中间显示小箭头图片*/
  background: url("http://ourjs.github.io/static/2015/arrow.png") no-repeat
    scroll right center transparent;

  /*为下拉小箭头留出一点位置，避免被文字覆盖*/
  /* padding-right: 19px; */
}

.rank_list ul li:nth-child(2) span {
  width: 50px;
  height: 20px;
  background-color: #cccccc;
  color: #ffffff;
  font-size: 14px;
  border-radius: 2px;
  padding: 3px 10px;
  margin: 0 0px 0 30px;
}
.rank_list ul li:nth-child(2) .curs {
  background-color: #ffa30b;
}
.rank_list ul li:nth-child(3) {
  font-size: 14px;
  margin-bottom: 20px;
  height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
}
.rank_list ul li:nth-child(1) img {
  height: 60px;
  width: 60px;
  margin: 30px 0px;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
.rank_list ul li {
  /* height: 30px; */
  width: 85%;
  line-height: 30px;
  color: #333333;
  float: left;
}
.rank_list ul li:nth-last-child(1) {
  border-top: 1px solid #dddddd;
  color: #666666;
  font-size: 14.4px;
  height: 50px;
  line-height: 50px;
}
.nav {
  border: 1px solid #dddddd;
  height: 70px;
  line-height: 70px;
  margin-bottom: 15px;
}
.nav .ct {
  max-width: 1200px;
  margin: 0 auto;
}
.nav .ct span {
  float: left;
  color: #333333;
  font-size: 20px;
}
.nav .ct .selects {
  float: right;
}
.rank_topbars {
  width: 100%;
  height: 440px;
  position: relative;
}
.rank_topbars {
  display: flex;
  justify-content: center;
}

.rank_topbars img {
  position: relative;
  overflow: hidden;
}
</style>
