<template>
  <div>
    <my-header></my-header>
    <div class="main_container">
      <div class="main_content">
        <div class="title_container">
          {{ $t('header.ContractPositionOrder') }}
        </div>
        <div class="tab_container">
          <div style="display: flex">
            <div
              class="tab_item"
              :class="tabIndex === 0 ? 'select_tab' : ''"
              @click="tabTap(0)"
            >
              {{ $t('contract.Position') }}
            </div>
            <div
              class="tab_item"
              :class="tabIndex === 1 ? 'select_tab' : ''"
              @click="tabTap(1)"
            >
              {{ $t('contract.History') }}
            </div>
          </div>
          <div style="display: flex">
            <div
              class="type_button"
              :class="type === 0 ? 'select_type' : ''"
              @click="changeType(0)"
            >
              {{ $t('contract.AllOpenTrade') }}
            </div>
            <div
              class="type_button"
              :class="type === 1 ? 'select_type' : ''"
              @click="changeType(1)"
            >
              {{ $t('contract.ByOpenTrade') }}
            </div>
          </div>
        </div>
        <table-header :type="type" :tab-index="tabIndex" />
        <!--全仓 持仓中-->
        <div v-if="type === 0 && tabIndex === 0" class="item_container">
          <qc-list-item
            v-for="(item, index) in qcList"
            :item="item"
            :key="index"
          >
            <div class="last">
              <div class="button_container" @click="editTap(item)">
                {{ $t('contract.Stop') }}
              </div>
              <div class="button_container" @click="coverTap(item)">
                {{ $t('contract.Positions') }}
              </div>
            </div>
          </qc-list-item>
        </div>
        <!--全仓 历史记录-->
        <div v-if="type === 0 && tabIndex === 1" class="item_container">
          <qc-list-item
            v-for="(item, index) in qcHistoryList"
            :item="item"
            :key="index"
          >
            <div>
              {{
                item.settleflag == 0
                  ? $t('contract.Settled')
                  : $t('contract.NoSettlement')
              }}({{ item.remark }})
            </div>
          </qc-list-item>
        </div>
        <!--逐仓 持仓中-->
        <div v-if="type === 1 && tabIndex === 0" class="item_container">
          <zc-list-item
            v-for="(item, index) in zcList"
            :item="item"
            :key="index"
          >
            <div class="last" v-if="tabIndex === 0">
              <div class="button_container" @click="editTap(item)">
                {{ $t('contract.Stop') }}
              </div>
              <div class="button_container" @click="coverTap(item)">
                {{ $t('contract.Positions') }}
              </div>
            </div>
          </zc-list-item>
        </div>
        <!--逐仓 历史记录-->
        <div v-if="type === 1 && tabIndex === 1" class="item_container">
          <zc-list-item
            v-for="(item, index) in zcHistoryList"
            :item="item"
            :key="index"
          >
            <div>
              {{
                item.status == 2
                  ? $t('contract.Settled')
                  : $t('contract.NoSettlement')
              }}({{ item.remark }})
            </div>
          </zc-list-item>
        </div>

        <div class="page" v-if="tabIndex === 1">
          <span class="up" @click="prePage">{{ $t('contract.Pre') }}</span>
          <span class="down" @click="nextPage">{{ $t('contract.Next') }}</span>
        </div>
      </div>
    </div>

    <div class="dialog_container">
      <el-dialog :visible.sync="dialogFormVisible" width="470px">
        <div slot="title">
          <div class="dialog_title">
            <Tag :is-kai-duo="selectItem.type === 1" />
            {{ selectItem.coin.toUpperCase().replace('_', '/') }}
          </div>
        </div>
        <el-form :model="form" label-position="top">
          <el-form-item :label="$t('contract.CheckPrice')" v-if="type == 1">
            <el-input
              v-model="form.win"
              autocomplete="off"
              :placeholder="
                selectItem.stopwin == ''
                  ? $t('contract.EnterCheckPrice')
                  : selectItem.stopwin
              "
            >
              <template slot="suffix">USDT</template>
            </el-input>
          </el-form-item>

          <el-form-item :label="$t('contract.CheckPrice')" v-if="type == 0">
            <el-input
              v-model="form.win"
              disabled
              autocomplete="off"
              :value="buyMargin"
              :placeholder="
                selectItem.stopwin == ''
                  ? $t('contract.EnterCheckPrice')
                  : selectItem.stopwin
              "
            >
              <template slot="suffix">USDT</template>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('contract.StopPrice')" v-if="type == 0">
            <el-input
              v-model="form.loss"
              autocomplete="off"
              :placeholder="price"
            >
              <template slot="suffix">USDT</template>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('contract.StopPrice')" v-if="type == 1">
            <el-input
              v-model="form.loss"
              autocomplete="off"
              :placeholder="
                type == 1
                  ? selectItem.stopfail == ''
                    ? $t('contract.EnterStopPrice')
                    : selectItem.stopfail
                  : selectItem.stopdonat == ''
                  ? $t('contract.EnterStopPrice')
                  : selectItem.stopdonat
              "
            >
              <template slot="suffix">USDT</template>
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <div style="display: flex; justify-content: center">
            <div class="confirm_button" @click="editConfirm">
              {{ $t('contract.Confirm') }}
            </div>
          </div>
        </div>
      </el-dialog>
    </div>

    <my-footer></my-footer>
  </div>
