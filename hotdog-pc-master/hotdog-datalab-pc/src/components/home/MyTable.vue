<template>
  <div class="wrap">
    <div class="content-table">
      <table class="my-table">
        <thead class="t-head">
            <tr class="h-tr">
              <th
                v-for="item in initTable"
                :key="item.prop"
                :style="{ textAlign:item.pos }"
              >{{ item.name }}</th>
            </tr>
        </thead>
        <tbody class="t-body">
          <div class="box" v-for="(item,index) in tableData" :key="index">
            <tr class="b-tr">
              <td v-for="ele in initTable" :key="ele.prop" :style="{ textAlign:ele.pos }">
                <span v-if="showTip.indexOf(ele.prop) < 0">{{ item[ele.prop] }}</span>
                <el-tooltip :content="item[ele.prop]" placement="top" v-else>
                  <span>{{ item[ele.prop] }}</span>
                </el-tooltip>
              </td>
            </tr>
          </div>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script>
export default {
  props: ["initTable", "tableData"],
  data() {
    return {
      showTip: ["r_address", "member_coin_addr"]
    };
  }
};
</script>

<style lang="scss" scoped>
@import "~/css/home/myTable.scss";
</style>