<template>
    <div>
        <div class="table">
            <div class="table_title">
                <div class="first">币种</div>
                <div class="other">
                    <div>返还佣金</div>
                    <div>返佣时间</div>
                </div>
            </div>
            <div v-if="moneyTotal!==0">
                <div v-for="(item,index) in moneyData" :key="index">
                    <div class="table_item">
                        <div class="first">{{item.currency}}</div>
                        <div class="other">
                            <div>{{item.return_commi_amount}}</div>
                            <div>{{item.create_time}}</div>
                        </div>
                    </div>
                </div>

                <div class="pagination">
                    <el-pagination
                        background
                        layout="prev, pager, next"
                        :total="moneyTotal">
                    </el-pagination>
                </div>
            </div>
            <ListEmpty v-else/>
        </div>

    </div>
</template>
<script>
import ListEmpty from './ListEmpty'
import {getMoneyByPage} from '../api/index'
export default {
    components:{ListEmpty},
    data() {
        return {
            moneyPage:{
                page:1,
                pageSize:10
            },

            moneyData:[],
            moneyTotal:0,
        }
    },
    mounted(){
        this.getMoneyByPage()
    },
    methods:{
        getMoneyByPage(){
            getMoneyByPage(this.moneyPage).then(res=>{
                if(res.state===1){
                    this.moneyData=res.datas
                    this.moneyTotal=res.total
                }
            })
        },
        handleCurrentChange(currentPage){
            this.moneyPage.page=currentPage;
            this.getMoneyByPage();
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
