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
            <div class="inner-news-list" id="innerNewsList">
              <div class="header">{{$t('news.Notice')}}</div>
              <div class="lists-main" id="Lists">
                <div class="list f-cb" v-for="(item,index) in lists" :key="index" @click="toDetail(item.id)">
                  <span class="f-fl">{{item.title}}</span>
                  <span class="f-fr">{{item.time}}</span>
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
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import { getNewsList } from '@/api/newsList'
import {getLang} from '@/libs/utils.js'
export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      lists: [],
      lang:'zh'
    }
  },
  watch:{
    lang:function(val){
      this.changeContentLang(val)
    }
  },
  created () {
    let lang = getLang()
    if(lang) {
      this.lang =lang
    }
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
    toDetail (item) {
      this.$router.push({ name: 'NewsDetail', query: { id: item } })
    },
    langChange(lang){
      this.lang=lang
    },
    changeContentLang(lang) {
      getNewsList(304, 1, 15).then(res => {
        if (res.state === 1) {
          this.lists = [];
          res.data.list.forEach(list => {
            if(lang == 'zh'){
              this.lists.push({title:list.a_title,time:list.a_time,id:list.id})
            }else if(lang == 'en'){
              this.lists.push({title:list.a_title_en,time:list.a_time,id:list.id})
            }else if(lang == 'ko'){
              this.lists.push({title:list.a_title_ko,time:list.a_time,id:list.id})
            }else if(lang == 'ja'){
              this.lists.push({title:list.a_title_jp,time:list.a_time,id:list.id})
            }
          });

        }
      })
    }
  }
}
</script>
