package de.hdm_stuttgart.mi.dad.core.entity;

/**
 * Record storing data of a single row column
 *
 * @param name  the name of the column
 * @param value value of the column
 */
public record ColumnValue(String name, String value) {

}
