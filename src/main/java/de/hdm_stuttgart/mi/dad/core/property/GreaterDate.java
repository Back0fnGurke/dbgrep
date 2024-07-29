package de.hdm_stuttgart.mi.dad.core.property;

import java.time.LocalDate;


//TODO: doku

final class GreaterDate extends Property<LocalDate> {
    private final LocalDate value;

    GreaterDate(LocalDate value) {
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.GREATER_DATE;
    }

    @Override
    public LocalDate getValue() {
        return value;
    }
}
