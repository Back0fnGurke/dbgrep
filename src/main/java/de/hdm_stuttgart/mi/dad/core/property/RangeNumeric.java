package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;
import java.util.Arrays;

public class RangeNumeric implements Property {

    private BigDecimal[] range;

    public RangeNumeric(BigDecimal[] range) {
        this.range = range;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.RANGENUMERIC;
    }

    @Override
    public Object getValue() {
        return Arrays.copyOf(range, range.length);
    }
}
