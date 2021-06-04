<template>
    <div class="main_container">
        <div class="title">
            <p>{{$t('contract.Dish')}}</p>
            <div class="UpDown">
                <p :class="type===0?'select':''" @click="changeType(0)">
                    <img src="../../../../assets/images/mixd1.png"/>
                </p>
                <p :class="type===1?'select':''" @click="changeType(1)">
                    <img src="../../../../assets/images/down1.png"/>
                </p>
                <p :class="type===2?'select':''" @click="changeType(2)">
                    <img src="../../../../assets/images/up1.png"/>
                </p>
            </div>
        </div>
        <div class="body">
            <div class="table_header">
                <p class="first">{{$t('contract.Price')}}(USDT)</p>
                <p class="middle">{{$t('contract.Amount2')}}({{symbol.split('/')[0]}})</p>
                <p class="end">{{$t('contract.Total')}}({{symbol.split('/')[0]}})</p>
            </div>


            <div class="table_body">
                <div :style="{height:redHeight+'px', transition: '0.5s', position: 'relative'}">
                    <div class="green_container">
                        <div class="table_item_container"
                             v-for="(item,index) in asksList" :key="index">
                            <div class="table_item">
                                <p class="red">{{item[0].toFixed(6)}}</p>
                                <p class="middle">{{item[1].toFixed(6)}}</p>
                                <p class="end">{{item[3].toFixed(6)}}</p>
                            </div>
                            <div class="bg_red" :style="{ width: item[2]*100+'%'}"></div>
                        </div>
                    </div>

                </div>
                <div class="current_price">
                    <p class="red">{{coinPrice}}</p>
                    <div>≈ {{coinCny}} CNY</div>
                </div>
                <div :style="{height:greenHeight+'px',overflow: 'hidden',transition: '0.5s'}">

                    <div class="table_item_container" v-for="(item,index) in bidsList" :key="index">
                        <div class="table_item">
                            <p class="green">{{item[0].toFixed(6)}}</p>
                            <p class="middle">{{item[1].toFixed(6)}}</p>
                            <p class="end">{{item[3].toFixed(6)}}</p>
                            <div></div>
                        </div>
                        <div class="bg_green" :style="{ width: item[2]*100+'%'}"></div>
                    </div>
                </div>
            </div>


        </div>
    </div>
</template>

<script>
    export default {
        name: "index",
        props: [
            "asks",
            "bids",
            "coinPrice",
            "coinCny",
            "symbol"
        ],
        data() {
            return {
                type: 0,//0--both 1--green 2--red
                redHeight: 360,
                greenHeight: 360,
                asksList:[],
                bidsList:[],
            }
        },
        watch: {
            asks(){
                let asks = this.asks.slice(0,this.asks.length);
                this.asksList = this.calc(asks,true).reverse();
            },
            bids(){
                this.bidsList = this.calc(this.bids,false);
            }
        },
        methods: {
            changeType(type) {
                this.type = type;
                if (this.type === 0) {
                    this.redHeight = 360;
                    this.greenHeight = 360;
                } else if (this.type === 1) {
                    this.redHeight = 0;
                    this.greenHeight = 720;
                } else {
                    this.redHeight = 720;
                    this.greenHeight = 0;
                }
            },
            // 计算asks bids
            calc(list,isReverse){
                let cacheList= [];
                // if(isReverse){
                //     list.reverse();
                // }
                list.forEach((item)=>{
                    cacheList.push(item[1])
                });
                let sum = 0;
                let maxNum = Math.max.apply(null,cacheList);
                list.forEach((item)=>{
                    item[2]=item[1]/maxNum;
                    sum+=item[1];
                    item[3]=sum;
                });
                return list;
            }
        }
    }
</script>

<style scoped>
    .main_container {
        width: 100%;
        height: 100%;
    }

    .title {
        height: 45px;
        background-color: #f2f6fa;
        display: flex;
        justify-content: space-between;
        padding: 0 20px;
        align-items: center;
    }

    .title p {
        font-size: 14px;
        color: #25425a;
    }

    .body {
        height: 791px;
        background-color: #fff;
        overflow: hidden;

    }

    .UpDown {
        display: flex;
    }

    .UpDown p {
        width: 30px;
        height: 27px;
        background-color: #e9eff4;
        border-radius: 2px;
        border: solid 1px #d5e1ee;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-left: 10px;
        cursor: pointer;
    }

    .UpDown img {
        width: 24px;
        height: 17px;
    }

    .UpDown p.select {
        border: solid 1px #196bdf;
    }

    .table_header {
        padding: 0 20px;
        box-sizing: border-box;
        height: 32px;
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 12px;
        color: #91a4b7;
    }

    .table_header p {
        flex: 1;
    }

    .table_body {
        height: 759px;
        overflow: hidden;
    }

    .table_item {
        width: 100%;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 20px;
        box-sizing: border-box;
        font-size: 12px;
        color: #25425a;
    }

    .table_item p {
        flex: 1;
    }

    .table_item_container {
        position: relative;

    }

    .bg_red {
        position: absolute;
        height: 20px;
        top: 0px;
        right: 0;
        z-index: 1;
        opacity: .16;
        background: #bc4d4d;
        transition: width .6s ease;
    }

    .bg_green {
        position: absolute;
        height: 20px;
        top: 0px;
        right: 0;
        z-index: 1;
        opacity: .16;
        background: #027859;
        transition: width .6s ease;
    }


    .first {
        text-align: start;
    }

    .middle {
        text-align: center;
    }

    .end {
        text-align: end;
    }

    .green {
        color: #5bbe8e;
    }

    .red {
        color: #ee6560;
    }

    .table_item:hover {
        background-color: #f2f6fa;
    }

    .current_price {
        height: 38px;
        border-top: 1px solid #e8eef5;
        border-bottom: 1px solid #e8eef5;
        width: 100%;
        display: flex;
        align-items: center;
        padding: 0 20px;
        box-sizing: border-box;
    }

    .current_price p {
        font-size: 16px;
    }

    .current_price div {
        font-size: 13px;
        color: #718bab;
    }

    .green_container {
        overflow: hidden;
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
    }


</style>
