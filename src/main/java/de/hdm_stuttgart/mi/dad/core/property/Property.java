package de.hdm_stuttgart.mi.dad.core.property;

/**
 * The Property interface represents a property with a specific type and value.
 * It is a generic interface where the type of the value is specified by the generic parameter T.
 * <p>
 * The interface provides two methods: getType and getValue.
 * The getType method returns the type of the property as a PropertyType enum.
 * The getValue method returns the value of the property as an object of type T.
 * <p>
 * Implementations of this interface are expected to provide a concrete type for T and implement the getType and getValue methods.
 * <p>
 * Example usage:
 * <p>
 * Property<String> stringProperty = new StringProperty("example");
 * PropertyType type = stringProperty.getType();  // Returns PropertyType.STRING
 * String value = stringProperty.getValue();  // Returns "example"
 */
public interface Property<T> {

    /**
     * Returns the type of the property.
     *
     * @return the type of the property as a PropertyType enum.
     */
    PropertyType getType();

    /**
     * Returns the value of the property.
     *
     * @return the value of the property as an object of type T.
     */
    T getValue();
}
