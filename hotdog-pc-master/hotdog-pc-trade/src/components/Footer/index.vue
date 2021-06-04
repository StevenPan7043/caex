<template>
  <div class="home-footer">
    <!-- <ChatRoomView v-if="showChat" /> -->
    <div class="footer-content f-cb">
      <div class="footer-left f-fl">
        <img
          src="@/assets/images/footer-logo.png"
          style="width: 191px; height: 32px"
        />
        <p class="logo-desc">{{ $t('footer.SafeAndProfessional') }}</p>
        <p class="iconWrap">
          <a href>
            <i class="iconfont icon-footer-twitter"></i>
          </a>
          <a href>
            <i class="iconfont icon-footer-telegram"></i>
          </a>
          <a href>
            <i class="iconfont icon-footer-weibo"></i>
          </a>
          <a @mouseenter="wxImg = true" @mouseleave="wxImg = false">
            <i class="iconfont icon-footer-wechat"></i>
            <img
              v-show="wxImg"
              src="@/assets/images/wxcode.png"
              style="left: -30px"
            />
          </a>

          <a href>
            <i class="iconfont icon-facebookfacebook52"></i>
          </a>
        </p>
      </div>
      <div class="footer-right f-fr">
        <dl>
          <dt>{{ $t('footer.about') }}</dt>
          <dd>
            <a @click.prevent="toaboutus">{{ $t('footer.aboutus') }}</a>
          </dd>
          <dd>
            <a @click.prevent="tocontactus">{{ $t('footer.ContactUs') }}</a>
          </dd>
        </dl>
        <dl>
          <dt>{{ $t('footer.CustomerService') }}</dt>
          <dd>
            <a @click.prevent="toNewHelp">{{ $t('footer.HelpCenter') }}</a>
          </dd>
          <dd>
            <a @click.prevent="toNews">{{
              $t('footer.AnnouncementOfTheCenter')
            }}</a>
          </dd>
          <dd>
            <a @click.prevent="toRate">{{ $t('footer.Rate') }}</a>
          </dd>
          <dd>
            <a @click.prevent="toSubmission">{{ $t('footer.Submission') }}</a>
          </dd>
        </dl>
        <dl>
          <dt>{{ $t('footer.Support') }}</dt>
          <dd>
            <a href="http://hk.mikecrm.com/2HX2n2I" target="_blank">{{
              $t('footer.OnCurrencyApplication')
            }}</a>
          </dd>
          <dd>
            <a href="">{{ $t('footer.APISupports') }}</a>
          </dd>
          <dd>
            <router-link to="/downloadPage">{{
              $t('footer.AppDown')
            }}</router-link>
          </dd>
          <dd>
            <a @click.prevent="toApply">{{ $t('footer.Merchants') }}</a>
          </dd>
        </dl>
        <dl>
          <dt>{{ $t('footer.clause') }}</dt>
          <dd>
            <a @click.prevent="toUserAgreement">{{
              $t('footer.UserAgreement')
            }}</a>
          </dd>
          <dd>
            <a @click.prevent="toPrivacy">{{ $t('footer.Privacy') }}</a>
          </dd>
          <dd>
            <a @click.prevent="toDisclaimer">{{ $t('footer.Disclaimer') }}</a>
          </dd>
          <dd>
            <a @click.prevent="toRules">{{ $t('footer.Rules') }}</a>
          </dd>
        </dl>
      </div>
    </div>
    <div class="footerBot">© 2019 - 2020 hotdogvip.com All Rights Reserved</div>
  </div>
</template>
<script>
import { getLang } from '@/libs/utils.js'
import ChatRoomView from '@/components/ChatRoom'
import { getMember, verifyLabAuth } from '@/api/login.js'
import { jumpUrl, dataLabUrl } from '../../../public/config.js'

