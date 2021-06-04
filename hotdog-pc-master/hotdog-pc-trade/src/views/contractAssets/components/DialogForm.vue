<template>
  <el-dialog
    :title="$t('contract.dt')"
    :visible.sync="dialogFormVisible"
    class="form"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
     :before-close="closeDialog"
     width="560px"
  >
    <el-form :model="form" label-position="top">
      <el-form-item :label="$t('contract.Coin')">
        <el-input v-model="form.coin" autocomplete="off" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item :label="$t('contract.CanUse')+formMoney+' USDT）'">
        <el-select v-model="form.from" :placeholder="$t('contract.SelectAccount')">
          <el-option :label="$t('contract.FiatAccount')" value="fb"></el-option>
          <el-option :label="$t('contract.CoinCurrencyAccount')" value="bb"></el-option>
          <el-option :label="$t('contract.AllWarehouseAccount')" value="qc"></el-option>
          <el-option :label="$t('contract.EachWarehouseAccount')" value="zc"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('contract.TransferTo')+toMoney+' USDT）'">
        <el-select v-model="form.to" :placeholder="$t('contract.SelectAccount')">
          <el-option :label="$t('contract.FiatAccount')" value="fb"></el-option>
          <el-option :label="$t('contract.CoinCurrencyAccount')" value="bb"></el-option>
          <el-option :label="$t('contract.AllWarehouseAccount')" value="qc"></el-option>
          <el-option :label="$t('contract.EachWarehouseAccount')" value="zc"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('contract.CanTransfer')+formMoney+' USDT）'">
        <el-input v-model="form.num" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{$t('contract.Cancel')}}</el-button>
      <el-button type="primary" @click="moveMoney">{{$t('contract.Confirm')}}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  accountAddChange,
  accountSubtChange,
  bb2QcAjax,
  qc2BbAjax,
  bb2ZcAjax,
  zc2BbAjax,
  fb2QcAjax,
  qc2FbAjax,
  fb2ZcAjax,
  zc2FbAjax,
  qc2ZcAjax,
  zc2QcAjax
} from "../../../api/exchange";
export default {
  computed: {
    formMoney: function() {
      let money = 0;
      switch (this.form.from) {
        case "fb":
          money = this.fbMoney;
          break;
        case "bb":
          money = this.bbMoney;
          break;
        case "qc":
          money = this.qcMoney;
          break;
        case "zc":
          money = this.zcMoney;
          break;
        default:
          break;
      }
      return money;
    },
    toMoney: function() {
      let money = 0;
      switch (this.form.to) {
        case "fb":
          money = this.fbMoney;
          break;
        case "bb":
          money = this.bbMoney;
          break;
        case "qc":
          money = this.qcMoney;
          break;
        case "zc":
          money = this.zcMoney;
          break;
        default:
          break;
      }
      return money;
    }
  },
  props: {
    dialogFormVisible: Boolean,
    bbMoney: Number,
    fbMoney: Number,
    qcMoney: Number,
    zcMoney: Number
  },
  data() {
    return {
      form: {
        coin: "USDT",
        from: "fb",
        to: "bb",
        num: ""
      }
    };
  },
  methods: {
    closeDialog() {
      this.$emit("close-dialog");
    },

    resetForm() {
      this.form = {
        coin: "USDT",
        from: "fb",
        to: "bb",
        num: ""
      };
    },
    moveMoney() {
      if (this.form.num > this.form.from) {
        this.$message({
          type: "error",
          message: "资金不足",
          duration: 3000,
          showClose: true
        });
        return;
      }

      if (
        this.form.from === "" ||
        this.form.to === "" ||
        this.form.num === ""
      ) {
        this.$message({
          type: "error",
          message: "请完成表单",
          duration: 3000,
          showClose: true
        });
        return;
      }

      if (this.form.from === this.form.to) {
        this.$message({
          type: "error",
          message: "请选择正确的地址",
          duration: 3000,
          showClose: true
        });
        return;
      }
      this.selectFunction();
    },

    async selectFunction() {
      let uid = localStorage.getItem("useruid");
      const params = {
        memberId: uid,
        currency: this.form.coin,
        num: this.form.num
      };

      let res;

      if (this.form.from === "bb" && this.form.to === "fb") {
        res = await accountAddChange(params);
      } else if (this.form.from === "bb" && this.form.to === "qc") {
        res = await bb2QcAjax(params);
      } else if (this.form.from === "bb" && this.form.to === "zc") {
        res = await bb2ZcAjax(params);
      }else if (this.form.from === "fb" && this.form.to === "bb") {
          res = await accountSubtChange(params);
      }else if (this.form.from === "fb" && this.form.to === "qc") {
          res = await fb2QcAjax(params);
      }else if (this.form.from === "fb" && this.form.to === "zc") {
          res = await fb2ZcAjax(params);
      }else if (this.form.from === "qc" && this.form.to === "fb") {
          res = await qc2FbAjax(params);
      }else if (this.form.from === "qc" && this.form.to === "bb") {
          res = await qc2BbAjax(params);
      }else if (this.form.from === "qc" && this.form.to === "zc") {
          res = await qc2ZcAjax(params);
      }else if (this.form.from === "zc" && this.form.to === "fb") {
          res = await zc2FbAjax(params);
      }else if (this.form.from === "zc" && this.form.to === "bb") {
          res = await zc2BbAjax(params);
      }else if (this.form.from === "zc" && this.form.to === "qc") {
          res = await zc2QcAjax(params);
      }
      if (res.state === 1) {
        this.$message({
          type: "success",
          message: "划转成功",
          duration: 3000,
          showClose: true
        });
        this.closeDialog();
        this.resetForm();
        this.$emit("reload-page");
      }
    }
  }
};
</script>

<style>
.form .el-select {
  width: 100%;
}
</style>
