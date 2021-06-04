<template>
    <div>
        <div class="table">
            <div class="table_title">
                <div class="first">被邀请人账号</div>
                <div class="other">
                    <div>邀请时间</div>
                    <div>状态</div>
                </div>
            </div>
            <div v-if="peopleTotal!==0">
                <div class="list_container">
                    <div class="table_item" v-for="(item,index) in peopleData" :key="index">
                        <div class="first">{{item.m_name}}</div>
                        <div class="other">
                            <div>{{item.create_time}}</div>
                            <div>{{item.expire_time|status}}</div>
                        </div>
                    </div>
                </div>
                <div class="pagination">
                    <el-pagination
                        @current-change="handleCurrentChange"
                        background
                        layout="prev, pager, next"
                        :total="peopleTotal">
                    </el-pagination>
                </div>
            </div>
            <ListEmpty v-else/>
        </div>
    </div>
</template>
<script>
import ListEmpty from './ListEmpty'
import {getPeopleByPage} from '../api/index'
export default {
    components:{
        ListEmpty
    },
    data() {
        return {
            peoplePage:{
                page:1,
                pageSize:10
            },
            peopleData:[],
            peopleTotal:0,
            nowTime:''
        }
    },

    filters:{
        status(time){
            var now = new Date();
            var yy = now.getFullYear();      //年
            var mm = now.getMonth() + 1;     //月
            var dd = now.getDate();          //日
            var hh = now.getHours();         //时
            var ii = now.getMinutes();       //分
            var ss = now.getSeconds();       //秒
            var clock = yy + "-";
            if(mm < 10) clock += "0";
            clock += mm + "-";
            if(dd < 10) clock += "0";
            clock += dd + " ";
            if(hh < 10) clock += "0";
            clock += hh + ":";
            if (ii < 10) clock += '0'; 
            clock += ii + ":";
            if (ss < 10) clock += '0'; 
            clock += ss;
            var nowTime = clock;
            if( ((new Date(time.replace(/-/g,"\/"))) >= (new Date(nowTime.replace(/-/g,"\/"))))){
                return '生效中'
            }else{
                return '已失效'
            }
        }
    },
    mounted(){
        this.getPeopleByPage();
    },
    methods:{
        getPeopleByPage(){
            this.getNowTime();
            getPeopleByPage(this.peoplePage).then(res=>{
                if(res.state===1){
                    this.peopleData=res.datas
                    this.peopleTotal=res.total
                }
            })
        },
        handleCurrentChange(currentPage){
            this.peoplePage.page=currentPage;
            this.getPeopleByPage();
        },
        getNowTime(){
            var now = new Date();
            var yy = now.getFullYear();      //年
            var mm = now.getMonth() + 1;     //月
            var dd = now.getDate();          //日
            var hh = now.getHours();         //时
            var ii = now.getMinutes();       //分
            var ss = now.getSeconds();       //秒
            var clock = yy + "-";
            if(mm < 10) clock += "0";
            clock += mm + "-";
            if(dd < 10) clock += "0";
            clock += dd + " ";
            if(hh < 10) clock += "0";
            clock += hh + ":";
            if (ii < 10) clock += '0'; 
            clock += ii + ":";
            if (ss < 10) clock += '0'; 
            clock += ss;
            this.nowTime = clock;  //获取当前日期
        }
    }
}
</script>
<style scoped>
.table{
    margin: 20px 30px 0 30px;
    height: 440px;
    overflow: auto;
}
.table_title{
    display: flex;
    font-size: 12px;
    color: #5a7896;
    height: 40px;
}
.table_title .first{
    width: 380px;
}
.table_title .other{
    display: flex;
    width: 100%;
    justify-content: space-between
}
.table_item{
    display: flex;
    font-size: 14px;
    color: #25425a;
    height: 40px;
}
.table_item .first{
    width: 380px;
}
.table_item .other{
    display: flex;
    width: 100%;
    justify-content: space-between
}
.pagination{
    width: 100%;
    display: flex;
    justify-content: center;
}
</style>
