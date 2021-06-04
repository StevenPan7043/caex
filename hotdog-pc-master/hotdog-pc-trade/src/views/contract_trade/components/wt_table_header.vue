<template>
    <div class="table_header_container"  >
        <div class="first">{{$t('contract.OrderNo')}}</div>
        <div>{{$t('contract.Coin')}}</div>
        <div>{{$t('contract.Type')}}</div>
        <div><p>{{$t('contract.EntrustedPrice')}}(USDT)</p></div>
        <div><p>{{$t('contract.EntrustedNum')}}({{symbol.split('/')[0]}})</p></div>
        <div><p>{{$t('contract.LeverageMultiple')}}</p></div>
        <div>{{$t('contract.time')}} </div>
        <div class="last">{{$t('contract.Operation')}}</div>
    </div>
</template>
<script>
    export default {
        name:'table_header',
        props:[
            'symbol'
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
        height: 40px;
        line-height: 16px;
        background-color: #fff;
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
