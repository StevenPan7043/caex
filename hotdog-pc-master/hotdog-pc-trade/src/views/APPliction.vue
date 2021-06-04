<template>
  <div>
    <my-header @lang-change="langChange"></my-header>
    <div id="MainView">
      <div class="container" id="helpCenter">
        <div class="main-panel f-cb">
          <div class="sidebar-nav f-fl">
            <ul>
              <li>{{$t('footer.HelpCenter')}}</li>
              <li>
                <a @click.prevent="toNews">{{$t('footer.AnnouncementOfTheCenter')}}</a>
              </li>
              <li>
                <router-link to="/feeList">{{$t('footer.Rate')}}</router-link>
              </li>
            </ul>
          </div>
          <div class="content" id="view">
            <div class="inner-fee-list" id="innerFeeList">
              <div class="header">{{$t('footer.OnCurrencyApplication')}}</div>
              <div class="lists-main" id="feeTB" v-html="data"></div>
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
import { getNews } from "@/api/feeList";

import { getLang } from "@/libs/utils.js";

export default {
  components: {
    MyHeader,
    MyFooter
  },
  data() {
    return {
      data: "",
      lang: "zh"
    };
  },
  watch: {
    lang: function(val) {
      this.changeContentLang(val);
    }
  },
  created() {
    this.lang = getLang();
    this.changeContentLang(this.lang);
  },
  methods: {
    toNews() {
      let lang = getLang();
      switch (lang) {
        case "zh":
          window.open("https://hotdogvip.zendesk.com/hc/zh-cn");
          break;

        default:
          window.open("https://hotdogvip.zendesk.com/hc/zh-cn");
          break;
      }
    },
    langChange(lang) {
      this.lang = lang;
    },
    changeContentLang(lang) {
      getNews("395").then(res => {
        if (res.state === 1) {
          let data = res.data;
          if (lang == "zh") {
            this.data = data.a_content;
          } else if (lang == "en") {
            this.data = data.a_content_en;
          } else if (lang == "ko") {
            this.data = data.a_content_ko;
          } else if (lang == "ja") {
            this.data = data.a_content_jp;
          }
        }
      });
    }
  }
};
</script>
