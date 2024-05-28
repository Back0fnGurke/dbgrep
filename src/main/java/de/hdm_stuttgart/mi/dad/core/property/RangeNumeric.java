package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;
import java.util.Arrays;

public class RangeNumeric implements Property<BigDecimal[]> {

    private final BigDecimal[] value;

    public RangeNumeric(final BigDecimal[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }

    public RangeNumeric(final BigDecimal from, final BigDecimal to) {
        this.value = new BigDecimal[]{from, to};
    }

    @Override
    public PropertyType getType() {
        return PropertyType.RANGENUMERIC;
    }

    @Override
    public BigDecimal[] getValue() {
        return Arrays.copyOf(value, value.length);
    }
}
