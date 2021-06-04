<template>
  <div class="chatPage" :style="setStyle">
    <div class="taleBox" ref="taleNode">
      <chatList :list="taleList" @load="loadDone" :userInfo="userInfo" :tools1="toolConfig1"/>
    </div>
    <div class="toolBox">
      <tools :tools="toolConfig" @enter="showEnter" class="tools" />
      <VEmojiPicker
        :pack="pack"
        @select="selectEmoji"
        v-show="emojiSh"
        class="emoji-picker"
      />
      <EnterBox
        @submit="enter"
        v-model="msg"
        @enter="showEnter"
        :userInfo="userInfo"
      />
    </div>
  </div>
</template>

<script>
import chatList from './chatList'
import EnterBox from './enterBox'
import tools from './tools'
import VEmojiPicker from 'v-emoji-picker'
export default {
  components: {
    chatList,
    EnterBox,
    tools,
    VEmojiPicker,
  },
  props: {
    taleList: {
      type: Array,
      default: () => [],
    },
    scroll: {
      type: Number,
      default: -1,
    },
    height: {
      default: '440',
    },
    width: {
      default: '100%',
    },
    value: {
      default: '',
    },
    toolConfig: {
      type: Object,
      default: () => ({
        show: ['file', 'video', 'img'],
        callback: Function,
        select: Boolean,
      }),
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
      msg: '',
      pack: null,
      emojiSh: false,
    }
  },
  watch: {
    scroll(newVal) {
      if (typeof newVal === 'number') {
        this.setScroll(newVal)
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
  computed: {
    setStyle() {
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
  methods: {
    loadDone(boolean) {
      if (boolean) {
        this.setScroll()
      }
    },
    setScroll(count = this.$refs.taleNode.scrollHeight) {
      //滚动条一直处于下方
      this.$nextTick(() => {
        this.$refs.taleNode.scrollTop = count
      })
    },
    enter(msg) {
      this.$emit('enter', msg)
      this.setScroll()
    },
    showEnter(type) {
      if (type) {
        this.emojiSh = !this.emojiSh
      } else {
        this.emojiSh = false
      }
    },
    selectEmoji(emoji) {
      var elInput = document.getElementById('emojiInput') //根据id选择器选中对象
      var startPos = elInput.selectionStart // input 第0个字符到选中的字符
      var endPos = elInput.selectionEnd // 选中的字符到最后的字符
      if (startPos === undefined || endPos === undefined) return
      var txt = elInput.value
      // 将表情添加到选中的光标位置
      var result =
        txt.substring(0, startPos) + emoji.data + txt.substring(endPos)
      elInput.value = result // 赋值给input的value
      // // 重新定义光标位置
      elInput.focus()
      elInput.selectionStart = startPos + emoji.data.length
      elInput.selectionEnd = startPos + emoji.data.length
      this.msg = result // 赋值给表单中的的字段

      this.showEnter()
    },
  },
}
</script>

<style scoped>
.emoji-picker {
  position: absolute;
  z-index: 9;
  bottom: 10px;
  left: 50px;
}
.chatPage {
  background-image: url(/image/bg.webp);
  margin: 0 auto;
  background: #f3f3f3;
  overflow: hidden;
}
.taleBox {
  height: calc(100% - 160px);
  min-height: 100px;
  padding: 6px;
  overflow: auto;
  overflow-x: hidden;
}
.toolBox {
  height: 148px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}
</style>