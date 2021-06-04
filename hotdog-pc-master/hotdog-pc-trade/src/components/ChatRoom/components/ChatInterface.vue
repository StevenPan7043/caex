<template>
  <div class="ChatPage" :style="faceSize">
    <div class="header">
      <div class="header-left">
        <img v-if="config.img" :src="config.img" class="header_cover  m-no-select" alt />
        <div class="header-info">
          <p class="web__logo-name">{{ config.name }}</p>
          <p class="web__logo-dept">{{ config.dept }}</p>
        </div>
        <slot name="header"></slot>
      </div>
      <div class="userbutton" @click="handleClose">
        <i class="el-icon-user-solid"></i>
      </div>
    </div>
    <div class="main">
      <Chat
        :taleList="taleList"
        @enter="enter"
        v-model="msg"
        :toolConfig="toolConfig"
        :toolConfig1="toolConfig1"
        :userInfo="userInfo"
      />
      <div class="rightBox" v-show="drawerf">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
import Chat from './chat/index.vue'
export default {
  components: {
    Chat,
  },
  props: {
    config: {
      type: Object,
      default: () => ({
        img: require('@/assets/images/dark_log.png'),
        name: 'Hotdog',
        dept: '快速交易,随时随地交流',
      }),
    },
    taleList: {
      type: Array,
      default: () => {
        return []
      },
    },
    height: {
      type: String,
      default: '500',
    },
    width: {
      default: '100%',
    },
    value: {
      default: '',
    },
    toolConfig: {
      type: Object,
    },
    toolConfig1: {
      type: Object,
    },
    userInfo: {
      type: Object,
    },
  },
  data() {
    return {
      chatHeight: '',
      chatWidth: '',
      msg: '',
      drawerf: false,
    }
  },
  computed: {
    faceSize() {
      let height = this.height
      let width = this.width
      if (height.match(/\d$/)) {
        height += 'px'
      }
      if (width.match(/\d$/)) {
        width += 'px'
      }
      const style = { height, width }
      return style
    },
  },
  watch: {
    height(newval) {
      if (newval) {
        this.chatHeight = newval - 90
      }
    },
    width(newval) {
      if (newval) {
        this.chatWidth = newval * 0.8
      }
    },
    value: {
      handler() {
        this.msg = this.value
      },
      immediate: true,
    },
    msg: {
      handler() {
        this.$emit('input', this.msg)
      },
      immediate: true,
    },
  },
  methods: {
    enter(msg) {
      this.$emit('enter', msg)
    },
    handleClose() {
      this.drawerf = !this.drawerf
    },
  },
}
</script>

<style  scoped>
.chatBox {
  width: 65%;
}
.header-left {
  display: flex;
  align-items: center;
  height: 60px;
}
.userbutton {
  float: right;
  height: 30px;
  width: 30px;
  line-height: 30px;
  cursor: pointer;
  color: white;
  text-align: center;
  position: absolute;
  right: 20px;
}
.userbutton i{
  font-size: 25px;
}
.ChatPage {
  margin: 0 auto;
  background: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.ChatPage .header {
  background-color: #409eff;
  display: flex;
  margin: 0 auto;
  align-items: center;
  height: 60px;
  position: relative;
}
.ChatPage .header .header_cover {
  border-radius: 50%;
  width: 35px;
  height: 35px;
  margin: 0 12px;
  box-shadow: 0 3px 3px 0 rgba(0, 0, 0, 0.1);
}
.ChatPage .header .header-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.ChatPage .header .header-info p {
  margin: 0;
  color: #fff;
  margin: 0;
  padding: 0;
  width: 175px;
  text-overflow: ellipsis;
  overflow: hidden;
  text-align: left;
  white-space: nowrap;
  font-size: 14px;
}

.ChatPage .header .header-info p:last-child {
  margin-top: 5px;
  font-size: 12px;
}

.ChatPage .main {
  display: flex;
  height: calc(100% - 60px);
}
.ChatPage .main .rightBox {
  box-shadow: 0 -3px 3px 0 rgba(0, 0, 0, 0.1);
  width: 35%;
  overflow: auto;
  background-color: #f3f3f3;
}
</style>
