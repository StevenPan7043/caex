<template>
  <div class="main_container">
    <div class="title">
      <div
        :class="tabIndex === 0 ? 'select_tab_button' : 'un_select_tab_button'"
        @click="changeTabIndex(0)"
      >
        {{ $t('contract.AllWarehouseOrders') }}
      </div>
      <div
        :class="tabIndex === 1 ? 'select_tab_button' : 'un_select_tab_button'"
        @click="changeTabIndex(1)"
      >
        {{ $t('contract.EachWarehouseOrders') }}
      </div>
      <div
        :class="tabIndex === 2 ? 'select_tab_button' : 'un_select_tab_button'"
        @click="changeTabIndex(2)"
      >
        {{ $t('contract.EntrustTheOrders') }}
      </div>
    </div>
    <div>
      <div v-if="tabIndex === 0">
        <qc_table_header :symbol="symbol" />
        <div class="table_container">
          <qc_list_item
            v-for="(item, index) in qcList"
            :key="index"
            :item="item"
          >
            <div class="last">
              <div class="button_container" @click="editTap(item)">
                {{ $t('contract.Stop') }}
              </div>
              <div class="button_container" @click="coverTap(item)">
                {{ $t('contract.Positions') }}
              </div>
            </div>
          </qc_list_item>
        </div>
      </div>

      <div v-else-if="tabIndex === 1">
        <zc_list_header />
        <div class="table_container">
          <zc_list_item
            v-for="(item, index) in zcList"
            :key="index"
            :item="item"
          >
            <div class="last">
              <div class="button_container" @click="editTap(item)">
                {{ $t('contract.Stop') }}
              </div>
              <div class="button_container" @click="coverTap(item)">
                {{ $t('contract.Positions') }}
              </div>
            </div>
          </zc_list_item>
        </div>
      </div>

      <div v-else>
        <wt_table_header :symbol="symbol" />
        <div class="record-container">
          <div class="table_container">
            <wt_list_item
              v-for="(item, index) in wtList"
              :key="index"
              :item="item"
            >
              <p class="cancel_button" @click="cancelDelegate(item)">
                {{ $t('contract.Cancel') }}
              </p>
            </wt_list_item>
          </div>
        </div>

        <div class="page">
          <span class="up" @click="prePage">{{ $t('contract.Pre') }}</span>
          <span class="down" @click="nextPage">{{ $t('contract.Next') }}</span>
        </div>
      </div>
    </div>
    <div class="dialog_container">
      <el-dialog
        :visible.sync="dialogFormVisible"
        width="470px"
        :modal="false"
        style="opacity: 0.9"
      >
        <div slot="title">
          <div class="dialog_title">
            <Tag :is-kai-duo="selectItem.type === 1" />
            {{ selectItem.coin.toUpperCase().replace('_', '/') }}
          </div>
        </div>
        <el-form :model="form" label-position="top">
          <el-form-item :label="$t('contract.CheckPrice')" v-if="tabIndex == 1">
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
          <el-form-item :label="$t('contract.CheckPrice')" v-if="tabIndex == 0">
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
          <el-form-item :label="$t('contract.StopPrice')" v-if="tabIndex == 0">
            <el-input
              v-model="form.loss"
              autocomplete="off"
              :placeholder="price"
            >
              <template slot="suffix">USDT</template>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('contract.StopPrice')" v-if="tabIndex == 1">
            <el-input
              v-model="form.loss"
              autocomplete="off"
              :placeholder="
                tabIndex == 1
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
  </div>
</template>

<script>
import qc_table_header from './qc_table_header'
import qc_list_item from './qc_list_item'
import zc_list_header from './zc_list_header'
import zc_list_item from './zc_list_item'
import wt_table_header from './wt_table_header'
import wt_list_item from './wt_list_item'
import Tag from '../../contractOrder/components/tag'

