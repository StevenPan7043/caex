<template>
  <div class="web__msg" @keyup.enter="handleSend">
    <el-input
      type="textarea"
      v-model.trim="currentMsg"
      :rows="3"
      autofocus
      :placeholder="
        userInfo.isBan == 1
          ? '您已被禁言,禁言时间至: ' + userInfo.banTime
          : placeholder
      "
      class="web__msg-input"
      resize="none"
      :disabled="userInfo.isBan == 1"
      id="emojiInput"
      @focus="inputFocus"
    ></el-input>
    <div class="web__msg-menu">
      <el-button
        class="web__msg-submit"
        type="primary"
        size="mini"
        @click="handleSend"
        :disabled="userInfo.isBan == 1"
        >发送</el-button
      >
    </div>
  </div>
</template>

<script>
export default {
  name: 'JwChat_enterbox',
  props: {
    placeholder: {
      type: String,
      default: '请输入...',
    },
    value: {
      default: '',
    },
    userInfo: {
      type: Object,
    },
  },
  data() {
    return {
      visible: false,
      currentMsg: this.msg,
    }
  },
  watch: {
    value: {
      handler() {
        this.currentMsg = this.value
      },
      immediate: true,
    },
    currentMsg: {
      handler(newval) {
        if (newval) {
          const msg = newval.trim()
          this.$emit('input', msg)
        }
      },
      immediate: true,
    },
  },
  methods: {
    //用户主动发送
    handleSend() {
      this.$emit('submit', this.currentMsg)
      this.$nextTick(() => {
        this.currentMsg = ''
      })
    },
    inputFocus(val) {
      this.$emit('enter', false)
    },
  },
}
</script>
<style scoped>
.web__msg {
  padding: 10px 10px 0;
  height: auto;
  overflow: hidden;
}
.web__msg--img,
.web__msg--video,
.web__msg--file {
  position: relative;
  max-width: 250px;
  min-width: 200px;
  width: 100%;
  margin: 10px 0;
  border: 1px solid #eee;
  overflow: hidden;
  border-radius: 5px;
  cursor: pointer;
  display: block;
}

.web__msg .web__msg {
  height: 220px;
  background-color: #fff;
}
.web__msg .web__msg span {
  box-sizing: border-box;
  padding: 3px 5px;
  color: #333;
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: center;
  height: calc(100% - 80px);
  overflow: hidden;
  font-size: 13px;
  text-align: center;
}

.web__msg .web__msg h2 {
  margin: 0;
  width: 100%;
  text-align: center;
  line-height: 80px;
  background-color: #409eff;
  color: #fff;
}

.web__msg--map {
  height: 220px;
}

.web__msg-input {
  display: block;
  width: 100%;
  height: 75px;
  overflow-x: hidden;
  overflow-y: auto;
  box-sizing: border-box;
  resize: none;
  outline: 0;
  background-color: #F3F3F3;
  border: 0;
  word-break: break-all;
  font-size: 13px;
  line-height: 17px;
  -webkit-appearance: none;
}
.web__msg-menu {
  text-align: right;
}
.web__msg-input textarea {
  border: 0 !important;
}
.web__msg-submit {
  display: inline-block;
  outline: none;
  cursor: pointer;
  text-align: center;
}
</style>
<style>
.el-textarea__inner{
  background-color: #F3F3F3;
  border: solid 1px #e0e0e0;
}
</style>
