package de.hdm_stuttgart.mi.dad.core.property.properties;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;

import java.util.regex.Pattern;

final class Regex extends Property<String> {
    private final Pattern value;

    Regex(Pattern value) {
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.REGEX;
    }

    @Override
    public String getValue() {
        return value.pattern();
    }
}
