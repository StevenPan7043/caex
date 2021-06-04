<template>
  <div class="supheader">
    <div class="placeholder"></div>
    <div class="inner-header f-cb">
      <h1 class="head-logo f-fl">
        <router-link to="/">
          <img src="@/assets/images/nlogo.png" alt="Hotdog全球领先区块链数字资产交易平台">
        </router-link>
      </h1>
      <ul class="head-user f-fr">
        <li class="change-language">
          <a @click.prevent="changeLanguage">{{lang|langFilter}}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { setLang, getLang } from "../../libs/utils.js";
export default {
  data() {
    return {
      lang: ""
    };
  },
  filters: {
    langFilter(value) {
      if (value === "" || value === "zh") {
        return "English";
      } else {
        return "简体中文";
      }
    }
  },
  mounted() {
    this.lang = getLang();
  },
  methods: {
    changeLanguage() {
      if (this.lang === "" || this.lang === "zh") {
        this.lang = "en";
        this.$i18n.locale = this.lang;
        setLang("en");
        this.$emit("lang-change", "en");
      } else {
        this.lang = "zh";
        this.$i18n.locale = this.lang;
        this.$emit("lang-change", "zh");
        setLang("zh");
      }
    }
  }
};
</script>

<style lang="scss" scoped>

.inner-header ul {
  font-size: 14px;

-moz-user-select: none;  
-webkit-user-select: none;  
-ms-user-select: none;
user-select: none;
}

.inner-header .head-nav li {
  float: left;
  margin-left: 30px;
  padding-top: 20.5px;
  padding-bottom: 20.5px;
}

.inner-header .head-user li {
  float: left;
  padding-top: 20.5px;
  padding-bottom: 20.5px;
}

.inner-header .head-user li.user {
  position: relative;
  width: 108px;
  white-space: nowrap;
}

.inner-header .head-user .drop-menu {
  display: none;
  width: 120px;
  position: absolute;
  top: 56px;
  border-radius: 5px;
  background-color: #20293e;
  -webkit-box-shadow: 2px 2px 10px #000;
  box-shadow: 2px 2px 10px #000;
  -webkit-transition: 1s;
  transition: 1s;
  z-index: 10;
}
.inner-header .head-user .change-language {
  padding: 0 13px;
  margin: 19px 40px 19px 40px;
  border-radius: 6px;
  -webkit-transition: 0.6s;
  transition: 0.6s;
  white-space: nowrap;
  width: 72px;
  height: 20px;
  line-height: 20px;
  overflow: hidden;
  text-align: center;
  background-image: url("../../assets/images/sup.png");
  background-repeat: no-repeat;
  background-size: 16px 9px;
  background-position-x: right;
  background-position-y: center;
}
.inner-header .head-user .change-language a {
  color: #999999;
}
.supheader {
  position: relative;
  max-width: 1200px;
  margin: 0 auto;
}

.supheader .inner-header {
  background-color: #ffffff;
  position: absolute;
  width: 100%;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;
}

.supheader .placeholder {
  height: 60px;
}

.supheader .head-logo {
  height: 60px;
}

.supheader .head-logo img {
  width: 130px;
  height: 40px;
  display: block;
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
