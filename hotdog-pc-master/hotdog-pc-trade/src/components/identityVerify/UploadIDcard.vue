<template>
    <div class="idcard-wrap">
        <ul class="tips">
            <li class="item">· 以下材料需用 JPG / JPEG / PNG 格式上传，并保证照片清晰、边角完整、亮度均匀，且大小不得超过5M；</li>
            <li class="item">· 提交本申请代表承诺你的身份信息真实可靠，不存在盗用他人信息资料的情况；</li>
            <li class="item">· 您的提交申请我们将在一个工作日内进行审核处理，并告知审核处理结果。</li>
        </ul>
        <div class="pics-sub">
            <div class="id-card" v-if="step">
                <div class="row">
                    <div class="title">本人身份证正面照片</div>
                    <div class="upload_container">
                        <div class="row_one">
                            <div class="text_container"></div>
                            <div class="img_container">
                                <img :src="frontImgUrl" alt />
                                <input type="file" class="file-upload" ref="idFrontImg" @change="frontImgChange" />
                            </div>
                            <img class="img_container" src="../../assets/images/upload1.1.png" alt />
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="title">本人身份证背面照片</div>
                    <div class="upload_container">
                        <div class="row_one">
                            <div class="text_container"></div>
                            <div class="img_container">
                                <img :src="backImgUrl" alt />
                                <input type="file" class="file-upload" ref="idBackImg" @change="backImgChange" />
                            </div>
                            <img class="img_container" src="../../assets/images/upload2.1.png" alt  />
                        </div>
                    </div>
                </div>
                <div class="btn">
                    <button>下一步</button>
                </div>
            </div>
            <div class="card-hand" v-else>
                <div class="row">
                    <div class="title">本人身份证背面照片</div>
                    <div class="upload_container">
                        <div class="row_one">
                            <div class="text_container"></div>
                            <div class="img_container">
                                <img :src="handholdImgUrl" alt />
                                <input
                                        type="file"
                                        class="file-upload"
                                        ref="idHandheldImg"
                                        @change="handholdImgChange"
                                />
                            </div>
                            <img class="img_container" src="../../assets/images/upload3.1.png" alt />

                        </div>
                    </div>
                </div>
                <div class="btn" >
                    <button>下一步</button>
                </div>
            </div>
            <!--************************-->

        </div>
    </div>
</template>
<script>
    import { identityAuto } from "@/api/identityVerify.js";
    export default {
        data() {
            return {
                step: true,
                frontImgUrl:require('../../assets/images/add.png'),
                backImgUrl:require('../../assets/images/add.png'),
                handholdImgUrl:require('../../assets/images/add.png'),
            };
        },
        methods: {
            frontImgChange(ele) {
                console.log(ele.target.flies[0])
                this.imageUrl = URL.createObjectURL(ele.target.flies[0])
                console.log(this.imageUrl)
            }
            //   handleAvatarSuccess(res, file) {
            //     this.imageUrl = URL.createObjectURL(file.raw);
            //   },
            //   beforeAvatarUpload(file) {
            //     const isJPG = file.type === "image/jpeg";
            //     const isLt2M = file.size / 1024 / 1024 < 2;

            //     if (!isJPG) {
            //       this.$message.error("上传头像图片只能是 JPG 格式!");
            //     }
            //     if (!isLt2M) {
            //       this.$message.error("上传头像图片大小不能超过 2MB!");
            //     }
            //     return isJPG && isLt2M;
            //   }
        }
    }

</script>
<style lang="scss" scoped>
    .idcard-wrap {
        height: 783px;
        width: 890px;
        //   border: 1px solid black;
        transform: translateX(-126px);
        .tips {
            .item {
                font-size: 14px;
                color: #999999;
                margin-bottom: 10px;
            }
        }
        .pics-sub {
            margin-top: 78px;
            margin-left: 250px;
            width: 638px;
            height: 426px;
            // border: 1px solid black;
            .id-card {
                .row {
                    .title {
                        font-size: 14px;
                        color: #999999;
                        margin-bottom: 17px;
                        margin-left: 12px;
                    }
                    .upload_container {
                        width: 302px;

                        .row_one {
                            display: flex;
                            flex-direction: row;
                            justify-content: space-between;
                            margin-bottom: 40px;
                        }
                        .img_container {
                            margin-right: 79px;
                            width: 200px;
                            height: 126px;
                            position: relative;
                            img {
                                width: 200px;
                                height: 126px;
                            }

                            .file-upload {
                                position: absolute;
                                right: 0;
                                top: 0;
                                opacity: 0;
                                width: 200px;
                                height: 126px;
                                cursor: pointer;
                            }
                        }
                        .text_container {
                            margin-top: 12px;
                            width: 220px;
                            p {
                                font-size: 12px;
                                font-weight: normal;
                                letter-spacing: 0px;
                                color: #6c859a;
                                line-height: 24px;
                            }
                        }
                    }
                }
                .btn {
                    margin-top: 134px;
                    button {
                        width: 464px;
                        height: 48px;
                        background-color: #718ca3;
                        color: #fff;
                    }
                }
            }
            .card-hand {
                .row {
                    .title {
                        font-size: 14px;
                        color: #999999;
                        margin-bottom: 17px;
                        margin-left: 12px;
                    }
                    .upload_container {
                        width: 302px;

                        .row_one {
                            display: flex;
                            flex-direction: row;
                            justify-content: space-between;
                            margin-bottom: 40px;
                        }
                        .img_container {
                            margin-right: 79px;
                            width: 200px;
                            height: 126px;
                            position: relative;
                            img {
                                width: 200px;
                                height: 126px;
                            }

                            .file-upload {
                                position: absolute;
                                right: 0;
                                top: 0;
                                opacity: 0;
                                width: 200px;
                                height: 126px;
                                cursor: pointer;
                            }
                        }
                        .text_container {
                            margin-top: 12px;
                            width: 220px;
                            p {
                                font-size: 12px;
                                font-weight: normal;
                                letter-spacing: 0px;
                                color: #6c859a;
                                line-height: 24px;
                            }
                        }
                    }
                }
                .btn {
                    margin-top: 434px;
                    button {
                        width: 464px;
                        height: 48px;
                        background-color: #718ca3;
                        color: #fff;
                    }
                }
            }
        }
    }
</style>
