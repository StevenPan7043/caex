<template>
    <div>
        <my-header></my-header>

        <div class="main_container">
            <div class="main_content">
                <div class="title_container">
                    {{$t('header.ContractOrder')}}
                </div>
                <div class="tab_container">
                    <div style="display: flex">
                        <div class="tab_item" :class="tabIndex===0?'select_tab':''" @click="tabTap(0)">{{$t('contract.CommissionedIn')}}</div>
                        <div class="tab_item" :class="tabIndex===1?'select_tab':''" @click="tabTap(1)">{{$t('contract.EntrustSuccess')}}</div>
                        <div class="tab_item" :class="tabIndex===2?'select_tab':''" @click="tabTap(2)">{{$t('contract.Cancel')}}</div>
                    </div>
                </div>
                <TableHeader :tab-index="tabIndex"/>
                <div class="list_item_container">
                    <TableItem v-for="(item,index) in list" :key="index" :item="item">
                        <p class="cancel_button" v-if="tabIndex===0" @click="cancelDelegate(item)">
                          {{$t('contract.Cancel')}}
                        </p>
                        <p v-else-if="tabIndex===1">
                          {{$t('contract.EntrustSuccess')}}
                        </p>
                        <p v-else>
                          {{$t('contract.Canceled')}}
                        </p>
                    </TableItem>
                </div>
                <div class="page" >
                    <span class="up" @click="prePage">{{$t('contract.Pre')}}</span>
                    <span class="down" @click="nextPage">{{$t('contract.Next')}}</span>
                </div>
            </div>
        </div>
        <my-footer></my-footer>
    </div>
</template>

<script>
    import MyHeader from "@/components/Header";
    import MyFooter from "@/components/Footer";
    import TableHeader from './components/table_header'
    import TableItem from './components/table_item'

    export default {
        components: {
            MyHeader,
            MyFooter,
            TableHeader,TableItem
        },
        watch:{
            tabIndex(){
                this.list=[];
                this.page=1;
                this.getData();
            }
        },
        data() {
            return {
                tabIndex:0,
                list:[],
                page:1
            };
        },

        mounted() {
          this.checkLoginStatus();
          this.getData();
        },
        methods: {
            // 检查登录情况
            checkLoginStatus(){
                this.$ajax("/account/getMyInfos", {
                    token: this.$userID()
                }).then(res => {
                    if (res.data.status) {
                        if (this.$route.name === "Login") {
                            this.$router.push("/");
                        }
                    } else {
                        this.$router.push("/login");
                    }
                });
            },

            // tab选择
            tabTap(e) {
                this.tabIndex = e;
            },
            getData(){
                this.$ajax("/trade/queryMyEntrust", {
                    token: this.$userID(),
                    status: this.tabIndex + 1,
                    page: this.page
                }).then(res => {
                    if (res.data.status) {
                        this.list =[...this.list,...res.data.data] ;
                    }
                });
            },
            prePage() {
                if (this.page !== 1) {
                    this.page = this.page - 1;
                    this.getData();
                }
            },
            nextPage() {
                this.page = this.page + 1;
                this.getData();
            },

            // 取消委托订单
            cancelDelegate(item) {
                const coin = item.coin.toUpperCase().replace('_','/')
                const h = this.$createElement;
                this.$msgbox({
                    title: '提示',
                    message: h('p', {style: 'display:flex'}, [
                        item.type === 1 ? h('div', {
                                style: ` width: 40px;
                                    height: 20px;
                                    color: #fff;
                                    line-height: 20px;
                                    text-align: center;
                                    background-color: #5bbe8e;`
                            }, '开多') :
                            h('div', {
                                style: ` width: 40px;
                                    height: 20px;
                                    color: #fff;
                                    line-height: 20px;
                                    text-align: center;
                                    background-color: #ee6560;`
                            }, '开空'),
                        h('span', {style: 'margin: 0 10px'}, coin),
                        h('span', null, '是否取消委托单？'),
                    ]),
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(() => {
                    this.$ajax('/trade/handleCancelEntrust', {
                        token: this.$userID(),
                        ordercode: item.ordercode
                    }).then(
                        res => {
                            if (res.data.status) {
                                this.$message({
                                    type: "success",
                                    message: res.data.desc
                                });
                                this.page=1;
                                this.list=[];
                                this.getData();
                            } else {
                                this.$message({
                                    type: "error",
                                    message: res.data.desc
                                });
                            }
                        })
                })

            },
        }
    };
</script>

<style scoped>
    .main_container {
        display: flex;
        width: 100%;
        min-height: 700px;
        justify-content: center;
        background-color: #f2f6f9;
    }

    .main_content {
        width: 1200px;
        min-height: 700px;
        margin-top: 20px;
        margin-bottom: 60px;
    }
    .title_container {
        width: 1200px;
        height: 60px;
        background-color: #fff;
        display: flex;
        font-size: 18px;
        color: #1c242c;
        align-items: center;
        padding: 0 30px;
        box-sizing: border-box;
        margin-bottom: 10px;
    }
    .tab_container {
        width: 1200px;
        height: 50px;
        background-color: #fff;
        display: flex;
        padding: 0 30px;
        box-sizing: border-box;
        justify-content: space-between;
        align-items: center;
    }

    .tab_item {
        cursor: pointer;
        padding: 0 20px;
        color: #1c242c;
        font-size: 16px;
        text-align: center;
        line-height: 44px;
        border-bottom: 3px solid transparent;
        border-top: 3px solid transparent;
        margin-right: 30px;
    }

    .select_tab {
        border-bottom: 3px solid #196bdf;
        color: #196bdf;
    }
    .cancel_button{
        width: 38px;
        height: 20px;
        border-radius: 2px;
        border: solid 1px #196bdf;
        font-size: 12px;
        color: #196bdf;
        text-align: center;
        line-height: 20px;
        cursor: pointer;
    }
    .cancel_button:hover{
        background-color: #196bdf;
        color: #fff;
    }
    .page {
        text-align: right;
        padding: 15px 30px;
        background-color: #fff;
    }
    .page span {
        font-size: 12px;
        color: #1c242c;
        margin-left: 10px;
    }
    .page .up:hover {
        color: #196bdf;
        cursor: pointer;
    }
    .page .down:hover {
        color: #196bdf;
        cursor: pointer;
    }
    .list_item_container{
        height: 800px;
        background-color: #fff;
      overflow: auto;
    }

</style>
