package de.hdm_stuttgart.mi.dad.core.entity;

import java.util.List;

/**
 * Record for storing data of a table
 *
 * @param name the name of the table
 * @param rows a List of Rows
 */
public record TableOutput(String name, List<RowOutput> rows) {

}