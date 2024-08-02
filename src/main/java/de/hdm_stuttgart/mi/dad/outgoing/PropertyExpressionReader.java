package de.hdm_stuttgart.mi.dad.outgoing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * The PropertyExpressionReader class is responsible for reading the propertyExpressions map from a JSON file.
 * It uses the Jackson library to parse the JSON file and convert it into an EnumMap.
 * <p>
 * The class is not meant to be instantiated. Instead, use its static method to read the propertyExpressions map.
 * <p>
 * The class has a single static method `readPropertyExpressions` that reads the propertyExpressions map from a JSON file.
 * The file name is passed as a string, and the method returns an EnumMap of PropertyType and String.
 * <p>
 * Example usage:
 * <p>
 * EnumMap<PropertyType, String> propertyExpressions = PropertyExpressionReader.readPropertyExpressions("propertyExpressions.json");
 */
public class PropertyExpressionReader {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(PropertyExpressionReader.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private PropertyExpressionReader() {
    }

    /**
     * Reads the propertyExpressions map from a JSON file.
     *
     * @param fileName the name of the JSON file.
     * @return the propertyExpressions map.
     * @throws IOException if an I/O error occurs.
     */
    public static Map<PropertyType, String> readPropertyExpressions(final String fileName) throws IOException {
        log.debug("Reading property expressions from file: {}", fileName);

        final ObjectMapper mapper = new ObjectMapper();
        final InputStream inputStream = PropertyExpressionReader.class.getClassLoader().getResourceAsStream(fileName);
        final TypeReference<EnumMap<PropertyType, String>> typeRef = new TypeReference<>() {
        };
        return mapper.readValue(inputStream, typeRef);
    }
}
