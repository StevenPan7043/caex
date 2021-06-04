export const sameMethod = {
    methods: {
        getStatus(val) {
            switch (val) {
                case 0:
                    return "待审核";
                case 1:
                    return "已完成";
                default:
                    return "";
            }
        },
        filterStatus(data) {
            data.forEach(item => {
              item.tType = "提现";
              item.r_status = this.getStatus(item.r_status);
            });
          },
          dealCoinPair(str) {
            let reg = /\//g;
            return str.replace(reg, "");
          },
    }
}