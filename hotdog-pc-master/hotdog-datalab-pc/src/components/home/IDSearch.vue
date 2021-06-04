<template>
    <div class="input-box">
        <label for="search">ID搜索</label>
        <input type="text" id="search" @input="hangleInput">
    </div>
</template>
<script>
export default {
    methods: {
        hangleInput(e) {
            this.$emit('input',e.target.value)
        }
    }
}
</script>
<style lang="scss" scoped>
@import "~/css/home/idSearch.scss"
</style>