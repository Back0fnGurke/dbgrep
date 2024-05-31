package de.hdm_stuttgart.mi.dad.core.property;

public interface Property<T> {

    PropertyType getType();

    T getValue();
}
