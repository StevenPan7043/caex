<template>
    <div>
        <my-header></my-header>
        <div class="trade-page">
            <div class="trade-box main">
                <div class="trade-nav">
                    <div class="nav-name">USDT合约</div>
                    <div class="nav-list">
                        <div class="swiper-container">
                            <div class="swiper-wrapper nav-wrapper">
                                <div
                                        class="swiper-slide"
                                        :class="curID == index?'cur':''"
                                        @click="coinChange(index)"
                                        v-for="(item,index) in array"
                                        :key="index"
                                >
                                    <span class="name" style="flex:1;text-align:center">{{item.name}}</span>
                                    <span
                                            class="ratio"
                                            :class="item.isout==0?'red11':'green11'"
                                    >{{item.isout==0?'-':'+'}}{{item.scale * 100 | tofixed2}}%</span>
                                </div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>
                    </div>
                </div>
                <div class="trade-top">
                    <div class="trade-top-left">
                        <div class="current-trade-card" v-if="tableData">
                            <div class="trade-name">{{tableData.name}}</div>
                            <div class="trade-money">
                                <div class="money">
                                    <span>{{tableData.usdtPrice}}</span>
                                    <span
                                            class="tag"
                                    >{{tableData.isout==0?'-':'+'}}{{tableData.scale * 100 | tofixed2}}%</span>
                                </div>
                                <div class="ratio">≈{{tableData.cny}}CNY</div>
                            </div>
                            <div class="info-row">
                                <div class="cell">
                                    <div class="title">最高价</div>
                                    <div class="value">{{tableData.high}}</div>
                                </div>
                                <div class="cell">
                                    <div class="title">最低价</div>
                                    <div class="value">{{tableData.low}}</div>
                                </div>
                                <div class="cell">
                                    <div class="title">24H量</div>
                                    <div class="value">{{tableData.vol}}</div>
                                </div>
                            </div>
                        </div>
                        <div class="chart-box">
                            <div id="tradingview_2ac30" style="height: 530px"></div>
                        </div>
                        <!--  -->
                    </div>
                    <div class="trade-top-right">
                        <div class="trade-info">
                            <div class="info-row">
                                <div class="cell">
                                    <div class="title">总保证金</div>
                                    <div class="value">{{moneyMap.totalmoney || '--'}}</div>
                                </div>
                                <div class="cell">
                                    <div class="title">浮动盈利</div>
                                    <div class="value">{{moneyMap.totalrates || '--'}}</div>
                                </div>
                                <div class="cell">
                                    <div class="title">账户权益</div>
                                    <div class="value">{{moneyMap.totalval || '--'}}</div>
                                </div>
                                <div class="cell">
                                    <div class="title">风险率</div>
                                    <div class="value">{{moneyMap.scale || 0}}%</div>
                                </div>
                            </div>
                        </div>
                        <div class="trade-form">
                            <div class="form-nav">
                                <div class="nav-item" :class="{'cur':navIndex == 1}" @click="navTap(1)">全仓</div>
                                <div class="nav-item" :class="{'cur':navIndex == 2}" @click="navTap(2)">逐仓</div>
                            </div>
                            <div class="form-content">
                                <div class="content-item" v-if="navIndex == 1">
                                    <div class="form-row">
                                        <el-select class="select-item" v-model="buyValue" placeholder="请选择">
                                            <el-option
                                                    v-for="item in buyOptions"
                                                    :key="item.val"
                                                    :label="item.name"
                                                    :value="item.val"
                                            ></el-option>
                                        </el-select>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-cell" style="flex:none;width:180px;padding-right:0">
                                            <span>多仓</span>
                                            <el-select class="c-select" v-model="value" placeholder="请选择">
                                                <el-option
                                                        v-for="item in pickerTimes"
                                                        :key="item"
                                                        :label="item + ' X'"
                                                        :value="item"
                                                ></el-option>
                                            </el-select>
                                        </div>
                                        <div class="form-cell" style="flex:none;width:180px;padding-right:0">
                                            <span>空仓</span>
                                            <el-select class="c-select c-select-r" v-model="value" placeholder="请选择">
                                                <el-option
                                                        v-for="item in pickerTimes"
                                                        :key="item"
                                                        :label="item + ' X'"
                                                        :value="item"
                                                ></el-option>
                                            </el-select>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-cell">
                                            <span>USDT</span>
                                            <input
                                                    type="text"
                                                    v-if="buyValue == 1 "
                                                    disabled
                                                    :value="WebSocketData.coinPrice"
                                                    placeholder="执行价格"
                                            />
                                            <input type="text" v-else v-model="usdtVal" placeholder="执行价格"/>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-cell">
                                            <span>{{array[curID].coin || 'BTC'}}</span>
                                            <input type="text" v-model="usdtNum" placeholder="数量"/>
                                        </div>
                                    </div>
                                    <p class="form-tips">可用USDT {{usdt_num.usdt}}</p>
                                    <div class="form-btn" style="margin-top:30px" @click="deal(0)">买入开多(看涨)</div>
                                    <!-- <p class="form-tips">可开 0 BTC</p> -->
                                    <p class="form-tips">保证金 {{deposit}} USDT</p>
                                    <div
                                            class="form-btn form-btn-red"
                                            style="margin-top:30px"
                                            @click="deal(1)"
                                    >卖出开空(看跌)
                                    </div>
                                    <!-- <p class="form-tips">可开 0 BTC</p> -->
                                    <p class="form-tips">保证金 {{deposit}} USDT</p>
                                </div>
                                <div class="content-item" v-if="navIndex == 2">
                                    <div class="form-list">
                                        <div
                                                class="list-cell"
                                                :class="{'cur':zhuCangCur == index}"
                                                v-for="(item,index) in zhuCangList"
                                                :key="index"
                                                @click="ZCTap(index)"
                                        >{{item}}USDT
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-cell">
                                            <span>USDT</span>
                                            <input type="text" disabled v-model="zhuCangPrice" placeholder="执行价格"/>
                                        </div>
                                    </div>
                                    <div class="form-num-card">
                                        <div class="num-item">数量({{array[curID].coin}})</div>
                                        <div
                                                class="num-item"
                                                style="font-size:24px;cursor: pointer;"
                                                @click="ZCNum(0)"
                                        >-
                                        </div>
                                        <div class="num-item">{{zhuCangNumText}}</div>
                                        <div
                                                class="num-item"
                                                style="font-size:24px;color:#3387FF;cursor: pointer;"
                                                @click="ZCNum(1)"
                                        >+
                                        </div>
                                    </div>
                                    <p class="form-tips">可用USDT {{usdt_num.zcUsdt}}</p>
                                    <div class="form-btn" style="margin-top:30px" @click="ZCDeal(0)">买入开多(看涨)</div>
                                    <!-- <p class="form-tips">保证金  424.0016 USDT</p> -->
                                    <div
                                            class="form-btn form-btn-red"
                                            style="margin-top:30px"
                                            @click="ZCDeal(1)"
                                    >卖出开空(看跌)
                                    </div>
                                    <!-- <p class="form-tips">保证金  424.0016 USDT</p> -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="quotation-right-bottom" style="margin-bottom:20px">
                    <div class="chart-deep-head">
                        <span class="flex1">买入</span>
                        <span class="flex2">数量({{tableData && tableData.coin}})</span>
                        <span style="text-align: center;">金额(USTD)</span>
                        <span class="flex2" style="text-align: right;">数量({{tableData && tableData.coin}})</span>
                        <span class="flex1" style="text-align: right;">卖出</span>
                    </div>
                    <div class="chart-deep-box" v-if="tableData">
                        <div class="chart-deep-list">
                            <div
                                    class="chart-deep-item"
                                    v-for="(item,index) in tableData.depth.asks"
                                    :key="index"
                            >
                                <span>{{index+1}}</span>
                                <span>{{item[0]}}</span>
                                <span>{{item[1] | tofixed4}}</span>
                            </div>
                        </div>
                        <div class="chart-deep-list">
                            <div
                                    class="chart-deep-item2"
                                    v-for="(item,index) in tableData.depth.bids"
                                    :key="index"
                            >
                                <span>{{item[1] | tofixed4}}</span>
                                <span>{{item[0]}}</span>
                                <span>{{index+1}}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="trade-bottom">
                    <div class="trade-bottom-wrap">
                        <div class="trade-bottom-tab">
              <span
                      v-for="(item,index) in tabs"
                      :key="index"
                      :class="tabCur==index?'cur':''"
                      @click="tabClick(index)"
              >{{item}}</span>
                        </div>
                        <!-- <router-link to='/order' class="all-order">全部订单>></router-link> -->
                    </div>
                    <div class="trade-bottom-head">
                        <template v-if="tabCur != 2">
                            <p>合约</p>
                            <p>模式</p>
                            <p>
                                持仓价
                                <span>(USDT)</span>
                            </p>
                            <p v-if="tabCur == 0">
                                持仓量
                                <span>(BTC)</span>
                            </p>
                            <p>
                                保证金
                                <span>(USDT)</span>
                            </p>
                            <p>
                                现价
                                <span>(USDT)</span>
                            </p>
                            <!-- <p v-if="tabCur == 0">手续费<span>(USDT)</span></p> -->
                            <!-- <p>利息<span>(USDT)</span></p> -->
                            <p>
                                浮动盈亏
                                <span>(USDT)</span>
                            </p>
                            <p>
                                止盈价格
                                <span>(USDT)</span>
                            </p>
                            <p>
                                止损价格
                                <span>(USDT)</span>
                            </p>
                            <p v-if="tabCur == 0">
                                杠杆倍数
                                <span>(倍)</span>
                            </p>
                            <p>时间</p>
                            <p>操作</p>
                            <!-- <p v-if="tabCur == 1">状态</p> -->
                        </template>
                        <template v-else>
                            <p>币种</p>
                            <p>数量</p>
                            <p>委托价(USDT)</p>
                            <p>杠杆倍数(倍)</p>
                            <p>时间</p>
                            <p>操作</p>
                        </template>
                    </div>
                    <template v-if="list != 0 && tabCur == 0">
                        <div class="trade-bottom-item" v-for="(item,index) in list" :key="index">
                            <p>
                                <span :class="item.type == 1?'green11':'red11'">{{item.type == 1?'开多':'开空'}}</span>
                                {{item.coinname}}
                            </p>
                            <p>全仓</p>
                            <p>{{item.buyprice}}</p>
                            <p>{{item.coinnum}}</p>
                            <p>{{item.realmoney}}</p>
                            <p>{{item.stopprice}}</p>
                            <!-- <p>{{item.tax}}</p> -->
                            <!-- <p>2045.00</p> -->
                            <p>{{item.rates}}</p>
                            <p>{{item.stopwin || '--'}}</p>
                            <p>{{item.stopdonat || '--'}}</p>
                            <p>{{item.gearing}}</p>
                            <p>{{item.timestr || '--'}}</p>
                            <p class="trade-btn">
                                <a href="javascript:;" @click="editTap(index)">止盈/止损</a>
                                <a href="javascript:;" @click="coverTap(index)">一键平仓</a>
                            </p>
                        </div>
                    </template>
                    <template v-else-if="zcList != 0 && tabCur == 1">
                        <div class="trade-bottom-item" v-for="(item,index) in zcList" :key="index">
                            <p>
                                <span :class="item.type == 1?'green11':'red11'">{{item.type == 1?'开多':'开空'}}</span>
                                {{item.coinname}}
                            </p>
                            <p>逐仓</p>
                            <p>{{item.buyprice}}</p>
                            <!-- <p>{{item.coinnum}}</p> -->
                            <p>{{item.capital}}</p>
                            <p>{{item.stopprice}}</p>
                            <!-- <p>{{item.tax}}</p> -->
                            <!-- <p>2045.00</p> -->
                            <p>{{item.reward}}</p>
                            <p>{{item.stopwin || '--'}}</p>
                            <p>{{item.stopdonat || '--'}}</p>
                            <!-- <p>{{item.gearing}}</p> -->
                            <p>{{item.timestr || '--'}}</p>
                            <p class="trade-btn">
                                <a href="javascript:;" @click="editTap2(index)">止盈/止损</a>
                                <a href="javascript:;" @click="coverTap2(index)">一键平仓</a>
                            </p>
                        </div>
                    </template>
                    <template v-else-if="WebSocketData2 != 0 && tabCur == 2">
                        <div class="trade-bottom-item" v-for="(item,index) in WebSocketData2" :key="index">
                            <p>{{item.coin}}</p>
                            <p>{{item.coinnum}}</p>
                            <p>{{item.price}}</p>
                            <p>{{item.gearing}}</p>
                            <p>{{item.createtimeStr}}</p>
                            <p class="cancel_button" @click.prevent="cancelDelegate(item)">撤销</p>
                        </div>
                    </template>
                    <div class="state-none" v-else>
                        <img src="../static/images/none.png" alt/>
                        <span>暂无订单</span>
                    </div>
                </div>
            </div>
        </div>
        <!-- 确认交易弹窗 -->
        <div class="popup-bg" v-show="payWordStatus"></div>
        <div class="trade-popup" v-show="payWordStatus">
            <div class="trade-popup-wrap">
                <div class="trade-popup-title">
                    <span>确认订单</span>
                    <img src="../static/images/close.png" alt title="关闭" @click="closePay"/>
                </div>
                <div class="trade-popup-item">
                    <span>{{orderType == 1?'买入':'卖出'}}数量</span>
                    <div class="trade-popup-item-right">
                        <p>{{percentValue}}</p>
                        <b>{{array[curID].coin || '--'}}</b>
                    </div>
                </div>
                <div class="trade-popup-item">
                    <span>需付保证金</span>
                    <div class="trade-popup-item-right">
                        <p>{{deposit}}</p>
                        <b>USDT</b>
                    </div>
                </div>
                <div class="trade-popup-item">
                    <span>支付密码</span>
                    <input type="password" v-model="payWordStr" placeholder="输入支付密码" maxlength="6"/>
                </div>
                <div style="height: 10px;"></div>
                <div class="trade-popup-btn" @click="payConfirm">确定</div>
            </div>
        </div>
        <!-- 修改止盈止损弹窗 -->
        <div class="popup-bg" v-if="coverStatus"></div>
        <div class="trade-popup" v-if="coverStatus">
            <div class="trade-popup-wrap">
                <div class="trade-popup-title">
          <span>
            修改·
            <span
                    :class="list[stopIndex].type == 1?'green11':'red11'"
                    style="margin: 0 5px;"
            >{{list[stopIndex].type == 1?'开多':'开空'}}</span>
            {{list[stopIndex].coinname}}
          </span>
                    <img src="../static/images/close.png" alt title="关闭" @click="coverClose"/>
                </div>
                <div class="trade-popup-item">
                    <span>止盈价格</span>
                    <div class="trade-popup-item-right">
                        <input class="tc" type="tel" v-model="stopwin" placeholder="请输入止盈价格"/>
                        <b>USDT</b>
                    </div>
                </div>
                <div class="trade-popup-item">
                    <span>止损价格</span>
                    <div class="trade-popup-item-right">
                        <input class="tc" type="tel" v-model="stopdonat" placeholder="请输入止损价格"/>
                        <b>USDT</b>
                    </div>
                </div>
                <div style="height: 10px;"></div>
                <div class="trade-popup-btn" @click="editConfirm">确定</div>
            </div>
        </div>
        <!-- 修改止盈止损弹窗2 -->
        <div class="popup-bg" v-if="coverStatus2"></div>
        <div class="trade-popup" v-if="coverStatus2">
            <div class="trade-popup-wrap">
                <div class="trade-popup-title">
          <span>
            修改·
            <span
                    :class="zcList[stopIndex].type == 1?'green11':'red11'"
                    style="margin: 0 5px;"
            >{{zcList[stopIndex].type == 1?'开多':'开空'}}</span>
            {{zcList[stopIndex].coinname}}
          </span>
                    <img src="../static/images/close.png" alt title="关闭" @click="coverClose"/>
                </div>
                <div class="trade-popup-item">
                    <span>止盈价格</span>
                    <div class="trade-popup-item-right" style="flex:1">
                        <div class="percent-list">
                            <div
                                    class="item"
                                    v-for="(item,index) in percent1"
                                    :key="item+index"
                                    @click="editTap2Scale(0,index)"
                                    :class="{'cur':percent1Index === index}"
                            >{{item * 100}}%
                            </div>
                        </div>
                        <b>USDT</b>
                    </div>
                </div>
                <div class="trade-popup-item">
                    <span>止损价格</span>
                    <div class="trade-popup-item-right" style="flex:1">
                        <div class="percent-list">
                            <div
                                    class="item"
                                    v-for="(item,index) in percent2"
                                    :key="item+index"
                                    @click="editTap2Scale(1,index)"
                                    :class="{'cur':percent2Index === index}"
                            >{{item * 100}}%
                            </div>
                        </div>
                        <b>USDT</b>
                    </div>
                </div>
                <div style="height: 10px;"></div>
                <div class="trade-popup-btn" @click="editConfirm2">确定</div>
            </div>
        </div>
        <my-footer></my-footer>
    </div>
