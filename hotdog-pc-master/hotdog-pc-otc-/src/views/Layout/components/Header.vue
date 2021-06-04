<template>
  <div class="header_container">
    <div class="header_left">
      <img src="@/assets/images/logo.png" alt @click="linkTo(2)" />
      <span :class="hash==='home'?'cur':''" @click="linkTo(0)">法币交易</span>
      <span :class="hash==='publishAds'?'cur':''" @click="linkTo(1)">发布广告</span>
      <span @click.prevent="routerLink(0)">币币交易</span>
      <span @click.prevent="routerLink(1)">合约交易</span>
    </div>
    <div class="header_right">
      <span @click="linkTo(5)" v-if="!loginState">登录</span>
      <span @click="linkTo(6)" v-if="!loginState">注册</span>
      <div class="order">
        <Dropdown v-if="loginState" trigger="click">
          <div @click="loadOrder">
            <Badge
              :count="orderCount"
              overflow-count="99"
              :class="hash==='order'?'cur':''"
              v-if="orderCount!=0"
            >
              <div class="ordername">订单</div>
            </Badge>
            <span :class="hash==='order'?'cur':''" class="ordername" v-else>订单</span>
          </div>
          <DropdownMenu slot="list">
            <order-summary :rows="rows" />
          </DropdownMenu>
        </Dropdown>
      </div>

      <span @click="linkTo(4)" v-if="loginState" :class="hash==='assets'?'cur':''">资产</span>
      <div class="avatar" v-if="loginState">
        <Dropdown>
          <div class="avatar_style" :style="{backgroundColor:userInfo.color}">
            <i class="iconfont icon-person" v-if="!userInfo.m_nick_name" style="color:white"></i>
            <div v-else>{{userInfo.m_nick_name[0]}}</div>
          </div>
          <DropdownMenu slot="list">
            <DropdownItem>
              <router-link to="/user">
                <div class="text_align_center">个人中心</div>
              </router-link>
            </DropdownItem>
            <DropdownItem>
              <router-link to="/myAds">
                <div class="text_align_center">我的广告</div>
              </router-link>
            </DropdownItem>
            <DropdownItem>
              <router-link to="/businessApp">
                <div class="text_align_center">商家申请</div>
              </router-link>
            </DropdownItem>
            <DropdownItem>
              <a @click.prevent="logout">
                <div class="text_align_center">退出登录</div>
              </a>
            </DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </div>

      <div class="lang_dropdown">
        <Dropdown>
          <!-- <div src="" alt="" class="nation-flag"/> -->
          <img src="../../../assets/images/china.png" alt class="nation-flag" />
          <DropdownMenu slot="list">
            <DropdownItem>简体中文</DropdownItem>
            <DropdownItem>English</DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </div>
    </div>
  </div>
</template>
<script>
import OrderSummary from './orderSummary.vue';
import {
  getTradingPage,
  getMerchantInfo,
  getAccountId,
  getMember,
} from '../api';
import { logoutApi } from '@/api/GetCode';
import { jumpUrl } from '../../../../public/config';
import { chooseBackgroundColor } from '@/libs/utils';

