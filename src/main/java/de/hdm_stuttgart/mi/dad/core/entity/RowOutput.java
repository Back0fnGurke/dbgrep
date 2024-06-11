package de.hdm_stuttgart.mi.dad.core.entity;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValueOutput;

import java.util.List;

/**
 * Record for storing column data of a table row
 *
 * @param columns a List of ColumnValues
 */
public record RowOutput(List<ColumnValueOutput> columns) {

}