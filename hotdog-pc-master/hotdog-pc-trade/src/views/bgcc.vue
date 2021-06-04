<template>
  <div>
    <my-header></my-header>
    <div class="rank">
      <div class="rank_topbars" ondragstart="return false;" oncontextmenu="return false;">
        <img :src="bg1" alt="" v-if="curs==1" >
        <img :src="bg2" alt="" v-if="curs==2">
        <img :src="bg3" alt="" v-if="curs==3">
      </div>
      <div id="MainView">
          <div class="tabs">
              <ul>
                  <li :class="curs==1?'curs':''" @click="tab(1)">{{$t('activity.TransactionRanking')}}</li>
                  <!-- <li :class="curs==2?'curs':''" @click="tab(2)">{{$t('activity.PositionRanking')}}</li> -->
                  <li :class="curs==3?'curs':''" @click="tab(3)">{{$t('activity.ActivityDescription')}}</li>
              </ul>
          </div>                
          <div class="rank_count_down"  v-if="curs==1||curs==2">
                  <div class="inner_count_down f-cb">
                    <div class="left-wrap f-fl">
                      <span>{{curs==1?'BGCC交易排名赛:':'BGCC持仓奖励活动：'}}</span>
                      <span>2019年7月23日12:00 - 2019年8月07日12:00</span>
                    </div>
                    <div class="right-wrap f-fr">
                      <div class="f-cb">
                        <div class="title">
                            {{$t('activity.CountDown')}}
                        </div>
                        <div class="time" v-if="!this.hidss">
                          <count-down
                            v-on:start_callback="countDownS_cb()"
                            v-on:end_callback="countDownE_cb()"
                            :currentTime="ac"
                            :startTime="ac"
                            :endTime="1565150400"
                            :tipText="'距离开始文字1'"
                            :tipTextEnd="'距离结束文字1'"
                            :endText="'结束自定义文字2'"
                            :dayTxt="'天'"
                            :hourTxt="'时'"
                            :minutesTxt="'分'"
                            :secondsTxt="'秒'"
                          ></count-down>
                        </div>
                        <div class="time" v-if="hidss">
                            <p>
                              <span>
                                <span>00</span>
                                <i>天</i>
                              </span>
                              <span>00</span>
                              <i>时</i>
                              <span>00</span>
                              <i>分</i>
                              <span>00</span>
                              <i>秒</i>
                            </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
        <div class="container" id="mylockdest">
          <div class="main-panel f-cb">
            <div class="content" id="view">
              <div class="container">

                <div class="rank_list" v-if="curs==4">
                    
                </div>
                <div class="contlist idq" v-if="curs==1">
                    <div class="top">
                        <ul v-for="(item,index) in alist" :key="index">
                            <li :class="index+1|a(1)">{{index+1}}</li>
                            <li>{{item.account?item.account:$t('activity.NoTime')}}</li>
                            <!-- <li><span>{{$t('home.HourlyVolume')}}：</span> <span>{{item.dayNum?item.dayNum:0}}</span></li> -->
                            <li><span>{{$t('activity.TotalVolume')}}</span> <span>{{item.num?item.num:0}}</span></li>
                            <li><span>{{$t('activity.EventPrizes')}}</span> <span>{{index+1|a(item.num)}}</span></li>
                        </ul>
                    </div>
                </div>
                <div class="contlist" v-if="curs==2">
                    <div class="top">
                        <ul v-for="(item,index) in clist" :key="index">
                            <li :class="index+1|a(1)">{{index+1}}</li>
                            <li>{{item.account}}</li>
                            <li><span>{{$t('activity.Number')}}</span> <span>{{item.num?item.num:0}}</span></li>
                            <li><span>{{$t('activity.EventPrizes')}}</span> <span>{{index+1|b(item.num)}}</span></li>
                        </ul>
                    </div>
                </div>
                <div class="contents" v-if="curs==3">
                    <div class="top">
                        <ul>
                            <th>交易排名赛（活动规则）:</th>
                            <li>累计BGCC交易净买入量排名具体奖励如下：</li>
                            <li><span>TOP 1  5000 BGCC</span></li>
                            <li><span>TOP 2  3000 BGCC</span></li>
                            <li><span>TOP 3  2000 BGCC</span></li>
                            <li><span>注：若未进入前三名且净买入量大于等于1000 BGCC的用户，每人可获得100 BGCC奖励。</span></li>
                            <th>活动时间:</th>
                            <li>2019年7月23日12:00 - 2019年8月7日12:00</li>
                            <li><em>奖励发放：</em>交易排名活动获得奖励均被锁定，活动结束之日后三日内发放到账户锁仓余额，每天释放5%，直至释放完毕。</li>
                        </ul>
                    </div>
                    <div class="bottom">
                        <ul>
                            <th>持仓BGCC获额外奖励（活动规则）:</th>
                            <li>BGCC持仓增量额外奖励活动如下：</li>
                            <li>凡是在活动期间购买BGCC并持仓≥5000个的实名用户，即可获得持仓增量10%的奖励。</li>
                            <li>（用户的持仓增量 = 账户活动结束日期BGCC量 - 账户活动开始日期BGCC量）</li>
                            <li>注：活动开始和结束时间点将对用户账户持仓量个进行一次快照。</li>
                            <th>活动时间:</th>
                            <li>2019年7月23日12:00 - 2019年8月7日12:00</li>
                            <th>奖励发放：</th>
                            <li>持仓增量活动获得奖励均被锁定，活动结束之日后三日内发放到账户锁仓余额，每天释放5%，直至释放完毕。</li>
                        </ul>
                    </div>
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
import CountDown from "vue2-countdown";
import {getCurrency,getlistday,getCurrencylist,getList} from '@/api/currency';

