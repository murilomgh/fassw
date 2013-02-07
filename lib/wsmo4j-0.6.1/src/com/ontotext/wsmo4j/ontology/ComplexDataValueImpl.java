/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2005, OntoText Lab. / SIRMA
                     University of Innsbruck, Austria

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

package com.ontotext.wsmo4j.ontology;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import java.math.*;
import java.util.*;

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;

public class ComplexDataValueImpl implements ComplexDataValue {

    private WsmlDataType dataType;
    private SimpleDataValue[] values;
    private Object value;

    public ComplexDataValueImpl(ComplexDataType type, SimpleDataValue[] vals) {
        assert type != null;
        assert vals != null;
        this.dataType = type;
        int arity = vals.length;
        this.values = new SimpleDataValue[arity];
        System.arraycopy(vals, 0, this.values, 0, arity);
        this.value = createJavaValueFromSimpleTypes(type, vals);
    }
    
    public ComplexDataValueImpl(ComplexDataType type, SimpleDataValue vals) {
        this(type, new SimpleDataValue[] {vals});
    }

    public WsmlDataType getType() {
        return dataType;
    }
    
    public int getArity() {
    	return values.length;
    }

    public SimpleDataValue getArgumentValue(byte pos) {
        if (pos < 0
                || pos >= values.length) {
            throw new IllegalArgumentException(
                    "Invalid argument position for type '"
                    + dataType.toString()
                    + "'!");
        }
        return values[pos];
    }

    public String asString() {
        VisitorSerializeWSMLTerms v = new VisitorSerializeWSMLTerms(null);
        this.accept(v);
        return v.getSerializedObject();
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        return asString();
    }
    
    public void accept(Visitor v) {
    	v.visitComplexDataValue(this);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null
                || false == object instanceof ComplexDataValue) {
            return false;
        }
        ComplexDataValue testVal = (ComplexDataValue) object;
        if (false == dataType.equals(testVal.getType())) {
            return false;
        }
        if (dataType.getIRI().toString().equals(WsmlDataType.WSML_DOUBLE)||
                dataType.getIRI().toString().equals(WsmlDataType.WSML_FLOAT)){
            return value.equals(testVal.getValue());
        }
        
        if (testVal.getArity()!=getArity()){
            return false;
        }
        