</template>

<script>
    import Swiper from "swiper";
    import "swiper/dist/css/swiper.css";
    import MyHeader from "@/components/Header";
    import MyFooter from "@/components/Footer";
    import {contractWsUr} from '../../public/config'

    export default {
        components: {
            MyHeader,
            MyFooter
        },
        data() {
            return {
              socket:null,
                buyOptions: [
                    {
                        name: "普通买入",
                        val: 1
                    },
                    {
                        name: "委托买入",
                        val: 2
                    }
                ], //买入选项
                buyValue: 1,
                curID: 0, //币种选择
                value: "", //杠杆的值
                // 买入数量百分比
                percent: [0.25, 0.5, 0.75, 1],
                percentValue: "",
                percent1: [0.25, 0.5, 0.75, 1], // 逐仓 止盈百分比
                percent1Index: 0,
                percent2: [0.25, 0.5, 0.75, 1], // 逐仓 止损百分比
                percent2Index: 0,
                // 菜单tab
                tabs: ["全仓订单", "逐仓订单", "委托订单"],
                tabCur: 0,
                //
                pickerTimes: "", //杠杆倍数
                pickerTimesIndex: 0, //杠杆倍数
                //
                coverStatus: false,
                coverStatus2: false,
                //
                array: [
                    //币种
                    {name: "BTC/USDT"}
                ],
                usdt_num: "", //获取USDT数量
                timer: null, //socket发送计时器
                // 数据
                WebSocketData: {
                    asks: 5,
                    bids: 5,
                    moneyMap: {
                        totalmoney: 0,
                        totalrates: 0,
                        totalval: 0
                    },
                    list: []
                },
                moneyMap: {
                    totalmoney: 0,
                    totalrates: 0,
                    totalval: 0
                },
                asks: [],
                bids: [],
                list: [], //订单列表
                zcList: [], //逐仓订单列表
                WebSocketData2: "", //全部列表
                deposit: 0, // 保证金
                CoverIndex: 0, //平仓的索引
                // 止盈
                stopwin: "",
                // 止损
                stopdonat: "",
                // 止损index
                stopIndex: 0,
                stopIndex2: 0,

                // ordercode
                ordercode: "",
                ordercode2: "",
                // 支付密码
                payWordStr: "",
                payWordStatus: false, //支付确认弹窗
                // 买入卖出type
                orderType: 0,
                //
                tableData: "", //当前币种的数据
                //选项卡切换
                navIndex: 1,
                // 逐仓数据
                zhuCangList: [],
                zhuCangCur: 0,
                zhuCangPrice: 0,
                zhuCangNumText: 1,
                zhuCangBtnIndex: 0,
                usdtVal: "", //usdt价格
                usdtNum: "" //usdt数量
            };
        },
        filters: {
            tofixed2: function (value) {
                return value.toFixed(2);
            },
            tofixed4: function (value) {
                return value.toFixed(6);
            }
        },
      beforeDestroy () {
          this.socket.close()
      },
      mounted() {
            // 杠杆
            this.$ajax("/trade/queryGearing", {}).then(res => {
                if (res.data.status) {
                    this.pickerTimes = res.data.data;
                    this.value = this.pickerTimes[0];
                }
            });

            // 获取usdt  (目的是验证token是否有效)
            this.$ajax("/account/getMyInfos", {
                token: this.$userID()
            }).then(res => {
                if (res.data.status) {
                    this.usdt_num = res.data.data;
                    this.initSocket();
                    this.tradingViewShow(this.array[this.curID].coin || "BTC");
                } else {
                    clearInterval(this.timer);
                    this.timer = null;
                    this.$router.push("/login");
                    //this.$token(res)
                }
            });
            // 获取逐仓可以选择的金额
            this.$ajax("/trade/queryZCmoney").then(res => {
                if (res.data.status) {
                    this.zhuCangList = res.data.data;
                }
            });
        },
        destroyed() {
            console.log("clear");
            clearInterval(this.timer);
            this.timer = null;
        },
        watch: {
            buyValue() {
                this.deposit = 0.0;
                this.usdtVal = "";
                this.usdtNum = "";
            },
            usdtNum(e) {
                let val = e;
                let rex = /^[0-9]+(.[0-9]{1,4})?$/;
                // let b = this.WebSocketData.coinPrice / this.pickerTimes[this.curID]
                let x = 0,
                    b;
                if (this.buyValue == 1) {
                    b = this.WebSocketData.coinPrice / this.value;
                } else {
                    b = this.usdtVal / this.value;
                }
                if (rex.test(val)) {
                    x = val;
                }
                this.deposit = (x * b).toFixed(4); //保证金
            },
            array() {
                var swiper = new Swiper(".swiper-container", {
                    slidesPerView: "auto",
                    // slidesOffsetAfter:30,
                    // slidesOffsetBefore:30,
                    spaceBetween: 2,
                    // slidesPerColumnFill:'row',
                    navigation: {
                        nextEl: ".swiper-button-next",
                        prevEl: ".swiper-button-prev"
                    }
                });
            }
        },
        methods: {
            // 取消委托订单
            cancelDelegate(item) {
                this.$confirm("是否要平仓?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.$ajax('/trade/handleCancelEntrust', {
                        token: this.$userID(),
                        ordercode: item.ordercode
                    }).then(
                        res => {
                            if (res.data.status) {
                                this.$message({
                                    type: "success",
                                    message: res.data.desc
                                });
                                this.$ajax("/trade/queryMyEntrust", {
                                    //委托订单
                                    token: this.$userID(),
                                    page: 1,
                                    status: 1 //1委托中 2委托成功 3已取消
                                }).then(res => {
                                    if (res.data.status) {
                                        this.WebSocketData2 = res.data.data;
                                    }
                                });
                            } else {
                                this.$message({
                                    type: "error",
                                    message: res.data.desc
                                });
                            }
                        })
                })

            },
            //逐仓金额选择
            ZCTap(i) {
                this.zhuCangCur = i;
                this.zhuCangPrice = this.zhuCangNumText * this.zhuCangList[i];
            },
            // 逐仓调整数量
            ZCNum(e) {
                if (e === 1 && this.zhuCangNumText < 99) {
                    this.zhuCangNumText++;
                } else if (e === 0 && this.zhuCangNumText >= 2) {
                    this.zhuCangNumText--;
                }
                this.ZCTap(this.zhuCangCur);
            },
            navTap(i) {
                if (i === 2 && this.array[this.curID].type !== 0) {
                    this.$alert("当前币种无法选择逐仓");
                } else {
                    this.navIndex = i;
                    this.ZCTap(0);
                    this.tabCur = i--; //控制下方全仓逐仓订单tab
                }
            },
            // 图表方法
            tradingViewShow(coin) {
                new TradingView.widget({
                    autosize: true,
                    symbol: "HUOBI:" + coin + "USDT",
                    interval: "1",
                    timezone: "Asia/Shanghai",
                    theme: "Light",
                    style: "1",
                    locale: "zh_CN",
                    toolbar_bg: "#f1f3f6",
                    enable_publishing: false,
                    hide_legend: true,
                    save_image: false,
                    studies: [
                        "MACD@tv-basicstudies",
                        "BB@tv-basicstudies",
                        "Volume@tv-basicstudies",
                        "VWAP@tv-basicstudies"
                    ],
                    container_id: "tradingview_2ac30"
                });
            },
            // 初始化socket
            initSocket() {
              let socket =new WebSocket( contractWsUr+this.$userID())
                socket.onopen = () => {
                    socket.send(this.$userID() + "_depth_" + this.array[this.curID].name);
                    socket.send(this.$userID() + "_home");
                    socket.send(this.$userID() + "_kline_" + this.array[this.curID].name);
                    this.timer = setInterval(() => {
                        socket.send(this.$userID() + "_home");
                        socket.send(this.$userID() + "_depth_" + this.array[this.curID].name);
                        socket.send(this.$userID() + "_kline_" + this.array[this.curID].name);
                    }, 1500);
                };
                socket.onmessage = e => {
                    const res = JSON.parse(e.data);
                    if (res instanceof Array) {
                        this.array = res;
                    } else if (res.moneyMap) {
                        this.WebSocketData = res;
                        this.list = this.WebSocketData.list;
                        this.zcList = this.WebSocketData.zclist;
                        this.moneyMap = this.WebSocketData.moneyMap;
                        this.asks = this.WebSocketData.asks;
                        this.bids = this.WebSocketData.bids;
                    } else {
                        console.log(res);
                        this.tableData = res;
                    }
                };
               this.socket=socket
            },
            // 选择币种
            coinChange(e) {
                if (e == this.curID) return;
                this.curID = e;
                this.navIndex = 1;
                this.buyValue = 1;
                //this.pickerTimesIndex = e;
                // 清空数量计算
                this.usdtNum = "";
                // 清空保证金
                this.deposit = 0;
                this.tradingViewShow(this.array[this.curID].coin);
                const loading = this.$loading({
                    lock: true,
                    text: "Loading",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                });
                setTimeout(() => {
                    loading.close();
                }, 1000);
            },
            // 切换tab
            tabClick(e) {
                this.tabCur = e;
                if (e == 2) {
                    this.$ajax("/trade/queryMyEntrust", {
                        //委托订单
                        token: this.$userID(),
                        page: 1,
                        status: 1 //1委托中 2委托成功 3已取消
                    }).then(res => {
                        if (res.data.status) {
                            this.WebSocketData2 = res.data.data;
                        }
                    });
                }
            },
            // 交易
            deal(e) {
                this.orderType = e;
                let rex = /^[0-9]+(.[0-9]{1,4})?$/;
                if (this.usdtNum == "") {
                    this.$alert("请输入数量", "", 1200);
                } else if (!rex.test(this.usdtNum)) {
                    this.$alert("输入数量有误", "", 1200);
                } else {
                    // this.$alert('输入支付密码')
                    //this.payWordStatus = true
                    this.dealSubmit();
                }
            },
            dealSubmit() {
                if (this.buyValue == 1) {
                    //普通购买
                    this.$ajax("/trade/handlContractOrder", {
                        token: this.$userID(),
                        // payword:this.payPasswordStr,
                        type: this.orderType + 1,
                        coinnum: this.usdtNum,
                        gearing: this.value,
                        symbol: this.array[this.curID].symbol
                    }).then(res => {
                        this.$alert(res.data.desc, "", 1000);
                        if (res.data.status) {
                            // 查询USDT余额
                            this.$ajax(
                                "/account/getMyInfos",
                                {
                                    token: this.$userID()
                                },
                                res => {
                                    if (res.data.status) {
                                        this.usdt_num = res.data.data;
                                    }
                                }
                            );
                        }
                    });
                } else {
                    //委托购买
                    this.$ajax("/trade/handleEntrust", {
                        token: this.$userID(),
                        // payword:this.payPasswordStr,
                        type: this.orderType + 1,
                        coinnum: this.usdtNum,
                        gearing: this.value,
                        symbol: this.array[this.curID].symbol,
                        price: this.usdtVal
                    }).then(res => {
                        this.$alert(res.data.desc, "", 1000);
                        if (res.data.status) {
                            // 查询USDT余额
                            this.$ajax(
                                "/account/getMyInfos",
                                {
                                    token: this.$userID()
                                },
                                res => {
                                    if (res.data.status) {
                                        this.usdt_num = res.data.data;
                                    }
                                }
                            );
                        }
                    });
                }
            },
            //逐仓交易
            ZCDeal(e) {
                this.zhuCangBtnIndex = e;
                // this.$refs.zhuCang.showPay()    //显示支付密码组件
                this.ZCSubmit();
            },
            //逐仓交易提交
            ZCSubmit() {
                this.$ajax("/trade/handlZCOrder", {
                    token: this.$userID(),
                    //payword:this.payPasswordStr3,
                    type: this.zhuCangBtnIndex + 1, //1开多  2开空
                    coinnum: this.zhuCangNumText, // 持仓量
                    price: this.zhuCangList[this.zhuCangCur], //金额单价
                    symbol: this.array[this.curID].symbol //交易对 btc_usdt eth_usdt等等
                }).then(res => {
                    this.$alert(res.data.desc);
                    if (res.data.status) {
                        // this.$refs.zhuCang.closeTap()   //隐藏支付密码组件
                        // 查询USDT余额
                        this.$ajax(
                            "/account/getMyInfos",
                            {
                                token: this.$userID()
                            },
                            res => {
                                if (res.data.status) {
                                    this.usdt_num = res.data.data;
                                }
                            }
                        );
                    }
                });
            },
            // 关闭交易弹窗
            closePay() {
                this.payWordStatus = false;
            },
            // 切换杠杆
            valueScaleChange(e) {
                // this.value = e
                this.percentValue = "";
                this.deposit = "0.0000";
            },
            // 数量输入
            inputVal(e) {
                // let val = e.detail.value
                let val = e.target.value;
                console.log(e.target.value);
                let rex = /^[0-9]+(.[0-9]{1,4})?$/;
                // let b = this.WebSocketData.coinPrice / this.pickerTimes[this.curID]
                let x = 0,
                    b;
                if (this.buyValue == 1) {
                    b = this.WebSocketData.coinPrice / this.value;
                } else {
                    b = this.usdtVal / this.value;
                }
                if (rex.test(val)) {
                    x = val;
                }
                this.deposit = (x * b).toFixed(4); //保证金
            },
            // 百分比选择
            percentTap(e) {
                // console.log((this.percent[e]))
                let a = (this.usdt_num.balance - 0) * this.percent[e];
                // let b = this.WebSocketData.coinPrice / this.pickerTimes[this.curID]
                let b = this.WebSocketData.coinPrice / this.value;
                let x = (a / b).toFixed(4);
                this.deposit = (x * b).toFixed(4); //保证金
                console.log(a, b, x);
                this.percentValue = x || "0.0000";
            },
            // 确定支付
            payConfirm() {
                if (this.payWordStr.length < 6 || !this.payWordStr) {
                    this.$alert("支付密码不正确");
                } else if (!this.value) {
                    this.$alert("请选择杠杆");
                } else {
                    this.$ajax("/trade/handlContractOrder", {
                        token: this.$userID(),
                        payword: this.payWordStr,
                        type: this.orderType,
                        coinnum: this.percentValue,
                        gearing: this.value,
                        symbol: this.array[this.curID].symbol
                    }).then(res => {
                        this.$alert(res.data.desc);
                        if (res.data.status) {
                            // 查询USDT余额
                            this.$ajax("/account/getMyInfos", {
                                token: this.$userID()
                            }).then(res => {
                                if (res.data.status) {
                                    this.usdt_num = res.data.data;
                                    this.payWordStatus = false;
                                }
                            });
                        } else {
                            this.payWordStr = "";
                        }
                    });
                }
            },
            // 逐仓 修改止盈止损	弹窗
            editTap2(e) {
                this.stopIndex2 = e;
                this.coverStatus2 = true;
                this.ordercode2 = this.WebSocketData.zclist[e].ordercode;
            },
            // 逐仓 修改止盈止损 比例选择
            editTap2Scale(i, index) {
                if (i === 0) {
                    this.percent1Index = index;
                    this.stopwin2 = this.percent1[index];
                } else if (i === 1) {
                    this.percent2Index = index;
                    this.stopdonat2 = this.percent2[index];
                }
            },
            // 修改止盈止损（弹窗）
            editTap(e) {
                this.stopIndex = e;
                this.coverStatus = true;
                this.ordercode = this.WebSocketData.list[e].ordercode;
            },
            // 修改确认操作
            editConfirm() {
                let rex = /^[0-9]+(.[0-9]{1,4})?$/;
                if (this.stopwin == "" || this.stopdonat == "") {
                    this.$alert("请输入价格", "", 1200);
                } else if (!rex.test(this.stopwin) || !rex.test(this.stopdonat)) {
                    this.$alert("输入价格不正确", "", 1200);
                } else {
                    this.$ajax("/trade/handleEditOrder", {
                        token: this.$userID(),
                        ordercode: this.ordercode,
                        stopwin: this.stopwin,
                        stopdonat: this.stopdonat
                    }).then(res => {
                        if (res.data.status) {
                            this.$message({
                                type: "success",
                                message: res.data.desc
                            });
                            this.coverStatus = false;
                            this.stopwin = "";
                            this.stopdonat = "";
                        } else {
                            this.$message({
                                type: "error",
                                message: res.data.desc
                            });
                        }
                    });
                }
            },
            //修改2
            editConfirm2() {
                this.$ajax("/trade/handleZCStop", {
                    token: this.$userID(),
                    ordercode: this.ordercode2,
                    stopwin: this.percent1[this.percent1Index],
                    stopfail: this.percent2[this.percent2Index]
                }).then(res => {
                    if (res.data.status) {
                        this.$message({
                            type: "success",
                            message: res.data.desc
                        });
                        this.coverStatus2 = false;
                    } else {
                        this.$message({
                            type: "error",
                            message: res.data.desc
                        });
                    }
                });
            },
            // 止盈止损修改 弹窗关闭
            coverClose() {
                this.coverStatus = false;
                this.stopwin = "";
                this.stopdonat = "";
                this.coverStatus2 = false;
            },
            // 平仓
            coverTap(e) {
                this.CoverIndex = e;
                this.$confirm("是否要平仓?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.$ajax("/trade/handleCloseOrder", {
                        token: this.$userID(),
                        ordercode: this.WebSocketData.list[this.CoverIndex].ordercode
                    }).then(res => {
                        if (res.data.status) {
                            this.$message({
                                type: "success",
                                message: res.data.desc
                            });
                            // 查询USDT余额
                            this.$ajax("/account/getMyInfos", {
                                token: this.$userID()
                            }).then(res => {
                                if (res.data.status) {
                                    this.usdt_num = res.data.data;
                                }
                            });
                        } else {
                            this.$message({
                                type: "error",
                                message: res.data.desc
                            });
                        }
                    });
                });
            },
            // 平仓2
            coverTap2(e) {
                this.$confirm("是否要平仓?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.$ajax("/trade/handleCloseZc", {
                        token: this.$userID(),
                        ordercode: this.WebSocketData.zclist[e].ordercode
                    }).then(res => {
                        if (res.data.status) {
                            this.$message({
                                type: "success",
                                message: res.data.desc
                            });
                            // 查询USDT余额
                            this.$ajax("/account/getMyInfos", {
                                token: this.$userID()
                            }).then(res => {
                                if (res.data.status) {
                                    this.usdt_num = res.data.data;
                                }
                            });
                        } else {
                            this.$message({
                                type: "error",
                                message: res.data.desc
                            });
                        }
                    });
                });
            }
        }
    };
