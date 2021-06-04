package com.pmzhongguo.ex.core.web;


/**
 * 俄罗斯
 *
 * @author jary
 * @creatTime 2019/7/2 2:07 PM
 */
public enum InternationalSmsRUEnum {
//    CLT_CODE_TIME(1001, "ваш код аутентификации %1$s, срок действия %2$s мин"),
//    CLT_MEMBERAPPEAL_MEMBER_ORDER(1002, "【ZZEX】Пользователь %1$s подал жалобу на заказ %2$s, пожалуйста, своевременно."),
//    CLT_CODE(1003, "【ZZEX】Ваш код аутентификации %1$s.  например, не моя операция, пожалуйста, игнорируйте это сообщение "),
//    CLT_ZZEXAPPEAL_MEMBER_ORDER(1004, "【ZZEX】 %1$s уже получил заказ %2$s подать жалобу, платформа будет участвовать в обработке, пожалуйста, внимание."),
//    CLT_CANCEL_MEMBER_ORDER(1005, "【ZZEX】%1$s отменили заказ %2$s, если у вас есть вопросы, свяжитесь с платформой пассажирских услуг."),
//    CLT_MEMBER_CURRENCY(1006, "【ZZEX】%1$s подтвердил получение и отправку ваших платежей, купленные вами %2$s были выданы на ваш лицевой счет, пожалуйста, зарегистрироваться для получения."),
//    CLT_PAY_MEMBER_ORDER(1007, "【ZZEX】%1$s отметьте заказ %2$s как \"оплаченный\" статус, пожалуйста, проверьте и подтвердите пропуск счета. "),
//    CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME(1008, "【ZZEX】Вы приобрели у %1$s %2$s %3$s, купить за единицу цены %4$s %5$s, номер заказа %6$s, пожалуйста, платите в течение %7$s минут."),
//    CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER(1009, "【ZZEX】 Вы продали пользователю %1$s %2$s %3$s, продажная цена за единицу %4$s %5$s, номер заказа %6$s, ожидание оплаты другой стороной.");


    JUHE_CODE_TIME(11001,"ваш код аутентификации #code# , срок действия #m# мин.","#code#=%1$s&#m#=%2$s");

    private Integer type;

    private String cnCode;
    private String formatCode;

    InternationalSmsRUEnum(Integer type, String cnCode, String formatCode) {
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

    public static InternationalSmsRUEnum getEnumByType(int type) {
        for (InternationalSmsRUEnum o : InternationalSmsRUEnum.values()) {
            if (o.getType() == type) {
                return o;
            }
        }
        return null;
    }

    public static InternationalSmsRUEnum getEnumByCode(String code) {
        for (InternationalSmsRUEnum o : InternationalSmsRUEnum.values()) {
            if (o.getCnCode().equals(code)) {
                return o;
            }
        }
        return null;
    }
}
