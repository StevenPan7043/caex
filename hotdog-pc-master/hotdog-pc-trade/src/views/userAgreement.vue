<template>
  <div>
    <my-header @lang-change="langChange"></my-header>
    <div id="MainView">
      <div class="container" id="customerService">
          <div class="main-panel f-cb">
              <div class="sidebar-nav f-fl">
                  <ul>
                      <li>{{$t('footer.CustomerService')}}</li>
                      <li>
                          <a href="#/userAgreement">{{$t('footer.UserAgreement')}}</a>
                      </li>
                      <li>
                          <a href="https://github.com/ZZEXPRO/ZZEX-API-Docs" target="_blank">{{$t('footer.APISupports')}}</a>
                      </li>
                  </ul>
              </div>
              <div class="content" id="view">
                  <div class="inner-user-agreement" id="innerUserAgreement">
                      <div class="header">{{$t('footer.UserAgreement')}}</div>
                      <div class="lists-main" id="userAgreementTB">
                          <p v-html="data.a_content"></p>
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
import { getNews } from '@/api/userAgreement'
import {getLang} from '../libs/utils.js'
export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      data: {},
      lang:'zh'
    }
  },
    watch:{
        lang:function(val){
            this.changeContentLang(val)
        }
    },
  created () {
    let lang =getLang()
    if(lang) {
      this.lang=lang
    }
    this.changeContentLang(this.lang);
  },
  methods: {
    langChange(lang){
      this.lang=lang
    },
      changeContentLang(lang){
          getNews('151').then((response) => {
              if (response.state === 1) {
                  let data = response.data
                  if(lang == 'zh'){
                      this.data={
                          a_abstract: data.a_abstract,
                          a_content: data.a_content,
                          a_title: data.a_title,
                          a_img_file: data.a_img_file
                      }
                  } else if(lang == 'en'){
                      this.data={
                          a_abstract: data.a_abstract_en,
                          a_content: data.a_content_en,
                          a_title: data.a_title_en,
                          a_img_file: data.a_img_file_en
                      }
                  } else if(lang == 'ko'){
                      this.data={
                          a_abstract: data.a_abstract_ko,
                          a_content: data.a_content_ko,
                          a_title: data.a_title_ko,
                          a_img_file: data.a_img_file_ko
                      }
                  } else if(lang == 'ja'){
                      this.data={
                          a_abstract: data.a_abstract_jp,
                          a_content: data.a_content_jp,
                          a_title: data.a_title_jp,
                          a_img_file: data.a_img_file_jp
                      }
                  }

              }
          })
      }
  }
}
</script>