</template>

<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import TableHeader from './components/table_header'
import QcListItem from './components/qc_list_item'
import ZcListItem from './components/zc_list_item'
import Tag from './components/tag'
import { contractWsUr } from '../../../public/config'

export default {
  components: {
    MyHeader,
    MyFooter,
    TableHeader,
    QcListItem,
    ZcListItem,
    Tag,
  },
  data() {
    return {
      socket: null,
      tabIndex: 0,
      // 0-->全仓 1-->逐仓
      type: 0,
      qcList: [],
      qcHistoryList: [],
      zcList: [],
      zcHistoryList: [],
      dialogFormVisible: false,
      form: {
        win: '',
        loss: '',
      },
      selectItem: {
        coin: '',
      },
      page: 1,
      price: 0,
      price1:0,
    }
  },
  mounted() {
    this.page = 1
    this.initSocket()
    this.$ajax('/account/getMyInfos', {
      token: this.$userID(),
    }).then((res) => {
      if (res.data.status) {
        if (this.$route.name === 'Login') {
          this.$router.push('/')
        }
      } else {
        this.$router.push('/login')
      }
    })
    this.loadHistory()
  },
  beforeDestroy() {
    clearInterval(this.timer)
    this.timer = null
    this.socket.close()
  },
  watch: {
    tabIndex() {
      if (this.tabIndex === 1) {
        this.page = 1
        this.zcHistoryList = []
        this.qcHistoryList = []
        this.loadHistory()
      }
    },
    type() {
      this.page = 1
      this.zcHistoryList = []
      this.qcHistoryList = []
      this.loadHistory()
    },
  },
  computed: {
    // 买保证金
    buyMargin() {
      if (this.form.loss) {
        if (this.form.loss.length > 0) {
          let price
          if (this.selectItem['type'] == 1) {
            price = (
              this.selectItem['buyprice'] -
              this.selectItem['buyprice'] / this.selectItem['gearing'] / 2
            ).toFixed(4)
          } else {
            price = (
              this.selectItem['buyprice'] +
              this.selectItem['buyprice'] / this.selectItem['gearing'] / 2
            ).toFixed(4)
          }
          if (this.selectItem['type'] == 1) {
            if (parseFloat(this.form.loss) > parseFloat(price)) {
              this.$notify({
                title: 'Hotdog 提示:',
                message: '止损不能大于 ' + parseFloat(price),
                type: 'warning',
                duration: 1500,
              })
              this.form.win = this.price1
              return this.price1
            } else {
              this.form.win = (
                this.selectItem['buyprice'] +
                (this.selectItem['buyprice'] - parseFloat(this.form.loss))
              ).toFixed(4)
              return (
                this.selectItem['buyprice'] +
                (this.selectItem['buyprice'] - parseFloat(this.form.loss))
              ).toFixed(4)
            }
          } else {
            if (parseFloat(this.form.loss) < parseFloat(price)) {
              this.$notify({
                title: 'Hotdog 提示:',
                message: '止损不能小于 ' + parseFloat(price),
                type: 'warning',
                duration: 1500,
              })
              this.form.win = this.price1
              return this.price1
            } else {
              this.form.win = (
                this.selectItem['buyprice'] +
                (this.selectItem['buyprice'] - parseFloat(this.form.loss))
              ).toFixed(4)
              return (
                this.selectItem['buyprice'] +
                (this.selectItem['buyprice'] - parseFloat(this.form.loss))
              ).toFixed(4)
            }
          }
        } else {
          return this.price1
        }
      } else {
        return this.price1
      }
    },
    // 卖保证金
    sellMargin() {
      return (this.sellForm.price / this.level) * this.sellForm.num
    },
  },
  methods: {
    // tab选择
    tabTap(index) {
      this.form.win = ''
      this.form.loss = ''
      this.tabIndex = index
    },
    // 全仓/逐仓切换
    changeType(type) {
      this.type = type
    },
    prePage() {
      if (this.page !== 1) {
        this.page = this.page - 1
        this.loadHistory()
      }
    },
    nextPage() {
      this.page = this.page + 1
      this.loadHistory()
    },

    // 初始化socket
    initSocket() {
      let socket = new WebSocket(contractWsUr + this.$userID())
      socket.onopen = () => {
        socket.send(this.$userID() + '_position_')
        this.timer = setInterval(() => {
          socket.send(this.$userID() + '_position_')
        }, 1500)
      }
      socket.onmessage = (e) => {
        const res = JSON.parse(e.data)
        this.qcList = res.qcPosition
        this.zcList = res.zcPosition
      }
      this.socket = socket
    },

    // 加载历史订单
    loadHistory() {
      if (this.type === 0) {
        this.$ajax('/trade/queryContractOrder', {
          token: this.$userID(),
          page: this.page,
          status: 2,
        }).then((res) => {
          if (res.data.status) {
            this.qcHistoryList = [...this.qcHistoryList, ...res.data.data]
          }
        })
      } else {
        this.$ajax('trade/queryZCorder', {
          token: this.$userID(),
          page: this.page,
          status: 2,
        }).then((res) => {
          if (res.data.status) {
            this.zcHistoryList = [...this.zcHistoryList, ...res.data.data]
          }
        })
      }
    },
    // 修改止盈止损（弹窗）
    editTap(item) {
      this.dialogFormVisible = true
      let price,price1;

      if (this.type == 0) {
        if (item['type'] == 1) {
          price = (
            item['buyprice'] -
            item['buyprice'] / item['gearing'] / 2
          ).toFixed(4)
        } else {
          price = (
            item['buyprice'] +
            item['buyprice'] / item['gearing'] / 2
          ).toFixed(4)
        }
        price1 = (item['buyprice'] + (item['buyprice'] - price)).toFixed(4)
        this.price = price
        this.form.win = price1
        this.price1 = price1
      }
      this.selectItem = item
    },
    // 修改确认操作
    editConfirm() {
      let rex = /^[0-9]+(.[0-9]{1,4})?$/
      if (this.form.win == '' || this.form.loss == '') {
        this.$alert('请输入价格', '', 1200)
      } else if (!rex.test(this.form.win) || !rex.test(this.form.loss)) {
        this.$alert('输入价格不正确', '', 1200)
      } else {
        if (this.type === 0) {
          this.handleQcEdit()
        } else {
          this.handleZcEdit()
        }
      }
    },

    // 全仓止盈止损
    handleQcEdit() {
      let price
      if (this.selectItem['type'] == 1) {
        price = (
          this.selectItem['buyprice'] -
          this.selectItem['buyprice'] / this.selectItem['gearing'] / 2
        ).toFixed(4)
      } else {
        price = (
          this.selectItem['buyprice'] +
          this.selectItem['buyprice'] / this.selectItem['gearing'] / 2
        ).toFixed(4)
      }

      if (this.selectItem['type'] == 1) {
        if (this.form.loss > price) {
          this.$message({
            type: 'error',
            message: '止损不能大于 ' + price,
          })
          return
        }
      } else {
        if (this.form.loss < price) {
          this.$message({
            type: 'error',
            message: '止损不能小于 ' + price,
          })
          return
        }
      }
      this.$ajax('/trade/handleEditOrder', {
        token: this.$userID(),
        ordercode: this.selectItem.ordercode,
        stopwin: this.form.win,
        stopdonat: this.form.loss,
      }).then((res) => {
        if (res.data.status) {
          this.$message({
            type: 'success',
            message: res.data.desc,
          })
          this.form = {}
          this.dialogFormVisible = false
        } else {
          this.$message({
            type: 'error',
            message: res.data.desc,
          })
          this.form = {}
        }
      })
    },
    // 逐仓止盈止损
    handleZcEdit() {
      this.$ajax('/trade/handleZCStop', {
        token: this.$userID(),
        ordercode: this.selectItem.ordercode,
        stopwin: this.form.win,
        stopfail: this.form.loss,
      }).then((res) => {
        if (res.data.status) {
          this.$message({
            type: 'success',
            message: res.data.desc,
          })
          this.form = {}
          this.dialogFormVisible = false
        } else {
          this.$message({
            type: 'error',
            message: res.data.desc,
          })
          this.form = {}
        }
      })
    },

    // 平仓
    coverTap(item) {
      const h = this.$createElement
      this.$msgbox({
        title: '提示',
        message: h('p', { style: 'display:flex' }, [
          item.type === 1
            ? h(
                'div',
                {
                  style: ` width: 40px;
                                    height: 20px;
                                    color: #fff;
                                    line-height: 20px;
                                    text-align: center;
                                    background-color: #5bbe8e;`,
                },
                '开多'
              )
            : h(
                'div',
                {
                  style: ` width: 40px;
                                    height: 20px;
                                    color: #fff;
                                    line-height: 20px;
                                    text-align: center;
                                    background-color: #ee6560;`,
                },
                '开空'
              ),
          h('span', { style: 'margin-left:10px' }, item.coinname),
          h('span', null, '是否要平仓？'),
        ]),
        showCancelButton: true,
        confirmButtonText: '一键平仓',
        cancelButtonText: '取消',
      }).then((action) => {
        if (action === 'confirm') {
          // 全仓 一键平仓
          if (this.type === 0) {
            this.$ajax('/trade/handleCloseOrder', {
              token: this.$userID(),
              ordercode: item.ordercode,
            }).then((res) => {
              if (res.data.status) {
                this.$message({
                  type: 'success',
                  message: res.data.desc,
                })
              } else {
                this.$message({
                  type: 'error',
                  message: res.data.desc,
                })
              }
            })
          } else {
            // 逐仓 一键平仓
            this.$ajax('/trade/handleCloseZc', {
              token: this.$userID(),
              ordercode: item.ordercode,
            }).then((res) => {
              if (res.data.status) {
                this.$message({
                  type: 'success',
                  message: res.data.desc,
                })
              } else {
                this.$message({
                  type: 'error',
                  message: res.data.desc,
                })
              }
            })
          }
        }
      })
    },
  },
}
</script>