export default {
  data() {
    return {
      wxImgShow: false,
      wxImg: false,
      showChat: false,
    }
  },
  components: {
    ChatRoomView,
  },
  mounted() {
    const loginState = localStorage.getItem('loginState')
    if (!loginState) {
      console.log(232323)
      this.showChat = false
      return false
    }
    this.isUserLogin()
  },
  methods: {
    isUserLogin() {
      getMember().then((res) => {
        if (res.state === 1) {
          this.showChat = true
        } else {
          this.showChat = false
        }
      })
    },
    toaboutus() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960654-%E5%85%B3%E4%BA%8E%E6%88%91%E4%BB%AC'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960654-%E5%85%B3%E4%BA%8E%E6%88%91%E4%BB%AC'
          )
          break
      }
    },
    tocontactus() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960694-%E8%81%94%E7%B3%BB%E6%88%91%E4%BB%AC'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960694-%E8%81%94%E7%B3%BB%E6%88%91%E4%BB%AC'
          )
          break
      }
    },
    toUserAgreement() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050743293-%E7%94%A8%E6%88%B7%E5%8D%8F%E8%AE%AE'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050743293-%E7%94%A8%E6%88%B7%E5%8D%8F%E8%AE%AE'
          )
          break
      }
    },
    toPrivacy() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049959654-%E9%9A%90%E7%A7%81%E6%9D%A1%E6%AC%BE'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049959654-%E9%9A%90%E7%A7%81%E6%9D%A1%E6%AC%BE'
          )
          break
      }
    },
    toDisclaimer() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050744173-%E5%85%8D%E8%B4%A3%E5%A3%B0%E6%98%8E'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050744173-%E5%85%8D%E8%B4%A3%E5%A3%B0%E6%98%8E'
          )
          break
      }
    },
    toRules() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960114-%E6%B3%95%E5%B8%81%E4%BA%A4%E6%98%93%E8%A7%84%E5%88%99'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960114-%E6%B3%95%E5%B8%81%E4%BA%A4%E6%98%93%E8%A7%84%E5%88%99'
          )
          break
      }
    },
    toOnCurrencyApplication() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open('')
          break

        default:
          window.open('')
          break
      }
    },
    toApply() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050744553-%E6%B3%95%E5%B8%81%E5%95%86%E5%AE%B6%E7%94%B3%E8%AF%B7'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050744553-%E6%B3%95%E5%B8%81%E5%95%86%E5%AE%B6%E7%94%B3%E8%AF%B7'
          )
          break
      }
    },
    toNews() {
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
    toSubmission() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open('https://hotdogvip.zendesk.com/hc/zh-cn/requests/new')
          break

        default:
          window.open('https://hotdogvip.zendesk.com/hc/zh-cn/requests/new')

          break
      }
    },
    toRate() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960474-%E8%B4%B9%E7%8E%87%E8%AF%B4%E6%98%8E'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960474-%E8%B4%B9%E7%8E%87%E8%AF%B4%E6%98%8E'
          )
          break
      }
    },
    toNewHelp() {
      let lang = getLang()
      switch (lang) {
        case 'zh':
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/categories/360004079614-%E5%B8%AE%E5%8A%A9%E4%B8%AD%E5%BF%83'
          )
          break

        default:
          window.open(
            'https://hotdogvip.zendesk.com/hc/zh-cn/categories/360004079614-%E5%B8%AE%E5%8A%A9%E4%B8%AD%E5%BF%83'
          )
          break
      }
    },
  },
}
</script>
<style scoped>
/* a:hover{
    color: #d98746;
  }
  .wx_img_style{
    position:relative;
    top: -150px;
    right: 100px;
    float: right
  }
  .home-footer .footer-content{
    color: #faf7f4;
  } */
/* .home-footer .footer-content .footer-right dl dt{
    color: #5a7896;
  }
  .home-footer .footer-content .footer-right dl dd.icon-wrap i{
    font-size: 26px;
  } */
a:hover {
  color: #196bdf;
}

.f-fr,
.f-fl {
  font-size: 12px;
}

.iconWrap {
  display: flex;
  flex-direction: row;
}

.iconWrap a {
  margin-right: 12px;
  position: relative;
  color: #7f8fa5;
}

.iconWrap a .iconfont {
  font-size: 23px;
}

.iconWrap a img {
  width: 80px;
  height: 80px;
  position: absolute;
  display: block;
  margin-top: 8px;
}

.iconWrap a:hover {
  color: #196bdf;
}

.zzex-custom img {
  width: 100px;
  height: 100px;
}
</style>

