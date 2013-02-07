/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2005, University of Innsbruck, Austria
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
package org.deri.wsmo4j.factory;


import java.math.*;
import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;

import com.ontotext.wsmo4j.ontology.*;


/**
 * Interface or class description
 *
 * <pre>
 * Created on Sep 5, 2005
 * Committed by $Author$
 * </pre>
 *
 * @author Holger Lausne (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public class DataFactoryImpl
        implements DataFactory {

    /**
     * contains the IRI of an wsml data type and the corresponding array of
     * possible constuctors (SimpleTypes)
     */
    private HashMap <String, SimpleDataType[]> complexDataTypeConstructors = new HashMap  <String, SimpleDataType[]> ();
    /**
     * contains the iri of an wsml data tyoe and the coressponding java value
     */
    private HashMap <String, Class> complexDataTypeJavaMapping = new HashMap <String, Class> ();

    private HashMap <String, SimpleDataType> simpleDataTypes = new HashMap <String, SimpleDataType> ();

    private SimpleDataType decimal;

    private SimpleDataType integer;

    private SimpleDataType string;

    private WsmoFactory factory;
    
    public DataFactoryImpl() {
        this(null);
    }

    /**
     * The DataFactory is initialised based on the supplied preferences. 
     * The properties map can contain the factories to be used as Strings or as 
     * instances. If a factory to use is only indicated as String, the constructor
     * needs to create an instance of this given factory.
     * 
     * @param map
     */
    public DataFactoryImpl(Map map) {
        if (map != null && map.containsKey(DATAFACTORY_WSMO_FACTORY)) {
            factory = (WsmoFactory) map.get(DATAFACTORY_WSMO_FACTORY);
        }
        if (factory == null) {
            factory = Factory.createWsmoFactory(null);
        }

        decimal = new SimpleDataTypeImpl(factory.createIRI(WsmlDataType.WSML_DECIMAL));
        integer = new SimpleDataTypeImpl(factory.createIRI(WsmlDataType.WSML_INTEGER));
        string = new SimpleDataTypeImpl(factory.createIRI(WsmlDataType.WSML_STRING));

        simpleDataTypes.put(WsmlDataType.WSML_INTEGER, integer);
        simpleDataTypes.put(WsmlDataType.WSML_STRING, string);
        simpleDataTypes.put(WsmlDataType.WSML_DECIMAL, decimal);

        complexDataTypeConstructors.put(WsmlDataType.WSML_FLOAT,
                                        new SimpleDataType[] {string});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_FLOAT, Float.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_DOUBLE,
                                        new SimpleDataType[] {string});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_DOUBLE, Double.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_IRI,
                                        new SimpleDataType[] {string});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_IRI, String.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_SQNAME,
                                        new SimpleDataType[] {string, string});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_SQNAME, String[].class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_BOOLEAN,
                                        new SimpleDataType[] {string});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_BOOLEAN, Boolean.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_DURATION,
                                        new SimpleDataType[] {integer, integer, integer,
                                        integer, integer, integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_DURATION, String.class);

        //2 last are optional
        complexDataTypeConstructors.put(WsmlDataType.WSML_DATETIME,
                                        new SimpleDataType[] {integer, integer, integer,
                                        integer, integer, integer, integer, integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_DATETIME, Calendar.class);

        //2 last are optional
        complexDataTypeConstructors.put(WsmlDataType.WSML_TIME,
                                        new SimpleDataType[] {integer, integer, integer,
                                        integer, integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_TIME, Calendar.class);

        //2 last are optional
        complexDataTypeConstructors.put(WsmlDataType.WSML_DATE,
                                        new SimpleDataType[] {integer, integer, integer,
                                        integer, integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_DATE, Calendar.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_GYEARMONTH,
                                        new SimpleDataType[] {integer, integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_GYEARMONTH, Integer[].class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_GMONTHDAY,
                                        new SimpleDataType[] {integer, integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_GMONTHDAY, Integer[].class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_GYEAR,
                                        new SimpleDataType[] {integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_GYEAR, Integer.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_GDAY,
                                        new SimpleDataType[] {integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_GDAY, Integer.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_GMONTH,
                                        new SimpleDataType[] {integer});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_GMONTH, Integer.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_HEXBINARY,
                                        new SimpleDataType[] {string});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_GDAY, String.class);

        complexDataTypeConstructors.put(WsmlDataType.WSML_BASE64BINARY,
                                        new SimpleDataType[] {string});
        complexDataTypeJavaMapping.put(WsmlDataType.WSML_GDAY, String.class);
    }

    public ComplexDataValue createDataValue(ComplexDataType type, SimpleDataValue argumentValues) {
        return createDataValue(type, new SimpleDataValue[] {argumentValues});
    }

    public ComplexDataValue createDataValue(ComplexDataType type, SimpleDataValue[] argumentValues) {
        String typeIRIString = type.getIRI().toString();
        //check correct arity
        if (type.getArity() != argumentValues.length) {
            if (!((typeIRIString.equals(WsmlDataType.WSML_DATE) ||
                   typeIRIString.equals(WsmlDataType.WSML_DATETIME) ||
                   typeIRIString.equals(WsmlDataType.WSML_TIME)) &&
                  type.getArity() - 2 == argumentValues.length)) {
                throw new IllegalArgumentException("Data type " + typeIRIString +
                        " must have " + type.getArity() + " number of arguments");
            }
        }
        //check if data type is known
        SimpleDataType[] types = complexDataTypeConstructors.get(typeIRIString);
        if (types == null) {
            throw new IllegalArgumentException("Data type" + type.getIRI().toString() +
                                               " not supported!");
        }
        //check if arguments are of correct type
        for (byte i = 0; i < argumentValues.length; i++) {
            if (!argumentValues[i].getType().equals(types[i])) {
                throw new IllegalArgumentException("Wrong type of Argument for complex datatype " +
                        typeIRIString + ": Found at position " + i + ": " +
                        argumentValues[i].getType().getIRI() + " Expected: " + types[i].getIRI());
            }
        }
        //finally pass to complexDataValueImpl
        return new ComplexDataValueImpl(type, argumentValues);
    }

    /**
     *
     * @param type
     * @param value
     * @return
     */
    public DataValue createDataValueFromJavaObject(WsmlDataType type, Object value) {
        if (type instanceof SimpleDataType) {
            if (type.getIRI().toString().equals(WsmlDataType.WSML_INTEGER)){
                return createWsmlInteger(value.toString());
            }
            else if (type.getIRI().toString().equals(WsmlDataType.WSML_DECIMAL)) {
                return createWsmlDecimal(value.toString());
            }
            else {
                return createWsmlString(value.toString());
            }
        }
        else { //ComplexDataType
            //check if correct java object is supplied:
            Class javaType = complexDataTypeJavaMapping.get(type.getIRI().toString());
            if (javaType == null) {
                throw new IllegalArgumentException("Data type" + type.getIRI().toString() +
                        " not supported!");
            }

            if (!javaType.isInstance(value)) {
                throw new IllegalArgumentException("Java value for type " + type.getIRI().toString() +
                        " must be of type " + javaType + " (Found: " + value.getClass() + ")");
            }
            return new ComplexDataValueImpl((ComplexDataType)type,
                                            createSimpleTypesFromJavaValue((ComplexDataType)type, value));
        }

    }

    public SimpleDataValue createWsmlDecimal(BigDecimal value) {
        return new SimpleDataValueImpl(simpleDataTypes.get(WsmlDataType.WSML_DECIMAL), value);
    }

    public SimpleDataValue createWsmlInteger(BigInteger value) {
        return new SimpleDataValueImpl( simpleDataTypes.get(WsmlDataType.WSML_INTEGER), value);
    }

    public SimpleDataValue createWsmlString(String value) {
        return new SimpleDataValueImpl( simpleDataTypes.get(WsmlDataType.WSML_STRING), value);
    }

    public WsmlDataType createWsmlDataType(IRI typeIRI) {
        return createWsmlDataType(typeIRI.toString());
    }

    public WsmlDataType createWsmlDataType(String typeIRI) {
        if (simpleDataTypes.get(typeIRI) != null) {
            return simpleDataTypes.get(typeIRI);
        }
        SimpleDataType[] types = complexDataTypeConstructors.get(typeIRI);
        if (types == null) {
            throw new IllegalArgumentException("Given datatype: <" + typeIRI + "> not supprted");
        }
        IRI iri = factory.createIRI(typeIRI);
        return new ComplexDataTypeImpl(iri, types);
    }

    private SimpleDataValue[] createSimpleTypesFromJavaValue(ComplexDataType type, Object value) {
        String typeIRI = type.getIRI().toString();
        if (typeIRI.equals(WsmlDataType.WSML_FLOAT) ||
            typeIRI.equals(WsmlDataType.WSML_DOUBLE) ||
            typeIRI.equals(WsmlDataType.WSML_BOOLEAN)) {
            return new SimpleDataValue[] {
                    createWsmlString(value.toString())};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_IRI)) {
            return new SimpleDataValue[] {
                    createWsmlString(value.toString())};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_SQNAME)) {
            String[] stringArray = (String[])value;
            if (stringArray.length != 2) {
                throw new IllegalArgumentException("datatype " + typeIRI + " must have 2 strings as argument!");
            }
            return new SimpleDataValue[] {
                    createWsmlString(stringArray[0]),
                    createWsmlString(stringArray[1])};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_DURATION)) {
            //"-PnYn MnDTnH nMnS" lexical duration representation
            return new SimpleDataValue[] {
                    createWsmlString(value.toString())};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_DATETIME)) {
            Calendar cal = (Calendar)value;
            if (cal.isSet(Calendar.ZONE_OFFSET)) {
                int zoneOffSetHour = cal.getTimeZone().getRawOffset() / 1000 / 60;
                int zoneOffSetMinute = cal.get(Calendar.ZONE_OFFSET) / 1000 % 60;
                return new SimpleDataValue[] {
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.YEAR))),
                        createWsmlInteger(new BigInteger("" + (cal.get(Calendar.MONTH) + 1))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.DAY_OF_MONTH))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.HOUR_OF_DAY))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.MINUTE))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.SECOND))),
                        createWsmlInteger(new BigInteger("" + zoneOffSetHour)),
                        createWsmlInteger(new BigInteger("" + zoneOffSetMinute))};
            }
            else {
                return new SimpleDataValue[] {
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.YEAR))),
                        createWsmlInteger(new BigInteger("" + (cal.get(Calendar.MONTH) + 1))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.DAY_OF_MONTH))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.HOUR_OF_DAY))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.MINUTE))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.SECOND)))};
            }
        }
        else if (typeIRI.equals(WsmlDataType.WSML_DATE)) {
            Calendar cal = (Calendar)value;
            if (cal.isSet(Calendar.ZONE_OFFSET)) {
                int zoneOffSetHour = cal.getTimeZone().getRawOffset() / 1000 / 60;
                int zoneOffSetMinute = cal.get(Calendar.ZONE_OFFSET) / 1000 % 60;
                return new SimpleDataValue[] {
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.YEAR))),
                        createWsmlInteger(new BigInteger("" + (cal.get(Calendar.MONTH) + 1))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.DAY_OF_MONTH))),
                        createWsmlInteger(new BigInteger("" + zoneOffSetHour)),
                        createWsmlInteger(new BigInteger("" + zoneOffSetMinute))};
            }
            else {
                return new SimpleDataValue[] {
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.YEAR))),
                        createWsmlInteger(new BigInteger("" + (cal.get(Calendar.MONTH) + 1))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.DAY_OF_MONTH)))};
            }
        }
        else if (typeIRI.equals(WsmlDataType.WSML_TIME)) {
            Calendar cal = (Calendar)value;
            if (cal.isSet(Calendar.ZONE_OFFSET)) {
                int zoneOffSetHour = cal.getTimeZone().getRawOffset() / 1000 / 60;
                int zoneOffSetMinute = cal.get(Calendar.ZONE_OFFSET) / 1000 % 60;
                return new SimpleDataValue[] {
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.HOUR_OF_DAY))),
                        createWsmlInteger(new BigInteger("" + (cal.get(Calendar.MINUTE)))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.SECOND))),
                        createWsmlInteger(new BigInteger("" + zoneOffSetHour)),
                        createWsmlInteger(new BigInteger("" + zoneOffSetMinute))};
            }
            else {
                return new SimpleDataValue[] {
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.HOUR_OF_DAY))),
                        createWsmlInteger(new BigInteger("" + (cal.get(Calendar.MINUTE)))),
                        createWsmlInteger(new BigInteger("" + cal.get(Calendar.SECOND)))};
            }
        }
        else if (typeIRI.equals(WsmlDataType.WSML_GYEARMONTH)) {
            Integer[] integer = (Integer[])value;
            if (integer.length != 2) {
                throw new IllegalArgumentException("datatype " + typeIRI + " must have 2 Integers as argument!");
            }
            return new SimpleDataValue[] {
                    createWsmlInteger(new BigInteger("" + integer[0])),
                    createWsmlInteger(new BigInteger("" + integer[1]))};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_GMONTHDAY)) {
            Integer[] integer = (Integer[])value;
            if (integer.length != 2) {
                throw new IllegalArgumentException("datatype " + typeIRI + " must have 2 Integers as argument!");
            }
            return new SimpleDataValue[] {
                    createWsmlInteger(new BigInteger("" + integer[0])),
                    createWsmlInteger(new BigInteger("" + integer[1]))};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_GYEAR)) {
            return new SimpleDataValue[] {
                    createWsmlInteger(new BigInteger("" + value))};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_GDAY)) {
            return new SimpleDataValue[] {
                    createWsmlInteger(new BigInteger("" + value))};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_GMONTH)) {
            return new SimpleDataValue[] {
                    createWsmlInteger(new BigInteger("" + value))};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_HEXBINARY)) {
            return new SimpleDataValue[] {
                    createWsmlString((String)value)};
        }
        else if (typeIRI.equals(WsmlDataType.WSML_BASE64BINARY)) {
            return new SimpleDataValue[] {
                    createWsmlString((String)value)};
        }
        else {
            throw new UnsupportedOperationException("DataType " + typeIRI + " not supported!");
        }
    }

    public ComplexDataValue createWsmlBoolean(Boolean value) {
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_BOOLEAN),
                value);
    }

    public ComplexDataValue createWsmlBoolean(String value){
        Boolean bValue = Boolean.valueOf(value);
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_BOOLEAN),
                bValue);
    }


    public ComplexDataValue createWsmlDuration(int year, int month, int day, int hour, int minute, int second){
        //"-PnYnMnDTnHnMnS"
        //FIXME: this is ignoring the duartion value space and equivalence relation!
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_DURATION),
                "P"+year+"Y"+month+"M"+day+"DT"+hour+"H"+minute+"M"+second+"S");
    }

    public ComplexDataValue createWsmlDuration(String year, String month, String day, String hour, String minute, String second){
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_DURATION),
                "P"+year+"Y"+month+"M"+day+"DT"+hour+"H"+minute+"M"+second+"S");
    }


    public ComplexDataValue createWsmlDateTime(Calendar value){
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_DATETIME),
                value);
    }

    public ComplexDataValue createWsmlDateTime(int year, int month, int day, int hour, int minute, int second, int tzHour, int tzMinute){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_DATETIME),
                new SimpleDataValue[]{
                    createWsmlInteger(year),
                    createWsmlInteger(month),
                    createWsmlInteger(day),
                    createWsmlInteger(hour),
                    createWsmlInteger(minute),
                    createWsmlInteger(second),
                    createWsmlInteger(tzHour),
                    createWsmlInteger(tzMinute),
                });
    }

    public ComplexDataValue createWsmlDateTime(String year, String month, String day, String hour, String minute, String second, String tzHour, String tzMinute){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_DATETIME),
                new SimpleDataValue[]{
                    createWsmlInteger(year),
                    createWsmlInteger(month),
                    createWsmlInteger(day),
                    createWsmlInteger(hour),
                    createWsmlInteger(minute),
                    createWsmlInteger(second),
                    createWsmlInteger(tzHour),
                    createWsmlInteger(tzMinute),
                });    
    }


    public ComplexDataValue createWsmlTime(Calendar value){
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_TIME),
                value);    }

    public ComplexDataValue createWsmlTime(int hour, int minute, int second, int tzHour, int tzMinute){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_TIME),
                new SimpleDataValue[]{
                    createWsmlInteger(hour),
                    createWsmlInteger(minute),
                    createWsmlInteger(second),
                    createWsmlInteger(tzHour),
                    createWsmlInteger(tzMinute),
                });    
    }

    public ComplexDataValue createWsmlTime(String hour, String minute, String second, String tzHour, String tzMinute){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_TIME),
                new SimpleDataValue[]{
                    createWsmlInteger(hour),
                    createWsmlInteger(minute),
                    createWsmlInteger(second),
                    createWsmlInteger(tzHour),
                    createWsmlInteger(tzMinute),
                }); 
    }


    public ComplexDataValue createWsmlDate(Calendar value){
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_DATE),
                value);
    }

    public ComplexDataValue createWsmlDate(int year, int month, int day, int tzHour, int tzMinute){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_DATE),
                new SimpleDataValue[]{
                    createWsmlInteger(year),
                    createWsmlInteger(month),
                    createWsmlInteger(day),
                    createWsmlInteger(tzHour),
                    createWsmlInteger(tzMinute),
                });  
    }

    public ComplexDataValue createWsmlDate(String year, String month, String day, String tzHour, String tzMinute){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_DATE),
                new SimpleDataValue[]{
                    createWsmlInteger(year),
                    createWsmlInteger(month),
                    createWsmlInteger(day),
                    createWsmlInteger(tzHour),
                    createWsmlInteger(tzMinute),
                });  
    }


    public ComplexDataValue createWsmlGregorianYearMonth(int year, int month){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GYEARMONTH),
                new SimpleDataValue[]{
                    createWsmlInteger(year),
                    createWsmlInteger(month),
                });  
    }

    public ComplexDataValue createWsmlGregorianYearMonth(String year, String month){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GYEARMONTH),
                new SimpleDataValue[]{
                    createWsmlInteger(year),
                    createWsmlInteger(month),
                });  
    }


    public ComplexDataValue createWsmlGregorianYear(int year){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GYEAR),
                new SimpleDataValue[]{
                    createWsmlInteger(year),
                });  
    }

    public ComplexDataValue createWsmlGregorianYear(String year){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GYEAR),
                new SimpleDataValue[]{
                    createWsmlInteger(year),
                });  
    }


    public ComplexDataValue createWsmlGregorianMonthDay(int month, int day){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GMONTHDAY),
                new SimpleDataValue[]{
                    createWsmlInteger(month),
                    createWsmlInteger(day),
                });  
    }

    public ComplexDataValue createWsmlGregorianMonthDay(String month, String day){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GMONTHDAY),
                new SimpleDataValue[]{
                    createWsmlInteger(month),
                    createWsmlInteger(day),
                });  
    }


    public ComplexDataValue createWsmlGregorianMonth(int month){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GMONTH),
                new SimpleDataValue[]{
                    createWsmlInteger(month),
                });  
    }

    public ComplexDataValue createWsmlGregorianMonth(String month){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GMONTH),
                new SimpleDataValue[]{
                    createWsmlInteger(month),
                });  
    }


    public ComplexDataValue createWsmlGregorianDay(int day){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GDAY),
                new SimpleDataValue[]{
                    createWsmlInteger(day),
                });  
    }

    public ComplexDataValue createWsmlGregorianDay(String day){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_GDAY),
                new SimpleDataValue[]{
                    createWsmlInteger(day),
                });  
    }
    
    public ComplexDataValue creatWsmlHexBinary(byte[] value){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_HEXBINARY),
                new SimpleDataValue[]{
                    createWsmlString(new String(value)),
                });  
    }

    public ComplexDataValue createWsmlBase64Binary(byte[] value){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_BASE64BINARY),
                new SimpleDataValue[]{
                    createWsmlString(new String(value)),
                });  
    }

    public SimpleDataValue createWsmlDecimal(String value){
    	//FIXME we're just doing sanity checking and correction here, somewhere in
    	//the calltrace an additional space is inserted after the sign of the decimal
    	//number, even if the originally parsed expression didn't have a space
    	value = value.replaceFirst("-\\s+", "-");
        return createWsmlDecimal(new BigDecimal(value));  
    }

    public SimpleDataValue createWsmlInteger(String value){
        return createWsmlInteger(new BigInteger(value));
    }

    public SimpleDataValue createWsmlInteger(int value){
        return createWsmlInteger(new BigInteger(value+""));
    }

    public ComplexDataValue createWsmlFloat(String value){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_FLOAT),
                new SimpleDataValue[]{
                    createWsmlString(value),
                });  
    }

    public ComplexDataValue createWsmlFloat(Float value){
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_FLOAT),
                value);  
    }

    public ComplexDataValue createWsmlDouble(Double value){
        return (ComplexDataValue)createDataValueFromJavaObject(
                createWsmlDataType(WsmlDataType.WSML_DOUBLE),
                value);     
    }

    public ComplexDataValue createWsmlDouble(String value){
        return createDataValue(
                (ComplexDataType)createWsmlDataType(WsmlDataType.WSML_DOUBLE),
                new SimpleDataValue[]{
                    createWsmlString(value),
                }); 
    }

}
/*
 * $Log$
 * Revision 1.14  2007/04/02 12:13:24  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.13  2006/02/28 16:55:12  haselwanter
 * Space-proofing the decimal creation.
 *
 * Revision 1.12  2006/01/09 14:18:43  nathaliest
 * javadoc added
 *
 * Revision 1.11  2005/10/12 13:30:54  ohamano
 * fix type determination for simple data types
 *
 * Revision 1.10  2005/09/30 11:56:12  alex_simov
 * minor fixes
 *
 * Revision 1.9  2005/09/23 12:20:42  holgerlausen
 * *** empty log message ***
 *
 * Revision 1.8  2005/09/17 08:50:53  vassil_momtchev
 * constructor to accept map added
 *
 * Revision 1.7  2005/09/15 15:36:19  holgerlausen
 * additional createXXX methods for data values and /some/ unit tests
 *
 * Revision 1.6  2005/09/12 12:10:53  marin_dimitrov
 * added specific createXXX methods
 *
 * Revision 1.5  2005/09/12 12:05:53  marin_dimitrov
 * added specific createXXX methods
 *
 * Revision 1.4  2005/09/09 15:51:41  marin_dimitrov
 * formatting
 *
 * Revision 1.3  2005/09/09 10:00:15  holgerlausen
 * fixed serializing problem eported by jan (for data types in relations)
 *
 * Revision 1.2  2005/09/08 15:16:27  holgerlausen
 * fixes for parsing and serializing datatypes
 *
 * Revision 1.1  2005/09/06 18:34:14  holgerlausen
 * added implementation of datatype factory
 *
 */
