<template>
  <div class="pagesz">
    <div class="pbottom">
      <div class="pagesn">
        <span @click="prevClick"><</span>
        <span @click="goPage(1)" v-if="total!==1" :class="1 == page2 ? 'curs':''">1</span>
        <span v-if="preClipped">...</span>
        <span v-for="index in pages" :key="index" @click="goPage(index)" :class="index==page2?'curs':''">{{index}}</span>
        <span v-if="backClipped">...</span>
        <span :class="total==page2?'curs':''" @click="goPage(total)">{{total}}</span>
        <span @click="nextClick">></span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props:['total'],
  data() {
    return {
        page2:1,
        curSum2:5,
        backClipped:true,
        preClipped:false,
    };
  },
  created() {
      
  },
  computed:{
    pages: function () {
      let ret = []
      if (this.page2 > 3) {
        ret.push(this.page2 - 2)
        ret.push(this.page2 - 1)
        if (this.page2 > 4) {
          this.preClipped = true
        }
      } else {
        this.preClipped = false
        for (let i = 2; i < this.page2; i++) {
          ret.push(i)
        }
      }
      if (this.page2 !== this.total && this.page2 !== 1) {
        ret.push(this.page2)
      }
      if (this.page2 < (this.total - 2)) {
        ret.push(this.page2 + 1)
        ret.push(this.page2 + 2)
        if (this.page2 <= (this.total - 3)) {
          this.backClipped = true
        }
      } else {
        this.backClipped = false
        for (let i = (this.page2 + 1); i < this.total; i++) {
          ret.push(i)
        }
      }
      return ret
    }
  },
  methods: {
    goPage (i){
        if(i!==this.page2){
        this.$emit("page", { page: i, cursum: this.curSum2 });
        this.page2 = i;
        }
    },
    prevClick() {
      this.page2--;
      if (this.page2 < 1) {
        this.page2++;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsPageOne"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.$emit("page", { page: this.page2, cursum: this.curSum2 });
      }
    },
    nextClick() {
      this.page2++;
      if (this.page2 > this.total) {
        this.page2--;
        this.$message({
          type: "error",
          message: this.$t("tradecenter.ThisIsTheLastPage"),
          duration: 3000,
          showClose: true
        });
      } else {
        this.$emit("page", { page: this.page2, cursum: this.curSum2 });
      }
    }
  },
  component: {}
};
</script>
<style lang="scss" scoped>
.pagesz .pbottom {
  float: left;
  height: 50px;
  width: 100%;
  text-align: center;
  margin: 40px 0;
  -moz-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
.pagesz .pbottom .pagesn span {
  width: 28px;
  height: 30px;
  line-height: 30px;
  border: 1px solid #dcdcdc;
  color: #333333;
  display: inline-block;
  border-radius: 5px;
  margin: 5px;
  cursor: pointer;
}
.pagesz .pbottom .pagesn .curs {
  border: 1px solid #3b78c3;
  color: #3b78c3;
}
</style>
