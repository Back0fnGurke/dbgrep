package de.hdm_stuttgart.mi.dad.core.property;

/**
 * The Property abstract class represents a property with a specific type and value.
 * It is a generic abstract class where the type of the value is specified by the generic parameter T.
 * <p>
 * The abstract class provides two methods: getType and getValue.
 * The getType method returns the type of the property as a PropertyType enum.
 * The getValue method returns the value of the property as an object of type T.
 * <p>
 * Extending classes of this abstract class are expected to provide a concrete type for T and override the getType and getValue methods.
 * <p>
 */

public abstract class Property<T> {
    
    /**
     * Returns the type of the property.
     *
     * @return the type of the property as a PropertyType enum.
     */
    public abstract PropertyType getType();

    /**
     * Returns the value of the property.
     *
     * @return the value of the property as an object of type T.
     */
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
