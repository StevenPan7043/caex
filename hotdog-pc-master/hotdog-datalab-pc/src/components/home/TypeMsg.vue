<template>
    <div class="type-msg" :style="{backgroundImage:msg.domStyle}">
        <div class="msg">
            <p>{{ msg.type }}</p>
            <div class="msg-box">
                <span class="content">{{ msg.num }}</span>
                <span class="uint">&nbsp;{{ msg .unit }}</span>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    props:['msg']
}
</script>
<style lang="scss" scoped>
@import '~/css/home/typeMsg.scss'
</style>