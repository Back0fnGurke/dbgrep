package de.hdm_stuttgart.mi.dad.core.entity;

import org.apache.logging.log4j.core.config.Property;

import java.util.List;

/**
 * Record storing data of a single row column
 *
 * @param name  the name of the column
 * @param value value of the column
 */
public record ColumnValueOutput(String name, String value, List<Property> properties) {

    private boolean isMatch(String value, List<Property> properties){
        return false;
    }
}