export default {
  components: {
    MyHeader,
    MyFooter,
    CountDown
  },
  filters: {
    a(v, s) {
   
    },
    b(v, s) {
     
    }
  },
  data() {
    return {
      rank: [],
      list:[ 1, 2, 3],
      lists:[ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ],
      d: "00",
      h: "00",
      m: "00",
      s: "00",
      a: "00",
      curs:1,
      joinBtnDisabled:false,
      hied:true,
      bg1: bg1,
      bg2: bg2,
      bg3: bg3,
      clist:[],
      alist:[],
      ac:0,
      hidss:false
    };
  },
  created() {
    this.ac = Date.parse(new Date()) / 1000;
    // getCurrencylist('bgccusdt',10).then(res=>{
    //     this.clist = res.datas;
    //     if (res.total < 10) {
    //       var c = { account: "暂无", amount: 0, no: 0 };
    //       for (var i = res.total; i < 10; i++) {
    //         this.clist.push(c);
    //       }
    //     }
    // })
    getList('bgccusdt',3).then(res=>{
        this.alist = res.data.list;
        if (res.data.total < 20) {
          var c = { account: "暂无", amount: 0, no: 0 };
          for (var i = res.total; i < 20; i++) {
            this.alist.push(c);
          }
        }
    })
    if (this.ac>=1565150400 || this.ac<=1563854400){
        this.hidss = true
    }
  },
  mounted() {
    // getrank(10).then(res => {
    //   if (res.datas) {
    //     this.rank = res.datas;
    //     if (res.total < 10) {
    //       var c = { account: "暂无", amount: 0, no: 0 };
    //       for (var i = res.total; i < 10; i++) {
    //         this.rank.push(c);
    //       }
    //     }
    //   }
    // });
  },
  methods: {
    // countDownS_cb: function(x) {
    //   console.log(x)
    // },
    // countDownE_cb: function(x) {
    //   console.log(x)
    // },
    // joinClick(){
    //   if(this.joinBtnDisabled===true){
    //     return
    //   }
    //   this.joinBtnDisabled=true
    //   getMember().then(res=>{
    //     this.joinBtnDisabled=false
    //     if(res.state===1){
    //       this.$router.push('/myLockup')
    //     }else{
    //       this.$router.push('/login')
    //     }
    //   })
    // },
    tab (i){
        this.curs = i;

    }
  }
};
</script>
<style scoped>
.contlist{

    min-height: 800px;
    color: #333333;
    font-size: 16px;
    margin: 15px 0;
}
.contlist .top ul li{
    line-height: 66px;
    width: 20%;
    float: left;
    padding: 0 20px;
    overflow: hidden;
    text-overflow: ellipsis;
color: #333333;
font-size: 20px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}
.contlist .top ul li span{
display: block;
height: 30px;
line-height: 30px;
}
.contlist .top ul li:nth-last-child(1) span:nth-child(2){
    color: #9f9f9e;
    font-size: 14px;
}
.idq .top ul li:nth-last-child(1) span:nth-child(2){
    color: #333333;
}
.contlist .top ul li span:nth-child(1){
    color: #666666;
    font-size: 14px;
}
.contlist .top ul li:nth-child(1){
    text-align: center;
    
}
#MainView .rank_count_down .right-wrap .title{
    float: left;
    height: 60px;
    line-height: 60px;
}
.rank_count_down .right-wrap .time{
    float: left;
}



.contlist .top ul{
    width: 100%;
    height: 60px;
    display: flex;
    justify-content: space-between;
    float: left;
    background-color: #ffffff;
    padding: 30px 0;
    border-bottom: 1px solid #eeeeee;
}
.tabs {
    height: 60px;
    line-height: 60px;
    box-shadow: 0px 15px 0px #f2f3f8;
    margin: 0 0 15px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}
