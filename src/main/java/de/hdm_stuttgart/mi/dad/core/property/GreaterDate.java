package de.hdm_stuttgart.mi.dad.core.property;

import java.time.LocalDate;

//TODO: doku

record GreaterDate(LocalDate value) implements Property<LocalDate> {

    @Override
    public PropertyType getType() {
        return PropertyType.GREATERDATE;
    }

    @Override
    public LocalDate getValue() {
        return value;
    }
}
