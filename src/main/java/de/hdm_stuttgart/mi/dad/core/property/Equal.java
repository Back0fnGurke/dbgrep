package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;

//TODO: doku

record Equal(BigDecimal value) implements Property<BigDecimal> {

    @Override
    public PropertyType getType() {
        return PropertyType.EQUAL;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
