package com.pmzhongguo.ex.core.web;

/**
 * 日本
 * @author jary
 * @creatTime 2019/7/2 2:07 PM
 */
public enum InternationalSmsJAEnum {
//    CLT_CODE_TIME(1001, "検証コードは %1$s で、有効期限は %2$s分です。"),
//    CLT_MEMBERAPPEAL_MEMBER_ORDER(1002, "【ZZZZEX】ユーザ %1$s が注文書に対して %2$s を訴えました。適時に処理してください。"),
//    CLT_CODE(1003, "【ZZZEX】認証コードは %1$s です。本人が操作していない場合は、このメッセージを無視してください。"),
//    CLT_ZZEXAPPEAL_MEMBER_ORDER(1004, "【ZZZEX】%1$s は注文書 %2$s をクレームしました。プラットフォームのカスタマーサービスが介入します。ご注意ください。"),
//    CLT_CANCEL_MEMBER_ORDER(1005, "【ZZZZEX】%1$s は注文をキャンセルしました。もし問題があれば、プラットフォームのカスタマーサービスに連絡してください。"),
//    CLT_MEMBER_CURRENCY(1006, "【ZZZZEX】%1$s はあなたの支払いを確認しました。あなたが購入した %2$s はすでにお支払いしました。アカウントに登録してください。"),
//    CLT_PAY_MEMBER_ORDER(1007, "ZZZZEX】%1$s は注文書 %2$s を「支払済み」状態としてマークします。入金口座に登録して確認してください。"),
//    CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME(1008, "【ZZZZEX】%1$s のユーザに %2$s %3$s を購入しました。購入単価は %4$s  %5$s で、注文番号は %6$s です。 %7$s 以内でお願いします。"),
//    CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER(1009, "【ZZZZEX】お客様は %1$s に %2$s %3$s を販売しています。販売単価は %4$s %5$sで、注文番号は %6$s で、お支払いを待っています。");

    JUHE_CODE_TIME(10999, "検証コードは #code# で、有効期限は #m# 分です。","#code#=%1$s&#m#=%2$s");

    private String formatCode;

    private Integer type;

    private String cnCode;

    InternationalSmsJAEnum(Integer type, String cnCode, String formatCode) {
        this.type = type;
        this.cnCode = cnCode;
        this.formatCode = formatCode;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public Integer getType() {
        return type;
    }

    public String getCnCode() {
        return cnCode;
    }

    public static InternationalSmsJAEnum getEnumByType(int type) {
        for (InternationalSmsJAEnum o : InternationalSmsJAEnum.values()) {
            if (o.getType() == type) {
                return o;
            }
        }
        return null;
    }

    public static InternationalSmsJAEnum getEnumByCode(String code) {
        for (InternationalSmsJAEnum o : InternationalSmsJAEnum.values()) {
            if (o.getCnCode().equals(code)) {
                return o;
            }
        }
        return null;
    }
}
