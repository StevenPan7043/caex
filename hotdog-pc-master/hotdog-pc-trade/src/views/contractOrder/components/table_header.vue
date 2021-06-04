<template>
    <div>
        <div class="table_header_container" v-if="type===0">
            <div class="first">{{$t('contract.Contract')}}</div>
            <div>{{$t('contract.Model')}}</div>
            <div><p>{{$t('contract.PositionPrice')}}</p>
                <p>(USDT)</p></div>
            <div>{{$t('contract.Holdings')}}</div>
            <div><p>{{$t('contract.Margin')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{tabIndex===0?$t('contract.PresentPrice'):$t('contract.SettlementPrice')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.Floating')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.CheckPrice')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.StopPrice')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.LeverageMultiple')}}</p>
                </div>
            <div>{{$t('contract.time')}}</div>
            <div class="last">{{tabIndex===0?$t('contract.Operation'):$t('contract.Status')}}</div>
        </div>

        <div class="table_header_container" v-else>
            <div class="first">{{$t('contract.Contract')}}</div>
            <div>{{$t('contract.Model')}}</div>
            <div><p>{{$t('contract.PositionPrice')}}</p>
                <p>(USDT)</p></div>
            <div><p>{$t('contract.Margin')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{tabIndex===0?$t('contract.PresentPrice'):$t('contract.SettlementPrice')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.Floating')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.CheckPrice')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.StopPrice')}}</p>
                <p>(USDT)</p></div>
            <div>{{$t('contract.time')}}</div>
            <div class="last">{{tabIndex===0?$t('contract.Operation'):$t('contract.Status')}}</div>
        </div>
    </div>
</template>
<script>
    export default {
        name: 'table_header',
        props: [
            'tabIndex', // 0： 持仓中 1：历史记录
            'type' // 0 全仓 1 逐仓
        ]
    }
</script>

<style scoped>

    .table_header_container {
        display: flex;
        color: #91a4b7;
        justify-content: space-between;
        padding: 0 30px;
        box-sizing: border-box;
        height: 60px;
        line-height: 16px;
        background-color: #f5f8fa;
        font-size: 12px;
    }

    .table_header_container div {
        display: flex;
        flex-direction: column;
        justify-content: center;
        flex: 1;
        align-items: center;
    }

    .table_header_container div.first {
        align-items: start;
    }

    .table_header_container div.last {
        align-items: flex-end;
    }
</style>
