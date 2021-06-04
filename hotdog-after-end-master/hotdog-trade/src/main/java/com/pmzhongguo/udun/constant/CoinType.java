package com.pmzhongguo.udun.constant;

public enum CoinType {
    BTC("BTC", "0", "BTC", "0"),
    LTC("LTC", "2", "LTC", "2"),
    DOGE("DOGE", "3", "DOGE", "3"),
    ETH("ETH", "60", "ETH", "60"),
    ETC("ETC", "61", "ETC", "61"),
    XRP("XRP", "144", "XRP", "144"),
    BCH("BCH", "145", "BCH", "145"),
    EOS("EOS", "194", "EOS", "194"),
    TRX("TRX", "195", "TRX", "195"),
    NEO("NEO", "888", "NEO", "888"),
    XNE("XNE", "208", "XNE", "208"),
    TEC("TEC", "206", "TEC", "206"),
    GCA("GCA", "500", "GCA", "500"),
    GCB("GCB", "501", "GCB", "501"),
    GCC("GCC", "502", "GCC", "502"),
    DASH("DASH", "5", "DASH", "5"),
    ZEC("ZEC", "133", "ZEC", "133"),
    QTUM("QTUM", "2301", "QTUM", "2301"),
    TECO("TECO", "506", "TECO", "506"),
    CNYT("CNYT", "509", "CNYT", "509"),
    STO("STO", "99", "STO", "99"),
    ERC20("ETH", "60", "USDT", "0xdac17f958d2ee523a2206206994597c13d831ec7"),
    TRC20("TRX", "195", "USDT", "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t"),
    OMNI("BTC", "0", "USDT", "31"),
    CNT("CNT", "520", "CNT", "520"),
    FIL("FIL", "2307", "FIL", "2307"),
    MKE("TRX", "195", "MKE", "TYCV6CTCTxBr6zXL1rQr9C5Z4tFos4jewZ"),
    BLN("TRX", "195", "BLN", "TVk4C9DoGMJKW6tQ9S7poqpgmgbR21v5Ap");

    private String mainUnit;
    private String mainCoinType;
    private String unit;
    private String coinType;

    CoinType(String mainUnit, String mainCoinType, String unit, String coinType) {
        this.mainUnit = mainUnit;
        this.mainCoinType = mainCoinType;
        this.unit = unit;
        this.coinType = coinType;
    }

    public String getMainCoinType() {
        return mainCoinType;
    }

    public String getUnit() {
        return unit;
    }

    public String getCoinType() {
        return coinType;
    }

    public String getMainUnit() {
        return mainUnit;
    }

    public static CoinType getCoin(String coin) {
        if ("BTC".equals(coin) || BTC.getCoinType().equals(coin)) return BTC;
        else if ("LTC".equals(coin) || LTC.getCoinType().equals(coin)) return LTC;
        else if ("DOGE".equals(coin) || DOGE.getCoinType().equals(coin)) return DOGE;
        else if ("ETH".equals(coin) || ETH.getCoinType().equals(coin)) return ETH;
        else if ("ETC".equals(coin) || ETC.getCoinType().equals(coin)) return ETC;
        else if ("XRP".equals(coin) || XRP.getCoinType().equals(coin)) return XRP;
        else if ("BCH".equals(coin) || BCH.getCoinType().equals(coin)) return BCH;
        else if ("EOS".equals(coin) || EOS.getCoinType().equals(coin)) return EOS;
        else if ("TRX".equals(coin) || TRX.getCoinType().equals(coin)) return TRX;
        else if ("NEO".equals(coin) || NEO.getCoinType().equals(coin)) return NEO;
        else if ("XNE".equals(coin) || XNE.getCoinType().equals(coin)) return XNE;
        else if ("TEC".equals(coin) || TEC.getCoinType().equals(coin)) return TEC;
        else if ("GCA".equals(coin) || GCA.getCoinType().equals(coin)) return GCA;
        else if ("GCB".equals(coin) || GCB.getCoinType().equals(coin)) return GCB;
        else if ("GCC".equals(coin) || GCC.getCoinType().equals(coin)) return GCC;
        else if ("DASH".equals(coin) || DASH.getCoinType().equals(coin)) return DASH;
        else if ("ZEC".equals(coin) || ZEC.getCoinType().equals(coin)) return ZEC;
        else if ("QTUM".equals(coin) || QTUM.getCoinType().equals(coin)) return QTUM;
        else if ("TECO".equals(coin) || TECO.getCoinType().equals(coin)) return TECO;
        else if ("CNYT".equals(coin) || CNYT.getCoinType().equals(coin)) return CNYT;
        else if ("STO".equals(coin) || STO.getCoinType().equals(coin)) return STO;
        else if ("ERC20".equals(coin) || ERC20.getCoinType().equals(coin)) return ERC20;
        else if ("TRC20".equals(coin) || TRC20.getCoinType().equals(coin)) return TRC20;
        else if ("OMNI".equals(coin) || OMNI.getCoinType().equals(coin)) return OMNI;
        else if ("CNT".equals(coin) || CNT.getCoinType().equals(coin)) return CNT;
        else if ("FIL".equals(coin) || FIL.getCoinType().equals(coin)) return FIL;
        else if ("MKE".equals(coin) || MKE.getCoinType().equals(coin)) return MKE;
        else if ("BLN".equals(coin) || BLN.getCoinType().equals(coin)) return BLN;
        else if ("USDT".equals(coin)) return ERC20;//传USDT时，默认时erc20
        return null;
    }
}
