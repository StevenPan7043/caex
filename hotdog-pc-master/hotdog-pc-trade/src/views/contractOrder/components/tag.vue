<template>
    <div>
        <div class="tag_container" :class="isKaiDuo?'green':'red'">
            {{isKaiDuo?$t('contract.OpenMore'):$t('contract.OpenEmpty')}}
        </div>
    </div>
</template>

<script>
    export default {
        name:'Tag',
        props:[
            'isKaiDuo'
        ]
    }
</script>

<style scoped>
    .tag_container{
        width: 40px;
        height: 20px;
        color: #fff;
        line-height: 20px;
        text-align: center;
        margin-right: 10px;
    }
    .green{
        background-color: #5bbe8e;
    }
    .red{
        background-color: #ee6560;
    }
</style>
