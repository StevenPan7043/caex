<template>
    <div class="list_item_container">
        <div class="first">
            <Tag :isKaiDuo="item.type===1"/>
            <p>{{item.coinname}}</p></div>
        <div>
            全仓
        </div>
        <div>{{item.buyprice}}</div>
        <div>{{item.coinnum}}</div>
        <div>{{item.realmoney}}</div>
        <div>{{item.stopprice}}</div>
        <div>{{item.rates}}</div>
        <div>{{item.stopwin||'--'}}</div>
        <div>{{item.stopdonat||'--'}}</div>
        <div>{{item.gearing}}</div>
        <div>{{item.timestr}}</div>
        <slot/>

    </div>
</template>
<script>
    import Tag from '../../contractOrder/components/tag'
    export default {

        props: [
            'item',
        ],
        components:{
            Tag
        }
    }
</script>
<style scoped>
    .list_item_container {
        height: 40px;
        width: 100%;
        background-color: #fff;
        display: flex;
        align-items: center;
        padding: 0 30px;
        box-sizing: border-box;
        color: #1c242c;
        font-size: 12px;
        line-height: 20px;
    }

    .list_item_container:hover {
        background-color: #f5f9fc;
    }

    .list_item_container div {
        display: flex;
        flex-direction: column;
        justify-content: center;
        flex: 1;
        align-items: center;
    }

    .list_item_container div.first {
        align-items: start;
        display: flex;
        flex-direction: row;
    }

    .list_item_container div.last {
        flex-direction: row;
        flex: 2;

    }

    .button_container {
        width: 70px;
        background-color: #196bdf;
        border-radius: 2px;
        height: 28px;
        color: #fff;
        font-size: 12px;
        line-height: 28px;
        cursor: pointer;
        margin-left: 10px;
    }
</style>
