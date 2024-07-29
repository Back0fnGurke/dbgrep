package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import de.hdm_stuttgart.mi.dad.incoming.ArgumentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class TestArgumentType {

    @Test
    void testCreatePropertyFromArgumentTypeWithLike() {
        Property<?> actualProperty = assertDoesNotThrow(() -> ArgumentType.createPropertyFromArgumentType(ArgumentType.LIKE, "%r"));
        Property<?> expectedProperty = PropertyFactory.getProperty(PropertyType.LIKE, Pattern.compile("%r"));
        assertEquals(expectedProperty, actualProperty);

        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.LIKE, "{n,m}"));
    }

    @Test
    void testCreatePropertyFromArgumentTypeWithEqual() {
        Property<?> actualProperty = assertDoesNotThrow(() -> ArgumentType.createPropertyFromArgumentType(ArgumentType.EQUAL, "13.07"));
        Property<?> expectedProperty = PropertyFactory.getProperty(PropertyType.EQUAL, new BigDecimal("13.07"));
        assertEquals(expectedProperty, actualProperty);

        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.EQUAL, "string"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.EQUAL, "1o.54"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.EQUAL, "10.54.89"));
    }

    @Test
    void testCreatePropertyFromArgumentTypeWithGreater() {
        Property<?> actualPropertyDate = assertDoesNotThrow(() -> ArgumentType.createPropertyFromArgumentType(ArgumentType.GREATER, "2007-12-24"));
        Property<?> expectedPropertyDate = PropertyFactory.getProperty(PropertyType.GREATER_DATE, LocalDate.parse("2007-12-24"));
        assertEquals(expectedPropertyDate, actualPropertyDate);

        Property<?> actualPropertyNumeric = assertDoesNotThrow(() -> ArgumentType.createPropertyFromArgumentType(ArgumentType.GREATER, "13.07"));
        Property<?> expectedPropertyNumeric = PropertyFactory.getProperty(PropertyType.GREATER_NUMERIC, new BigDecimal("13.07"));
        assertEquals(expectedPropertyNumeric, actualPropertyNumeric);

        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.GREATER, "2007.12.24"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.GREATER, "12-24-2007"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.GREATER, "test"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.GREATER, "1o.5A"));
    }

    @Test
    void testCreatePropertyFromArgumentTypeWithRegex() {
        Property<?> actualProperty = assertDoesNotThrow(() -> ArgumentType.createPropertyFromArgumentType(ArgumentType.REGEX, "%r"));
        Property<?> expectedProperty = PropertyFactory.getProperty(PropertyType.REGEX, Pattern.compile("%r"));
        assertEquals(expectedProperty, actualProperty);

        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.REGEX, "{n,m}"));
    }

    @Test
    void testCreatePropertyFromArgumentTypeWithRange() {
        Property<?> actualProperty = assertDoesNotThrow(() -> ArgumentType.createPropertyFromArgumentType(ArgumentType.RANGE, "13.07,15.67"));
        BigDecimal[] expectedBigDecimal = {new BigDecimal("13.07"), new BigDecimal("15.67")};
        Property<?> expectedProperty = PropertyFactory.getProperty(PropertyType.RANGE_NUMERIC, expectedBigDecimal);
        assertEquals(expectedProperty, actualProperty);

        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.RANGE, "14.987"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.RANGE, "14.987, 16, 23.45"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.RANGE, "14.A34, 16"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.RANGE, "14.34, 16.o2"));
        assertThrows(IllegalArgumentException.class , () -> ArgumentType.createPropertyFromArgumentType(ArgumentType.RANGE, "14.A34, 16.o2"));
    }


}
