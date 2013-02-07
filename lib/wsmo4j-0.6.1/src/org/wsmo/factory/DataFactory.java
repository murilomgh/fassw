/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License along
 with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package org.wsmo.factory;


import java.math.*;
import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;


public interface DataFactory {
    
    public static final String DATAFACTORY_WSMO_FACTORY = "wsmo_factory";

    WsmlDataType createWsmlDataType(IRI typeIRI);

    WsmlDataType createWsmlDataType(String typeIRI);

    DataValue createDataValueFromJavaObject(WsmlDataType type, Object value);

    ComplexDataValue createDataValue(ComplexDataType type, SimpleDataValue[] argumentValues);

    ComplexDataValue createDataValue(ComplexDataType type, SimpleDataValue argumentValues);

    SimpleDataValue createWsmlString(String value);

    SimpleDataValue createWsmlDecimal(BigDecimal value);
    SimpleDataValue createWsmlDecimal(String value);

    SimpleDataValue createWsmlInteger(BigInteger value);
    SimpleDataValue createWsmlInteger(String value);

    ComplexDataValue createWsmlFloat(Float value);
    ComplexDataValue createWsmlFloat(String value);

    ComplexDataValue createWsmlDouble(Double value);
    ComplexDataValue createWsmlDouble(String value);

    ComplexDataValue createWsmlBoolean(Boolean value);
    ComplexDataValue createWsmlBoolean(String value);

    ComplexDataValue createWsmlDuration(int year, int month, int day, int hour, int minute, int second);
    ComplexDataValue createWsmlDuration(String year, String month, String day, String hour, String minute, String second);

    ComplexDataValue createWsmlDateTime(Calendar value);
    ComplexDataValue createWsmlDateTime(int year, int month, int day, int hour, int minute, int second, int tzHour, int tzMinute);
    ComplexDataValue createWsmlDateTime(String year, String month, String day, String hour, String minute, String second, String tzHour, String tzMinute);

    ComplexDataValue createWsmlTime(Calendar value);
    ComplexDataValue createWsmlTime(int hour, int minute, int second, int tzHour, int tzMinute);
    ComplexDataValue createWsmlTime(String hour, String minute, String second, String tzHour, String tzMinute);

    ComplexDataValue createWsmlDate(Calendar value);
    ComplexDataValue createWsmlDate(int year, int month, int day, int tzHour, int tzMinute);
    ComplexDataValue createWsmlDate(String year, String month, String day, String tzHour, String tzMinute);

    ComplexDataValue createWsmlGregorianYearMonth(int year, int month);
    ComplexDataValue createWsmlGregorianYearMonth(String year, String month);

    ComplexDataValue createWsmlGregorianYear(int year);
    ComplexDataValue createWsmlGregorianYear(String year);

    ComplexDataValue createWsmlGregorianMonthDay(int month, int day);
    ComplexDataValue createWsmlGregorianMonthDay(String month, String day);

    ComplexDataValue createWsmlGregorianMonth(int month);
    ComplexDataValue createWsmlGregorianMonth(String month);

    ComplexDataValue createWsmlGregorianDay(int day);
    ComplexDataValue createWsmlGregorianDay(String day);

    ComplexDataValue creatWsmlHexBinary(byte[] value);

    ComplexDataValue createWsmlBase64Binary(byte[] value);
}
/*
 * $Log: DataFactory.java,v $
 * Revision 1.9  2005/09/23 12:20:42  holgerlausen
 * *** empty log message ***
 *
 * Revision 1.8  2005/09/17 08:50:15  vassil_momtchev
 * wsmo_factory constant added to be used in the map of the constructor
 *
 * Revision 1.7  2005/09/16 12:07:05  marin_dimitrov
 * wsmo4j.properties moved
 *
 * Revision 1.6  2005/09/15 15:36:19  holgerlausen
 * additional createXXX methods for data values and /some/ unit tests
 *
 * Revision 1.5  2005/09/12 12:00:22  marin_dimitrov
 * added specific createXXX methods
 *
 * Revision 1.4  2005/09/12 11:16:23  marin_dimitrov
 * overloaded createWsmlDecimal and createWsmlInteger
 *
 * Revision 1.3  2005/09/09 10:45:51  marin_dimitrov
 * formatting
 *
 */