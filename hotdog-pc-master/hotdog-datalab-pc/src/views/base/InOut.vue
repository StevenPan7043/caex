<template>
  <div class="in-out">
    <div class="in-out-wrap">
      <title-box :title="titleMsg" />
      <div class="data-box">
        <shadow-box>
          <div class="content">
            <div class="trade-coin">
              <my-disinput mr="20" desc="币种" :val="coin" />
            </div>
            <div class="coin-type">
              <my-download desc="类型" mr="20" :list="downloadList" ref="downItem" />
            </div>
            <div class="id-search">
              <id-search v-model="idVal" />
            </div>
            <div class="time">
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
import myDisinput from "@/components/tradeData/MyDisinput.vue";
import myDownload from "@/components/tradeData/MyDownload.vue";
import idSearch from "@/components/home/IDSearch.vue";
import myDatepicker from "@/components/tradeData/MyDatepicker.vue";
import searchBtn from "@/components/home/SearchBtn.vue";
import myTable from "@/components/home/MyTable.vue";
import pagination from "@/components/home/Pagination.vue";
import { vuexStatus } from "@/mixin/vuexStatus";
import { sameMethod } from "@/mixin/sameMethod";
import { assetsRecharge, assetsWithDraw } from "@/api/inOut/index.js";
export default {
  mixins: [vuexStatus, sameMethod],
  created() {
    this._assetsRecharge();
    this.initTable = this.inData;
  },
  methods: {
    initsearchData() {
      this.searchData.member_id = this.idVal;
      this.searchData.startTime = this.$refs.dateCmp.startTime;
      this.searchData.endTime = this.$refs.dateCmp.endTime;
    },
    searchByCondition() {
      let type = this.$refs.downItem.tType ? this.$refs.downItem.tType : "充币";
      if (type == "充币") {
        this.initsearchData();
        this._assetsRecharge();
        this.initTable = this.inData;
      } else {
        this.initsearchData();
        this._assetsWithDraw();
        this.initTable = this.outData;
      }
    },
    _assetsRecharge() {
      let baseData = {
        currency: this.coin,
        page: this.pages,
        pagesize: this.pageSizes
      };
      if (JSON.stringify(this.searchData) != "{}") {
        baseData = Object.assign(baseData, this.searchData);
      }
      assetsRecharge(this.pages, baseData).then(res => {
        if (res.data.state == 1) {
          this.tableData = res.data.data.rechargeLst;
          this.filterStatus(this.tableData);
          this.totalPages = res.data.data.total;
        }
      });
    },
    _assetsWithDraw() {
      let baseData = {
        currency: this.coin,
        page: this.pages,
        pagesize: this.pageSizes
      };
      if (JSON.stringify(this.searchData) != "{}") {
        baseData = Object.assign(baseData, this.searchData);
      }
      assetsWithDraw(this.pages, baseData).then(res => {
        if (res.data.state == 1) {
          this.tableData = res.data.data.withdrawLst;
          this.filterStatus(this.tableData);
          this.totalPages = res.data.data.total;
        }
      });
    },
    CurPage(val) {
      this.pages = val;
      this.searchByCondition();
    },
    filterStatus(data) {
      data.forEach(item => {
        if (item.r_status) {
          item.r_status = this.getStatus(item.r_status);
          item.type = "充币";
        } else {
          item.w_status = this.getStatus(item.w_status);
          item.type = "提币";
        }
      });
    }
  },
  computed: {
    titleMsg() {
      return this._coinName.split("/")[0] + "充提数据";
    },
    coin() {
      return this._coinName.split("/")[0];
    }
  },
  watch: {
    coin: function() {
      this._assetsRecharge();
    },
    idVal: function(val) {
      if (!val) {
        this.$refs.downItem.tType = "充币";
        this.$refs.dateCmp.startTime = "";
        this.$refs.dateCmp.endTime = "";
        this.searchData.tType = "";
        this.searchData.startTime = "";
        this.searchData.endTime = "";
        this.searchData.uid = "";
        this.searchByCondition();
      }
    }
  },
  data() {
    return {
      searchData: {},
      idVal: "",
      totalPages: null,
      pageSizes: 20,
      pages: 1,
      downloadList: ["充币", "提币"],
      initTable: "",
      inData: [
        {
          name: "币种",
          prop: "currency",
          pos: "left"
        },
        {
          name: "类型",
          prop: "type",
          pos: "left"
        },
        {
          name: "会员ID",
          prop: "member_id",
          pos: "left"
        },
        {
          name: "会员账号",
          prop: "m_name",
          pos: "center"
        },
        {
          name: "数量",
          prop: "r_amount",
          pos: "left"
        },
        {
          name: "地址",
          prop: "r_address",
          pos: "left"
        },
        {
          name: "状态",
          prop: "r_status",
          pos: "right"
        },
        {
          name: "时间",
          prop: "r_create_time",
          pos: "right"
        }
      ],
      outData: [
        {
          name: "币种",
          prop: "currency",
          pos: "left"
        },
        {
          name: "类型",
          prop: "type",
          pos: "left"
        },
        {
          name: "会员ID",
          prop: "member_id",
          pos: "left"
        },
        {
          name: "会员账号",
          prop: "m_name",
          pos: "center"
        },
        {
          name: "数量",
          prop: "w_amount",
          pos: "left"
        },
        {
          name: "地址",
          prop: "member_coin_addr",
          pos: "left"
        },
        {
          name: "状态",
          prop: "w_status",
          pos: "right"
        },
        {
          name: "时间",
          prop: "w_create_time",
          pos: "right"
        }
      ],
      tableData: []
    };
  },
  components: {
    titleBox,
    shadowBox,
    myDisinput,
    myDownload,
    idSearch,
    myDatepicker,
    searchBtn,
    myTable,
    pagination
  }
};
</script>
<style lang="scss" scoped>
@import "~/css/inOut/index.scss";
</style>