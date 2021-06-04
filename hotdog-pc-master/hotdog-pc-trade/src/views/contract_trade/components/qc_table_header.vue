<template>
    <div class="table_header_container" >
        <div class="first">{{$t('contract.Contract')}}</div>
        <div>{{$t('contract.Model')}}</div>
        <div><p>{{$t('contract.PositionPrice')}}(USDT)</p></div>
        <div><p>{{$t('contract.Holdings')}}({{symbol.split('/')[0]}})</p></div>
        <div><p>{{$t('contract.Margin')}}(USDT)</p></div>
        <div><p>{{$t('contract.PresentPrice')}}(USDT)</p></div>
        <div><p>{{$t('contract.Floating')}}(USDT)</p></div>
        <div><p>{{$t('contract.CheckPrice')}}(USDT)</p></div>
        <div><p>{{$t('contract.StopPrice')}}(USDT)</p></div>
        <div><p>{{$t('contract.LeverageMultiple')}}</p></div>
        <div>{{$t('contract.time')}} </div>
        <div class="last">{{$t('contract.Operation')}}</div>
    </div>
</template>

<script>
    export default {
        name: "qc_table_header",
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
        flex: 2;
    }
</style>
