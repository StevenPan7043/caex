<template>
  <div class="home-header">
    <div class="placeholder"></div>
    <div class="inner-header f-cb">
      <h1 class="head-logo f-fl">
        <router-link to="/">
          <img
            src="@/assets/images/logo.png"
            alt="Hotdog全球领先区块链数字资产交易平台"
          />
        </router-link>
      </h1>
      <ul class="head-nav f-fl">
        <li>
          <a @click.prevent="toFabi" v-if="showOtc">{{
            $t('header.FiatDeal')
          }}</a>
        </li>
        <li>
          <router-link to="/tradecenter">{{ $t('header.Coins') }}</router-link>
        </li>


        <li>
          <router-link to="/trade">{{$t("header.Contract")}}</router-link>
        </li>
        <!-- <li>
          <a @click="gotu">{{ $t('header.Contract') }}</a>
        </li> -->
        <li style="position: relative">
          <router-link to="/downloadPage">{{
            $t('header.AppDownload')
          }}</router-link>
        </li>
        <li>
          <a href="http://hk.mikecrm.com/2HX2n2I" target="_blank">{{
            $t('footer.OnCurrencyApplication')
          }}</a>
        </li>
        <li>
          <a @click.prevent="dataLab" v-if="isShowLab">数据实验室</a>
        </li>
      </ul>
      <ul class="head-user f-fr">
        <li
          class="change-language"
          @mousemove="langMousemove"
          @mouseout="langMouseout"
        >
          <el-dropdown>
            <span class="el-dropdown-link">
              <i class="iconfont icon-earth"></i>
              {{ lang | langFilter() }}
              <i
                class="el-icon--right"
                :class="langShow ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"
              ></i>
            </span>
            <el-dropdown-menu
              slot="dropdown"
              style="
                margin-top: 20.5px;
                color: #7f8fa5;
                width: 125px;
                margin-right: -15px;
              "
            >
              <el-dropdown-item style="color: #b4c6ee">
                <span @click="changeLanguage('zh')">简体中文</span>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <span @click="changeLanguage('en')">English</span>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <span @click="changeLanguage('ko')">한국어</span>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <span @click="changeLanguage('ja')">日本語</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
      </ul>
      <ul class="head-user border-i f-fr">
        <li class="news li-asset-manager">
          <el-dropdown>
            <span class="el-dropdown-link" style="color: #7f8fa5">
              <i class="iconfont icon-lingdang1"></i>
            </span>
            <el-dropdown-menu
              slot="dropdown"
              style="margin-top: 20.5px; color: #b4c6ee; margin-right: -160px"
            >
              <el-dropdown-item
                style="
                  color: #b4c6ee;
                  width: 250px;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                  font-size: 12px;
                "
                v-for="item in news"
                :key="item.id"
              >
                <a @click="toNoticeDeatil(item)">{{ item.title }}</a>
              </el-dropdown-item>
              <el-dropdown-item
                style="
                  color: #196bdf;
                  width: 250px;
                  text-align: center;
                  font-size: 12px;
                  margin-top: 10px;
                "
              >
                <a @click="toUserNews">{{ $t('header.more') }}</a>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
      </ul>
      <ul class="head-user f-fr" id="headlogin" v-show="loginState">
        <li class="li-asset-manager">
          <el-dropdown>
            <span class="el-dropdown-link">{{ $t('user.Assets') }}</span>
            <el-dropdown-menu
              slot="dropdown"
              style="
                margin-top: 20.5px;
                color: #b4c6ee;
                margin-right: -110px;
                padding: 10px 40px 10px 0;
              "
            >
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/myAssets">{{ $t('header.bb') }}</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <a @click.prevent="toFbAccount">{{
                  $t('contract.FiatAccount')
                }}</a>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/contractAssets">{{
                  $t('contract.ContractAccount')
                }}</router-link>
              </el-dropdown-item>
              <!-- <el-dropdown-item style="color:#b4c6ee;">
                <router-link to="/addressManage">{{$t('user.Address1')}}</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="color:#b4c6ee;">
                <router-link to="/assetsRecord">{{$t('user.Financial')}}</router-link>
              </el-dropdown-item> -->
            </el-dropdown-menu>
          </el-dropdown>
        </li>
        <li class="li-user-center">
          <el-dropdown>
            <span class="el-dropdown-link" style="color: #7f8fa5">{{
              $t('header.Order')
            }}</span>
            <el-dropdown-menu
              slot="dropdown"
              style="
                margin-top: 20.5px;
                color: #b4c6ee;
                margin-right: -110px;
                padding: 10px 40px 10px 0;
              "
            >
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/tradeBill">{{
                  $t('header.CoinMoneyOrders')
                }}</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <a @click.prevent="toFbOrders">{{
                  $t('header.FiatMoneyOrders')
                }}</a>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/order">{{
                  $t('header.ContractPositionOrder')
                }}</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/order_entrust">{{
                  $t('header.ContractOrder')
                }}</router-link>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
        <li class="li-user-center">
          <el-dropdown>
            <span class="el-dropdown-link" style="color: #7f8fa5">
              <i class="iconfont icon-geren"></i>
            </span>
            <el-dropdown-menu
              slot="dropdown"
              style="
                margin-top: 20.5px;
                color: #b4c6ee;
                margin-right: -110px;
                padding: 10px 40px 10px 0;
              "
            >
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/accountInfo">{{
                  $t('user.AccountInfos')
                }}</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/identityVerify">{{
                  $t('user.Certification')
                }}</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/invite">{{
                  $t('header.Invite')
                }}</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <router-link to="/apiManager">{{
                  $t('user.APIManagement')
                }}</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="color: #b4c6ee">
                <span @click="logoutClick">{{ $t('user.LogOut') }}</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
      </ul>
      <ul class="head-user f-fr" id="headlogout" v-show="!loginState">
        <li>
          <router-link to="/login" :class="loginRouter ? 'LoginPa' : ''">{{
            $t('header.Login')
          }}</router-link>
        </li>
        <li>
          <router-link class to="/register" v-if="loginState">{{
            $t('header.Register')
          }}</router-link>
          <router-link
            :class="!loginRouter ? 'LoginPa' : ''"
            to="/register"
            v-else
            >{{ $t('header.Register') }}</router-link
          >
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
import { setLang, getLang } from '../../libs/utils.js'
import { getMemberLogout } from '@/api/logout.js'
import { getMember, verifyLabAuth } from '@/api/login.js'
import { jumpUrl, dataLabUrl } from '../../../public/config.js'
import { getNewNewsCoin } from '@/api/home.js'
export default {
  props: ['selectHeaderTab'],
  data() {
    return {
      isAuth: false,
      downloadImg: false,
      loginState: false,
      m_name_hidden: '',
      lang: 'en',
      langShow: false,
      showOtc: true,
      loginRouter: false,
      news: [],
    }
  },
  filters: {
    langFilter(value) {
      if (value == '' || value == null || value === 'zh') {
        return '简体中文'
      } else if (value === 'en') {
        return 'English'
      } else if (value === 'ko') {
        return '한국어'
      } else if (value === 'ja') {
        return '日本語'
      }
    },
  },
  computed: {
    isShowLab() {
      return this.loginState && this.isAuth
    },
  },

  watch: {
    lang: function (val) {
      this.changeContentLang(val)
    },
  },
  created() {
    this.isLabAuth()
    this.changeContentLang(this.lang)
  },
  mounted() {
    this.init()
  },
  methods: {
    gotu() {
      this.$message({
        message: '合约系统维护中',
      })
    },
    toFbOrders() {
      const token = localStorage.getItem('token')
      window.location.href =
        jumpUrl.split('#')[0] + '#/order' + '?token=' + token
    },
    toFbAccount() {
      const token = localStorage.getItem('token')
      window.location.href =
        jumpUrl.split('#')[0] + '#/assets' + '?token=' + token
    },
    dataLab() {
      window.location.href = dataLabUrl
    },
    isLabAuth() {
      verifyLabAuth().then((res) => {
        if (res.state == 1) {
          this.isAuth = true
        }
      })
    },

    init() {
      this.lang = getLang()
      this.isUserLogin()
      if (this.$root._route.name == 'Login') {
        this.loginRouter = true
      }
    },
    getNewNews(lang) {
      getNewNewsCoin(lang).then((res) => {
        this.news = res['articles']
      })
    },
    async changeContentLang(lang) {
      var res
      switch (lang) {
        case 'zh':
          res = await this.getNewNews('zh-cn')
          break

        // default:
        //   res = await this.getNewNews("en-us");
        //   break;
      }
    },
    langMousemove() {
      this.langShow = true
    },
    langMouseout() {
      this.langShow = false
    },
    appMousemove() {
      this.downloadImg = true
    },
    appMouseout() {
      this.downloadImg = false
    },
    changeLanguage(lang) {
      if (lang === 'zh') {
        this.lang = 'zh'
        this.$i18n.locale = this.lang
        setLang('zh')
        this.$emit('lang-change', 'zh')
      } else if (lang === 'en') {
        this.lang = 'en'
        this.$i18n.locale = this.lang
        this.$emit('lang-change', 'en')
        setLang('en')
      } else if (lang === 'ko') {
        this.lang = 'ko'
        this.$i18n.locale = this.lang
        this.$emit('lang-change', 'ko')
        setLang('ko')
      } else if (lang === 'ja') {
        this.lang = 'ja'
        this.$i18n.locale = this.lang
        this.$emit('lang-change', 'ja')
        setLang('ja')
      }
    },
    toNoticeDeatil(item) {
      window.open(item.html_url)
    },
    logoutClick() {
      getMemberLogout().then((res) => {
        if (res.state === 1) {
          this.userLogout()
        }
      })
    },
    userLogout() {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      localStorage.removeItem('token')
      this.$router.push('/login')
    },
    toUserNews() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/categories/360004132653-%E5%85%AC%E5%91%8A%E4%B8%AD%E5%BF%83'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/categories/360004132653-%E5%85%AC%E5%91%8A%E4%B8%AD%E5%BF%83'
          )
          break
      }
    },
    isUserLogin() {
      getMember().then((res) => {
        if (res.state === 1) {
          // 用户处于登录状态，将状态保存到session
          let loginState = true
          let userInfo = res.data
          localStorage.setItem('loginState', loginState)
          localStorage.setItem('useruid', userInfo.uid)
          localStorage.setItem('m_name_hidden', userInfo.m_name_hidden)
          // this.topbarUserOptionList()
          if (userInfo.area_code != 86) {
            this.showOtc = false
          }
          localStorage.setItem('showOtc', this.showOtc)
        } else {
          let loginState = false
          localStorage.setItem('loginState', loginState)
        }
        let loginState = localStorage.getItem('loginState')
        if (loginState === 'true') {
          this.loginState = true
        } else {
          this.loginState = false
        }
        this.m_name_hidden = localStorage.getItem('m_name_hidden')
      })
    },
    toFabi() {
      const token = localStorage.getItem('token')
      window.location.href = jumpUrl + '?token=' + token
    },
  },
}
</script>

