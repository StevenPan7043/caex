<template>
  <div class="trade-main">
    <div class="trade-data">
      <title-box :title="titleMsg" />
      <div class="trade-msg">
        <shadow-box>
          <div class="shadow-container">
            <div class="trade-pairs">
              <my-disinput :val="coinPair" mr="10" desc="交易对" />
            </div>
            <id-search v-model="idVal" />
            <div class="trade-type">
              <my-download desc="交易类型" mr="10" ref="downItem" :list="downloadList" />
            </div>
            <div class="date-box">
              <my-datepicker ref="dateCmp" />
            </div>
            <div class="btn">
              <search-btn @searchHandle="searchByCondition" />
            </div>
          </div>
        </shadow-box>
        <div class="table-box">
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
import titleBox from "@/components/home/TitleBox.vue";
import shadowBox from "@/components/home/ShadowBox.vue";
import idSearch from "@/components/home/IDSearch.vue";
import myDatepicker from "@/components/tradeData/MyDatepicker.vue";
import searchBtn from "@/components/home/SearchBtn.vue";
import myTable from "@/components/home/MyTable.vue";
import pagination from "@/components/home/Pagination.vue";
import myDisinput from "@/components/tradeData/MyDisinput.vue";
import myDownload from "@/components/tradeData/MyDownload.vue";
import { getTradeList } from "@/api/tradeData/index.js";
import { vuexStatus } from "@/mixin/vuexStatus";
import { sameMethod } from "@/mixin/sameMethod";
export default {
  mixins: [vuexStatus, sameMethod],
  data() {
    return {
      idVal: "",
      totalPages: null,
      pageSizes: 20,
      pages: 1,
      searchData: {},
      downloadList: ["全部", "buy", "sell"],
      initTable: [
        {
          name: "会员ID",
          prop: "uid",
          pos: "left"
        },
        {
          name: "会员账号",
          prop: "mName",
          pos: "left"
        },
        {
          name: "交易类型",
          tType: "buy",
          prop: "tType",
          pos: "center"
        },
        {
          name: "成交价格",
          prop: "price",
          pos: "left"
        },
        {
          name: "成交数量",
          prop: "volume",
          pos: "left"
        },
        {
          name: "成交金额",
          prop: "amount",
          pos: "right"
        },
        {
          name: "交易时间",
          prop: "done_time",
          pos: "right"
        }
      ],
      tableData: []
    };
  },
  created() {
    this._getTradeList(this.coinPair);
  },
  computed: {
    titleMsg() {
      return this._coinName.split("/")[0] + "交易数据";
    },
    coinPair() {
      return this.dealCoinPair(this._coinName);
    }
  },
  watch: {
    coinPair: function(val) {
      this._getTradeList(val);
    },
    idVal: function(val) {
      if (!val) {
        this.$refs.downItem.tType = "全部";
        this.$refs.dateCmp.startTime = "";
        this.$refs.dateCmp.endTime = "";
        this.searchData.tType = "";
        this.searchData.startTime = "";
        this.searchData.endTime = "";
        this.searchData.uid = "";
        this._getTradeList(this.coinPair);
      }
    },
  },
  methods: {
    searchByCondition() {
      // console.log(this.$refs.dateCmp.endTime)
      this.idVal && (this.searchData.uid = this.idVal);
      this.$refs.downItem.tType &&
        (this.searchData.tType =
          this.$refs.downItem.tType == "全部" ? "" : this.$refs.downItem.tType);
      this.$refs.dateCmp.startTime &&
        (this.searchData.startTime = this.$refs.dateCmp.startTime);
      this.$refs.dateCmp.endTime &&
        (this.searchData.endTime = this.$refs.dateCmp.endTime);
      this._getTradeList(this.coinPair);
    },
    CurPage(val) {
      this.pages = val;
      this._getTradeList(this.coinPair);
    },
    _getTradeList(val) {
      let baseData = {
        symbol: val,
        page: this.pages,
        pagesize: this.pageSizes
      };
      if (!val) return;
      if (JSON.stringify(this.searchData) != "{}") {
        baseData = Object.assign(baseData, this.searchData);
      }
      getTradeList(baseData).then(res => {
        if (res.data.state == 1) {
          this.tableData = res.data.data.Rows;
          this.totalPages = res.data.data.Total;
        }
      });
    }
  },
  components: {
    titleBox,
    shadowBox,
    idSearch,
    myDatepicker,
    searchBtn,
    myTable,
    pagination,
    myDisinput,
    myDownload
  }
};
</script>
<style lang="scss" scoped>
@import "~/css/tradeData/index.scss";
</style>