<template>
  <div class="chat_border">
    <div class="title">
      <div class="container">
        <div class="avatar" :style="{backgroundColor:oppoMerchant.color}">{{oppoMerchant.m_nick_name&&oppoMerchant.m_nick_name[0]}}</div>
        <div class="right">
          <p @click="toUserInfo">{{oppoMerchant.m_nick_name&&oppoMerchant.m_nick_name}}</p>
          <div>30日成单：{{done30}}</div>
        </div>
      </div>
    </div>
    <div class="main_container" id="main_container">
      <!--<div class="time">{{currentdate}}</div>-->
      <div
        v-for="(item,index) in msg"
        :key="index"
      >
        <!-- 接受 -->
        <div
          class="receive"
          v-if="item.type==='receive'"
        >
          <div class="avatar" :style="{backgroundColor:oppoMerchant.color}">{{item.name[0]}}</div>
          <div class="right">
            <div class="chart_box">
              {{item.text}}
            </div>
            <div class="timer">{{item.time}}</div>
          </div>
        </div>
        <!-- 发送 -->
        <div
          class="send"
          v-if="item.type==='send'"
        >
          <div class="right">
            <div class="chart_box">
              {{item.text}}
            </div>
            <div class="timer">{{item.time}}</div>
          </div>
          <div class="avatar" :style="{backgroundColor:userInfo.color}">{{item.name[0]}}</div>
        </div>
        <!-- 提示 -->
        <p v-if="item.type==='tip'">{{item.text}} {{item.time}}</p>
      </div>
    </div>
    <div class="footer">
      <Input
        placeholder="输入信息"
        style="width:220px"
        v-model="inputMsg"
        @keyup.enter.native="sendMsg"
      />
      <Button type="primary" @click="sendMsg">发送</Button>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    order: Object,
    done30: Number,
    msg: Array,
    oppoMerchant: Object,
  },
  data() {
    return {
      currentdate: '',
      inputMsg: '',
      userInfo: '',
    };
  },
  watch: {
    msg() {
      this.$nextTick(() => {
        const ele = document.getElementById('main_container');
        ele.scrollTop = ele.scrollHeight;
      });
    },

  },
  created() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    this.getNowFormatDate();
  },
  methods: {
    getNowFormatDate() {
      const date = new Date();
      const seperator1 = '-';
      const year = date.getFullYear();
      let month = date.getMonth() + 1;
      let strDate = date.getDate();
      if (month >= 1 && month <= 9) {
        month = `0${month}`;
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = `0${strDate}`;
      }
      const currentdate = year + seperator1 + month + seperator1 + strDate;
      this.currentdate = currentdate;
    },
    sendMsg() {
      this.$emit('send-msg', this.inputMsg);
      this.inputMsg = '';
    },
    toUserInfo() {
      localStorage.setItem('check-merchant-id', this.oppoMerchant.memberId);
      localStorage.setItem('check-merchant-nickName', this.oppoMerchant.m_nick_name);
      this.$router.push('/userInfo');
    },
  },
};
</script>

<style scoped>
.chat_border {
  width: 335px;
  height: 550px;
  background-color: #ffffff;
  box-shadow: 0px 2px 10px 0px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  display: flex;
  flex-direction: column;
}
.title {
  width: 100%;
  height: 80px;
  border-bottom: 1px solid #eeeeee;
}
.title .container {
  height: 40px;
  margin-top: 26px;
  margin-left: 20px;
  display: flex;
}
.title .container .avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  font-size: 17px;
  line-height: 40px;
  color: #ffffff;
  text-align: center;
}
.title .container .right {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-left: 20px;
}
.title .container .right p {
  font-size: 16px;
  line-height: 16x;
  color: #3b78c3;
  cursor: pointer;
}
.title .container .right div {
  font-size: 12px;
  line-height: 12px;
  color: #666666;
}
.footer {
  height: 60px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  box-sizing: border-box;
  padding: 0 28px 0 23px;
  align-items: center;
  border-top: 1px solid #e1e1e1;
}
.footer p {
  font-size: 14px;
  line-height: 14px;
  color: #999999;
}
.footer .send_img {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: blue;
}
.main_container {
  height: 430px;
  width: 100%;
  overflow-y: auto;
  box-sizing: border-box;
  padding: 0 30px 0 20px;
}
.main_container .time {
  width: 100%;
  text-align: center;
  font-size: 12px;
  line-height: 12px;
  height: 32px;
  margin-top: 10px;
}
.main_container p {
  width: 100%;
  font-size: 12px;
  line-height: 18px;
  color: #333333;
  margin-top: 10px;
  margin-bottom: 20px;
}
.main_container .receive {
  display: flex;
  margin-top: 10px;
  margin-bottom: 20px;
}
.main_container .receive .avatar {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  text-align: center;
  font-size: 16px;
  line-height: 34px;
  color: #ffffff;
}
.main_container .receive .right {
  margin-left: 10px;
}
.main_container .receive .right .chart_box {
  max-width: 220px;
  background-color: #f4f4f4;
  padding: 14px;
  box-sizing: border-box;
  font-size: 12px;
  color: #39404f;
  line-height: 18px;
  border-radius: 5px;
  word-wrap:break-word;
}
.main_container .receive .right .timer {
  font-size: 12px;
  line-height: 12px;
  color: #999999;
  margin-top: 6px;
}

.main_container .send {
  display: flex;
  margin-top: 10px;
  margin-bottom: 20px;
  justify-content: flex-end;
}
.main_container .send .avatar {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  text-align: center;
  font-size: 16px;
  line-height: 34px;
  color: #ffffff;
}
.main_container .send .right {
  margin-right: 10px;
}
.main_container .send .right .chart_box {
  max-width: 220px;
  background-color: #f4f4f4;
  padding: 14px;
  box-sizing: border-box;
  font-size: 12px;
  color: #39404f;
  line-height: 18px;
  border-radius: 5px;
  word-wrap:break-word;
}
.main_container .send .right .timer {
  font-size: 12px;
  line-height: 12px;
  color: #999999;
  margin-top: 6px;
  text-align: end;
}
</style>
