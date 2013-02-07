/*
 wsmo4j - a WSMO API and Reference Implementation
 
 Copyright (c) 2004-2005, OntoText Lab. / SIRMA
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

import org.omwg.ontology.SimpleDataType;
import org.omwg.ontology.WsmlDataType;
import org.wsmo.common.IRI;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

public class SimpleDataTypeImpl implements SimpleDataType{
    IRI iri;
    public SimpleDataTypeImpl(IRI iri) {
        this.iri = iri;
    }
    public String toString() {
        return iri.toString();
    }
    
    public IRI getIRI() {
        return iri;
    }
    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null
                || false == object instanceof WsmlDataType) {
            return false;
        }
        return toString().equals(object.toString());
    }
    
    public int hashCode() {
        return iri.hashCode();
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
* Revision 1.4  2005/07/04 15:25:16  alex_simov
* DataValue/DataType changes
*
*/
