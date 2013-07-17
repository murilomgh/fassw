/*
 * Copyright (C) 2013 Murilo Honorio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fassw.util;

/**
 *
 * @author Murilo Honorio
 */
public enum XML_WSMO_BuiltInTypes {
    STRING ("string", "xsd#string"),
    DECIMAL ("decimal", "xsd#decimal"), 
    INTEGER ("integer", "xsd#integer"), 
    FLOAT ("float", "xsd#float"),
    DOUBLE ("double", "xsd#double"),
    BOOLEAN ("boolean", "xsd#boolean"),
    DATETIME ("dateTime", "xsd#dateTime"),
    TIME ("time", "xsd#time"),
    DATE ("date", "xsd#date"),
    GYEARMONTH ("gYearMonth", "xsd#gYearMonth"),
    GYEAR ("gYear", "xsd#gYear"),
    GMONTHDAY ("gMonthDay", "xsd#gMonthDay"),
    GDAY ("gDay", "xsd#gDay"),
    GMONTH ("gMonth", "xsd#gMonth"),
    HEXBINARY ("hexBinary", "xsd#hexBinary");

    private final String xml;
    private final String wsml;
    
    XML_WSMO_BuiltInTypes(String xml, String wsml) {
        this.xml = xml;
        this.wsml = wsml;
    }
    public String wsml() { return wsml; }
    public String xml() { return xml; }
}
