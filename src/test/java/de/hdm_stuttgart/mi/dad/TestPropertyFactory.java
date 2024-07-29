package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
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
        Property property = PropertyFactory.createProperty(PropertyType.GREATERNUMERIC, new BigDecimal("10"));
        assertTrue(property.getType().equals(PropertyType.GREATERNUMERIC));
    }

    @Test
    void testCreatePropertyGreaterDate() {
        Property property = PropertyFactory.createProperty(PropertyType.GREATERDATE, LocalDate.now());
        assertTrue(property.getType().equals(PropertyType.GREATERDATE));
    }

    @Test
    void testCreatePropertyRangeNumeric() {
        Property property = PropertyFactory.createProperty(PropertyType.RANGENUMERIC, new BigDecimal[]{new BigDecimal("10"), new BigDecimal("20")});
        assertTrue(property.getType().equals(PropertyType.RANGENUMERIC));
    }

    @Test
    void testCreatePropertyInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.REGEX, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.LIKE, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.EQUAL, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.GREATERNUMERIC, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.GREATERDATE, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.RANGENUMERIC, "test"));
        assertThrows(IllegalArgumentException.class, () -> PropertyFactory.createProperty(PropertyType.REGEX, "test"));
    }
}
