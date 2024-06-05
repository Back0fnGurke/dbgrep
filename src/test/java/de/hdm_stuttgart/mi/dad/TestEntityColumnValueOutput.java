package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValueOutput;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import org.checkerframework.checker.regex.qual.Regex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestEntityColumnValueOutput {

    @Test
    public void testRegex(){
        ColumnValueOutput column = new ColumnValueOutput("Name", "Mia Winter",
                List.of(
                        PropertyFactory.getProperty(REGEX, Pattern.compile("Mia.*?"))
                ));

        assertTrue(column.isMatch());
    }

    @Test
    public void testLike(){
        ColumnValueOutput column = new ColumnValueOutput("Name", "Mia Winter",
                List.of(
                        PropertyFactory.getProperty(LIKE, Pattern.compile("Mia%"))
                ));

        assertTrue(column.isMatch());
    }
}
