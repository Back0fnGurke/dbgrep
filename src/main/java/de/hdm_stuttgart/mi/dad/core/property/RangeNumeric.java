package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;
import java.util.Arrays;

//TODO: doku

record RangeNumeric(BigDecimal[] value) implements Property<BigDecimal[]> {

    RangeNumeric(BigDecimal[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }

    @Override
    public PropertyType getType() {
        return PropertyType.RANGENUMERIC;
    }

    @Override
    public BigDecimal[] getValue() {
        return new BigDecimal[]{value[0], value[1]};
    }
}
