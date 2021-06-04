package com.contract.enums;

import java.math.BigDecimal;

public enum GdLevelEnum {
    level1("1", new BigDecimal("0.1")),
    level2("2", new BigDecimal("0.15")),
    level3("3", new BigDecimal("0.2"));
    private String level;
    private BigDecimal scale;

    GdLevelEnum(String level, BigDecimal scale) {
        this.level = level;
        this.scale = scale;
    }

    public String getLevel() {
        return level;
    }

    public BigDecimal getScale() {
        return scale;
    }
}
