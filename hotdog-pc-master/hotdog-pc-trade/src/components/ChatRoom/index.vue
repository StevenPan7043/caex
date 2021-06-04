<template>
  <div>
    <el-button type="success" @click="chatFu" class="chatB"
      ><i class="el-icon-s-comment"></i>聊天室</el-button
    >
    <el-dialog
      :visible.sync="chatVisible"
      :show-close="true"
      title="聊天室"
      :close-on-press-escape="true"
      :close-on-click-modal="true"
      :before-close="handleClose"
      :modal="false"
      v-dialogDrag
      class="dialog-body"
      top="80px"
    >
      <ChatPage
        :taleList="list"
        @enter="bindEnter"
        v-model="inputMsg"
        :toolConfig="tool"
        :toolConfig1="tool1"
        :userInfo="userInfo"
      >
        <div class="rightSlot">
          <div class="rightTatol m-no-select">
            在线人数 <b>{{ tatol }}</b> 人
          </div>
          <div class="userList">
            <ul>
              <li
                v-for="item in allUser"
                :key="item.name ? item.name : item.id"
                :class="userInfo.sendType == 1 ? '' : 'userI'"
                @dblclick="userFu(item)"
              >
                <div
                  class="avatar m-no-select"
                  :style="{ backgroundColor: item.color }"
                >
                  {{ item.name | nickname(1, item.id) }}
                </div>
                <span>{{ item.name | nickname(2, item.id) }}</span>
              </li>
            </ul>
          </div>
        </div>
      </ChatPage>
    </el-dialog>
    <el-dialog
      title="会员管理"
      :visible.sync="dialogVisible"
      :show-close="true"
      class="dialog-body2 m-no-select"
      :before-close="handleClose1"
      :close-on-press-escape="true"
      :close-on-click-modal="true"
    >
      <el-row class="tableRow">
        <el-col :span="6" class="tableCol"> 用户昵称 </el-col>
        <el-col :span="16" class="tableCol">
          {{ Auser.name ? Auser.name : '未设置昵称' }}
        </el-col>
        <el-col :span="6" class="tableCol">用户ID </el-col>
        <el-col :span="16" class="tableCol">
          {{ Auser.id }}
        </el-col>
        <el-col :span="6" class="tableCol"> 用户手机号 </el-col>
        <el-col :span="16" class="tableCol">
          {{ Auser.phone }}
        </el-col>
        <el-col :span="6" class="tableCol"> 用户等级 </el-col>
        <el-col :span="16" class="tableCol">
          <el-input-number
            v-model="level"
            @change="handleChange"
            :min="0"
            :max="5"
          ></el-input-number>
          级
        </el-col>
        <el-col :span="6" class="tableCol">
          <span class="demonstration">禁言</span>
        </el-col>
        <el-col :span="16" class="tableCol">
          <div class="block">
            <el-date-picker
              v-model="value2"
              type="datetime"
              placeholder="选择日期时间"
              align="right"
              :picker-options="pickerOptions"
              :default-value="Date()"
              @change="dateSel"
            >
            </el-date-picker>
          </div>
        </el-col>
        <el-col :span="6" class="tableCol"> 管理员 </el-col>
        <el-col :span="16" class="tableCol">
          <el-select
            v-model="switchValue"
            class="m-no-select"
            placeholder="请选择"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="Cifroge">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import ChatPage from './components/ChatInterface'
