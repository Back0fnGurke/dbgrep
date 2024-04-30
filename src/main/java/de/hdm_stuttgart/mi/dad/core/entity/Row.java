package de.hdm_stuttgart.mi.dad.core.entity;

import java.util.List;

/**
 * Record for storing column data of a table row
 *
 * @param columns a List of ColumnValues
 */
public record Row(List<ColumnValue> columns) {

}
