<template>
  <el-carousel height="480px">
    <el-carousel-item v-for="(item,index) in banners" :key="index">
      <img :src="item.a_img_file" @click="toDetail(item)" />
    </el-carousel-item>
  </el-carousel>
</template>

<script>
import { getNewsBanner } from '@/api/home.js'
// import {getLang} from '../../libs/utils.js'
export default {
  name: 'carrousel',
  props: {
    lang: {
      type: String,
      default: 'zh'
    }
  },
  data() {
    return {
      banners: []
    }
  },
  computed: {
    myLang() {
      return this.lang
    }
  },
  watch: {
    lang: function(val) {
      this.changeContentLang(val)
    }
  },
  created() {
    this.changeContentLang(this.lang)
  },
  methods: {
    changeContentLang(lang) {
      getNewsBanner().then(res => {
        this.banners = []
        res.data.forEach(banner => {
          if (lang == 'zh') {
            this.banners.push({
              a_img_file: banner.a_img_file,
              jump_url: banner.jump_url,
              is_jump: banner.is_jump
            })
          } else if (lang == 'en') {
            this.banners.push({
              a_img_file: banner.a_img_file_en,
              jump_url: banner.jump_url,
              is_jump: banner.is_jump
            })
          } else if (lang == 'ko') {
            this.banners.push({
              a_img_file: banner.a_img_file_ko,
              jump_url: banner.jump_url,
              is_jump: banner.is_jump
            })
          } else if (lang == 'ja') {
            this.banners.push({
              a_img_file: banner.a_img_file_jp,
              jump_url: banner.jump_url,
              is_jump: banner.is_jump
            })
          }
        })
      })
    },
    toDetail(item) {
      // if (item.is_jump === 0) {
      // this.$router.push({ path: "/newsdeta", query: { id: item.id } });
      // this.$router.push({path:'/newsDetail',query:{id:item.id}})
      // } else
      if (item.is_jump === 1) {
        if (item.jump_url) {
          window.location.href = item.jump_url
        }
      }
    }
  }
}
</script>
<style>
.el-carousel__item img {
  margin: 0;
  width: 100%;
  height: 100%;
}
.el-carousel__indicators {
  bottom: 20px;
}
.el-carousel__button {
  width: 15px;
  height: 5px;
  border-radius: 30px;
  background-color: deepskyblue;
}
</style>


