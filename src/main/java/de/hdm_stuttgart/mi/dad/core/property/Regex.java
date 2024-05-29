package de.hdm_stuttgart.mi.dad.core.property;

import java.util.regex.Pattern;

class Regex implements Property<String> {

    private final Pattern value;

    public Regex(final Pattern value) {
        this.value = Pattern.compile(value.pattern());
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
