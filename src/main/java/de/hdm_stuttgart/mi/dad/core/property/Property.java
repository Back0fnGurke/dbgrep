package de.hdm_stuttgart.mi.dad.core.property;

//TODO: doku

public abstract class Property<T> {

    public abstract PropertyType getType();

    public abstract T getValue();

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (null == other){
            return false;
        }
        if (!(other instanceof Property<?> otherProperty)) {
            return false;
        }
        return otherProperty.getType() == this.getType() && otherProperty.getValue().equals(this.getValue());
    }
}
