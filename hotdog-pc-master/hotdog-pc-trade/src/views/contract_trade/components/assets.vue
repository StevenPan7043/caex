<template>
    <div class="main_container">
        <div class="title">
            <p>{{$t('contract.ContractAccount')}}</p>
            <a @click.prevent="openDialog">{{$t('contract.FundsTransfer')}}</a>
        </div>
        <div class="body">
            <div class="money_container">
                <div class="qc_container">
                    <div class="flex">
                        <p>{{$t('contract.Full')}}</p>
                        <i class="iconfont" :class="showAssets?'icon-eye-slash':'icon-eye'" @click="changeShow"></i>
                    </div>
                    <p v-if="showAssets">{{assetsInfo.usdt}} USDT ≈ {{assetsInfo.usdtCny}} CNY</p>
                    <p v-else>*********</p>
                </div>
                <div class="zc_container">
                    <div>
                        {{$t('contract.Warehouse')}}
                    </div>
                    <p v-if="showAssets">{{assetsInfo.zcUsdt}} USDT ≈ {{assetsInfo.zcUsdtCny}} CNY</p>
                    <p v-else>*********</p>
                </div>
            </div>
            <div class="divider"></div>
            <div class="info_container">
                <div class="info_item">
                    <p>{{$t('contract.Margin')}}</p>
                    <div>{{moneyMap.totalmoney||'--'}} USDT</div>
                </div>
                <div class="info_item">
                    <p>{{$t('contract.FloatingProfit')}}</p>
                    <div>{{moneyMap.totalrates||'--'}} USDT</div>
                </div>
                <div class="info_item">
                    <p>{{$t('contract.AccountRights')}}</p>
                    <div>{{moneyMap.totalval||'--'}} USDT</div>
                </div>
                <div class="info_item">
                    <p>{{$t('contract.Risk')}}</p>
                    <div>{{moneyMap.scale||0}}%</div>
                </div>
            </div>
        </div>

        <DialogForm
                :dialogFormVisible="dialogFormVisible"
                :bbMoney="bbMoney"
                :fbMoney="fbMoney"
                :qcMoney="qcMoney"
                :zcMoney="zcMoney"
                @close-dialog="closeDialog"
                @reloadPage="reloadPage"
        />
    </div>
</template>

<script>
    import {getAssets} from "../../../api/contract";
    import DialogForm from "../../contractAssets/components/DialogForm";
    import Record from "../../contractAssets/components/Record";

    export default {
        name: "assets",
        props: [
            "moneyMap",
            "assetsInfo"
        ],
        components: {
            DialogForm,
        },
        mounted() {
            const show = localStorage.getItem('showAssets')
            this.showAssets = show;
        },
        data() {
            return {
                showAssets: false,
                dialogFormVisible: false,
                bbMoney: 0,
                fbMoney: 0,
                qcMoney: 0,
                zcMoney: 0,
            }
        },
        methods: {
            openDialog() {
                this.dialogFormVisible = true;
                this.getBBFbMoney();
                this.getQCZCMoney();
            },
            closeDialog() {
                this.dialogFormVisible = false;
            },
            changeShow() {
                this.showAssets = !this.showAssets;
                localStorage.setItem('showAssets', this.showAssets);
            },
            reloadPage() {
              this.$emit('reloadAssets')
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

        }
    }
</script>

<style scoped>
    .main_container {
        width: 100%;
        height: 100%;
    }

    .title {
        width: 354px;
        height: 45px;
        background-color: #f2f6fa;
        display: flex;
        padding: 0 20px;
        justify-content: space-between;
        box-sizing: border-box;
        align-items: center;
    }

    .title p {
        font-size: 14px;

        color: #25425a;
    }

    .title a {
        color: #196bdf;
        font-size: 12px;
    }

    .body {
        background-color: #fff;
        height: 320px;
        width: 100%;
    }

    .money_container {
        height: 160px;
        width: 100%;
        padding: 25px 20px;
        box-sizing: border-box;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    .qc_container {
        display: flex;
        flex-direction: column;
        height: 40px;
        justify-content: space-between;
    }

    .flex {
        display: flex;
        justify-content: space-between;
    }

    .qc_container .flex p {
        font-size: 12px;
        color: #91a4b7;
    }

    .qc_container p {
        font-size: 14px;
        color: #25425a;
    }

    .zc_container {
        display: flex;
        height: 40px;
        flex-direction: column;
        justify-content: space-between;
    }

    .zc_container div {
        font-size: 12px;
        color: #91a4b7;
    }

    .zc_container p {
        font-size: 14px;
        color: #25425a;
    }

    .divider {
        width: 354px;
        height: 1px;
        background-color: #e8eef5;
    }

    .info_container {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        height: 159px;
        padding: 20px 0;
        box-sizing: border-box;
    }

    .info_item {
        display: flex;
        padding: 0 20px;
        box-sizing: border-box;
        justify-content: space-between;
    }

    .info_item p {
        font-size: 12px;
        color: #91a4b7;
    }

    .info_item div {
        font-size: 12px;
        color: #25425a;
    }
</style>
