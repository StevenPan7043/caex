<template>
  <div>
    <my-header></my-header>
    <div class="main_view">
      <Title />
      <AssetsContent @tab-change="tabChange" />
    </div>
    <my-footer></my-footer>
  </div>
</template>

<script>
import MyHeader from "@/components/Header/index.vue";
import MyFooter from "@/components/Footer";
import Title from "./components/Title";
import AssetsContent from "./components/Content";



export default {
  components: {
    MyHeader,
    MyFooter,
    Title,
    AssetsContent,
  },
  data(){
    return{
      tabIndex:0
    }
  },
  methods:{
    tabChange(index){
     this.tabIndex=index
    },

  }
};
</script>

<style scoped>
.main_view {
  background-color: #f2f6f9;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
