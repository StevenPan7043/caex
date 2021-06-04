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
                <a href="#/feeList">{{$t('footer.Rate')}}</a>
              </li>
            </ul>
          </div>
          <div class="content" id="view">
            <div class="inner-news-detail" id="innerNewsDetail">
              <div class="header">
                <a href="#/newsList">{{$t('news.Notice')}}</a>
                <i class="iconfont icon-angleright"></i>
                <span>{{$t('news.Details')}}</span>
              </div>
              <div class="detail-main" id="Detail">
                <div class="title">{{news.a_title}}</div>
                <div class="sub-title">
                  <span>{{news.a_time}}</span>
                  <span>{{news.a_author}}</span>
                  <span>{{news.a_source}}</span>
                </div>
                <p v-html="news.a_content"></p>
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
import { getNews } from "@/api/newsDetail";
import { getLang } from "@/libs/utils.js";

export default {
  components: {
    MyHeader,
    MyFooter
  },
  data() {
    return {
      id: "",
      news: {},
      lang: "zh"
    };
  },
  watch: {
    lang: function(val) {
      this.changeContentLang(val, this.id);
    }
  },
  mounted() {
    let lang = getLang();
    if (lang) {
      this.lang = lang;
    }
    this.id = this.$route.query.id;
    this.changeContentLang(this.lang, this.id);
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
    changeContentLang(lang, id) {
      console.log(id);
      getNews(id).then(res => {
        let data = res.data;
        if (lang == "zh") {
          this.news = {
            a_abstract: data.a_abstract,
            a_content: data.a_content,
            a_img_file: data.a_img_file,
            a_title: data.a_title
          };
        } else if (lang == "en") {
          this.news = {
            a_abstract: data.a_abstract_en,
            a_content: data.a_content_en,
            a_img_file: data.a_img_file_en,
            a_title: data.a_title_en
          };
        } else if (lang == "ko") {
          this.news = {
            a_abstract: data.a_abstract_ko,
            a_content: data.a_content_ko,
            a_img_file: data.a_img_file_ko,
            a_title: data.a_title_ko
          };
        } else if (lang == "ja") {
          this.news = {
            a_abstract: data.a_abstract_jp,
            a_content: data.a_content_jp,
            a_img_file: data.a_img_file_jp,
            a_title: data.a_title_jp
          };
        }
      });
    }
  }
};
</script>
