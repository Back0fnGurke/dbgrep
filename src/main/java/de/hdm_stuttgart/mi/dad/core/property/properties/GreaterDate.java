package de.hdm_stuttgart.mi.dad.core.property.properties;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;

import java.time.LocalDate;

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
