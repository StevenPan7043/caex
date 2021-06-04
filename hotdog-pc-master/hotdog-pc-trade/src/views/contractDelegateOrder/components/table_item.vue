<template>
    <div class="list_item_container_item">
        <div class="first">
            {{item.ordercode}}
        </div>

        <div>
           {{item.coin.toUpperCase().replace('_','/')}}
        </div>
        <div :class="item.type===1?'green':'red'">{{item.type===1?$t('contract.OpenMore'):$t('contract.OpenEmpty')}}</div>
        <div>{{item.price}}</div>
        <div>{{item.coinnum}}</div>
        <div>{{item.gearing}}</div>
        <div>{{item.createtimeStr}}</div>
        <div class="last">
            <slot/>
        </div>

    </div>

</template>
<script>

    export default {
        props: [
            'item',
        ],
    }
</script>
<style scoped>
    .list_item_container_item {
        height: 80px;
        width: 1200px;
        background-color: #fff;
        display: flex;
        align-items: center;
        padding: 0 30px;
        box-sizing: border-box;
        color: #1c242c;
        font-size: 12px;
        line-height: 20px;
    }

    .list_item_container_item:hover {
        background-color: #f5f9fc;
    }

    .list_item_container_item div {
        display: flex;
        flex-direction: column;
        justify-content: center;
        flex: 1;
        align-items: center;
    }

    .list_item_container_item div.first {
        align-items: start;
        display: flex;
        flex-direction: column;
    }

    .list_item_container_item div.last {
        align-items: flex-end;
    }

    .green{
        color: #29d190;
    }
    .red{
        color: #da3f4d;
    }

</style>
