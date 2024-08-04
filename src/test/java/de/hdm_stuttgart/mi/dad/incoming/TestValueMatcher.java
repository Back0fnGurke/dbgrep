package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.property.properties.PropertyFactory;
import de.hdm_stuttgart.mi.dad.incoming.output.ValueMatcher;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestValueMatcher {

    @Test
    void testRegex() {
        assertTrue(ValueMatcher.isMatch(
                "Mia Winter",
                List.of(
                        PropertyFactory.createProperty(REGEX, Pattern.compile("Mia.*?"))
                )
        ));
        assertFalse(ValueMatcher.isMatch(
                "Mia Winter",
                List.of(
                        PropertyFactory.createProperty(REGEX, Pattern.compile("Alexa"))
                )
        ));
    }

    @Test
    void testLike() {
        assertTrue(ValueMatcher.isMatch(
                "Mia Winter",
                List.of(
                        PropertyFactory.createProperty(LIKE, Pattern.compile("Mia%"))
                )
        ));
        assertTrue(ValueMatcher.isMatch(
                "Mia Winter",
                List.of(
                        PropertyFactory.createProperty(LIKE, Pattern.compile("M_a%"))
                )
        ));
        assertFalse(ValueMatcher.isMatch(
                "Mia Winter",
                List.of(
                        PropertyFactory.createProperty(LIKE, Pattern.compile("Mia"))
                )
        ));
    }

    @Test
    void testEqual() {
        assertTrue(ValueMatcher.isMatch(
                "25",
                List.of(
                        PropertyFactory.createProperty(EQUAL, new BigDecimal(25))
                )
        ));
        assertFalse(ValueMatcher.isMatch(
                "25",
                List.of(
                        PropertyFactory.createProperty(EQUAL, new BigDecimal(55))
                )
        ));
    }

    @Test
    void testGreaterNumeric() {
        assertFalse(ValueMatcher.isMatch(
                "25",
                List.of(
                        PropertyFactory.createProperty(GREATER_NUMERIC, new BigDecimal(25))
                )
        ));
        assertFalse(ValueMatcher.isMatch(
                "25",
                List.of(
                        PropertyFactory.createProperty(GREATER_NUMERIC, new BigDecimal(55))
                )
        ));
        assertTrue(ValueMatcher.isMatch(
                "25",
                List.of(
                        PropertyFactory.createProperty(GREATER_NUMERIC, new BigDecimal(15))
                )
        ));
    }

    @Test
    void testGreaterDate() {
        assertTrue(ValueMatcher.isMatch(
                "1996-05-25",
                List.of(
                        PropertyFactory.createProperty(GREATER_DATE, LocalDate.of(1996, 5, 20))
                )
        ));
        assertTrue(ValueMatcher.isMatch(
                "1996-05-25",
                List.of(
                        PropertyFactory.createProperty(GREATER_DATE, LocalDate.of(1996, 2, 25))
                )
        ));
        assertTrue(ValueMatcher.isMatch(
                "1996-05-25",
                List.of(
                        PropertyFactory.createProperty(GREATER_DATE, LocalDate.of(1896, 5, 25))
                )
        ));
        assertFalse(ValueMatcher.isMatch(
                "1996-05-25",
                List.of(
                        PropertyFactory.createProperty(GREATER_DATE, LocalDate.of(1996, 5, 25))
                )
        ));
        assertFalse(ValueMatcher.isMatch(
                "1996-05-25",
                List.of(
                        PropertyFactory.createProperty(GREATER_DATE, LocalDate.of(1996, 12, 25))
                )
        ));
    }

    @Test
    void testRangeNumeric() {
        final BigDecimal[] range = {new BigDecimal(20), new BigDecimal(30)};
        assertTrue(ValueMatcher.isMatch(
                "25",
                List.of(
                        PropertyFactory.createProperty(RANGE_NUMERIC, range)
                )
        ));

        final BigDecimal[] range2 = {new BigDecimal(30), new BigDecimal(40)};
        assertFalse(ValueMatcher.isMatch(
                "25",
                List.of(
                        PropertyFactory.createProperty(RANGE_NUMERIC, range2)
                )
        ));

        final BigDecimal[] range3 = {new BigDecimal(25), new BigDecimal(25)};
        assertTrue(ValueMatcher.isMatch(
                "25",
                List.of(
                        PropertyFactory.createProperty(RANGE_NUMERIC, range3)
                )
        ));
    }
}
