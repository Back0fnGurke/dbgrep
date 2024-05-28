package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;

public class Equal implements Property<BigDecimal> {

    private final BigDecimal value;

    public Equal(final BigDecimal value) {
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.EQUAL;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
