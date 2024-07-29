package de.hdm_stuttgart.mi.dad.core.property;

import java.util.regex.Pattern;

//TODO: doku

final class Like extends Property<String> {
    private final Pattern value;

    Like(Pattern value) {
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.LIKE;
    }

    @Override
    public String getValue() {
        return value.pattern();
    }
}