export default {
  name: 'order',
  components: {
    qc_table_header,
    qc_list_item,
    zc_list_header,
    zc_list_item,
    wt_table_header,
    wt_list_item,
    Tag,
  },

  watch: {
    tabIndex() {
      this.form = {}
      if (this.tabIndex === 2) {
        this.emitReloadDelegate()
      }
    },
  },
  props: ['qcList', 'zcList', 'wtList', 'symbol'],
  data() {
    return {
      tabIndex: 0,
      dialogFormVisible: false,
      selectItem: {
        coin: '',
      },
      form: {},
      price: 0,
      price1: 0,
    }
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
    emitReloadDelegate() {
      this.$emit('reloadDelegate')
    },
    changeTabIndex(index) {
      this.tabIndex = index
    },

    // 取消委托订单
    cancelDelegate(item) {
      const coin = item.coin.toUpperCase().replace('_', '/')
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
          h('span', { style: 'margin: 0 10px' }, coin),
          h('span', null, '是否取消委托单？'),
        ]),
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(() => {
        this.$ajax('/trade/handleCancelEntrust', {
          token: this.$userID(),
          ordercode: item.ordercode,
        }).then((res) => {
          if (res.data.status) {
            this.$message({
              type: 'success',
              message: res.data.desc,
            })
            this.emitReloadDelegate()
            this.emitReloadAssets()
          } else {
            this.$message({
              type: 'error',
              message: res.data.desc,
            })
          }
        })
      })
    },

    getData() {
      this.$ajax('/trade/queryMyEntrust', {
        token: this.$userID(),
        status: 1,
        page: this.page,
      }).then((res) => {
        if (res.data.status) {
          this.wtList = [...this.wtList, ...res.data.data]
        }
      })
    },
    prePage() {
      if (this.page !== 1) {
        this.page = this.page - 1
        this.getData()
      }
    },
    nextPage() {
      this.page = this.page + 1
      this.getData()
    },
    // 修改止盈止损（弹窗）
    editTap(item) {
      this.dialogFormVisible = true
      let price, price1
      if (this.tabIndex == 0) {
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
        this.form.win = price1
        this.price1 = price1
        this.price = price
      }
      this.selectItem = item
    },
    lossTextChange(val) {
      console.log(val)
    },
    // 修改确认操作
    editConfirm() {
      let rex = /^[0-9]+(.[0-9]{1,4})?$/
      console.log(this.form.win, this.form.loss)
      if (this.form.win == '' || this.form.loss == '') {
        this.$alert('请输入价格', '', 1200)
      } else if (!rex.test(this.form.win) || !rex.test(this.form.loss)) {
        this.$alert('输入价格不正确', '', 1200)
      } else {
        if (this.tabIndex === 0) {
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
          if (this.tabIndex === 0) {
            this.$ajax('/trade/handleCloseOrder', {
              token: this.$userID(),
              ordercode: item.ordercode,
            }).then((res) => {
              if (res.data.status) {
                this.$message({
                  type: 'success',
                  message: res.data.desc,
                })
                this.emitReloadAssets()
              } else {
                this.$message({
                  type: 'error',
                  message: res.data.desc,
                })
              }
            })
          } else if (this.tabIndex === 1) {
            this.$ajax('/trade/handleCloseZc', {
              token: this.$userID(),
              ordercode: item.ordercode,
            }).then((res) => {
              if (res.data.status) {
                this.$message({
                  type: 'success',
                  message: res.data.desc,
                })
                this.emitReloadAssets()
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
    emitReloadAssets() {
      this.$emit('reloadAssets')
    },
  },
}
</script>

<style scoped>
.main_container {
  height: 100%;
  width: 100%;
  background-color: #fff;
}

.title {
  height: 45px;
  width: 100%;
  background-color: #f2f6fa;
  display: flex;
  align-items: center;
  padding: 0 20px;
  box-sizing: border-box;
}

.select_tab_button {
  padding: 0 10px;
  height: 24px;
  background-color: #196bdf;
  border-radius: 2px;
  font-size: 14px;
  color: #ffffff;
  text-align: center;
  line-height: 24px;
  margin-right: 25px;
  cursor: pointer;
}

.un_select_tab_button {
  padding: 0 10px;
  height: 24px;
  background-color: transparent;
  border-radius: 2px;
  font-size: 14px;
  color: #25425a;
  text-align: center;
  line-height: 24px;
  margin-right: 25px;
  cursor: pointer;
}

.cancel_button {
  width: 38px;
  height: 20px;
  border-radius: 2px;
  border: solid 1px #196bdf;
  font-size: 12px;
  color: #196bdf;
  text-align: center;
  line-height: 20px;
  cursor: pointer;
}

.cancel_button:hover {
  background-color: #196bdf;
  color: #fff;
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
  min-height: 320px;
}

.table_container {
  height: 360px;
  overflow: auto;
}
</style>
