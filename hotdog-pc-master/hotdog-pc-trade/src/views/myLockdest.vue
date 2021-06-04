<template>
  <div>
    <my-header></my-header>
    <div class="rank">
      <div class="rank_topbar" ondragstart="return false;" oncontextmenu="return false;">
        <!-- <img src="../assets/images/ibg.png" alt> -->
        <div class="join_btn" @click="joinClick">
          <i class="iconfont icon-dianji"></i>
          <p>参与锁仓</p>
        </div>
      </div>
      <div class="rank_count_down">
        <div class="inner_count_down f-cb">
          <div class="left-wrap f-fl">
            <span>Ebank锁仓排名大赛规则：</span>
            <span>1、参与资格：锁仓期限三个月及以上的纳入活动统计；</span>
            <span>2、活动奖品根据锁仓数量排名依次发放不能叠加；</span>
            <span>3、活动时间：2018年12月27日12:00 - 2019年01月15日15:00；</span>
            <span>4、奖品发放：活动结束时会对页面进行快照，联系客服登记信息后3-5个工作日统一发放。</span>
          </div>
          <div class="right-wrap f-fr">
            <div class="f-cb">
              <div class="title">
                <p>锁仓排名大赛倒计时</p>
              </div>
              <div class="time">
                <count-down
                  v-on:start_callback="countDownS_cb()"
                  v-on:end_callback="countDownE_cb()"
                  :currentTime="a"
                  :startTime="a"
                  :endTime="1547535600"
                  :tipText="'距离开始文字1'"
                  :tipTextEnd="'距离结束文字1'"
                  :endText="'结束自定义文字2'"
                  :dayTxt="' : '"
                  :hourTxt="' : '"
                  :minutesTxt="' : '"
                  :secondsTxt="''"
                ></count-down>
              </div>
              <div class="date">
                <span>DAYS</span>
                <span>HOURS</span>
                <span>MINUTES</span>
                <span>SECONDS</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div id="MainView">
        <div class="container" id="mylockdest">
          <div class="main-panel f-cb">
            <div class="content" id="view">
              <div class="container">
                <div class="rank_list">
                  <div class="list" v-for="(item , index) in rank" :key="index">
                    <div class="inner-list f-cb">
                      <span :class="index+1|a(1)">{{index+1}}</span>
                      <span class="r_name">{{item.account}}</span>
                      <span class="r_24_volume">
                        <em>锁仓数量：</em>
                        <em>{{item.amount}}</em>
                      </span>
                      <span class="r_reward">
                        <em>活动奖品</em>
                        <em>{{index+1|a(2)}}</em>
                      </span>
                    </div>
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
import { getMember } from "../api/myLockdest.js";
import MyHeader from "@/components/Header";
import MyFooter from "@/components/Footer";
import { getrank } from "@/api/myLockup.js";
import CountDown from "vue2-countdown";
export default {
  components: {
    MyHeader,
    MyFooter,
    CountDown
  },

  data() {
    return {
      rank: [],
      d: "00",
      h: "00",
      m: "00",
      s: "00",
      a: "00",
      joinBtnDisabled: false
    };
  },
  created() {
    this.a = Date.parse(new Date()) / 1000;
  },
  mounted() {
    getrank(10).then(res => {
      if (res.datas) {
        this.rank = res.datas;
        if (res.total < 10) {
          var c = { account: "暂无", amount: 0, no: 0 };
          for (var i = res.total; i < 10; i++) {
            this.rank.push(c);
          }
        }
      }
    });
  },
  methods: {
    joinClick() {
      if (this.joinBtnDisabled === true) {
        return;
      }
      this.joinBtnDisabled = true;
      getMember().then(res => {
        this.joinBtnDisabled = false;
        if (res.state === 1) {
          this.$router.push("/myLockup");
        } else {
          this.$router.push("/login");
        }
      });
    }
  }
};
</script>
<style scoped>
.container {
  position: relative;
}
.rank_count_down .inner_count_down {
  padding: 15px 30px;
  border-bottom: 1px solid #303c5a;
  border-top: 1px solid #303c5a;
}
.join_btn {
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
.rank_topbar {
  display: flex;
  justify-content: center;
}
</style>
