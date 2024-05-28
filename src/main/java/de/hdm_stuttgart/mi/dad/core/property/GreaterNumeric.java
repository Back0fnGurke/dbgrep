package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;

public class GreaterNumeric implements Property<BigDecimal> {

    private final BigDecimal value;

    public GreaterNumeric(final BigDecimal value) {
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.GREATERNUMERIC;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
