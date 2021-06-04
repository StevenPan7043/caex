<template>
    <div class="up-and-down f-fr">
        <span class="up" :class="upDownSelect===0?'hideheight':'showshell'" @click="upDownTabClick(0)"></span>
        <span class="down" :class="upDownSelect===1?'hideheight':'showshell'" @click="upDownTabClick(1)"></span>
        <span class="mixed" :class="upDownSelect===2?'hideheight':'showshell'" @click="upDownTabClick(2)"></span>
    </div>
</template>

<script>
    export default {
        data(){
            return{
                upDownSelect:2,
                // depthBuyStyle: '',
                // depthSellStyle: '',
            }
        },
        watch:{
            hashsymbol(){
                this.upDownTabClick(2)
            }
        },
        methods:{
            upDownTabClick (index) {
                this.upDownSelect = index
                switch (index) {
                    case 0:
                        this.depthBuyStyle = 'showshell'
                        this.depthSellStyle = 'hideheight'
                        break
                    case 1:
                        this.depthBuyStyle = 'hideheight'
                        this.depthSellStyle = 'showshell'
                        break
                    case 2:
                        this.depthBuyStyle = ''
                        this.depthSellStyle = ''
                        break
                    default:
                        break
                }
                this.$emit('appenddata', [this.depthBuyStyle,this.depthSellStyle]);
            },
        }
    }
</script>

<style scoped>

</style>
