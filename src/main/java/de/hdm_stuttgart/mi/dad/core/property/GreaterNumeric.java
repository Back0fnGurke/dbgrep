package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;

//TODO: doku

record GreaterNumeric(BigDecimal value) implements Property<BigDecimal> {

    @Override
    public PropertyType getType() {
        return PropertyType.GREATERNUMERIC;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
