<template>
  <div class="view-content">
    <div class="wrap-holdings">
      <title-box :title="titleMsg" />
      <div class="lab-msg">
        <type-msg v-for="item in labData" :key="item.id" :msg="item" />
      </div>
      <div class="hold-msg">
        <shadow-box>
          <div class="shadow-content">
            <div class="left">{{ holdTitle }}</div>
            <div class="right">
              <div class="id-search">
                <id-search v-model="idVal" />
              </div>
              <search-btn @searchHandle="searchById" />
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
import typeMsg from "@/components/home/TypeMsg.vue";
import shadowBox from "@/components/home/ShadowBox.vue";
import searchBtn from "@/components/home/SearchBtn.vue";
import idSearch from "@/components/home/IDSearch.vue";
import myTable from "@/components/home/MyTable.vue";
import pagination from "@/components/home/Pagination.vue";
import { vuexStatus } from "@/mixin/vuexStatus";
import { sameMethod } from "@/mixin/sameMethod";
import { getDataLabFirst, getPositionAcountList } from "@/api/home/home.js";
//  import Cookies from 'js-cookie'
export default {
  mixins: [vuexStatus, sameMethod],
  created() {
    // console.log(location)
    // console.log(Cookies.get("JSESSIONID"));
    // this._getDataLabFirst(this._coinName);
    // console.log(this.msg)
  },
  mounted() {
    this._getPositionAcountList(this._coinName);
    this._getDataLabFirst(this._coinName);
  },
  computed: {
    titleMsg() {
      return this._coinName.split("/")[0] + "数据实验室";
    },
    holdTitle() {
      return this._coinName.split("/")[0] + "持仓分布";
    },
  },
  watch: {
    _coinName: function(val) {
      this._getDataLabFirst(val);
      this._getPositionAcountList(val);
    },
    idVal: function(val) {
      if (!val) {
        this._getPositionAcountList(this._coinName);
      }
    },
  },
  methods: {
    searchById() {
      this._getPositionAcountList(this._coinName);
    },
    CurPage(val) {
      this.pages = val;
      this._getPositionAcountList(this._coinName);
    },
    _getDataLabFirst(val) {
      let _val = this.dealCoinPair(val);
      if (!_val) return;
      getDataLabFirst({ symbol: _val }).then(res => {
        this.labFirst = res.data.data;
        this.setNum();
      });
    },
    _getPositionAcountList(val) {
      let _val = this.dealCoinPair(val);
      let baseData = {
        symbol: _val,
        page: this.pages,
        pagesize: this.pageSizes
      };
      if (!_val) return;
      if (this.idVal) {
        baseData = Object.assign(baseData, { uid: this.idVal });
      }
      getPositionAcountList(baseData).then(res => {
        // console.log(res)
        this.tableData = res.data.data.Rows;
        this.totalPages = res.data.data.Total;
      });
    },
    setNum() {
      this.labData.forEach((item, index) => {
        switch (index) {
          case 0:
            item.num = this.labFirst.uidList;
            break;
          case 1:
            item.num = this.labFirst.currencyFlow;
            break;
          case 2:
            item.num = this.labFirst.rechargeNo;
            break;
          case 3:
            item.num = this.labFirst.withdrawNo;
            break;
          default:
            null;
        }
      });
    }
  },
  data() {
    return {
      idVal: "",
      totalPages: null,
      pageSizes: 20,
      pages: 1,
      labFirst: "",
      labData: [
        {
          id: 1,
          type: "持仓活跃用户数",
          num: null,
          unit: "人",
          domStyle: "linear-gradient(90deg,#4457cd 0%, #5751da 100%)"
        },
        {
          id: 2,
          type: "币种平台流通量",
          num: null,
          unit: "枚",
          domStyle: "linear-gradient(90deg, #01aa86 0%, #12866c 100%)"
        },
        {
          id: 3,
          type: "今日充币数",
          num: null,
          unit: "枚",
          domStyle: "linear-gradient(90deg, #5744cd 0%, #5420bf 100%)"
        },
        {
          id: 4,
          type: "今日提币数",
          num: null,
          unit: "枚",
          domStyle: "linear-gradient(90deg, #f89426 0%, #f76c1c 100%)"
        }
      ],
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
          name: "币种",
          prop: "currency",
          pos: "left"
        },
        {
          name: "总余额",
          prop: "total_balance",
          pos: "right"
        },
        {
          name: "冻结余额",
          prop: "frozen_balance",
          pos: "right"
        }
      ],
      tableData: []
    };
  },
  components: {
    titleBox,
    typeMsg,
    shadowBox,
    searchBtn,
    idSearch,
    myTable,
    pagination
  }
};
</script>
<style lang="scss" scoped>
@import "~/css/home/index.scss";
</style>