<template>
    <div class="container">
        <div class="title">
            <div class="left">
                <div class="tab" :class="tabIndex===0?'select':''" @click="tabClick(0)">
                    <span>{{$t('contract.AllWarehouseAccount')}}</span>
                </div>
                <div class="tab" :class="tabIndex===1?'select':''" @click="tabClick(1)">
                    <span>{{$t('contract.EachWarehouseAccount')}}</span>
                </div>
            </div>
            <div class="right">
                <p>
                    <router-link to="/order">{{$t('header.ContractPositionOrder')}}</router-link>
                </p>
                <p>&nbsp&nbsp/&nbsp&nbsp</p>
                <p>
                    <router-link to="/order_entrust">{{$t('header.ContractOrder')}}</router-link>
                </p>
            </div>
        </div>
        <div class="content">
            <div class="left">
                <p>{{$t('contract.Full')}}</p>
                <div>
                    <span class="main">{{assetsInfo.usdt||assetsInfo.zcUsdt?(tabIndex===0?assetsInfo.usdt:assetsInfo.zcUsdt).toFixed(6):0}}</span>
                    <span class="main">&nbspUSDT</span>
                    <span
                            class="sub"
                    >≈ {{assetsInfo.usdtCny||assetsInfo.zcUsdtCny?(tabIndex===0?assetsInfo.usdtCny:assetsInfo.zcUsdtCny).toFixed(2):0}} CNY</span>
                </div>
            </div>
            <div class="right">
                <a @click.prevent="toUsdtRecharge">
                    <div class="button">{{$t('user.Deposit')}}</div>
                </a>
                <a @click.prevent="toUsdtWithdraw">
                    <div class="button">{{$t('user.Withdraw')}}</div>
                </a>
                <div class="button" @click="openDialog">{{$t('contract.FundsTransfer')}}</div>
            </div>
        </div>

        <Record
                :list="list"
                :page="page"
                :total="total"
                :pageSize="pageSize"
                @pre-page="prePage"
                @next-page="nextPage"
        />

        <DialogForm
                :dialogFormVisible="dialogFormVisible"
                :bbMoney="bbMoney"
                :fbMoney="fbMoney"
                :qcMoney="qcMoney"
                :zcMoney="zcMoney"
                @close-dialog="closeDialog"
                @reload-page="reloadPage"
        />
    </div>
</template>

<script>
    import DialogForm from "./DialogForm";
    import Record from "./Record";
    import {getQcRecordAjax, getZcRecordAjax} from "../../../api/contract";

    import {getAssets} from "../../../api/contract";

    export default {
        components: {
            DialogForm,
            Record
        },
        mounted() {
            this.getAssets();
            this.getRecord();
        },
        data() {
            return {
                tabIndex: 0,
                assetsInfo: {},
                dialogFormVisible: false,
                bbMoney: 0,
                fbMoney: 0,
                qcMoney: 0,
                zcMoney: 0,
                page: 1,
                pageSize: 10,
                list: [],
                total: 0
            };
        },
        methods: {
            toUsdtRecharge() {
                this.$router.push({
                    name: "Recharge",
                    params: {currency: 'USDT'}
                });
            },
            toUsdtWithdraw() {
                this.$router.push({
                    name: "Withdraw",
                    params: {currency: 'USDT'}
                });
            },
            getAssets() {
                this.$ajax("/account/getMyInfos", {
                    token: this.$userID()
                }).then(res => {
                    if (res.data.status) {
                        if (this.$route.name === "Login") {
                            this.$router.push("/");
                        }
                        this.assetsInfo = res.data.data;
                    } else {
                        this.$router.push("/login");
                    }
                });
            },
            tabClick(index) {
                this.tabIndex = index;
                this.$emit("tab-change", index);
                this.getAssets();
                this.page = 1;
                this.getRecord();
            },
            openDialog() {
                this.dialogFormVisible = true;
                this.getBBFbMoney();
                this.getQCZCMoney();
            },
            closeDialog() {
                this.dialogFormVisible = false;
            },
            reloadPage() {
                this.getAssets();
                this.page = 1;
                this.getRecord();
            },
            // 获取币币和法币的资金
            async getBBFbMoney() {
                let res = await getAssets();
                res.data.bb.forEach(item => {
                    if (item.name === "USDT") {
                        this.bbMoney = item.enable;
                    }
                });
                res.data.fb.forEach(item => {
                    if (item.name == "USDT") {
                        this.fbMoney = item.enable;
                    }
                });
            },
            // 获取全仓和逐仓的资金
            getQCZCMoney() {
                this.$ajax("/account/getMyInfos", {
                    token: this.$userID()
                }).then(res => {
                    if (res.data.status) {
                        if (this.$route.name === "Login") {
                            this.$router.push("/");
                        }
                        this.qcMoney = res.data.data.usdt;
                        this.zcMoney = res.data.data.zcUsdt;
                    } else {
                        this.$router.push("/login");
                    }
                });
            },
            // 获取全仓活逐仓划转记录
            getRecord() {
                if (this.tabIndex === 0) {
                    this.getQcRecord();
                } else {
                    this.getZcRecord();
                }
            },

            // 获取全仓划转记录
            async getQcRecord() {
                const params = {
                    page: this.page,
                    pagesize: this.pageSize
                };
                var res = await getQcRecordAjax(params);
                this.total = res.data.total;
                this.list = res.data.rows;
            },

            // 获取逐仓划转记录
            async getZcRecord() {
                const params = {
                    page: this.page,
                    pagesize: this.pageSize
                };
                var res = await getZcRecordAjax(params);
                this.total = res.data.total;
                this.list = res.data.rows;
            },
            prePage() {
                this.page = this.page - 1;
                this.getRecord();
            },
            nextPage() {
                this.page = this.page + 1;
                this.getRecord();
            }
        }
    };
</script>

<style scoped>
    .container {
        width: 1200px;
        display: flex;
        flex-direction: column;
    }

    .title {
        height: 50px;
        width: 1140px;
        border-bottom: 1px solid #e1e8ed;
        background-color: white;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        padding: 0 30px;
    }

    .title .left {
        display: flex;
        align-items: center;
    }

    .tab {
        padding: 0 20px;
        height: 47px;
        text-align: center;
        line-height: 47px;
        color: #1c242c;
        cursor: pointer;
        border-bottom: 3px solid transparent;
    }

    .title .right {
        display: flex;
        height: 50px;
    }

    .title .right p {
        line-height: 50px;
        font-size: 14px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #196bdf;
        cursor: pointer;
    }

    .title .right p a:hover {
        opacity: 0.7;
    }

    .select {
        border-bottom: 3px solid #196bdf;
        color: #196bdf;
    }

    .content {
        height: 180px;
        width: 1140px;
        background-color: #fff;
        padding: 0 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .content .left p {
        font-size: 16px;
        color: #9aa5b5;
        margin-bottom: 20px;
    }

    .content .left .main {
        font-size: 20px;
        color: #1c242c;
    }

    .content .left .sub {
        font-size: 12px;
        color: #9199a4;
        margin-left: 12px;
    }

    .content .right {
        display: flex;
    }

    .content .right .button {
        width: 120px;
        height: 36px;
        border-radius: 2px;
        border: solid 1px #196bdf;
        text-align: center;
        line-height: 36px;
        color: #196bdf;
        font-size: 14px;
        margin-left: 20px;
        cursor: pointer;
    }

    .content .right .button:hover {
        background-color: #196bdf;
        color: #fff;
    }

    .right p a {
        color: #196bdf;
        cursor: pointer;
    }
</style>