import { chooseBackgroundColor, formDate } from '@/libs/utils'
import { ChatRoom } from '../../../public/config'
import { banMember, updateManager, updateInfoById } from '@/api/chatRoom.js'
export default {
  components: {
    ChatPage,
  },
  data() {
    return {
      inputMsg: '',
      list: [],
      tatol: 0,
      tool: {
        show: ['more'],
        callback: this.toolEvent,
      },
      tool1: {
        userId: this.userInfo,
        callback1: this.toolEvent1,
      },
      allUser: [],
      userInfo: {},
      toolConfig: null,
      toolConfig1: null,
      websock: null,
      fullscreenSh: true,
      chatVisible: false,
      lockReconnect: false, //是否真正建立连接
      timeout: 89 * 1000, //89秒一次心跳
      timeoutnum: null, //断开 重连倒计时
      timers: null,
      token: '',
      dialogVisible: false,
      dateDis: '',
      pickerOptions: {
        shortcuts: [
          {
            text: '解除禁言',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setMinutes(date.getMinutes() - 1))
              picker.$emit('pick', date)
            },
          },
          {
            text: '永久禁言',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setFullYear(date.getFullYear() + 50))
              picker.$emit('pick', date)
            },
          },
          {
            text: '3分钟',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setMinutes(date.getMinutes() + 3))
              picker.$emit('pick', date)
            },
          },
          {
            text: '5分钟',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setMinutes(date.getMinutes() + 5))
              picker.$emit('pick', date)
            },
          },
          {
            text: '10分钟',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setMinutes(date.getMinutes() + 10))
              picker.$emit('pick', date)
            },
          },
          {
            text: '20分钟',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setMinutes(date.getMinutes() + 20))
              picker.$emit('pick', date)
            },
          },
          {
            text: '30分钟',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setMinutes(date.getMinutes() + 30))
              picker.$emit('pick', date)
            },
          },
          {
            text: '40分钟',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setMinutes(date.getMinutes() + 40))
              picker.$emit('pick', date)
            },
          },
          {
            text: '50分钟',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.setMinutes(date.getMinutes() + 50))
              picker.$emit('pick', date)
            },
          },
          {
            text: '一天',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24)
              picker.$emit('pick', date)
            },
          },
          {
            text: '一周',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
              picker.$emit('pick', date)
            },
          },
          {
            text: '二周',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 14)
              picker.$emit('pick', date)
            },
          },
          {
            text: '三周',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 21)
              picker.$emit('pick', date)
            },
          },
        ],
        disabledDate(time) {
          return time.getTime() <= Date.now() - 8.64e7
        },
      },
      value2: '',
      Auser: {},
      level: 0,
      switchValue: '', //0 管理员 1 会员 2 超级管理员
      options: [
        { value: 1, label: '会员' },
        { value: 0, label: '管理员' },
        { value: 2, label: '超级管理员' },
      ],
    }
  },
  filters: {
    nickname(val, index, id) {
      if (index == 2) {
        if (val) {
          return val
        }
        return id
      } else {
        if (val instanceof Number) {
          return val
        } else {
          if (val) {
            return val[0].toUpperCase()
          }
          return id.toString().slice(-2)
        }
      }
    },
  },
  destroyed() {
    this.websock.close() //离开路由之后断开websocket连接
    clearInterval(this.timers)
  },
  methods: {
    ///聊天 start
    dateSel(el) {
      const date = new Date(el)
      const date1 = new Date()
      if (date.getFullYear() > date1.getFullYear() + 49) {
        this.dateDis = '-1'
      } else {
        this.dateDis = ''
      }
    },
    Cifroge() {
      //会员禁言
      let date = ''
      if (this.value2) {
        if (this.dateDis == '') {
          // 当前时间戳
          var time1 = new Date()
          //禁言时间戳
          let time2 = this.value2.getTime()
          //两个时间相差的秒数
          date =
            Math.round(parseInt(time2 - time1) / 1000) < 0
              ? 0
              : Math.round(parseInt(time2 - time1) / 1000)
        } else {
          date = this.dateDis
        }
        let params = {
          id: this.Auser.id,
          banTime: date,
        }
        banMember(params).then((res) => {
          if (res.status == 1) {
            this.dialogVisible = false
            this.$notify({
              title: '成功',
              message: res.desc,
              type: 'success',
            })
          } else {
            this.$notify.error({
              title: '错误',
              message: res.desc,
            })
          }
        })
      }
      //修改会员的等级
      if (
        this.level.toString() != '' &&
        this.level.toString() != this.Auser.level.toString()
      ) {
        console.log(this.level, this.Auser, '修改会员的等级')
        let params = {
          id: this.Auser.id,
          level: this.level,
        }
        updateInfoById(params).then((res) => {
          if (res.status == 1) {
            this.dialogVisible = false
            this.$notify({
              title: '成功',
              message: res.desc,
              type: 'success',
            })
          } else {
            this.level = this.Auser.level
            this.$notify.error({
              title: '错误',
              message: res.desc,
            })
          }
        })
      } else {
        this.level = this.Auser.level
      }
      // 编辑管理员
      if (this.switchValue.toString() != '') {
        let params = {
          id: this.Auser.id,
          type: this.switchValue,
        }
        updateManager(params).then((res) => {
          if (res.status == 1) {
            this.dialogVisible = false
            this.$notify({
              title: '成功',
              message: res.desc,
              type: 'success',
            })
          } else {
            this.switchValue = this.Auser.type
            this.$notify.error({
              title: '错误',
              message: res.desc,
            })
          }
        })
      } else {
        this.switchValue = this.Auser.type
      }
    },
    userFu(el) {
      if (this.userInfo.sendType == 1) return
      this.Auser = el
      this.switchValue = el.type
      this.dialogVisible = true
    },
    bindEnter() {
      const msg = this.inputMsg
      let useruid = localStorage.getItem('useruid')
      let nickname = localStorage.getItem('nickname')
      if (!msg) return
      const msgObj = {
        type: 0x02,
        sendId: useruid,
        message: msg,
        token: this.token,
      }
      this.websocketsend(JSON.stringify(msgObj))
    },
    toolEvent1(msgId) {
      let useruid = localStorage.getItem('useruid')
      const msgObj = {
        type: 0x04,
        sendId: useruid,
        id: msgId,
        token: this.token,
      }
      this.websocketsend(JSON.stringify(msgObj))
    },
    toolEvent(type) {
      switch (type) {
        case 'more':
          // this.isShowEmojiPanel = !this.isShowEmojiPanel
          break
      }
    },
    chatFu() {
      this.chatVisible = true
      if (
        localStorage.getItem('loginState') &&
        localStorage.getItem('contactToken') &&
        localStorage.getItem('token')
      ) {
        this.initWebSocket()
      }
    },
    handleClose() {
      this.chatVisible = false
      this.websock.close() //离开路由之后断开websocket连接
      clearInterval(this.timers)
    },
    handleClose1() {
      this.dialogVisible = false
    },
    handleChange(el) {
      console.log(el)
    },
    initWebSocket() {
      //初始化weosocket
      const wsuri = ChatRoom
      this.websock = new WebSocket(wsuri)
      this.websock.onmessage = this.websocketonmessage
      this.websock.onopen = this.websocketonopen
      this.websock.onerror = this.websocketonerror
      this.websock.onclose = this.websocketclose
    },
    websocketonopen() {
      //连接建立之后执行send方法发送数据
      // console.log('聊天连接成功')
      let useruid = localStorage.getItem('useruid')
      let actions = { type: 0x01, sendId: useruid, client: 'WEB' }
      this.websocketsend(JSON.stringify(actions))
      ///定时发送心跳
      this.timer()
    },
    websocketonerror() {
      //连接建立失败重连
      this.reconnect()
      //   console.log('聊天重连..')
    },
    websocketonmessage(e) {
      let useruid = localStorage.getItem('useruid')
      //数据接收
      const redata = JSON.parse(e.data)
      if (redata.status == 1) {
        // console.log('聊天数据..', redata)
        if (redata.data.type == 2) {
          redata.data['mine'] = redata.data.sendId == useruid ? true : false
          redata.data['color'] = chooseBackgroundColor(redata.data.sendId)
          this.list.push(redata.data)
        } else if (redata.data.type == 1 && redata.data.id == useruid) {
          redata.data.list.forEach((e) => {
            e['color'] = chooseBackgroundColor(e.id)
          })
          this.tatol = redata.data.list.length
          this.allUser = redata.data.list.sort(this.sortNumber)
          redata.data.messageList.forEach((e) => {
            e['color'] = chooseBackgroundColor(e.sendId)
            e['mine'] = e.sendId == useruid ? true : false
          })
          this.list = redata.data.messageList.reverse()
          if (delete redata.data.list && delete redata.data.messageList) {
            this.userInfo = redata.data
            this.level = redata.data['level']
            this.token = redata.data['token']
            // this.switchValue = redata.data['sendType']
            localStorage.setItem('chatRoom_Token', redata.data['token'])
            this.userInfo['banTime'] = formDate(redata.data.banTime)
            this.userInfo['color'] = chooseBackgroundColor(redata.data.id)
          }
        } else if (redata.data.type == 1) {
          //用户上线
          const user = {
            name: redata.data['sendName'],
            id: redata.data['id'],
            level: redata.data['level'],
            phone: redata.data['sendPhone'],
            color: chooseBackgroundColor(redata.data.id),
          }
          let data = this.allUser
          if (data.findIndex((e) => e.id === user.id) == -1) {
            data.push(user)
          }
          this.tatol = data.length
          this.allUser = data.sort(this.sortNumber)
          // console.log(redata.data.sendName + '用户上线了')
        } else if (redata.data.type == 0) {
          //用户下线
          let data = this.allUser
          data.forEach((el) => {
            if (redata.data.sendId == el.id) {
              data.splice(
                data.findIndex((e) => e.id === redata.data.sendId),
                1
              )
            }
          })
          this.tatol = data.length
          this.allUser = data.sort(this.sortNumber)
          // console.log(redata.data.sendName + '用户下线了')
        } else if (redata.data.type == 4) {
          //撤回消息
          if (redata.data.id == useruid) {
            let index = this.list.findIndex((e) => e.id === redata.data.id)
            if (index > -1) {
              this.$notify({
                title: '提示',
                message: '消息撤销成功!',
                type: 'success',
              })
              this.list.splice(index, 1)
            }
          }
        } else if (redata.data.type == 5) {
          //撤回消息
          this.userInfo.sendType = redata.data.sendType
        }
      } else if (redata.status == 6 && redata.data.id == useruid) {
        //用户解禁
        // console.log(redata.data, '解禁用户')
        this.userInfo = redata.data
        this.userInfo['banTime'] = formDate(redata.data.banTime)
      } else if (redata.status == 5 && redata.data.id == useruid) {
        //用户解禁
        // console.log(redata.data, '禁用用户')
        this.userInfo = redata.data
        this.userInfo['banTime'] = formDate(redata.data.banTime)
      } else if (redata.status == 0 && redata.data.id == useruid) {
        this.$notify({
          title: '提示',
          message: redata.desc,
          type: 'error',
        })
      }
    },
    websocketsend(Data) {
      //数据发送
      this.websock.send(Data)
    },
    websocketclose(e) {
      clearInterval(this.timers)
      //关闭
      //   console.log('聊天断开连接', e)
    },

    reconnect() {
      //重新连接
      var that = this
      clearInterval(this.timers)
      if (that.lockReconnect) {
        return
      }
      that.lockReconnect = true
      //没连接上会一直重连，设置延迟避免请求过多
      that.timeoutnum && clearTimeout(that.timeoutnum)
      that.timeoutnum = setTimeout(function () {
        //新连接
        that.initWebSocket()
        that.lockReconnect = false
      }, 5000)
    },
    ///循环发送心跳
    timer() {
      var self = this
      self.timers = setInterval(() => {
        if (self.websock.readyState == 1) {
          //如果连接正常
          let actions = { type: 0x03 }
          self.websocketsend(JSON.stringify(actions))
        } else {
          //否则重连
          self.reconnect()
        }
      }, self.timeout)
    },
    ///聊天 end
    sortNumber(a, b) {
      return b.level - a.level
    },
  },
}
</script>

