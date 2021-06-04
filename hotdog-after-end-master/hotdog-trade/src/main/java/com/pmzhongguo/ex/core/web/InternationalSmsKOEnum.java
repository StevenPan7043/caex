package com.pmzhongguo.ex.core.web;

/**
 * 韩国
 * @author jary
 * @creatTime 2019/7/2 2:07 PM
 */
public enum InternationalSmsKOEnum {
//    CLT_CODE_TIME(1001, "인증 코딩은 %1$s 유효기간 %2$s 분"),
//    CLT_MEMBERAPPEAL_MEMBER_ORDER(1002, "[ZZEX] 사용자 %1$s 주문서에 대해 %2$s 제때에 처리해 주십시오."),
//    CLT_CODE(1003, "[ZZZX] 검증 코드는 %1$s 입니다.만약 본인이 조작하지 않는다면 문자 메시지를 소홀히 하십시오"),
//    CLT_ZZEXAPPEAL_MEMBER_ORDER(1004, "[ZZEX] %1$s  이미 주문서를 %2$s 고소, 플랫폼 고객은 개입 처리할 것이니 제때에 주목해 주십시오."),
//    CLT_CANCEL_MEMBER_ORDER(1005, "[ZZEX] %1$s 주문서 %2$s 취소, 의문이 있으면 플랫폼에 연락해 주십시오."),
//    CLT_MEMBER_CURRENCY(1006, "[ZZEX] %1$s 받은 돈을 확인하고 실행하였습니다. 구매한 %2$s 법폐 계좌에 보내셨습니다. 계좌에 로그인 해 주십시오."),
//    CLT_PAY_MEMBER_ORDER(1007, "[ZZEX] %1$s 주문서 %2$s 을 '지불' 상태로 표시하고 수금계좌에 로그인해서 확인해 보십시오."),
//    CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME(1008, "【ZZEX 】 이미 %1$s 사용자에게 구매 %2$s %3$s %4$s  %5$s 사용자 %6$s 사용자에게 구매 %7$s 분 내에 지불해 주십시오."),
//    CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER(1009, "【ZZEX 】 이미 %1$s %2$s  %3$s 판매 단가는  %4$s 주문 번호를 %5$s %6$s 으로 판매합니다.");

    JUHE_CODE_TIME(11000, "인증 코딩은 #code# 유효기간 #m# 분.","#code#=%1$s&#m#=%2$s");

    private Integer type;

    private String cnCode;

    private String formatCode;

    InternationalSmsKOEnum(Integer type, String cnCode, String formatCode) {
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


    public static InternationalSmsKOEnum getEnumByType(int type) {
        for (InternationalSmsKOEnum o : InternationalSmsKOEnum.values()) {
            if (o.getType() == type) {
                return o;
            }
        }
        return null;
    }

    public static InternationalSmsKOEnum getEnumByCode(String code) {
        for (InternationalSmsKOEnum o : InternationalSmsKOEnum.values()) {
            if (o.getCnCode().equals(code)) {
                return o;
            }
        }
        return null;
    }
}
