<template>
  <div class="wrap">
    <span class="pre" @click="pre" :class="[ current==1 ?'disabled':'']">上一页</span>
    <span class="next active" @click="next" :class="[ current==page ?'disabled':'']">下一页</span>
    <span class="total">第{{ current }}页/共{{ page }}页</span>
  </div>
</template>
<script>
export default {
  props: ["total", "pageSizes"],
  data() {
    return {
      current: 1
    };
  },
  computed: {
    page() {
      return Math.ceil(this.total / this.pageSizes);
    }
  },
  methods: {
    pre() {
      if (this.current > 1) {
        this.current--;
        this.$emit("getCurPage", this.current);
      }
    },
    next() {
      if (this.current < this.page) {
        this.current++;
        this.$emit("getCurPage", this.current);
      }
    }
  }
};
</script>
<style lang="scss" scoped>
@import "~/css/home/pagination.scss";
</style>