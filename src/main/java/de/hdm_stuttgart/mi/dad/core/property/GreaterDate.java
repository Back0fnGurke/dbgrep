package de.hdm_stuttgart.mi.dad.core.property;

import java.time.LocalDate;

class GreaterDate implements Property<LocalDate> {

    private final LocalDate value;

    public GreaterDate(final LocalDate value) {
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.GREATERDATE;
    }

    @Override
    public LocalDate getValue() {
        return value;
    }
}
