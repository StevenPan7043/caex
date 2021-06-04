<template>
  <div class="detailslists">
    <Header @lang-change="langChange"></Header>
    <div id="MainView">
      <div class="container" id="details">
        <div class="main-panel f-cb">
          <div class="content" id="view">
            <div class="detailsnav">
              <div class="detailsnavleft">
                <span @click="support">{{$t('news.HelpCenter')}}</span> >
                <span @click="backs" v-if="parent_id==314">{{$t('news.OfficialAnnouncement')}} > </span> 
                <span @click="backs" v-if="parent_id==315">{{$t('news.NoviceHelp')}} > </span> 
                <span @click="backs" v-if="parent_id==316">{{$t('news.AutonomousCommunity')}} > </span> 
                <span>{{lang=='zh'?parent_name:parent_name_en}}</span>
              </div>
              <div class="detailsnavright">
                <input type="text" :placeholder="$t('news.InputSearchKeyword')" v-model.trim="search" @keyup.enter="searchs">
              </div>
            </div>
            <div class="detailsleft"></div>
            <div class="detailscontent">
              <div class="detailstile">
                <span>{{lang=='zh'?parent_name:parent_name_en}}</span>
              </div>
              <div class="detailslist">
                <div class="coin">
                  <ul>
                    <li v-for="(item,index) in lista" :key="index" @click="detailsl(item.id,item.column_id,parent_name,parent_name_en)">
                      <i class="iconfont icon-androidstar"></i>
                      <span>{{lang=='zh'?item.a_title:item.a_title_en}}</span>
                      <span>{{item.a_time}}</span>
                    </li>
                  </ul>
                </div>
                <Pagesz v-on:page="onEnlargeText" :total="total"></Pagesz>
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
import { getNewsList } from '@/api/newsList'
import Pagesz from "@/components/Pages"
import { getLang } from "../libs/utils.js"
export default {
  data() {
    return {
      search:'',
      id:'',
      lista: [],
      lang:'zh',
      pages:1,
      pagesize:5,
      total:0,
      parent_id:0,
      parent_name:'',
      parent_name_en:'',
    };
  },
  created() {
    
      var e={'page':this.pages,'cursum':this.pagesize}
      this.onEnlargeText(e)
  },
  mounted() {
    // document
      // .querySelector("body")
      // .setAttribute("style", "background-color:#ffffff");
      this.id = this.$route.query.id;
      this.parent_id = this.$route.query.parent_id
      this.parent_name = this.$route.query.parent_name
      this.parent_name_en = this.$route.query.parent_name_en
      this.getlist(this.id,this.pages,this.pagesize)
    let lang = getLang()
    if(lang){
      this.lang = lang
    }
  },
  methods: {
    detailsl(item,column_id,name,name_en) {
      this.$router.push({ path: "/details", query: { id: item,column_id:column_id,parent_name:name,parent_name_en:name_en} });
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
    onEnlargeText (e) {
      var p  = e.page;
      var s = e.cursum;
      this.getlist('',p,s)
    },
    getlist (a=this.id,b,c){
      getNewsList(a,b,c).then(res => {
        // debugger
          if (res.data.list) {
              this.total = Math.ceil(res.data.total/c);
              this.lista = res.data.list
          } else {
            this.$message({
              type: "error",
              message: this.$t("tradecenter.ThisIsTheLastPage"),
              duration: 3000,
              showClose: true
            });
          }
      });
    },
    searchs () {
      this.$router.push({path:'/search',query:{key:this.search}})
    },
  },
  components: {
    Header,
    Footer,
    Pagesz
  }
};
</script>

<style lang="scss" scoped>
.detailslists{

  min-height:900px;
    min-width: 1400px;
  background-color: #ffffff;
}
.detailslists #MainView .container {
  min-height: 900px;
}
.detailslists #MainView .container .detailsnav {
  height: 90px;
  line-height: 90px;
  font-weight: 400;
}
.detailslists #MainView .container .detailsnav .detailsnavleft {
  float: left;
}
.detailslists #MainView .container .detailsnav .detailsnavleft span{
  cursor: pointer;
}
.detailslists #MainView .container .detailsnav .detailsnavleft span:nth-child(1){
  color: #3b78c3;
}
.detailslists #MainView .container .detailsnav .detailsnavleft span:nth-last-child(1){
  color: #333333;
}
.detailslists #MainView .container .detailsnav .detailsnavright {
  float: right;
}
.detailslists #MainView .container .detailsnav .detailsnavright input {
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

.detailslists
  #MainView
  .container
  .detailsnav
  .detailsnavright
  input::-webkit-input-placeholder {
  color: #999999;
  font-weight: lighter;
}
.detailslists #MainView .container .detailscontent {
  color: #333333;
  width: 1000px;
  float: right;
}
.detailslists #MainView .container .detailscontent .detailstile {
  font-size: 25px;
  font-weight: 400;
  height: 60px;
  line-height: 60px;
}
.detailslists #MainView .container .detailscontent .detailslist .coin {
  float: left;
  width: 100%;
  min-height: 500px;
}

.detailslists #MainView .container .detailscontent .detailslist .coin ul th {
  font-size: 16px;
}
.detailslists #MainView .container .detailscontent .detailslist .coin ul li {
  font-size: 14px;
  height: 45px;
  line-height: 45px;
  border-bottom: 1px solid #e2e2e2;
    overflow: hidden;
}
.detailslists #MainView .container .detailscontent .detailslist .coin ul li:hover {
  color: #3b78c3;
  -webkit-transition: 0.6s;
  transition: 0.6s;
  cursor: pointer;
}
.detailslists
  #MainView
  .container
  .detailscontent
  .detailslist
  .coin
  ul
  li
  span:nth-child(3) {
  font-size: 14px;
  float: right;
  color: #666666;
}
.detailslists
  #MainView
  .container
  .detailscontent
  .detailslist
  .coin
  ul
  li
  .icon-androidstar {
  background: linear-gradient(to right, #0c458a, #fff);
  -webkit-background-clip: text;
  color: transparent;
}

.detailslists #MainView .container .detailsleft {
  float: left;
}
</style>
