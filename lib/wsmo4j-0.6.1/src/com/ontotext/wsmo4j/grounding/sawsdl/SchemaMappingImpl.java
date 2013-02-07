/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2007, Ontotext Lab. / SIRMA

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

package com.ontotext.wsmo4j.grounding.sawsdl;

import java.net.URI;

import javax.xml.namespace.QName;
import org.wsmo.grounding.sawsdl.SchemaMapping;

public abstract class SchemaMappingImpl implements SchemaMapping {

    
    private QName source;
    private URI schema;
    
    SchemaMappingImpl(QName source, URI schema) {
        if (source == null || schema == null) {
            throw new IllegalArgumentException();
        }
        
        this.source = source;
        this.schema = schema;
    }

    public QName getSource() {
        return this.source;
    }

    public URI getSchema() {
        return this.schema;
    }
    
    public boolean equals(Object o) {
        if (o == null 
                || false == o instanceof SchemaMapping) {
            return false;
        }
        SchemaMapping ref = (SchemaMapping)o;
        return source.equals(ref.getSource()) 
                && schema.equals(ref.getSchema());
    }
    
    public int hashCode() {
        return source.hashCode() + schema.hashCode();
    }

}

/*
 * $Log: SchemaMappingImpl.java,v $
 * Revision 1.1  2007/04/24 16:47:20  alex_simov
 * new SA-WSDL impl
 *
 */
