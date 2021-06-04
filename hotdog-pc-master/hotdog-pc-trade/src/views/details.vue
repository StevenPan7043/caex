<template>
  <div class="details">
    <Header @lang-change="langChange"></Header>
    <div id="MainView">
      <div class="container" id="details">
        <div class="main-panel f-cb">
          <div class="content" id="view">
            <div class="detailsnav">
              <div class="detailsnavleft">
                <span @click="support">{{$t('news.HelpCenter')}}</span> >
                <span @click="backs" v-if="p_id==314">{{$t('news.OfficialAnnouncement')}} ></span> 
                <span @click="backs" v-if="p_id==315">{{$t('news.NoviceHelp')}} ></span> 
                <span @click="backs" v-if="p_id==316">{{$t('news.AutonomousCommunity')}} ></span> 
                <span> {{lang=='zh'?parent_name:parent_name_en}}</span>
              </div>
              <div class="detailsnavright">
                <input type="text" :placeholder="$t('news.InputSearchKeyword')" v-model.trim="search" @keyup.enter="searchs">
              </div>
            </div>
            <div class="detailsleft">
              <ul>
                <th>{{$t('news.ArticlesInThisGroup')}}</th>
                <li :class="item.id==id?'cures':' '" v-for="(item,index) in leftlist" :key="index" @click="detaild(item.id)">{{lang=='zh'?item.a_title:item.a_title_en}}</li>
                <td class="atil">
                      <a @click="detaillist(column_id,p_id,parent_name,parent_name_en)">{{$t('news.Viewall')}} {{total}} {{$t('news.Articles')}} >></a>
                </td>
              </ul>
            </div>
            <div class="detailscontent">
              <div class="detailstile">
                <span>{{lang=='zh'?news.a_title:news.a_title_en}}</span>
              </div>
              <div class="detailslist">
                <div class="navs">
                  <div class="navsleft">
                    <img class="imgsd" src="../assets/images/default-header.png" alt>
                  </div>
                  <div class="navsright">
                    <span>{{news.a_author}}</span>
                    <span>{{news.a_time}}</span>
                  </div>
                </div>

                <div class="contentd" v-html="lang=='zh'?news.a_content:news.a_content_en"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <Footer></Footer>
  </div>
</template>
<script>
import Header from "@/components/Header/newshd.vue";
import Footer from "@/components/Footer/newsfoot.vue";
import { getNews } from "../api/newsDetail.js";
import { getNewsList } from '@/api/newsList'
import { getLang } from "../libs/utils.js"
export default {
  data() {
    return {
      search:'',
      id: "",
      column_id: '',
      news: "",
      leftlist: "",
      cures:'',
      lang:'zh',
      total:0,
      parent_name:'',
      p_id:'',
    };
  },
  mounted() {
    // document
    //   .querySelector("body")
    //   .setAttribute("style", "background-color:#ffffff");
    this.id = this.$route.query.id;
    this.p_id = this.$route.query.p_id;
    this.column_id = this.$route.query.column_id;
    this.parent_name = this.$route.query.parent_name;
    this.parent_name_en = this.$route.query.parent_name_en;
    this.getdetas(this.id)
    this.getnewslists(this.column_id)
    let lang = getLang()
    if(lang){
      this.lang = lang
    }
  },
  methods: {
    getnewslists(column_id){
      getNewsList(column_id,1,5).then(res=>{
        if(res.state){
          this.leftlist = res.data.list
          this.total = res.data.total
        }
      })
    },
    detaillist(item,id,name,name_en) {
      this.$router.push({ path: "/detaillist", query: { id: item ,parent_id:id,parent_name:name,parent_name_en:name_en } });
    },
    detaild(item) {
      if (this.id != item){
        this.getdetas(item)
        this.id = item
      }
    },
    getdetas (i) {
      getNews(i).then(res => {
        this.news = res.data;
      });
    },
    langChange(lang){
      this.lang=lang
    },

    support () {
      this.$router.push('/support')
    },
    backs() {
      this.$router.go(-1)
    },
    searchs () {
      this.$router.push({path:'/search',query:{key:this.search}})
    },
  },
  components: {
    Header,
    Footer
  }
};
</script>

<style lang="scss" scoped>
.details {
  min-height: 900px;
    min-width: 1400px;
  background-color: #ffffff;
}
.details #MainView .container {
  min-height: 900px;
}
.details #MainView .container .detailsnav {
  height: 90px;
  line-height: 90px;
  font-weight: 400;
}
.details #MainView .container .detailsnav .detailsnavleft {
  float: left;
}
.details #MainView .container .detailsnav .detailsnavleft span{
  cursor: pointer;
}
.details #MainView .container .detailsnav .detailsnavleft span:nth-child(1){
  color: #3b78c3;
}
.details #MainView .container .detailsnav .detailsnavleft span:nth-last-child(1){
  color: #333333;
}
.details #MainView .container .detailsnav .detailsnavright {
  float: right;
}
.details #MainView .container .detailsnav .detailsnavright input {
  width: 210px;
  height: 30px;
  text-indent: 40px;
  font-size: 12px;
  color: #999999;
  border: 1px solid #dcdcdc;
  outline: none;
  border-radius: 5px;
  background: url("../assets/images/search.png");
  background-repeat: no-repeat;
  background-position-y: center;
  background-position-x: 10px;
}

.details
  #MainView
  .container
  .detailsnav
  .detailsnavright
  input::-webkit-input-placeholder {
  color: #999999;
  font-weight: lighter;
}
.details #MainView .container .detailscontent {
  color: #333333;
  width: 700px;
  float: left;
  margin: 0 30px;
}
.details #MainView .container .detailscontent .detailstile {
  font-size: 25px;
  font-weight: 400;
  line-height: 40px;
}
.details #MainView .container .detailscontent .detailslist .navs {
  font-size: 14px;
  height: 50px;
  padding: 30px 0px;
}
.details #MainView .container .detailscontent .detailslist .navs span {
  height: 20px;
  line-height: 20px;
  display: block;
}
.details #MainView .container .detailscontent .detailslist .navs .imgsd {
  width: 40px;
  height: 40px;
}

.details #MainView .container .detailscontent .detailslist .navs .navsleft {
  float: left;
}
.details #MainView .container .detailscontent .detailslist .navs .navsright {
  float: left;
  margin: 0 15px;
}
.details #MainView .container .detailsleft {
  float: left;
  width: 15%;
  font-size: 12px;
  color: #333333;
  -moz-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
.details #MainView .container .detailsleft ul th {
  padding: 15px 0 10px;
}
.details #MainView .container .detailsleft ul li {
  padding: 10px;
  margin: 10px 0;
  border-radius: 5px;
  line-height: 20px;
  cursor: pointer;
  font-weight: 400;
}
.details #MainView .container .detailsleft ul li:hover {
  color: #ffffff;
  -webkit-transition: 0.6s;
  transition: 0.6s;
  cursor: pointer;
  background-color: #3b78c3;
}
.details #MainView .container .detailsleft ul .cures {
  background-color: #3b78c3;
  color: #ffffff;
}
.details #MainView .container .detailsleft ul .atil a {
  border: none;
  text-decoration: underline;
  color: #333333;
}
.details #MainView .container .detailsleft ul .atil a:hover {
  color: #3b78c3;
}
</style>
