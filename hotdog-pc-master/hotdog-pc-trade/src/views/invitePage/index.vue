<template>
    <div>
        <my-header></my-header>
        <div id="MainView">
            <div class="banner_container">
            </div>
            <div class="content_container">
                <InviteMethod :userInfo="userInfo" :inviteUrl="inviteUrl"/>
                <div class="inviteBox f-cb">
                    <div class="inviteBoxPeo f-fl">
                        <p>{{$t('header.InviteNumber')}}</p>
                        <div>
                            <span>{{$t('header.InviteTotalNumber')}}</span>
                            <span>{{returnMoney.introNum}}</span>
                        </div>
                    </div>
                    <div class="inviteBoxRMB f-fr">
                        <p>{{$t('header.Accumulated')}}</p>
                        <div>
                            <span>{{returnMoney.amount}}</span>
                            <span>USDT</span>
                        </div>
                    </div>
                </div>
                <InviteSummary :returnMoney="returnMoney"/>
                <InviteRecord/>
                <InviteRules/>
            </div>
        </div>
        <my-footer></my-footer>
    </div>
</template>
<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import InviteMethod from './component/InviteMethod'
import InviteSummary from './component/InviteSummary'
import InviteRecord from './component/InviteRecord'
import InviteRules from './component/InviteRules'

import {getMember,getReutrncommi} from './api/index'
export default {
    components: {
        MyHeader,
        MyFooter,
        InviteMethod,
        InviteSummary,
        InviteRules,
        InviteRecord,
    },
    data() {
        return {
            userInfo:{},
            returnMoney:'',
            inviteUrl:''
        }
    },
    mounted(){
        this.getUserInfo();
        this.getReutrncommi();
    },
    methods:{
        getUserInfo(){
            getMember().then(res => {
                if (res.state === 1) {
                this.userInfo = res.data
                this.inviteUrl=`https://hotdogvip.com/#/register?invite_code=${res.data.invite_code}`
                this.sendCode()
                } else if (res.state === -1) {
                if (res.msg === 'LANG_NO_LOGIN') {
                    this.$message({
                    type: 'error',
                    message: this.$t('header.PlaseLogin'),
                    duration: 3000,
                    showClose: true
                    })
                    this.userLogout()
                }
                }
            })
        },
        getReutrncommi(){
            getReutrncommi().then(res=>{
                if(res.state===1){
                    this.returnMoney = res.data
                }
            })
        },

        userLogout () {
            localStorage.setItem('loginState', false)
            this.$router.push('/login')
        }
    }
}
</script>
<style scoped>
.banner_container{
    background: url('../../assets/images/invit_banner.png');
    width:100%;
    height: 440px;
    background-color: #f5f5f5;
}
#MainView{
    width:100%;
    min-height: 800px;
    display: flex;
    flex-direction: column;
    align-items: center;
}
.content_container{
    position: relative;
    top: -40px;
}
.inviteBox{
    width: 1200px;
    height: 160px;
    margin-bottom: 20px;
}
.inviteBox .inviteBoxPeo,.inviteBox .inviteBoxRMB{
    width: 590px;
    height: 100%;
    background: #ffffff;
    padding: 45px 60px;
    box-sizing: border-box;
    color: #495666;
}
.inviteBox .inviteBoxPeo p,
.inviteBox .inviteBoxRMB p{
    font-size: 16px;
    margin-bottom: 30px;
}
.inviteBox .inviteBoxPeo span,
.inviteBox .inviteBoxRMB span{
    font-size: 14px;
    margin-right: 20px;
}
.inviteBox .inviteBoxPeo span:last-child,
.inviteBox .inviteBoxRMB span:last-child{
    font-size: 24px;
}
</style>
