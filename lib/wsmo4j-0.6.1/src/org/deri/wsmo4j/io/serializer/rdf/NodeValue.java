/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2006, University of Innsbruck, Austria

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
package org.deri.wsmo4j.io.serializer.rdf;

import java.math.*;

import org.omwg.ontology.*;
import org.openrdf.model.*;
import org.openrdf.model.Value;
import org.openrdf.vocabulary.*;
import org.wsmo.common.*;

/**
 * Helper type to serialize/deserialize xml element of data values
 * 
 * @author not attributable
 */
class NodeValue {

    static Value serialize(Object value, WsmlRdfSerializer serializer) {
        if (value == null || serializer == null) {
            throw new IllegalArgumentException();
        }
        Value valueElement = null;

        if (value instanceof SimpleDataValue) {
            URI type = serializer.getFactory().createURI(
                    XmlSchema.NAMESPACE +
                    ((SimpleDataValue) value).getType().getIRI().getLocalName().toString());
            String val = ((SimpleDataValue) value).getValue().toString();
            valueElement = serializer.getFactory().createLiteral(val,type);
        }
        else if (value instanceof ComplexDataValue) {
            ComplexDataValue t = (ComplexDataValue) value;
            URI type = serializer.getFactory().createURI(
                    XmlSchema.NAMESPACE +
                    t.getType().getIRI().getLocalName().toString());
            
            valueElement = serializer.getFactory().createLiteral(
                    serializeComplexDataValue(t),type);
        }
        else if (value instanceof IRI) {
            valueElement = serializer.getFactory().createURI(((IRI)value).toString());
        }
        else if (value instanceof Instance) {
            valueElement = serializer.getFactory().createURI(
                    ((Instance)value).getIdentifier().toString());
        }
        else {
            throw new RuntimeException("Invalid value to serialize!");
        }

        return valueElement;
    }
    
    private static String serializeComplexDataValue(ComplexDataValue val) {
        if(val.getType().getIRI().toString().equals(WsmlDataType.WSML_DATE)){
            return serializeDate(val);
        } else if(val.getType().getIRI().toString().equals(WsmlDataType.WSML_FLOAT)) {
            return val.getArgumentValue((byte)0).getValue().toString();
        } else if(val.getType().getIRI().toString().equals(WsmlDataType.WSML_DOUBLE)) {
            return val.getArgumentValue((byte)0).getValue().toString();
        } else if(val.getType().getIRI().toString().equals(WsmlDataType.WSML_BOOLEAN)) {
            return val.getArgumentValue((byte)0).getValue().toString();
        } else if(val.getType().getIRI().toString().equals(WsmlDataType.WSML_DATETIME)) {
            return serializeDateTime(val);
        } else if(val.getType().getIRI().toString().equals(WsmlDataType.WSML_TIME )) {
            return serializeTime(val);
        } else {
            int nbParams = val.getArity();
            StringBuffer buf = new StringBuffer();
            for (byte i = 0; i < nbParams; i++) {
                buf.append(val.getArgumentValue(i).toString()+" ");
            }
            return buf.toString();
        }
    }
    
    private static String serializeDate(ComplexDataValue val) {
        String ret = val.getArgumentValue((byte)0).getValue() + "-" +
            ensure00(val.getArgumentValue((byte)1).getValue().toString()) + "-" +
            ensure00(val.getArgumentValue((byte)2).getValue().toString());
        switch(val.getArity()) {
            case 4:
                return ret + ensure00(serializeTimezone(
                    val.getArgumentValue((byte)3).getValue(),null));
            case 5:
                return ret + ensure00(serializeTimezone(
                        val.getArgumentValue((byte)3).getValue(),
                        val.getArgumentValue((byte)4).getValue()));
        }
        return ret;
    }
    
    private static String serializeTime(ComplexDataValue val) {
        String ret =  ensure00(val.getArgumentValue((byte)0).getValue().toString()) + ":" +
            ensure00(val.getArgumentValue((byte)1).getValue().toString()) + ":" +
            ensure00(val.getArgumentValue((byte)2).getValue().toString());
        switch(val.getArity()) {
            case 4:
                return ret + ensure00(serializeTimezone(
                    val.getArgumentValue((byte)3).getValue(),null));
            case 5:
                return ret + ensure00(serializeTimezone(
                        val.getArgumentValue((byte)3).getValue(),
                        val.getArgumentValue((byte)4).getValue()));
        }
        return ret;
    }
    
    private static String serializeDateTime(ComplexDataValue val) {
        String ret =  val.getArgumentValue((byte)0).getValue() + "-" +
            ensure00(val.getArgumentValue((byte)1).getValue().toString()) + "-" +
            ensure00(val.getArgumentValue((byte)2).getValue().toString()) + "T" +
            ensure00(val.getArgumentValue((byte)3).getValue().toString()) + ":" +
            ensure00(val.getArgumentValue((byte)4).getValue().toString()) + ":" +
            ensure00(val.getArgumentValue((byte)5).getValue().toString());
        switch(val.getArity()) {
            case 7:
                return ret + ensure00(
                        serializeTimezone(
                                val.getArgumentValue((byte)6).getValue(),null));
            case 8:
                return ret + ensure00(
                        serializeTimezone(
                            val.getArgumentValue((byte)6).getValue(),
                            val.getArgumentValue((byte)7).getValue()));
        }
        return ret;
    }
    
    private static String serializeTimezone(Object h, Object m) {
        String ret = h.toString();
        if(((BigInteger)h).signum()>0) { ret = "+" + ret;  }
        if(m!=null) return ret + ":" + ensure00(m.toString()); else return ret;
    }
    
    private static String ensure00(String s) {
        if(s.length()==1) {
            return "0" + s;
        } else {
            return s;
        }
    }
}

/*
 * $Log: NodeValue.java,v $
 * Revision 1.2  2006/11/10 15:38:49  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 15:02:59  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 10:08:42  ohamano
 * *** empty log message ***
 *
 *
 */