</script>

<style>
    .main {
        width: 1200px;
        margin-left: auto;
        margin-right: auto;
    }

    .swiper-button-prev,
    .swiper-button-next {
        top: 50%;
        width: 15px;
        height: 24px;
        transform: translateY(-50%);
        margin-top: 0;
        background-size: 100%;
    }

    /*  */

    /*  */
    .trade-form {
        background: #ffffff;
    }

    .form-nav {
        display: flex;
        height: 54px;
        background: #fff;
    }

    .form-nav .nav-item {
        flex: 1;
        font-size: 16px;
        color: #666666;
        text-align: center;
        line-height: 54px;
        font-weight: bold;
        cursor: pointer;
    }

    .form-nav .cur {
        border-bottom: 2px solid #3387ff;
        box-sizing: border-box;
        color: #3387ff;
    }

    .form-content .content-item {
        padding: 16px 24px;
    }

    .form-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 16px;
    }

    .select-item {
        flex: 1;
        background: none;
    }

    .select-item input {
        background: none;
        color: #333333;
        border: 1px solid #cccccc;
        border-radius: 0;
    }

    .form-row .form-cell {
        flex: 1;
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 40px;
        padding: 0 16px;
        border: 1px solid #cccccc;
    }

    .form-row .form-cell span {
        color: #333333;
    }

    .form-row .form-cell input {
        text-align: right;
        color: #333333;
        background: none;
        border: none;
        outline: none;
    }

    .form-tips {
        font-size: 12px;
        color: #999999;
        margin-top: 10px;
    }

    .form-list {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
    }

    .form-list .list-cell {
        width: 48%;
        height: 40px;
        border: 1px solid #cccccc;
        font-size: 14px;
        color: #333333;
        text-align: center;
        line-height: 40px;
        margin-bottom: 16px;
        cursor: pointer;
    }

    .form-list .cur {
        border-color: #3387ff;
    }

    .form-num-card {
        display: flex;
        height: 40px;
        border: 1px solid #cccccc;
    }

    .form-num-card .num-item {
        flex: 1;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 14px;
        color: #333333;
        border-right: 1px solid #cccccc;
        box-sizing: border-box;
    }

    .form-num-card .num-item:last-of-type {
        border: none;
    }

    .form-row .form-cell .c-select {
        width: 100px;
        background: none;
    }

    .form-row .form-cell .c-select input {
        background: none;
        color: #1cc696;
        border-radius: 0;
    }

    .form-row .form-cell .c-select-r input {
        color: #ee534f;
    }

    /*  */
    .trade-info {
        display: flex;
        align-items: center;
        height: 72px;
        background: #ffffff;
        padding: 0 24px;
        margin-bottom: 8px;
    }

    .trade-info .info-row {
        flex: 1;
        display: flex;
    }

    .trade-info .info-row .cell {
        flex: 1;
    }

    .trade-info .info-row .title {
        font-size: 12px;
        color: #999999;
        margin-bottom: 3px;
    }

    .trade-info .info-row .value {
        font-size: 16px;
        color: #333333;
        font-weight: bold;
    }

    /*  */
    .chart-box {
        margin-bottom: 8px;
        background: white;
    }

    /*  */
    .quotation-right-bottom {
        padding: 8px 24px 16px;
        background: #ffffff;
    }

    .chart-deep-head {
        display: flex;
        margin-bottom: 10px;
    }

    .chart-deep-head span {
        flex: 1;
        font-size: 12px;
        color: #999999;
    }

    .chart-deep-head .flex1 {
        flex: 0 0 10%;
    }

    .chart-deep-head .flex2 {
        flex: 0 0 20%;
    }

    .chart-deep-box {
        display: flex;
    }

    .chart-deep-list {
        flex: 1;
    }

    .chart-deep-item {
        display: flex;
        height: 30px;
        line-height: 30px;
    }

    .chart-deep-item span {
        flex: 1;
        font-size: 12px;
        color: #333333;
    }

    .chart-deep-item span:first-of-type {
        flex: 0 0 20%;
    }

    .chart-deep-item span:nth-of-type(2) {
        flex: 0 0 30%;
        padding-right: 5px;
    }

    .chart-deep-item span:nth-of-type(3) {
        text-align: right;
        padding: 0 5px;
        color: #00ad76;
        background: rgba(2, 173, 143, 0.1);
    }

    .chart-deep-item2 {
        display: flex;
        height: 30px;
        line-height: 30px;
    }

    .chart-deep-item2 span {
        flex: 1;
        font-size: 12px;
        color: #333333;
    }

    .chart-deep-item2 span:first-of-type {
        padding: 0 5px;
        color: #d14b64;
        background: rgba(209, 75, 100, 0.1);
    }

    .chart-deep-item2 span:nth-of-type(2) {
        text-align: right;
        flex: 0 0 30%;
        padding-right: 5px;
    }

    .chart-deep-item2 span:nth-of-type(3) {
        text-align: right;
        flex: 0 0 20%;
    }

    /*  */
    .current-trade-card {
        display: flex;
        align-items: center;
        height: 72px;
        background: #ffffff;
        padding: 0 24px;
        box-sizing: border-box;
        margin-bottom: 8px;
    }

    .current-trade-card .trade-name {
        font-size: 24px;
        color: #3387ff;
        font-weight: bold;
        margin-right: 40px;
    }

    .current-trade-card .trade-money {
        margin-right: 30px;
        color: #1cc696;
    }

    .current-trade-card .trade-money .money {
        font-size: 22px;
        font-weight: bold;
        display: flex;
        align-items: center;
    }

    .current-trade-card .trade-money .tag {
        padding: 0 6px;
        height: 20px;
        font-size: 12px;
        color: white;
        line-height: 20px;
        margin-left: 15px;
        background: #1cc696;
    }

    .current-trade-card .trade-money .ratio {
        font-size: 12px;
    }

    .current-trade-card .info-row {
        flex: 1;
        display: flex;
    }

    .current-trade-card .info-row .cell {
        flex: 1;
    }

    .current-trade-card .info-row .title {
        font-size: 12px;
        color: #999999;
        margin-bottom: 3px;
    }

    .current-trade-card .info-row .value {
        font-size: 16px;
        color: #333333;
        font-weight: bold;
    }

    /*  */
    .trade-nav {
        display: flex;
        height: 48px;
        margin-bottom: 2px;
    }

    .trade-nav .nav-name {
        width: 120px;
        font-size: 16px;
        color: #333333;
        font-weight: bold;
        line-height: 48px;
        background: #ffffff;
        text-align: center;
    }

    .trade-nav .nav-list {
        width: 1080px;
    }

    /* .trade-nav .nav-wrapper{display: flex;} */
    .trade-nav .nav-wrapper div {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 135px;
        background: #ffffff;
        padding: 0 10px;
        font-size: 12px;
        font-weight: bold;
        color: #333333;
        cursor: pointer;
    }

    .trade-nav .nav-wrapper div.cur {
        background: #d7e8ff;
        border-top: 2px solid #3387FF;
        color: #3387ff;
        box-sizing: border-box;
    }

    /*  */
    .trade-popup {
        width: 472px;
        background: #fff;
        position: fixed;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        z-index: 1001;
        border-radius: 6px;
    }

    .trade-popup-wrap {
        padding: 40px 48px;
    }

    .trade-popup-title {
        text-align: center;
        margin-bottom: 24px;
    }

    .trade-popup-title span {
        font-size: 24px;
        font-weight: bold;
    }

    .trade-popup-title img {
        position: absolute;
        right: 0;
        top: 0;
        padding: 15px;
        width: 28px;
        cursor: pointer;
    }

    .trade-popup-item {
        height: 48px;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 16px;
        font-size: 16px;
        margin-bottom: 16px;
    }

    .trade-popup-item span {
        color: #3387ff;
        margin-right: 15px;
    }

    .trade-popup-item input {
        height: 100%;
        line-height: 48px;
        border: 0;
        outline: none;
        text-align: right;
    }

    .trade-popup-item input.tc {
        text-align: left;
    }

    .trade-popup-item-right {
        display: flex;
        align-items: center;
        height: 100%;
    }

    .trade-popup-item-right p {
    }

    .trade-popup-item-right b {
        margin-left: 10px;
    }

    .trade-popup-btn {
        height: 48px;
        background: #3387ff;
        color: white;
        border-radius: 4px;
        text-align: center;
        line-height: 48px;
        font-size: 16px;
        cursor: pointer;
    }

    .trade-popup-btn:active {
        opacity: 1;
    }

    .trade-popup-btn:hover {
        opacity: 0.85;
    }

    .trade-popup-item .percent-list {
        flex: 1;
        display: flex;
        height: 100%;
        border-left: 1px solid #cccccc;
        border-right: 1px solid #cccccc;
    }

    .trade-popup-item .percent-list .item {
        flex: 1;
        font-size: 14px;
        color: #666666;
        text-align: center;
        line-height: 48px;
        border-right: 1px solid #cccccc;
    }

    .trade-popup-item .percent-list .item:last-child {
        border-right: none;
    }

    .trade-popup-item .percent-list .item:hover {
        background: #3387ff;
        color: white;
        cursor: pointer;
    }

    .trade-popup-item .percent-list .cur {
        background: #3387ff;
        color: white;
    }

    /*  */
    .item-right {
        width: 200px;
        background: #081724;
    }

    .item-right-title {
        padding: 20px 16px 4px 16px;
        border-bottom: 1px solid #22324b;
        display: flex;
        justify-content: space-between;
    }

    .item-right-title span {
        font-size: 12px;
        color: #778aa2;
    }

    .item-right-list {
        padding: 0 16px;
    }

    .item-right-item {
        display: flex;
        justify-content: space-between;
        height: 25px;
        line-height: 25px;
        background: rgba(209, 75, 100, 0.1);
    }

    .green span:first-child {
        color: #02ad8f !important;
    }

    .green {
        background: rgba(2, 173, 143, 0.1);
    }

    .item-right-item span:first-child {
        color: #d14b64;
    }

    .item-right-item span:nth-child(2) {
        color: #778aa2;
    }

    .item-right-all {
        padding: 20px 0;
    }

    .item-right-all p {
        font-size: 18px;
        color: #00ad76;
    }

    .item-right-all span {
        font-size: 12px;
        color: #778aa2;
    }

    .trade-top-right-content {
        display: flex;
        justify-content: space-between;
    }

    .item-left {
        background: #081724;
        width: 788px;
    }

    .item-left-title {
        display: flex;
        justify-content: space-between;
        height: 50px;
        align-items: center;
        padding: 0 30px;
    }

    .item-left-title h4 {
        color: #d1d5eb;
        font-size: 16px;
        font-weight: normal;
    }

    .select-box {
        width: 100px;
        background: none;
    }

    .select-box input {
        background: none;
        color: #d1d5eb;
    }

    .item-left-form {
        padding: 0 30px;
        height: 418px;
        display: flex;
        justify-content: space-between;
    }

    .item-left-form-item {
        width: 330px;
        padding-top: 32px;
    }

    .form-item {
        height: 38px;
        position: relative;
        display: flex;
    }

    .form-item input {
        color: #d1d5eb;
        background: none;
        outline: none;
        border: 1px solid #46586f;
        border-radius: 3px;
        padding: 0 16px;
        flex: 1;
        height: 38px;
    }

    .form-item input:focus {
        border-color: #ffaa00;
    }

    .form-item span {
        color: #778aa2;
        position: absolute;
        right: 16px;
        top: 50%;
        transform: translateY(-50%);
    }

    .item-left-form-item p {
        font-size: 12px;
        color: #778aa2;
        padding: 12px 0;
    }

    .form-percent {
        height: 34px;
        display: flex;
        border: 1px solid #46586f;
        border-top: 1px solid #081724;
        overflow: hidden;
    }

    .form-percent a {
        flex: 1;
        text-align: center;
        line-height: 34px;
        border-right: 1px solid #46586f;
        color: #d1d5eb;
        cursor: pointer;
    }

    .form-percent a:last-child {
        border-right: 0;
    }

    .form-percent a:active {
        background: #ffaa00;
        color: #fff;
    }

    .form-money {
        font-size: 16px;
        color: #778aa2;
        margin: 20px 0 30px 0;
    }

    .form-btn {
        height: 45px;
        background: #02ad8f;
        color: #fff;
        line-height: 45px;
        text-align: center;
        position: relative;
        font-size: 15px;
        cursor: pointer;
        display: block;
    }

    .form-btn span {
        position: absolute;
        left: 200px;
        top: 50%;
        transform: translateY(-50%);
        color: rgba(255, 255, 255, 0.4);
        line-height: 45px;
    }

    .form-btn:hover {
        opacity: 0.85;
    }

    .form-btn:active {
        opacity: 1;
    }

    .form-btn-red {
        background: #ee534f;
    }

    /*  */
    .trade-page {
        background: #f8f9fb;
    }

    .trade-box {
        padding: 2px 0 20px;
    }

    .trade-top {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;
    }

    .trade-top-left {
        width: 702px;
        overflow: hidden;
    }

    .trade-top-left-title {
        height: 48px;
        line-height: 48px;
        color: #56586f;
        padding-left: 32px;
    }

    .trade-top-left-list {
        display: flex;
        flex-direction: column;
        height: 516px;
    }

    .trade-top-left-list p {
        height: 48px;
        padding-left: 32px;
        color: #778aa2;
        line-height: 48px;
        cursor: pointer;
    }

    .trade-top-left-list p.cur {
        color: #ffaa00;
        position: relative;
        background: #141f30;
    }

    .trade-top-left-list p.cur:after {
        content: "";
        width: 2px;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        background: #ffaa00;
    }

    .swiper-container {
        width: 100%;
        height: 100%;
        overflow: hidden;
        position: relative;
    }

    .swiper-scrollbar-drag {
        background: #666;
    }

    .swiper-box {
        height: 467px;
    }

    /*  */
    .trade-top-right {
        width: 490px;
    }

    .trade-top-right-head {
        background: #081724;
        height: 80px;
        display: flex;
        align-items: center;
        margin-bottom: 16px;
    }

    .trade-top-right-head p {
        flex: 1;
        padding-left: 32px;
        color: #46586f;
        position: relative;
    }

    .trade-top-right-head p span {
        display: block;
        font-size: 18px;
        color: #d1d5eb;
        margin-top: 6px;
    }

    .trade-top-right-head p:after {
        content: "";
        width: 1px;
        height: 32px;
        background: #22324b;
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
    }

    .trade-top-right-head p:first-of-type:after {
        display: none;
    }
