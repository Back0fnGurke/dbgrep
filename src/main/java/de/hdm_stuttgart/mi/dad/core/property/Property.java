package de.hdm_stuttgart.mi.dad.core.property;

//TODO: doku

public interface Property<T> {

    PropertyType getType();

    T getValue();
}
