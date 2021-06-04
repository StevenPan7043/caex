<template>
    <div class="invite_summary">
        <div class="left">
            <p>邀请人数</p>
            <div>
                <span class="label">邀请总人数</span>
                <span class="number">{{returnMoney.introNum}}</span>
            </div>
        </div>
        <div class="right">
            <p>累计获得返佣</p>
            <div>
                <span >{{returnMoney.amount.toFixed(6)}}</span>
                <span >  USDT</span>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    props:{
        returnMoney:{
            type:Object,
            // eslint-disable-next-line vue/require-valid-default-prop
            default:{
                introNum:0
            }
        }
    }
}
</script>
<style scoped>
.invite_summary{
    width: 1200px;
    height: 160px;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between
}
.left{
    width: 590px;
	height: 160px;
    background-color: #ffffff;
    box-sizing: border-box;
    padding: 48px 60px;
    display: flex;
    flex-direction: column;
    justify-content: space-between

}
.left p{
	font-size: 16px;
	color: #25425a;
}
.left .label{
    font-size: 14px;
	color: #25425a;
}
.left .number{
	font-size: 24px;
    color: #25425a;
    margin-left: 30px;
}
.right{
    width: 590px;
	height: 160px;
    background-color: #ffffff;
    box-sizing: border-box;
    padding: 48px 60px;
    display: flex;
    flex-direction: column;
    justify-content: space-between
}
.right p{
	font-size: 16px;
	color: #25425a;
}
.right span{
    font-size: 24px;
	color: #25425a;
}
</style>
