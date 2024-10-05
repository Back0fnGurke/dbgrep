package de.hdm_stuttgart.mi.dad.core.property;

/**
 * Represents a property with a generic type.
 *
 * @param <T> the type of the property value
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
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (null == other) {
            return false;
        }
        if (!(other instanceof Property<?> otherProperty)) {
            return false;
        }
        return otherProperty.getType() == this.getType() && otherProperty.getValue().equals(this.getValue());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getType() == null ? 0 : getType().hashCode());
        result = 31 * result + (getValue() == null ? 0 : getValue().hashCode());
        return result;
    }
}