export default {
  components: {
    OrderSummary,
  },
  data() {
    return {
      curPage: 0,
      page: 1,
      pagesize: 10,
      rows: [],
      loop: null,
      orderCount: 0,
    };
  },
  created() {
    getMember().then((res) => {
      if (res.state === 1) {
        const userInfo = localStorage.getItem('userInfo');
        this.$store.commit('SET_USERINFO', JSON.parse(userInfo));
      } else if (res.state === -1) {
        const loginState = false;
        localStorage.setItem('loginState', loginState);
      }
    });
  },
  mounted() {
    this.loop = setInterval(() => {
      this.checkHasOrder();
    }, 6000);
  },
  beforeDestroy() {
    window.clearInterval(this.loop);
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo;
    },
    loginState() {
      return this.$store.state.loginState;
    },
    hash() {
      return this.$store.state.hash;
    },
  },
  methods: {
    linkTo(index) {
      const loginState = JSON.parse(localStorage.getItem('loginState'));
      const token = localStorage.getItem('token');

      switch (index) {
        case 0:
          this.curPage = index;
          this.$router.push('/');
          break;
        case 1:
          if (!loginState) {
            this.$router.push('/login');
            return;
          }
          getMerchantInfo().then(({ data }) => {
            if (data) {
              if (data.status === 'APPLY_PASSED') {
                // 判断是否绑定收款账号
                getAccountId().then(({ data }) => {
                  const accountInfo = [];
                  data.rows.forEach((ele) => {
                    if (ele.isDelete === 'NO') {
                      accountInfo.push(ele);
                    }
                  });
                  if (accountInfo.length < 1) {
                    this.$Message.error({
                      content: '请先添加收款方式',
                      duration: 2,
                      closable: true,
                    });
                    this.$router.push('/user');
                  } else {
                    this.$router.push('/publishAds');
                    this.curPage = index;
                  }
                });
              } else {
                this.$router.push('/businessApp2');
              }
            } else {
              this.$router.push('/businessApp');
            }
          });
          break;
        case 2:
          window.location.href = `${jumpUrl}?token=${token}`;
          break;
        case 3:
          this.$router.push('/order');
          this.curPage = index;
          break;
        case 4:
          this.$router.push('/assets');
          this.curPage = index;
          break;
        case 5:
          this.$router.push('/login');
          this.curPage = index;
          break;
        case 6:
          this.$router.push('/register');
          this.curPage = index;
          break;
        default:
          this.$router.push('/');
          this.curPage = 0;
          break;
      }
    },
    routerLink(index) {
      const token = localStorage.getItem('token');
      if (index === 0) {
        window.location.href = `${jumpUrl}tradecenter?token=${token}`;
      }
      if (index === 1) {
        const contactToken = localStorage.getItem('contactToken')
        window.location.href = `${jumpUrl}trade?token=${token}&contactToken=${contactToken}`;
      }
    },
    loadOrder() {
      this.rows = [];
      const params = {
        page: this.page,
        pagesize: this.pagesize,
      };
      getTradingPage(params).then(({ data }) => {
        this.total = data.total;
        if (data.rows) {
          const orderData = data.rows;

          const dateTime = new Date();

          orderData.forEach((ele) => {
            ele.color = chooseBackgroundColor(ele.opposite_member_id);
            ele.startTime = new Date().getTime();
            ele.endTime = new Date(ele.createTime).getTime() + ele.paymentTime * 60 * 1000;
          });
          this.rows = orderData;
        }
      });
    },

    checkHasOrder() {
      if (!this.loginState) {
        return;
      }
      const params = {
        page: this.page,
        pagesize: this.pagesize,
      };
      getTradingPage(params).then(({ data }) => {
        if (data.rows && data.rows.length > 0) {
          this.orderCount = data.rows.length;
        } else {
          this.orderCount = 0;
        }
      });
    },
    logout() {
      const loginState = false;
      localStorage.setItem('loginState', loginState);
      localStorage.removeItem('userInfo');
      localStorage.removeItem('useruid');
      localStorage.removeItem('nickName');
      localStorage.removeItem('mNameHidden');
      this.$store.commit('CLEAR_USERINFO');
      this.$router.push('/login');
      localStorage.removeItem('token');
      logoutApi();
    },
  },
};
</script>

<style scoped>
.header_container {
  width: 100%;
  height: 66px;
  background-color: #1a1e27;
  display: flex;
  justify-content: space-between;
}
.header_left {
  display: flex;
  margin-left: 24px;
  align-items: center;
}
.header_left img {
  height: 30px;
  cursor: pointer;
}
.header_left span {
  cursor: pointer;
  font-size: 14px;
  color: #fff;
  margin-left: 68px;
  white-space: nowrap;
}
.cur {
  color: #3b78c3 !important;
}
.header_right {
  display: flex;
  margin-right: 60px;
  align-items: center;
}
.header_right span {
  font-size: 14px;
  color: #fff;
  margin-right: 35px;
  cursor: pointer;
}

.nation-flag {
  cursor: pointer;
  width: 28px;
  height: 18px;
  background-color: red;
  margin-left: 38px;
}

.avatar {
  width: 32px;
}
.avatar_style {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  cursor: pointer;
}
.text_align_center {
  width: 100%;
  text-align: center;
  color: #333333;
}
.text_align_center:hover {
  color: #3b78c3;
}
.ordername {
  width: 35px;
}
</style>
<style>
.avatar .ivu-select-dropdown {
  margin-top: 17px;
}
.lang_dropdown .ivu-select-dropdown {
  margin-top: 24px;
}
.lang_dropdown {
  height: 18px;
}
.order .ivu-select-dropdown {
  margin-top: 22px;
}
</style>