.tabs ul {
    display: flex;
    justify-content: center;
    color: #333333;
    font-size: 16px;
}
.tabs ul li{
    background-color: #ffffff;
    width: 600px;
    text-align: center;
    cursor: pointer;
}
.tabs ul .curs{
color: #5d7ee6;
    background-color: #dde6ff;
}
.container {
  position: relative;
  padding: 0px 0px 80px 0;
}
.left-wrap{
    margin: 22px 0;
}
.rank{
    background-color: #fff;
    font: 14px/1 -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica,Arial,sans-serif;
}
.rank_count_down{
    box-shadow: 0px 15px 0px #f2f3f8;
    color: #333333;
    font-size: 20px;
}
.rank_count_down .inner_count_down{
    border: none !important;
}
.rank_count_down .left-wrap{
    margin: 0;
    padding: 66px 30px;
}
.rank_count_down .right-wrap{
    width: 45%;
    margin: 0;
    padding: 64px 30px;
}
.rank_count_down .inner_count_down {
    width: 1200px;
  border-bottom: 1px solid #303c5a;
  border-top: 1px solid #303c5a;
}
.rank_count_down .left-wrap span:nth-child(1){

    font-size: 18px;
}
.rank_count_down .left-wrap span:nth-child(2){

    font-size: 18px;
}
.join_btn{
  width: 160px;
	height: 36px;
  background-color: #573ddd;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  margin-top: 314px;
  font-size: 14px;
  color: #ffd200;
  border-radius: 5px;
}

.rank_topbars {
    width: 100%;
    height: 440px;
    position: relative;
    /* background-image: url('../assets/images/WechatIMG78.png'); */
    /* background-size: 100% 100%; */
    /* background-repeat: no-repeat; */
}

.rank_count_down .right-wrap .date span {
    float: left;
    margin-right: 20px;
    font-size: 30px; 
    color: #333333;
}

.rank_count_down .right-wrap .date span:nth-child(even)
 {
    margin: 0 5px;
    height: 65px;
    line-height: 65px;
    width: 57px;
    background-color: #f0f4ff;
    color: #333333;
    border-radius: 5px;
}

.rank_count_down .right-wrap .date span:nth-child(odd)
 {
    margin: 0 5px;
    height: 65px;
    line-height: 65px;
    width: 57px;
    color: #333333;
    border-radius: 5px;
    font-weight: 400;
    font-size: 24px;
    overflow: hidden;
}

.rank_count_down .right-wrap .date span:nth-child(1) {
    margin: 0 7px;
    font-size: 16px;
    width: 50px;
}
/* .rank_count_down .right-wrap .date span:nth-child(3) {
    margin: 0 -4px;
} */
/* 
.rank_count_down .right-wrap .date span:nth-child(4) {
    margin: 0 20px;
} */
.rank_count_down{
    background-color: #ffffff;
}
.rank_topbars{
  display: flex;
  justify-content: center
}

.rank_topbars img{
    position: relative;
    overflow: hidden;
}
.rank_list{
    background-image: url("../assets/images/WechatIMG66.png");
    background-repeat: no-repeat;
    background-position: center;
    background-color: #ffffff;
}
#mylockdest .container .contents{
    min-height: 800px;
    color: #333333;
    font-size: 16px;
}

#mylockdest .container .contents .top,
#mylockdest .container .contents .bottom{
    margin: 15px 0;
    padding: 30px;
    background-color: #ffffff;
    min-height: 300px;
}

#mylockdest .container .contents .top ul{
    width: 75%;
}

#mylockdest .container .contents .top ul li span{
    display: block;
    float: left;
    text-align: left;
}
#mylockdest .container .contents .top ul th:nth-child(9){
margin: 40px 0 0;
display: -webkit-box;
}
#mylockdest .container .contents .bottom ul th:nth-child(5),
#mylockdest .container .contents .bottom ul th:nth-child(7)
{
margin: 40px 0 0;
display: -webkit-box;
}
#mylockdest .container .contents .top ul li:nth-child(11){
margin: 40px 0 0;
display: -webkit-box;
width: 70%;
}
#mylockdest .container .contents .top ul li:nth-child(12),
#mylockdest .container .contents .top ul li:nth-child(13){
    text-indent: 30px;
    margin: 20px 0;
}
#mylockdest .container .contents .top ul th,
#mylockdest .container .contents .bottom ul th
{
    color: #5d7ee6;
    font-size: 16px;
    height: 40px;
    line-height: 40px;
    font-weight: 300;
}
#mylockdest .container .contents .top ul li,
#mylockdest .container .contents .bottom ul li
{
    font-size: 15px;
    height: 30px;
    line-height: 30px;
    font-weight: 300;
}

#mylockdest .container .contents .top ul li em,
#mylockdest .container .contents .bottom ul li em
{
    color: #5d7ee6;
}
#mylockdest .container .contents .top ul li span:nth-child(2){
    float: right;
}
#mylockdest .container .contents .bottom{
    background-color: #ffffff;
    min-height: 300px;
    margin: 0 0 60px;
}
.rank{
    background-color: #f2f3f8;
}
</style>
