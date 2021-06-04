<template>
  <div class="newsdeta">
    <Header @lang-change="langChange"></Header>
    <div id="MainView">
      <div class="container" id="newsdeta">
        <div class="main-panel f-cb">
          <div class="content" id="view">
            <div class="newsnav">
              <div class="newsnavleft">
                <span @click="support">{{$t('news.HelpCenter')}}</span> >
                <span @click="backs" v-if="id==314">{{$t('news.OfficialAnnouncement')}}</span> 
                <span @click="backs" v-if="id==315">{{$t('news.NoviceHelp')}}</span> 
                <span @click="backs" v-if="id==316">{{$t('news.AutonomousCommunity')}}</span> 
              </div>
              <div class="newsnavright">
                <input type="text" :placeholder="$t('news.InputSearchKeyword')" v-model.trim="search" @keyup.enter="searchs">
              </div>
            </div>
            <div class="newsleft"></div>
            <div class="newscontent">
              <div class="newstile">
                <span @click="backs" v-if="id==314">{{$t('news.OfficialAnnouncement')}}</span> 
                <span @click="backs" v-if="id==315">{{$t('news.NoviceHelp')}}</span> 
                <span @click="backs" v-if="id==316">{{$t('news.AutonomousCommunity')}}</span> 
              </div>
              <div class="newslist">
                <div class="coin" v-for="(items,index) in lists" :key="index">
                  <ul>
                    <th @click="detaillist(items.id,id,items.parent_name)" >{{lang=='zh'?items.parent_name:items.parent_name_en}}</th>
                    <li v-for="(item,index) in items.list" :key="index" @click="detail(item.id,items.id,id,items.parent_name,items.parent_name_en)">
                      <i class="iconfont icon-androidstar"></i> {{lang=='zh'?item.a_title:item.a_title_en}}
                    </li>
                    <li class="atil">
                      <a @click="detaillist(items.id,id,items.parent_name)">{{$t('news.Viewall')}} {{items.total}} {{$t('news.Articles')}} >></a>
                    </li>
                  </ul>
                </div>
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
import { getNewssub } from '@/api/newsdate'
import {getLang} from '../libs/utils.js'
export default {
  data() {
    return {
      search:'',
      lists:[],
      lang:'zh',
      id:'',
      total:0,
    };
  },
  mounted() {
    let lang = getLang()
    if(lang){
      this.lang = lang
    }
  },
  created() {
    this.id = this.$route.query.id;
    getNewssub(this.id, 3).then(res=>{
      if (res.state === 1) {
        this.lists = res.data;
      }
    })
  },
  methods: {
    detaillist(item,id,name) {
      this.$router.push({ path: "/detaillist", query: { id: item ,parent_id:id,parent_name:name } });
    },
    detail (item,column_id,id,name,name_en) {
      this.$router.push({ path: "/details", query: { id: item,column_id:column_id,p_id:id,parent_name:name,parent_name_en:name_en } });
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
.newsdeta{
  min-height:900px;
    min-width: 1400px;
  background-color: #ffffff;
}
.newsdeta #MainView .container {
  min-height: 900px;
}
.newsdeta #MainView .container .newsnav {
  height: 90px;
  line-height: 90px;
  font-weight: 400;
}
.newsdeta #MainView .container .newsnav .newsnavleft {
  float: left;
}
.newsdeta #MainView .container .newsnav .newsnavleft span{
  cursor: pointer;
}
.newsdeta #MainView .container .newsnav .newsnavleft span:nth-child(1){
  color: #3b78c3;
}
.newsdeta #MainView .container .newsnav .newsnavleft span:nth-last-child(1){
  color: #333333;
}
.newsdeta #MainView .container .newsnav .newsnavright {
  float: right;
}
.newsdeta #MainView .container .newsnav .newsnavright input {
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

.newsdeta
  #MainView
  .container
  .newsnav
  .newsnavright
  input::-webkit-input-placeholder {
  color: #999999;
  font-weight: lighter;
}
.newsdeta #MainView .container .newscontent {
  color: #333333;
  width: 1000px;
  float: right;
}
.newsdeta #MainView .container .newscontent .newstile {
  font-size: 25px;
  font-weight: 400;
  height: 60px;
  line-height: 60px;
}
.newsdeta #MainView .container .newscontent .newslist .coin {
  float: left;
  width: 45%;
  margin: 30px 0;
  padding-right: 50px;
  min-height: 250px;
}

.newsdeta #MainView .container .newscontent .newslist .coin ul li:hover {
  color: #3b78c3;
  -webkit-transition: 0.6s;
  transition: 0.6s;
  cursor: pointer;
}
.newsdeta #MainView .container .newscontent .newslist .coin ul th {
  font-size: 16px;
  cursor: pointer;
}
.newsdeta #MainView .container .newscontent .newslist .coin ul li {
  font-size: 14px;
  height: 45px;
  line-height: 45px;
  border-bottom: 1px solid #e2e2e2;
  overflow: hidden;
}
.newsdeta
  #MainView
  .container
  .newscontent
  .newslist
  .coin
  ul
  li
  .icon-androidstar {
  background: linear-gradient(to right, #0c458a, #fff);
  -webkit-background-clip: text;
  color: transparent;
}
.newsdeta #MainView .container .newscontent .newslist .coin ul .atil {
  font-size: 14px;
  height: 45px;
  line-height: 45px;
  border: none;
  text-decoration: underline;
}
.newsdeta #MainView .container .newscontent .newslist .coin ul .atil a {
  font-size: 12px;
  color: #333333;
}
.newsdeta #MainView .container .newscontent .newslist .coin ul .atil a:hover {
  color: #3b78c3;
}
.newsdeta #MainView .container .newsleft {
  float: left;
}
</style>
