<template>
    <div class="invite_record">
        <div class="title ">
            <p :class="type==='invite'?'cur':''" @click="changeTab(0)">邀请记录</p>
            <p :class="type!=='invite'?'cur':''" @click="changeTab(1)">返佣记录</p>
        </div>
        <InviteList v-show="type==='invite'" />
        <RebateList v-show="type==='rebate'" />

    </div>
</template>
<script>
import InviteList from './InviteList';
import RebateList from './RebateList'
export default {
    components:{
        InviteList,
        RebateList
    },
    data(){
        return {
            type:'invite',
        }
    },
    methods:{
        changeTab(index){
            if(index===0){
                this.type='invite';
            }else{
                this.type='rebate';
            }
        },
    }
}
</script>
<style scoped>
.invite_record{
    width: 1200px;
    height: 560px;
    background-color: #fff;
    margin-bottom: 20px;
    color: #25425a;
}
.title{
    height: 60px;
    width: 100%;
    box-shadow: 0px 2px 6px 0px 
    rgba(0, 0, 0, 0.1);
    display: flex;

}
.title p{
    margin-left: 30px;
	font-size: 16px;
    line-height: 60px;
    cursor: pointer;
}
.cur{
    color: #0c72ea;
    border-bottom: 2px solid #0c72ea
}



</style>