<style scoped>
.main_container {
  display: flex;
  width: 100%;
  min-height: 700px;
  justify-content: center;
  background-color: #f2f6f9;
}

.main_content {
  width: 1200px;
  min-height: 700px;
  margin-top: 20px;
  margin-bottom: 60px;
}

.title_container {
  width: 1200px;
  height: 60px;
  background-color: #fff;
  display: flex;
  font-size: 18px;
  color: #1c242c;
  align-items: center;
  padding: 0 30px;
  box-sizing: border-box;
  margin-bottom: 10px;
}

.tab_container {
  width: 1200px;
  height: 50px;
  background-color: #fff;
  display: flex;
  padding: 0 30px;
  box-sizing: border-box;
  justify-content: space-between;
  align-items: center;
}

.tab_item {
  cursor: pointer;
  width: 80px;
  color: #1c242c;
  font-size: 16px;
  text-align: center;
  line-height: 44px;
  border-bottom: 3px solid transparent;
  border-top: 3px solid transparent;
  margin-right: 30px;
}

.select_tab {
  border-bottom: 3px solid #196bdf;
  color: #196bdf;
}

.type_button {
  width: 60px;
  height: 26px;
  border: solid 1px #196bdf;
  line-height: 26px;
  color: #196bdf;
  text-align: center;
  margin-left: 20px;
  cursor: pointer;
}

.select_type {
  background-color: #196bdf;
  color: #fff;
}

.confirm_button {
  width: 390px;
  height: 40px;
  background-color: #196bdf;
  font-size: 16px;
  font-weight: normal;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #ffffff;
  text-align: center;
  line-height: 40px;
  cursor: pointer;
}

.dialog_title {
  display: flex;
  align-items: center;
  color: #1c242c;
  font-size: 16px;
}

.page {
  text-align: right;
  padding: 15px 30px;
  background-color: #fff;
}

.page span {
  font-size: 12px;
  color: #1c242c;
  margin-left: 10px;
}

.page .up:hover {
  color: #196bdf;
  cursor: pointer;
}

.page .down:hover {
  color: #196bdf;
  cursor: pointer;
}

.record-container {
  height: 400px;
}

.item_container {
  height: 800px;
  background-color: #fff;
  overflow: auto;
}
</style>
<style>
.dialog_container .el-input__inner {
  width: 390px;
}

.dialog_container .el-dialog__body {
  padding: 30px 40px;
}

.dialog_container .el-dialog__header {
  padding: 20px 40px 0px 40px;
}

.dialog_container .el-form-item__label {
  font-size: 12px;
  line-height: 12px;
  color: #495666;
}
</style>
