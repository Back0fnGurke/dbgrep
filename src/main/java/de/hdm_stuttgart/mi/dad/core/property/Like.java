package de.hdm_stuttgart.mi.dad.core.property;

import java.util.regex.Pattern;

public class Like implements Property<String> {

    private final Pattern value;

    public Like(final Pattern value) {
        this.value = Pattern.compile(value.pattern());
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
