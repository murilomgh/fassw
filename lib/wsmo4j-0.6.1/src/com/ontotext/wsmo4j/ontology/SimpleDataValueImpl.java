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
package com.ontotext.wsmo4j.ontology;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.omwg.logicalexpression.terms.Visitor;
import org.omwg.ontology.*;

/**
 * Interface or class description
 *
 * <pre>
 * Created on Sep 5, 2005
 * Committed by $Author$
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public class SimpleDataValueImpl implements SimpleDataValue{

    Object value = null;
    SimpleDataType type = null;
    
    public SimpleDataValueImpl(SimpleDataType type, String value){
        if (!WsmlDataType.WSML_STRING.equals(
                type.getIRI().toString())){
            throw new IllegalArgumentException("Simple type string must have type wsml#string!");
        }
        this.type = type;
        this.value = value;
    }
    
    public SimpleDataValueImpl(SimpleDataType type, BigInteger value){
        if (!WsmlDataType.WSML_INTEGER.equals(
                type.getIRI().toString())){
            throw new IllegalArgumentException("Simple type integer must have type wsml#integer!");
        }
        this.type = type;
        this.value = value;
    }

    public SimpleDataValueImpl(SimpleDataType type, BigDecimal value){
        if (!WsmlDataType.WSML_DECIMAL.equals(
                type.getIRI().toString())){
            throw new IllegalArgumentException("Simple type decimal must have type wsml#decimal!");
        }
        this.type = type;
        this.value = value;
    }

    public String asString() {
        return value.toString();
    }
    
    public String toString() {
        return asString();
    }

    public WsmlDataType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void accept(Visitor v) {
        v.visitSimpleDataValue(this);
    }

    public boolean equals(Object o){
        if (o == null || !(o instanceof SimpleDataValue)){
            return false;
        }
        SimpleDataValue v = (SimpleDataValue)o;
        if (!v.getType().getIRI().toString().equals(type.getIRI().toString())){
            return false;
        }
        return v.getValue().equals(value);
    }
    
    public int hashCode(){
        return value.hashCode();
    }
}

/*
 * $Log$
 * Revision 1.2  2005/09/16 14:02:44  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.1  2005/09/06 18:33:42  holgerlausen
 *   support for datatypes: updated implementation according to interfaces
 *   renamed WsmlDataTypeImpl to SimpleDataTypeImpl
 *   moved DataValueImpl to ComplexDataValueImpl
 *   added SimpleDataValueImpl for basic strings, decimal, ints
 *
 */