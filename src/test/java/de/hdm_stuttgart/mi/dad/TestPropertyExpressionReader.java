package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import de.hdm_stuttgart.mi.dad.outgoing.PropertyExpressionReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestPropertyExpressionReader {

    @Test
    void testReadPropertyExpressions() throws IOException {
        Map<PropertyType, String> propertyExpressions = PropertyExpressionReader.readPropertyExpressions("TestPropertyExpressionReader/test_property_expressions.json");

        assertNotNull(propertyExpressions, "Property expressions map should not be null");
        assertEquals(6, propertyExpressions.size(), "Property expressions map should contain 6 entries");

        assertEquals("%s::text ~ ?", propertyExpressions.get(PropertyType.REGEX));
        assertEquals("%s::text LIKE ?", propertyExpressions.get(PropertyType.LIKE));
        assertEquals("%s::numeric = ?::numeric", propertyExpressions.get(PropertyType.EQUAL));
        assertEquals("%s::numeric > ?::numeric", propertyExpressions.get(PropertyType.GREATER_NUMERIC));
        assertEquals("%s::date > ?::date", propertyExpressions.get(PropertyType.GREATER_DATE));
        assertEquals("%s::numeric BETWEEN ?::numeric AND ?::numeric", propertyExpressions.get(PropertyType.RANGE_NUMERIC));
    }
}
