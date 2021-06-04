<template>
  <div>
    <div class="web__tools">
      <!-- <i
        v-for="item in tools.show"
        :key="item"
        :class="toolConfig[item]"
        @click="bindButton(item)"
        >{{ toolConfig[item] ? '' : item }}</i
      > -->
      <img
        src="@/assets/images/emoji.png"
        @click="bindButton('more')"
        :class="tools.select ? 'emoji select' : 'emoji'"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'JwChat_tools',
  props: {
    tools: {
      type: Object,
      default: () => {
        return {
          show: Object.keys(this.toolConfig),
          callback: Function,
          select: Boolean,
        }
      },
    },
  },
  data() {
    return {
      toolConfig: {
        img: 'el-icon-picture-outline',
        video: 'el-icon-video-camera',
        file: 'el-icon-folder-opened',
        more: 'el-icon-more',
      },
      // select: false,
    }
  },
  methods: {
    bindButton(type) {
      this.$emit('enter', type)
      // this.select = !this.select
      this.tools.callback && this.tools.callback(type)
    },
  },
}
</script>
<style>
.container-emoji {
  height: 160px !important;
}
</style>
<style  scoped>
.web__tools {
  text-align: left;
  padding-left: 8px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  height: 30px;
}
i {
  margin-right: 12px;
  font-size: 20px;
  color: #888a91;
}
i:hover {
  color: #76b1f9;
}
.emoji {
  width: 30px;
  cursor: pointer;
  transition: 0.5s;
  border-radius: 50%;
}
.emoji.select {
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
}
</style>