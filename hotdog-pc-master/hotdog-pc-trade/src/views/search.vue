<template>
  <div class="detailslists">
    <Header @lang-change="langChange"></Header>
    <div id="MainView">
      <div class="container" id="details">
        <div class="main-panel f-cb">
          <div class="content" id="view">
            <div class="detailsnav">
              <div class="detailsnavleft">
                <span @click="support">{{$t('news.HelpCenter')}}</span> 
                <span> > {{$t('news.SearchResults')}}</span>
              </div>
              <div class="detailsnavright">
                <input type="text" :placeholder="$t('news.InputSearchKeyword')" v-model.trim="searcha" @keyup.enter="searchw">
              </div>
            </div>
            <div class="detailsleft"></div>
            <div class="detailscontent">
              <div class="detailstile">
                <span>{{$t('news.SearchResults')}}</span>
              </div>
              <div class="detailslist">
                <div class="coin">
                  <ul>
                      <li>{{$t('news.Repository')}}</li>
                    <li v-for="(item,index) in slist" :key="index" @click="detailsl(item.id,item.column_id,)">
                      <p class="title">{{lang=='zh'?item.a_title:item.a_title_en}}</p>
                      <p class="cats"><em v-for="(v,i) in item.parent_info" :key="i">{{v.c_name}} > {{item.column_id[v]}}</em>  ·  {{item.a_author}}  ·  {{item.a_time}}</p>
                      <p class="content">{{item.a_abstract}}</p>
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
import Header from "@/components/Header/newshd.vue"
import Footer from "@/components/Footer/newsfoot.vue"
import Pagesz from "@/components/Pages"
import {getLang} from '../libs/utils.js'
import { getSearch } from '@/api/search'
export default {
  data() {
    return {
      id:'',
      lista: [],
      lang:'zh',
      pages:1,
      pagesize:5,
      total:0,
      key:'',
      slist:'',
      searcha:'',
    };
  },
  computed: {
  },
  created() {
    
      // var e={'page':this.pages,'cursum':this.pagesize}
      // this.onEnlargeText(e)
  },
  mounted() {
    // document
    //   .querySelector("body")
    //   .setAttribute("style", "background-color:#ffffff");
      this.key = this.$route.query.key;
      this.search(this.pages,this.pagesize,this.key);
    let lang = getLang()
    if(lang){
      this.lang = lang
    }
  },
  methods: {
    detailsl(item,column_id) {
      this.$router.push({ path: "/details", query: { id: item,column_id:column_id} });
    },
    langChange(lang){
      this.lang=lang
    },
    onEnlargeText (e) {
      var p  = e.page;
      var s = e.cursum;
      getSearch(p, s,this.key).then(res => {
        // debugger
          if (res.data.list) {
              this.total = Math.ceil(res.data.total/s);
              this.slist = res.data.list
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
    support () {
      this.$router.push('/support')
    },
    search(pages,pagesize,key){
      getSearch(pages,pagesize,key).then(res=>{
        if(res.state == 1){
          this.total = Math.ceil(res.data.total/pagesize);
          this.slist = res.data.list
          console.log(res.data.list);
        }
      })
    },
    searchw(){
      this.search(this.pages,this.pagesize,this.searcha);
    }
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
  font-weight: 300;
}
.detailslists #MainView .container .detailsnav .detailsnavleft {
  float: left;
}
.detailslists #MainView .container .detailsnav .detailsnavleft span:nth-child(1){
  color: #3b78c3;
  cursor: pointer;
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
  float: left;
  width: 600px;
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
.detailslists #MainView .container .detailscontent .detailslist .coin ul li:nth-child(1):hover{
  color: #333333;
  cursor:unset;
}
.detailslists #MainView .container .detailscontent .detailslist .coin ul li {
  font-size: 14px;
  line-height: 25px;
  border-bottom: 1px solid #e2e2e2;
    overflow: hidden;
    padding: 20px 0;
}
.detailslists #MainView .container .detailscontent .detailslist .coin ul li .title{
  font-size: 15px;
  color: #3b78c3;
}
.detailslists #MainView .container .detailscontent .detailslist .coin ul li .cats{
  font-size: 13px;
  color: #666666;
}
.detailslists #MainView .container .detailscontent .detailslist .coin ul li .cats em{
  color: #3b78c3;
}
.detailslists #MainView .container .detailscontent .detailslist .coin ul li .content{
  color: #333333;
  font-size: 15px;
  margin: 20px 0 0;
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
.detailslists #MainView .container .detailsleft {
  float: left;
}
</style>
