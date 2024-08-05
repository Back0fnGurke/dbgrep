package de.hdm_stuttgart.mi.dad.core.property.properties;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;

import java.math.BigDecimal;

final class GreaterNumeric extends Property<BigDecimal> {
    private final BigDecimal value;

    GreaterNumeric(BigDecimal value) {
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.GREATER_NUMERIC;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
