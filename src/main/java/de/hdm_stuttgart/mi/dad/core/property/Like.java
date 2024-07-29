package de.hdm_stuttgart.mi.dad.core.property;

import java.util.regex.Pattern;

record Like(Pattern value) implements Property<String> {

    @Override
    public PropertyType getType() {
        return PropertyType.LIKE;
    }

    @Override
    public String getValue() {
        return value.pattern();
    }
}
