<template>
  <el-container>
    <el-header style="padding: 0">
      <my-header />
    </el-header>
    <el-main style="padding: 0;overflow:hidden">
      <router-view />
    </el-main>
    <el-footer style="padding: 0;">
      <my-footer />
    </el-footer>
  </el-container>
</template>

<script>
import myHeader from "@/components/baseLayout/MyHead"
import myFooter from "@/components/baseLayout/MyFooter"
export default {
  components: {
    myHeader,
    myFooter
  }
};
</script>

<style lang="scss" scoped>
</style>