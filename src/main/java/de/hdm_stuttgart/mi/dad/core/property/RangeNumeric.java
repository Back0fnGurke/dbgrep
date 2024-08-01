package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

final class RangeNumeric extends Property<BigDecimal[]> {
    private final BigDecimal[] value;


    RangeNumeric(BigDecimal[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }

    @Override
    public PropertyType getType() {
        return PropertyType.RANGE_NUMERIC;
    }

    @Override
    public BigDecimal[] getValue() {
        return new BigDecimal[]{value[0], value[1]};
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (null == other){
            return false;
        }
        if (!(other instanceof RangeNumeric otherProperty)) {
            return false;
        }
        boolean equalValue = Objects.equals(otherProperty.getValue()[0], this.getValue()[0]) && Objects.equals(otherProperty.getValue()[1], this.getValue()[1]);
        return otherProperty.getType() == this.getType() && equalValue;
    }
}