<style scoped>
span,
a {
  color: #7f8fa5;
}
.cur {
  color: #25425a;
}
span.el-dropdown-link:hover,
span.el-dropdown-link .iconfont:hover {
  color: #ffffff;
}
.el-dropdown-menu {
  background-color: #ffffff;
  /* border:rgb(32, 41, 62); */
  /* box-shadow: 2px 2px 10px #000 */
}
.el-dropdown-menu li {
  /*text-align: center;*/
}
.el-dropdown-menu__item:focus,
.el-dropdown-menu__item:not(.is-disabled):hover {
  color: #196bdf;
}
.el-dropdown-menu__item,
.el-dropdown-menu__item a {
  color: #25425a !important;
}
.el-dropdown-menu__item,
.el-dropdown-menu__item a:hover {
  color: #196bdf !important;
  width: 100%;
}
.el-dropdown-menu__item span {
  color: #25425a !important;
}
.el-dropdown-menu__item span:hover {
  color: #196bdf !important;
}
.el-dropdown-menu__item:hover {
  background: #f5f9fc !important;
}
.el-dropdown {
  cursor: pointer;
}

.app-download:hover,
a:hover {
  color: #ffffff;
}

.app-download {
  cursor: pointer;
}
#img-app-download {
  position: absolute;
  top: 68px;
  left: 1px;
}
#headlogout .LoginPa:hover {
  color: #ffffff;
}
.LoginPa {
  width: 70px;
  height: 28px;
  border: 1px solid #196bdf;
  color: #ffffff;
  padding: 3px 16px;
  border-radius: 3px;
}
.LoginPa:hover {
  background: #196bdf;
}
#img-app-download {
  width: 200px;
  height: 200px;
}
</style>