        for(byte i = 0; i < values.length; i++) {
            if (!values[i].equals(testVal.getArgumentValue(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return dataType.hashCode();
    }
    
    private Object createJavaValueFromSimpleTypes(ComplexDataType type, SimpleDataValue[] v){
        String typeIRI = type.getIRI().toString();
        if (typeIRI.equals(WsmlDataType.WSML_FLOAT)){
            return new Float(values[0].toString());
        }else if (typeIRI.equals(WsmlDataType.WSML_DOUBLE)){
            return new Double(values[0].toString());
        }else if (typeIRI.equals(WsmlDataType.WSML_BOOLEAN)){
            return Boolean.valueOf(values[0].toString());
        }else if (typeIRI.equals(WsmlDataType.WSML_DURATION)){
            //TODO: equality will return wrong results with respect to spec
            //http://www.w3.org/TR/xmlschema-2/#duration
            return values[0].toString();
        }else if (typeIRI.equals(WsmlDataType.WSML_DATETIME)){
            Calendar cal;
            if (values.length==8){ //including offset
                int offset = (((BigInteger) values[6].getValue()).intValue()*60+
                    ((BigInteger) values[7].getValue()).intValue())*1000;
                cal = new GregorianCalendar(new SimpleTimeZone(offset,""));
            }else{
                cal = new GregorianCalendar();
            }
            cal.set(
                    ((BigInteger) values[0].getValue()).intValue(),
                    ((BigInteger) values[1].getValue()).intValue()-1,
                    ((BigInteger) values[2].getValue()).intValue(),
                    ((BigInteger) values[3].getValue()).intValue(),
                    ((BigInteger) values[4].getValue()).intValue(),
                    ((BigInteger) values[5].getValue()).intValue());
            return cal;
        }else if (typeIRI.equals(WsmlDataType.WSML_DATE)){
            Calendar cal;
            if (values.length==5){ //including offset
                int offset = (((BigInteger) values[3].getValue()).intValue()*60+
                    ((BigInteger) values[4].getValue()).intValue())*1000;
                cal = new GregorianCalendar(new SimpleTimeZone(offset,""));
            }else{
                cal = new GregorianCalendar();
            }
            cal.set(
                    ((BigInteger) values[0].getValue()).intValue(),
                    ((BigInteger) values[1].getValue()).intValue()-1,
                    ((BigInteger) values[2].getValue()).intValue());
            return cal;
        }else if (typeIRI.equals(WsmlDataType.WSML_TIME)){
            Calendar cal;
            if (values.length==5){ //including offset
                int offset = (((BigInteger) values[3].getValue()).intValue()*60+
                    ((BigInteger) values[4].getValue()).intValue())*1000;
                cal = new GregorianCalendar(new SimpleTimeZone(offset,""));
            }else{
                cal = new GregorianCalendar();
            }
            cal.set(1970,1,1,
                    ((BigInteger) values[0].getValue()).intValue(),
                    ((BigInteger) values[1].getValue()).intValue(),
                    ((BigInteger) values[2].getValue()).intValue());
            return cal;
        }else if (typeIRI.equals(WsmlDataType.WSML_IRI)){
            throw new UnsupportedOperationException("Data type "+typeIRI+" not supported!");
        }else if (typeIRI.equals(WsmlDataType.WSML_SQNAME)){
            throw new UnsupportedOperationException("Data type "+typeIRI+" not supported!");
        }else if (typeIRI.equals(WsmlDataType.WSML_GYEARMONTH)){
            return new Integer[] {
                    new Integer(""+values[0].getValue()),
                    new Integer(""+values[1].getValue())};
        }else if (typeIRI.equals(WsmlDataType.WSML_GMONTHDAY)){
            return new Integer[] {
                    new Integer(""+values[0].getValue()),
                    new Integer(""+values[1].getValue())};
        }else if (typeIRI.equals(WsmlDataType.WSML_GYEAR)){
            return new Integer(""+values[0].getValue());
        }else if (typeIRI.equals(WsmlDataType.WSML_GDAY)){
            return new Integer(""+values[0].getValue());
        }else if(typeIRI.equals(WsmlDataType.WSML_GMONTH)){
            return new Integer(""+values[0].getValue());
        }else if (typeIRI.equals(WsmlDataType.WSML_HEXBINARY)){
            return ""+values[0];
        }else if (typeIRI.equals(WsmlDataType.WSML_BASE64BINARY)){
            return ""+values[0];
        }else{
            throw new UnsupportedOperationException("Data type "+typeIRI+" not supported!");
        }
    }
}

/*
 * $Log$
 * Revision 1.6  2007/04/02 12:13:21  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.5  2006/11/10 14:18:44  holgerlausen
 * Fixed Bug with Illegalargument Exception when compaing datatypes with different arities
 *
 * Revision 1.4  2005/10/17 17:36:37  holgerlausen
 * toString() to behave equaly then Serializer
 *
 * Revision 1.3  2005/09/30 11:56:11  alex_simov
 * minor fixes
 *
 * Revision 1.2  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.1  2005/09/06 18:33:42  holgerlausen
 *   support for datatypes: updated implementation according to interfaces
 *   renamed WsmlDataTypeImpl to SimpleDataTypeImpl
 *   moved DataValueImpl to ComplexDataValueImpl
 *   added SimpleDataValueImpl for basic strings, decimal, ints
 *
 * Revision 1.7  2005/09/02 13:32:45  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.6  2005/09/02 09:43:32  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.5  2005/08/19 09:00:49  marin_dimitrov
 * -
 *
 * Revision 1.4  2005/07/04 15:25:16  alex_simov
 * DataValue/DataType changes
 *
 * Revision 1.3  2005/06/25 13:19:44  damyan_rm
 * serializing to String changed
 *
 * Revision 1.2  2005/06/23 10:47:46  alex_simov
 * hashCode() added
 *
 * Revision 1.1  2005/06/01 12:09:05  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/30 15:07:05  alex
 * toString() delegates to asString()
 *
 * Revision 1.2  2005/05/26 10:06:47  damian
 * asString modified
 *
 * Revision 1.1  2005/05/12 15:19:39  alex
 * initial commit
 *
 *
 */
