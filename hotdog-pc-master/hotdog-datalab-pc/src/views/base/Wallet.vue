<template>
  <div class="wallet">
    <div class="wallet-box">
      <title-box :title="titleMsg" />
      <div class="wallet-contain">
        <shadow-box>
          <div class="withdraw-box">
            <div class="rest-item">
              <!-- <rest-money /> -->
              <rest-money
                :coin="customCoin"
                :rest="before.availableBalance"
                :cny="before.availableFb"
                :limit="baseWQuota"
              />
            </div>
            <div class="rest-item">
              <!-- <rest-money /> -->
              <rest-money
                :coin="baseCoin"
                :rest="behind.availableBalance"
                :cny="behind.availableFb"
                :limit="valuationWQuota"
              />
            </div>
            <div class="rec-withdraw">
              <router-link to="withdrawRec" class="withdraw-btn">提现记录</router-link>
            </div>
          </div>
        </shadow-box>
        <div class="table-box">
          <my-table :initTable="initTable" :tableData="tableData" />
        </div>
        <div class="pagination">
          <!-- <pagination
            v-if="totalPages != 0"
            :total="totalPages"
            :pageSizes="pageSizes"
            @getCurPage="CurPage"
          />-->
          <pagination v-if="totalPages != 0" :total="totalPages" :pageSizes="pageSizes" />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import titleBox from "@/components/home/TitleBox.vue";
import shadowBox from "@/components/home/ShadowBox.vue";
import restMoney from "@/components/wallet/RestMoney.vue";
import myTable from "@/components/home/MyTable.vue";
import pagination from "@/components/home/Pagination.vue";

import { getFeePairDetails, getFeeAmount } from "@/api/wallet/index.js";
import { vuexStatus } from "@/mixin/vuexStatus";
import { sameMethod } from "@/mixin/sameMethod";
import { mapState } from "vuex";
export default {
  mixins: [vuexStatus, sameMethod],
  created() {
    this._getFeePairDetails();
    this._getFeeAmount();
  },
  methods: {
    filterStatus(data) {
      data.forEach(item => {
        item.feeCurrency = item.feeCurrency + "手续费";
        item.feeScale = item.feeScale + "%";
      });
    },
    _getFeePairDetails() {
      let _val = this.dealCoinPair(this._coinName);
      if (!_val) return;
      getFeePairDetails({
        symbol: _val,
        page: this.pages,
        pagesize: this.pageSizes
      }).then(res => {
        if (res.data.state == 1) {
          this.tableData = res.data.data.Rows;
          this.filterStatus(this.tableData);
          this.totalPages = res.data.data.Total;
        }
      });
    },
    _getFeeAmount() {
      let _val = this.dealCoinPair(this._coinName);
      if (!_val) return;
      getFeeAmount({ symbol: _val }).then(res => {
        if (res.data.state == 1) {
          this.before = res.data.data[this.customCoin]
            ? res.data.data[this.customCoin]
            : "";
          this.behind = res.data.data[this.baseCoin]
            ? res.data.data[this.baseCoin]
            : "";
        }
      });
    }
  },
  watch: {
    _coinName: function() {
      this._getFeePairDetails();
      this._getFeeAmount();
    }
  },
  computed: {
    ...mapState("coinType", ["baseWQuota", "valuationWQuota"]),
    customCoin() {
      return this._coinName.split("/")[0];
    },
    baseCoin() {
      return this._coinName.split("/")[1];
    }
  },
  data() {
    return {
      totalPages: null,
      pageSizes: 20,
      pages: 1,
      titleMsg: "我的钱包",
      before: "",
      behind: "",
      initTable: [
        {
          name: "日期",
          prop: "createTime",
          pos: "left"
        },
        {
          name: "类型",
          prop: "feeCurrency",
          pos: "left"
        },
        {
          name: "数量",
          prop: "feeTotalAmount",
          pos: "left"
        },
        {
          name: "比例",
          prop: "feeScale",
          pos: "left"
        },
        {
          name: "实际到账",
          prop: "realAmount",
          pos: "left"
        }
      ],
      tableData: []
    };
  },
  components: {
    titleBox,
    shadowBox,
    restMoney,
    myTable,
    pagination
  }
};
</script>
<style lang="scss" scoped>
@import "~/css/wallet/index.scss";
</style>