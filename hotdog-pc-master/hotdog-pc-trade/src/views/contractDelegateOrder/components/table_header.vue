<template>
    <div>

        <div class="table_header_container"  >
            <div class="first">{{$t('contract.OrderNo')}}</div>
            <div>{{$t('contract.Coin')}}</div>
            <div>{{$t('contract.Type')}}</div>
            <div><p>{{$t('contract.EntrustedPrice')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.EntrustedNum')}}</p>
                <p>(USDT)</p></div>
            <div><p>{{$t('contract.Leverage')}}</p>
                </div>
            <div>{{$t('contract.Time')}} </div>
            <div class="last">{{tabIndex===0?$t('contract.Operation'):$t('contract.Status')}}</div>
        </div>
    </div>
</template>
<script>
    export default {
        name:'table_header',
        props:[
            'tabIndex', // 0： 持仓中 1：历史记录
        ]
    }
</script>

<style scoped>

    .table_header_container{
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
    .table_header_container div{
        display: flex;
        flex-direction: column;
        justify-content: center;
        flex: 1;
        align-items: center;
    }
    .table_header_container div.first{
        align-items: start;
    }
    .table_header_container div.last{
        align-items: flex-end;
    }
</style>
