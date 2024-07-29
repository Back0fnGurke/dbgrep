package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;

//TODO: doku

final class Equal extends Property<BigDecimal> {
    private final BigDecimal value;

    Equal(BigDecimal value) {
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
