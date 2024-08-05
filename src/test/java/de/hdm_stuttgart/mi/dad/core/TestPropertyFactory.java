package de.hdm_stuttgart.mi.dad.core;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import de.hdm_stuttgart.mi.dad.core.property.properties.PropertyFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPropertyFactory {

    @Test
    void testCreatePropertyRegex() {
        Property property = PropertyFactory.createProperty(PropertyType.REGEX, Pattern.compile("test"));
        assertTrue(property.getType().equals(PropertyType.REGEX));
    }

    @Test
    void testCreatePropertyLike() {
        Property property = PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test"));
        assertTrue(property.getType().equals(PropertyType.LIKE));
    }

    @Test
    void testCreatePropertyEqual() {
        Property property = PropertyFactory.createProperty(PropertyType.EQUAL, new BigDecimal("10"));
        assertTrue(property.getType().equals(PropertyType.EQUAL));
    }

    @Test
    void testCreatePropertyGreaterNumeric() {
        Property property = PropertyFactory.createProperty(PropertyType.GREATER_NUMERIC, new BigDecimal("10"));
        assertTrue(property.getType().equals(PropertyType.GREATER_NUMERIC));
    }

    @Test
    void testCreatePropertyGreaterDate() {
        Property property = PropertyFactory.createProperty(PropertyType.GREATER_DATE, LocalDate.now());
        assertTrue(property.getType().equals(PropertyType.GREATER_DATE));
    }

    @Test
    void testCreatePropertyRangeNumeric() {
        Property property = PropertyFactory.createProperty(PropertyType.RANGE_NUMERIC, new BigDecimal[]{new BigDecimal("10"), new BigDecimal("20")});
        assertTrue(property.getType().equals(PropertyType.RANGE_NUMERIC));
    }

    @Test
    void testCreatePropertyInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.REGEX, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.LIKE, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.EQUAL, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.GREATER_NUMERIC, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.GREATER_DATE, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.RANGE_NUMERIC, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.REGEX, "test"));
    }
}
