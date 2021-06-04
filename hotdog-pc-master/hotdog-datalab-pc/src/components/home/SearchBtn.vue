<template>
    <button class="btn" @click="handleClick">搜索</button>
</template>
<script>
export default {
    methods: {
        handleClick() {
            this.$emit('searchHandle')
        }
    }
}
</script>
<style lang="scss" scoped>
@import '~/css/home/searchBtn.scss'
</style>