<template>
    <div class="success_container" >

        <div class="tip">
            <img src="../../assets/images/pass.png" />
            <p>{{$t('user.YourIdentity')}}</p>
        </div>
        <!-- <div class="item">
          <div class="first">{{$t('user.DocumentType')}}</div>
          <div class="second">{{idInfo.authIdentity.id_type|idTypeFilter}}</div>
        </div>
        <div class="item">
          <div class="first">{{$t('user.IDNumber')}}</div>
          <div
            class="second"
          >{{idInfo.authIdentity.id_number==null?"":idInfo.authIdentity.id_number.slice(0,2)}}*********{{idInfo.authIdentity.id_number==null?'':idInfo.authIdentity.id_number.slice(idInfo.authIdentity.id_number.length-2,idInfo.authIdentity.id_number.length)}}</div>
        </div>
        <div class="item">
          <div class="first">{{$t('user.FullName')}}</div>
          <div class="second">*{{idInfo.authIdentity.given_name}}</div>
        </div> -->
    </div>
</template>
<script>
    export default {

    }
</script>
<style lang="scss" scoped>
    .success_container {
        margin-top: 166px;
        .tip {
            display: flex;
            flex-direction: row;
            align-items: center;
            img {
                width: 26px;
                margin-right: 10px;
            }
            p {
                font-size: 18px;
                font-weight: normal;
                font-stretch: normal;
                letter-spacing: 0px;
                color: #25425a;
            }
        }
        .item {
            margin-top: 35px;
            display: flex;
            flex-direction: row;
            .first {
                width: 148px;
                padding-left: 42px;
                box-sizing: border-box;
                color: #6c859a;
            }
            .second {
                width: 140px;
                font-weight: normal;
                font-stretch: normal;
                letter-spacing: 0px;
                color: #25425a;
            }
        }
    }
</style>
