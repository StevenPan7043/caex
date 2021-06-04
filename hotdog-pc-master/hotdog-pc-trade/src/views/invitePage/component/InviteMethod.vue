<template>
    <div class="my_way">
        <div class="title">
            <p>我的分享方式</p>
        </div>
        <div class="method">通过专属链接或者邀请码邀请好友，好友交易时您将获得返佣，微信邀请好友时候请告知好友，在浏览器打开连接</div>
        <div class="code_wrapper">
            <div class="invite_code">
                <p>专属邀请码</p>
                <div class="box">
                    <div class="code">{{userInfo.invite_code}}</div>
                    <div class="btn" v-clipboard:copy="userInfo.invite_code" v-clipboard:success="onCopy" v-clipboard:error="onError">复制邀请码</div>
                </div>
            </div>
            <div class="invite_url">
                <p>专属邀请链接</p>
                <div class="box">
                    <div class="code">{{inviteUrl}}</div>
                    <div class="btn" v-clipboard:copy="inviteUrl" v-clipboard:success="onCopy" v-clipboard:error="onError">复制链接分享</div>
                </div>
            </div>
        </div>
    </div>  
</template>
<script>
export default {
    props:{
        userInfo:{
            type:Object,
            // eslint-disable-next-line vue/require-valid-default-prop
            default:{}
        },
        inviteUrl:{
            type:String,
            default:''
        }
    },
    methods: {
    onCopy () {
      this.$message({
        type: 'success',
        message: this.$t('header.CopySuccess'),
        duration: 3000,
        showClose: true
      })
    },
    onError () {
      this.$message({
        type: 'error',
        message: this.$t('header.CopyFail'),
        duration: 3000,
        showClose: true
      })
    }
    },
}
</script>
<style scoped>
.my_way{
    width: 1200px;
    height: 300px;
    background-color: #fff;
    margin-bottom: 20px;
    color: #25425a;
}
.title{
    height: 60px;
    width: 100%;
    box-shadow: 0px 2px 6px 0px 
    rgba(0, 0, 0, 0.1);
}
.title p{
    margin-left: 30px;
    font-size: 18px;
    line-height: 60px;
}
.method{
    margin-left: 30px;
    margin-top: 20px;
}
.code_wrapper{
    margin: 60px 60px 0 60px;
    display: flex;
    justify-content: space-between
}
.invite_code{
    width: 300px;
    height: 73px;
    display: flex;
    flex-direction: column;
    justify-content: space-between
}
.invite_code p{
	font-size: 14px;
	color: #25425a;  
}
.invite_code .box{
    width: 300px;
	height: 50px;
	background-color: #f6f8fa;
	border-radius: 3px;
    border: solid 1px #cad7e0;
    display: flex;
    align-items: center;
}
.invite_code .box .code{
    width: 150px;
    text-align: center;
    font-size: 16px;
	color: #25425a;
}
.invite_code .box .btn{
    width: 149px;
    height: 30px;
    border-left: 1px solid #cad7e0;
    text-align: center;
    line-height: 30px;
	font-size: 16px;
	color: #0c72ea;
    cursor: pointer;
}
.invite_url{
	width: 700px;
    height: 73px;
    display: flex;
    flex-direction: column;
    justify-content: space-between
}
.invite_url p{
	font-size: 14px;
	color: #25425a;  
}
.invite_url .box{
    width: 700px;
	height: 50px;
	background-color: #f6f8fa;
	border-radius: 3px;
    border: solid 1px #cad7e0;
    display: flex;
    align-items: center;
}
.invite_url .box .code{
    width: 550px;
    text-align: center;
    font-size: 16px;
	color: #25425a;
}
.invite_url .box .btn{
    width: 149px;
    height: 30px;
    border-left: 1px solid #cad7e0;
    text-align: center;
    line-height: 30px;
	font-size: 16px;
	color: #0c72ea;
    cursor: pointer;
}
</style>