<style>
.el-dialog {
  min-width: 500px;
}
.el-dialog__body {
  padding: 5px 5px;
}
.dialog-body2 {
  min-width: 500px;
}
.el-dialog__headerbtn {
  font-size: 25px;
}
</style>

<style scoped>
/* 聊天 */
.tableCol {
  padding: 10px 10px;
}
.dialog-footer {
  margin-top: 20px;
}
.avatar {
  display: inline-block;
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 50%;
  color: white;
  font-weight: 400;
  line-height: 30px;
  margin-right: 8px;
}
.el-icon-s-comment {
  font-size: 20px;
}
.rightSlot {
  width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  /* justify-content: center; */
}
.notice {
  height: 30%;
  text-align: left;
  padding-left: 0.2rem;
}
.rightTatol {
  height: 35px;
  line-height: 35px;
  font-weight: 400;
  padding: 0 10px;
}
.rightTatol b {
  font-weight: bold;
  color: #409eff;
}
.userList {
  height: 100%;
  overflow: hidden;
  overflow-y: auto;
  text-align: left;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}
.userList li {
  height: 35px;
  line-height: 35px;
  padding: 5px;
}
.userList li.userI {
  cursor: pointer;
}
img {
  width: 25px;
  margin-right: 0.3rem;
  vertical-align: middle;
  border-radius: 50%;
}

.chatB {
  width: 90px;
  height: 40px;
  padding: 0px;
  margin: 10px 20px;
  position: fixed;
  bottom: 0px;
  overflow: visible;
  opacity: 1;
  border: 0px;
  z-index: 1000;
  transition-duration: 250ms;
  transition-timing-function: cubic-bezier(0.645, 0.045, 0.355, 1);
  transition-property: opacity, top, bottom;
  right: 0px;
}
/* 聊天 end */
</style>