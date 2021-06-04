<template>
  <div class="ndws">
    <Header @lang-change="langChange"></Header>
    <div id="MainView">
      <div class="topbat">
        <div class="search">
          <input type="text" :placeholder="$t('news.Pleasesearch')" v-model.trim="search" @keyup.enter="searchs">
          <span @click="searchs">{{$t('news.Search')}}</span>
        </div>
      </div>
      <div class="container" id="support">
        <div class="main-panel f-cb">
          <div class="content" id="view">
            <div class="container">
              <div class="supportnbt">
                <span @click="newdeta(314)">{{$t('news.OfficialAnnouncement')}}</span>
                <span @click="newdeta(315)">{{$t('news.NoviceHelp')}}</span>
                <span @click="newdeta(316)">{{$t('news.AutonomousCommunity')}}</span>
              </div>
              <div class="supportnews">
                <div class="supporttitle">{{$t('news.RecentActivities')}}</div>
                <div class="supportlist">
                  <ul>
                    <li v-for="(item,index) in list" :key="index" @click="newdeta(item.parent_id)">
                      <p>{{lang=='zh'?item.parent_name:item.parent_name_en}}</p>
                      <span>{{lang=='zh'?item.a_title:item.a_title_en}}</span>
                      <span class="dates">{{item.a_time}}</span>
                    </li>
                  </ul>
                </div>
                <div class="supportfot" @click="more">{{$t('news.SeeMore')}}</div>
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
import {getLang} from '../libs/utils.js'
import { getSupport } from '@/api/support'
export default {
  data() {
    return {
      search:'',
      lang:'zh',
      pages:1,
      pagesize:5,
      list: [],
      total:0,
    };
  },
  mounted() {
    this.support(this.pages,this.pagesize)
    let lang = getLang()
    if(lang){
      this.lang = lang
    }
  },
  methods: {
    langChange(lang){
      this.lang=lang
    },
    newdeta(item) {
      this.$router.push({ path: "/newsdeta", query: { id: item } });
    },
    searchs () {
      this.$router.push({path:'/search',query:{key:this.search}})
    },
    more () {
      this.support(this.pages,this.pagesize+2);
    },
    support(pages,pagesize){
      getSupport(pages,pagesize).then(res=>{
        if(res.state == 1){
          this.list = res.data;
          this.total = res.data.length
        }
      })
    }
  },
  components: {
    Header,
    Footer
  }
};
</script>
<style scoped>
.supportlist ul li {
  color: #333333;
  font-size: 14px;
  line-height: 25px;
  border-bottom: 1px solid #dddddd;
  padding: 15px 0px;
}
.supportlist ul li:hover {
  color: #3b78c3;
  -webkit-transition: 0.6s;
  transition: 0.6s;
  cursor: pointer;
}
.supportlist ul li .dates {
  color: #666666;
  font-size: 14px;
  float: right;
}
.supportnews .supporttitle {
  text-align: center;
  font-size: 20px;
  color: #333333;
  border-top: 1px solid #dddddd;
  padding: 30px;
}
.supportnews .supportfot {
  color: #333333;
  font-size: 14px;
  text-decoration: underline;
  height: 50px;
  line-height: 50px;
}
.supportnbt {
  display: flex;
  justify-content: space-between;
  margin: 60px 0px;
}
.supportnbt span {
    border: 1px solid #3b78c3;
    border-radius: 5px;
    width: 375px;
    height: 45px;
    line-height: 45px;
    color: #3b78c3;
    text-align: center;
}
.supportnbt span:hover {
  background-color: #3b78c3;
  color: #ffffff;
  -webkit-transition: 0.6s;
  transition: 0.6s;
  cursor: pointer;
}
#MainView .topbat {
  height: 300px;
  /* background-color: aquamarine; */
  background-image: url("../assets/images/supportbg.jpg");
  background-repeat: no-repeat;
}
#MainView .topbat .search {
  text-align: center;
  position: relative;
  top: 100px;
}

#MainView .topbat .search input {
  background: none;
  width: 80%;
  height: 1.2rem;
  font-size: 16px;
  color: #ffffff;
  border: 1px solid #1e5a8c;
  outline: none;
  background-image: url("../assets/images/search.png");
  background-repeat: no-repeat;
  background-position-y: center;
  background-position-x: 10px;
  text-indent: 3rem;
}

#MainView .topbat .search input::-webkit-input-placeholder {
  color: #b3d1f6;
  font-weight: lighter;
}
#MainView .topbat .search span {
  padding: 14px 39px;
  background-color: #3b78c3;
  color: #ffffff;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: lighter;
}
#MainView .topbat .search input {
  height: 45px;
  width: 455px;
  border-radius: 5px;
  font-weight: lighter;
}
#MainView .topbat .search input:hover {
  border: 1px solid #2395f3;
  -webkit-transition: 0.6s;
  transition: 0.6s;
}
#MainView .container .supportnews {
  min-height: 800px;
}
.ndws {
  min-height: 800px;
  min-width: 1400px;
  background-color: #ffffff;
}
 #MainView .container .supportnews .supportfot:hover {
   cursor: pointer;
  color: #3b78c3;
}
</style>