</style>
<style scoped>
    /*  */
    .trade-bottom {
        background: #ffffff;
    }

    .trade-bottom-wrap {
        position: relative;
    }

    .trade-bottom-tab {
        display: flex;
        width: 400px;
        height: 45px;
        line-height: 45px;
    }

    .trade-bottom-tab span {
        flex: 0 0 50%;
        text-align: center;
        font-size: 16px;
        color: #999999;
        cursor: pointer;
    }

    .trade-bottom-tab span.cur {
        color: #3387ff;
        position: relative;
    }

    .trade-bottom-tab span.cur:after {
        content: "";
        background: #3387ff;
        position: absolute;
        left: 0;
        bottom: 0;
        height: 2px;
        width: 100%;
    }

    .all-order {
        position: absolute;
        right: 20px;
        top: 50%;
        transform: translateY(-50%);
        color: #778aa2;
    }

    .all-order:hover {
        color: #ffaa00;
        text-decoration: underline;
    }

    .trade-bottom-head {
        display: flex;
        height: 64px;
        align-items: center;
        background: #f2f2f2;
    }

    .trade-bottom-head p {
        flex: 1;
        text-align: center;
        color: #999999;
    }

    .trade-bottom-head p span {
        display: block;
    }

    .trade-bottom-item {
        display: flex;
        height: 74px;
        align-items: center;
        border-bottom: 1px solid #ededed;
    }

    .trade-bottom-item p {
        flex: 1;
        text-align: center;
        color: #333333;
        font-size: 12px;
    }

    .trade-bottom-item p span {
        display: block;
    }

    .trade-btn {
        flex-direction: column;
        display: flex;
        align-items: center;
    }

    .trade-bottom-item p a {
        width: 70px;
        height: 28px;
        text-align: center;
        line-height: 28px;
        border-radius: 5px;
        background: #3387ff;
        font-size: 12px;
        color: white;
        transition: 0.25s all;
    }

    .trade-bottom-item p a:nth-child(2) {
        margin-top: 6px;
    }

    .trade-bottom-item p a:hover {
        opacity: 0.7;
    }

    .red11 {
        color: #d14b64 !important;
    }

    .green11 {
        color: #02ad8f !important;
    }

    .state-none {
        display: -ms-flexbox;
        padding-top: 20px;
        display: flex;
        -ms-flex-direction: column;
        flex-direction: column;
        -ms-flex-align: center;
        align-items: center;
    }

    .cancel_button {
        cursor: pointer;
    }
</style>
