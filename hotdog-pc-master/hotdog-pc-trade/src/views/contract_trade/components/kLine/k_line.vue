<template>
    <div class="chart-box">
        <div class="title">
            <div class="symbol_name">{{symbol}}{{$t('contract.Sustainable')}}</div>
            <div>
                <p class="price" :class="tableData.isout===0?'red':'green'">{{klineDetail.close}}</p>
                <p class="equal_price">≈ {{coinCny}}CNY</p>
            </div>
            
            <div>
                <p class="label">{{$t('contract.Applies')}}</p>
                <p :class="tableData.isout===0?'red':'green'">
                    {{tableData.isout==0?'-':'+'}}{{tableData.scale!=undefined?(tableData.scale*100).toFixed(2):0}}%</p>
            </div>
            <!-- <div>
                <p class="label">{{$t('contract.Applies')}}</p>
                <p :class="(klineDetail.open - klineDetail.close )<=0?'red':'green'">
                    {{(((klineDetail.close - klineDetail.open)/klineDetail.open)*100).toFixed(2) + "%"}}</p>
            </div> -->
            <div>
                <p class="label">{{$t('contract.Highest')}}</p>
                <p class="item">{{klineDetail.high}}</p>
            </div>
            <div>
                <p class="label">{{$t('contract.Lowest')}}</p>
                <p class="item">{{klineDetail.low}}</p>
            </div>
            <div>
                <p class="label">24H{{$t('contract.Amount')}}</p>
                <p class="item">{{klineDetail.amount!=undefined?klineDetail.amount.toFixed(2):0}}</p>
            </div>
        </div>

      <TvChartContainer
              :symbol="symbol.toLowerCase()"
              style="height: 420px;width: 100%"
              :tableData="tableData"
              :klineDetail="klineDetail"
              :klineList="klineList"
              @func="tiem"
      />
<!--        <div class="alert_container">-->
<!--            <el-alert-->
<!--                    title="合约交易是高风险高收益的投资行为，请仔细阅读交易规则，注意风险。"-->
<!--                    type="warning"-->
<!--                    center-->
<!--                    show-icon>-->
<!--            </el-alert>-->
<!--        </div>-->
    </div>
</template>

<script>
import TvChartContainer from './TvChartContainer'
    export default {
        name: "k_line",
        props: [
            "tableData",
            "symbol",
            "klineDetail",
            "klineList",
            "coinCny"
        ],
    components:{
          TvChartContainer
    },
    methods: {
        tiem(data){
            let that = this
            that.$emit('times',data)
        }
    },
    }
</script>

<style scoped>
.chart-box{
    position: relative;
}
    .title {
        width: 100%;
        height: 45px;
        background-color: #f2f6fa;
        display: flex;
        align-items: center;
        padding: 0 13px;
        box-sizing: border-box;
    }

    .title div {
        margin-right: 15px;
    }

    .title div p {
        margin: 3px 0;
    }

    .symbol_name {
        font-size: 16px;
        color: #25425a;

    }

    .price {
        font-size: 16px;

        color: #da3f4d;
    }

    .equal_price {
        font-size: 12px;
        color: #9eb0c3;
    }

    .label {
        font-size: 12px;
        color: #91a4b7;
    }

    .item {
        font-size: 12px;
        color: #445666;
    }

    .red {
        font-size: 12px;
        color: #da3f4d;
    }

    .green {
        font-size: 12px;
        color: #5bbe8e;
    }

    .alert_container {
        position: absolute;
        bottom: 10px;
        left: 0;
        width: 100%;
    }
</style>



