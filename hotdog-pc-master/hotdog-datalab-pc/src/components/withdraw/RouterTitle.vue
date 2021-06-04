<template>
    <div class="title-box">
        <router-link :to="{name:'wallet'}">我的钱包</router-link>
        <span>&nbsp;>&nbsp;{{ desc }}</span>
    </div>
</template>
<script>
export default {
    props:['desc']
}
</script>
<style lang="scss" scoped>
@import '~/css/withDraw/routerTitle.scss'
</style>