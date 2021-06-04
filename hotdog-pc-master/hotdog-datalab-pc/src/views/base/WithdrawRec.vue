<template>
  <div class="withdraw-rec">
    <div class="rec-wrap">
      <router-title desc="提现记录" />
      <div class="table-box">
        <shadow-box>
          <div class="rec-type">
            <ul class="item-wrap">
              <li class="item" :class="[ curIndex == 1 ? 'active' :'']" @click="coinRec(1)">
                <span>{{ customCoin }}提现记录</span>
              </li>
              <li class="item" :class="[ curIndex == 2 ? 'active' :'']" @click="coinRec(2)">
                <span>{{ baseCoin }}提现记录</span>
              </li>
            </ul>
          </div>
        </shadow-box>
        <div class="table-content">
          <my-table :initTable="initTable" :tableData="tableData" />
        </div>
        <div class="pagination">
          <pagination
            v-if="totalPages != 0"
            :total="totalPages"
            :pageSizes="pageSizes"
            @getCurPage="CurPage"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import routerTitle from "@/components/withdraw/RouterTitle.vue";
import shadowBox from "@/components/home/ShadowBox.vue";
import myTable from "@/components/home/MyTable.vue";
import pagination from "@/components/home/Pagination.vue";
import { mapActions, mapState } from "vuex";
import { vuexStatus } from "@/mixin/vuexStatus"
import { sameMethod } from "@/mixin/sameMethod"
export default {
  mixins:[vuexStatus,sameMethod],
  created() {
    this.changeWithdraw({
      page: this.pages,
      source: "DATALAB",
      pagesize: this.pageSizes,
      currency: this.customCoin
    });
  },
  computed: {
    ...mapState("withDrawRec", ["totalPages", "withDrawData"]),
    customCoin() {
      return this._coinName.split("/")[0];
    },
    baseCoin() {
      return this._coinName.split("/")[1];
    },
    tableData() {
      if (this.withDrawData) {
        this.filterStatus(this.withDrawData);
        return this.withDrawData;
      }
      return "";
    }
  },
  watch: {
    _coinName: function() {
      this.changeWithdraw({
        page: this.pages,
        source: "DATALAB",
        pagesize: this.pageSizes,
        currency: this.customCoin
      });
    }
  },
  methods: {
    ...mapActions("withDrawRec", ["changeWithdraw"]),
    CurPage(val) {
      this.pages = val;
      this.changeWithdraw({
        page: this.pages,
        source: "DATALAB",
        pagesize: this.pageSizes,
        currency: this.customCoin
      });
    },
    coinRec(val) {
      this.curIndex = val;
      if (this.curIndex == 1) {
        this.changeWithdraw({
          page: this.pages,
          source: "DATALAB",
          pagesize: this.pageSizes,
          currency: this.customCoin
        });
      } else if (this.curIndex == 2) {
        this.changeWithdraw({
          page: this.pages,
          source: "DATALAB",
          pagesize: this.pageSizes,
          currency: this.baseCoin
        });
      }
    }
  },

  data() {
    return {
      pageSizes: 20,
      pages: 1,
      curIndex: 1,
      initTable: [
        {
          name: "时间",
          prop: "r_create_time",
          pos: "left"
        },
        {
          name: "币种",
          prop: "currency",
          pos: "left"
        },
        {
          name: "类型",
          prop: "tType",
          pos: "center"
        },
        {
          name: "数量",
          prop: "r_amount",
          pos: "left"
        },
        {
          name: "提现地址",
          prop: "r_address",
          pos: "left"
        },
        {
          name: "状态",
          prop: "r_status",
          pos: "right"
        }
      ],
    };
  },

  components: {
    routerTitle,
    shadowBox,
    myTable,
    pagination
  }
};
</script>
<style lang="scss" scoped>
@import "~/css/withdrawRec/index.scss";
</style>