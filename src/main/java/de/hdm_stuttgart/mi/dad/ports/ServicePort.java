package de.hdm_stuttgart.mi.dad.ports;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface ServicePort {
    Map<String, List<List<String>>> searchTable(final List<String> tableNames, final Pattern pattern) throws Exception;
}
